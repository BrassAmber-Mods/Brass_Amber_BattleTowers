package com.brass_amber.ba_bt.worldGen.structures;


import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import static com.brass_amber.ba_bt.BABTMain.SAVE_TOWERS;
import static com.brass_amber.ba_bt.BattleTowersConfig.*;
import static com.brass_amber.ba_bt.util.BTUtil.chunkDistanceTo;

public abstract class TowerStructure extends Structure {

    protected final TowerStructure.BTStructureSettings extraSettings;
    protected String towerName;

    protected final List<Integer> minimumSeperations;
    protected final List<Integer> averageSeperations;
    protected int towerType = 0;
    protected int towerId = -1; // Tower number (Land = 0, Ocean = 1, etc. )
    protected String[] towerTypeConversion;
    protected int afterPlaceCount = 0;

    protected final Boolean buryTower = false;
    protected final Boolean randomBuryDepth = false;

    protected TowerStructure(StructureSettings structureSettings, BTStructureSettings extraSettings) {
        super(structureSettings);
        this.extraSettings = extraSettings;
        this.minimumSeperations = List.of(landMinimumSeperation.get(), oceanMinimumSeperation.get());
        this.averageSeperations = List.of(landAverageSeperationModifier.get(), oceanAverageSeperationModifier.get());
    }

    public static <S extends TowerStructure> RecordCodecBuilder<S, TowerStructure.BTStructureSettings> extraSettingsCodec() {
        return TowerStructure.BTStructureSettings.CODEC.forGetter((object) -> new BTStructureSettings(null));
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        super.afterPlace(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
    }

    @Override
    public @NotNull StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager templateManager, long seed, ChunkPos chunkPos, int i, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> biomePredicate) {
        Structure.GenerationContext structure$generationcontext = new Structure.GenerationContext(registryAccess, chunkGenerator, biomeSource, randomState, templateManager, seed, chunkPos, heightAccessor, biomePredicate);
        Optional<Structure.GenerationStub> optional = this.findGenerationPoint(structure$generationcontext);
        if (optional.isPresent()) {
            StructurePiecesBuilder structurepiecesbuilder = optional.get().getPiecesBuilder();
            StructureStart structurestart = new StructureStart(this, new ChunkPos(optional.get().position()), i, structurepiecesbuilder.build());
            if (structurestart.isValid()) {
                return structurestart;
            }
        }

        return StructureStart.INVALID_START;
    }

    @Override
    // Override findValidGeneration point as well as findGenerationPoint is called during locate command.
    // Override of generate above removes this being called anywhere except when locating the tower after chunkGen
    public @NotNull Optional<GenerationStub> findValidGenerationPoint(GenerationContext generationContext) {
        boolean canSpawn = false;
        ChunkPos checkPos = generationContext.chunkPos();
        for (ChunkPos towerPos : SaveTowers.towers.get(this.towerId)) {
            if (towerPos.x == checkPos.x && towerPos.z == checkPos.z) {
                canSpawn = true;
                break;
            }
        }

        if (canSpawn) {
            return Optional.of(new Structure.GenerationStub(checkPos.getWorldPosition(), (structurePiecesBuilder) -> new StructurePiecesBuilder()));
        }
        return Optional.empty();
    }

    @Override
    protected @NotNull Optional<Structure.GenerationStub> findGenerationPoint(GenerationContext generationContext) {

        ChunkPos chunkPos = generationContext.chunkPos();
        // BABTMain.LOGGER.info("Attempting Land Tower Spawn at " + chunkPos.x + " " + chunkPos.z);

        // Ensure tower chunk is outside initial player requested spawn range
        if (chunkDistanceTo(ChunkPos.ZERO, chunkPos) < BattleTowersConfig.firstTowerDistance.get()) {
            return Optional.empty();
        }

        int minimumSeparation = this.minimumSeperations.get(this.towerId);
        int seperationRange = this.averageSeperations.get(this.towerId);

        //
        int nextSeperation =  minimumSeparation + generationContext.random().nextInt(seperationRange * 2);
        int closestDistance = 2000;

        if (!SaveTowers.towers.get(this.towerId).isEmpty()) {
            for (ChunkPos towerPos: SaveTowers.towers.get(this.towerId)) {
                closestDistance = Math.min(closestDistance, chunkDistanceTo(chunkPos, towerPos));
                // BABTMain.LOGGER.info("Tower distance from generation try:" + closestDistance);
            }
        }

        if (closestDistance <= nextSeperation) {
            // BABTMain.LOGGER.info("Land not outside tower separation " + nextSeperation);
            return Optional.empty();
        }

        Pair<Boolean, BlockPos> canSpawn = isSpawnableChunk(generationContext);
        Rotation rotation = Rotation.getRandom(generationContext.random());

        if (canSpawn.getFirst()) {
            saveTower(canSpawn.getSecond(), chunkPos);
            return Optional.of(new Structure.GenerationStub(canSpawn.getSecond(), (piecesBuilder) -> {
                this.generatePieces(piecesBuilder, generationContext, canSpawn.getSecond(), rotation);
            }));
        }

        return Optional.empty();
    }

    protected void generatePieces(StructurePiecesBuilder piecesBuilder, Structure.GenerationContext generationContext, BlockPos blockPos, Rotation rotation) {
        List<TowerPieces.TowerPiece> list = Lists.newLinkedList();
        String variant;
        try {
            variant = this.towerTypeConversion[this.towerType];
        } catch (IndexOutOfBoundsException e) {
            variant = "normal";
        }
        TowerPieces.generateTower(generationContext.structureTemplateManager(), blockPos, rotation, list, generationContext.random(), this.towerName, variant);
        list.forEach(piecesBuilder::addPiece);
    }


    protected abstract Pair<Boolean, BlockPos> isSpawnableChunk(GenerationContext generationContext);

    public record BTStructureSettings(HolderSet<Structure> avoidStructures) {
        public static final MapCodec<TowerStructure.BTStructureSettings> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("avoid_structures").forGetter(btStructureSettings -> btStructureSettings.avoidStructures)
        ).apply(instance, BTStructureSettings::new));
    }
    // Used for tower saving and logging of tower positions
    public void saveTower(BlockPos spawnPos, ChunkPos chunkPos) {
        BABTMain.LOGGER.info("{} Tower at {} {}", this.towerName, spawnPos, chunkPos);
        SAVE_TOWERS.addTower(chunkPos, this.towerName);
    }

    protected abstract boolean isValidBiome(Structure.GenerationStub generationStub, Structure.GenerationContext generationContext, Block topBlock);
}


