package com.brass_amber.ba_bt.worldGen.structures;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.util.BTStatics;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
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
    public @NotNull StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager templateManager, long seed, ChunkPos chunkPos, int i, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> biomePredicate) {

        if (chunkGenerator instanceof FlatLevelSource) {
            return StructureStart.INVALID_START;
        }

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
        for (Pair<ChunkPos, Rotation> towerPosRotation : SaveTowers.towers.get(this.towerId)) {
            ChunkPos towerPos = towerPosRotation.getFirst();
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
            for (Pair<ChunkPos, Rotation> towerPosRotation: SaveTowers.towers.get(this.towerId)) {
                closestDistance = Math.min(closestDistance, chunkDistanceTo(chunkPos, towerPosRotation.getFirst()));
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
            saveTower(canSpawn.getSecond(), chunkPos, rotation);
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
    // Rotation is saved for rotation of loaded datamarker block containers after generation
    public void saveTower(BlockPos spawnPos, ChunkPos chunkPos, Rotation rotation) {
        BABTMain.LOGGER.info("{} Tower at {} {}", this.towerName, spawnPos, chunkPos);
        SAVE_TOWERS.addTower(chunkPos, rotation, this.towerId);
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        super.afterPlace(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);

        // After Place is called for every chunk that the structure occupies.
        this.afterPlaceCount++;
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int bbYStart = boundingbox.minY();

        BlockPos chunckCenter = chunkPos.getMiddleBlockPosition(bbYStart);

        // BrassAmberBattleTowers.LOGGER.info("Post Processing: In chunk: " + chunkPos + " " + chunckCenter);

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        blockpos$mutableblockpos.setY(bbYStart);
        // get start and end postions for x/z, using min/max to account for the MinBlock being -25 and the MaxBlock being -27
        int startX = chunckCenter.getX() - 15;
        int endX = chunckCenter.getX() + 15;
        // BrassAmberBattleTowers.LOGGER.info("X start: " + startX + " end: " + endX);

        int startZ = chunckCenter.getZ() - 15;
        int endZ = chunckCenter.getZ() + 15;
        // BrassAmberBattleTowers.LOGGER.info("X start: " + startZ + " end: " + endZ);

        List<BlockState> towerBlocks = BTStatics.towerBlocks.get(towerId);
        BlockState baseBlock = BTStatics.towerBaseBlocks.get(towerId);
        ArrayList<BlockPos> startPositions = new ArrayList<>();

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                blockpos$mutableblockpos.set(x, bbYStart, z);
                // BrassAmberBattleTowers.LOGGER.info("Block at: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (towerBlocks.contains(worldGenLevel.getBlockState(blockpos$mutableblockpos))) {
                    // BrassAmberBattleTowers.LOGGER.info("Block is acceptable: " + blockpos$mutableblockpos + " "+ worldGenLevel.getBlockState(blockpos$mutableblockpos));
                    startPositions.add(new BlockPos(x, bbYStart - 1, z));
                }
            }
        }

        for (BlockPos startPos: startPositions) {
            for (int y = startPos.getY(); y > worldGenLevel.getMinBuildHeight() ; y--) {
                blockpos$mutableblockpos.set(startPos.getX(), y, startPos.getZ());
                // BrassAmberBattleTowers.LOGGER.info("Block to check: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) || worldGenLevel.isWaterAt(blockpos$mutableblockpos)
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof TallGrassBlock
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof FlowerBlock
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof DeadBushBlock) {
                    worldGenLevel.setBlock(blockpos$mutableblockpos, baseBlock, 2);
                } else {
                    // Add two blocks into this ground level as well.
                    worldGenLevel.setBlock(blockpos$mutableblockpos, baseBlock, 2);
                    worldGenLevel.setBlock(blockpos$mutableblockpos.below(), baseBlock, 2);
                    break;
                }
            }
        }

        startPositions.clear();

        // Now check for replacing stone/dirt blocks around/under the tower
        List<BlockState> acceptableDirtBlocks = List.of(
                Blocks.DIRT.defaultBlockState(), Blocks.DIRT_PATH.defaultBlockState(),
                Blocks.COARSE_DIRT.defaultBlockState(), Blocks.ROOTED_DIRT.defaultBlockState(),
                Blocks.GRAVEL.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()
        );
        List<BlockState> acceptableStoneBlocks = List.of(
                Blocks.STONE.defaultBlockState(),  Blocks.IRON_ORE.defaultBlockState(),
                Blocks.COAL_ORE.defaultBlockState(), Blocks.ANDESITE.defaultBlockState(),
                Blocks.GRANITE.defaultBlockState(), Blocks.DIORITE.defaultBlockState()
        );

        BlockState state;

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                blockpos$mutableblockpos.set(x, bbYStart + 3, z);
                state = worldGenLevel.getBlockState(blockpos$mutableblockpos);
                // BrassAmberBattleTowers.LOGGER.info("Block at: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (acceptableDirtBlocks.contains(state) || acceptableStoneBlocks.contains(state)) {
                    // BrassAmberBattleTowers.LOGGER.info("Block is acceptable: " + blockpos$mutableblockpos + " "+ worldGenLevel.getBlockState(blockpos$mutableblockpos));
                    startPositions.add(new BlockPos(x, bbYStart + 2, z));
                }
            }
        }


        for (BlockPos startPos: startPositions) {
            for (int y = startPos.getY(); y > worldGenLevel.getMinBuildHeight() ; y--) {
                blockpos$mutableblockpos.set(startPos.getX(), y, startPos.getZ());
                state = worldGenLevel.getBlockState(blockpos$mutableblockpos.above());

                // BrassAmberBattleTowers.LOGGER.info("Block to check: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) || worldGenLevel.isWaterAt(blockpos$mutableblockpos)) {
                    if (acceptableDirtBlocks.contains(state) ){
                        worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                    } else {
                        worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.STONE.defaultBlockState(), 2);
                    }
                }
            }
        }

        // All Battletowers take up at least 9 chunks (29x29 XZ) on ninth chunk, place monolith at tower end.
        // Monolith handles placement of Obelisk.
        if (afterPlaceCount == 9) {
            BoundingBox endBB = piecesContainer.pieces().get(9).getBoundingBox();
            BlockPos center;
            ServerLevel level = worldGenLevel.getLevel();

            EntityType<BTMonolith> type;

            Entity monolith;

            switch (this.towerId) {
                case 1 -> {
                    center = endBB.getCenter().atY(endBB.minY()).above(3);
                    type = BTEntityType.OCEAN_MONOLITH.get();
                }
                case 2 -> {
                    center = endBB.getCenter().atY(endBB.minY()).above(2);
                    type = BTEntityType.CORE_MONOLITH.get();
                }
                case 3 -> {
                    center = endBB.getCenter().atY(endBB.minY()).above(3);
                    type = BTEntityType.NETHER_MONOLITH.get();
                }
                case 4 -> {
                    center = endBB.getCenter().atY(endBB.minY()).above(3);
                    type = BTEntityType.END_MONOLITH.get();
                }
                case 5 -> {
                    center = endBB.getCenter().atY(endBB.minY()).above(3);
                    type = BTEntityType.SKY_MONOLITH.get();
                }
                default -> {
                    center = endBB.getCenter().atY(endBB.minY()).above(3);
                    type = BTEntityType.LAND_MONOLITH.get();
                }
            }

            monolith = new BTMonolith(type, level, center.getX() + .5, center.getY(), center.getZ() + .5, Blocks.CLAY.defaultBlockState());
            worldGenLevel.addFreshEntity(monolith);
            BABTMain.LOGGER.debug("Spawned Monolith for {} at {}", this.towerName, center);
            this.afterPlaceCount = 0;
        }
    }

    protected abstract boolean isValidBiome(Structure.GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder);
}


