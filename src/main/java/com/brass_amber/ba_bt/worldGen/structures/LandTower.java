package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.BTTags;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Predicate;

public class LandTower extends Structure implements TowerStructure {
    static final Logger LOGGER = LogUtils.getLogger();
    
    public static final Codec<LandTower> CODEC = RecordCodecBuilder.<LandTower>mapCodec(instance ->
            instance.group(
                    Structure.settingsCodec(instance)
            ).apply(instance, LandTower::new)).codec();
    private int towerType = 0;

    @Override
    public String getTowerName() {
        return "land_tower";
    }

    @Override
    public int getTowerId() {
        return 0; // Tower number (Land = 0, Ocean = 1, etc. )
    }

    @Override
    public String[] getTowerTypeConversion() {
        return new String[]{"normal", "overgrown", "sandy", "icy", "ruined"};
    }

    @Override
    public int getTowerType() {
        return this.towerType;
    }

    public LandTower(StructureSettings structureSettings) {
        super(structureSettings);
    }

    public StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager templateManager, long seed, ChunkPos chunkPos, int references, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> biomes) {
        Structure.GenerationContext structure$generationcontext = new Structure.GenerationContext(registryAccess, chunkGenerator, biomeSource, randomState, templateManager, seed, chunkPos, heightAccessor, biomes);
        Optional<Structure.GenerationStub> optional = this.findValidGenerationPoint(structure$generationcontext);

        if (optional.isPresent()) {

            StructurePiecesBuilder structurepiecesbuilder = optional.get().getPiecesBuilder();
            StructureStart structurestart = new StructureStart(this, chunkPos, references, structurepiecesbuilder.build());
            LOGGER.debug("Got stub for: {} | Structure Start is : {}", chunkPos, structurestart.isValid());
            if (structurestart.isValid()) {
                return structurestart;
            }
        }

        return StructureStart.INVALID_START;
    }

    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();

        LOGGER.debug("Attempting Land Tower Spawn at {} {}", chunkPos.x, chunkPos.z);

        Pair<Boolean, Integer> canSpawn = isSpawnableChunk(generationContext);

        if (canSpawn.getFirst()) {
            LOGGER.debug("Can Spawn, Blockpos: {}", canSpawn.getSecond());
            BlockPos spawnPos = chunkPos.getMiddleBlockPosition(canSpawn.getSecond());

            StructurePiecesBuilder piecesBuilder = new StructurePiecesBuilder();
            this.generatePieces(piecesBuilder, generationContext, spawnPos);
            return Optional.of(new GenerationStub(spawnPos, Either.right(piecesBuilder)));
        }

        return Optional.empty();
    }

    @Override
    public Pair<Boolean, Integer> isSpawnableChunk(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();

        LOGGER.debug("Checking Land Spawn at: {}", chunkPos);

        int newLandHeight;
        int lowestY = 215;
        int highestY = 0;
        int minX = chunkPos.getMinBlockX();
        int minZ= chunkPos.getMinBlockZ();;
        int newX;
        int newZ;

        for (int x = 0; x < 6; x++) {
            for (int z = 0; z < 6; z++) {

                newX = minX + (x * 3);
                newZ = minZ + (z * 3);
                newLandHeight = chunkGen.getFirstOccupiedHeight(newX, newZ, Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState());

                lowestY = Math.min(newLandHeight, lowestY);
                highestY = Math.max(newLandHeight, highestY);

            }
        }

        // 12 Blocks seem to work well with allowing a good number of small cliff spawns, while removing the mountainside spawns
        boolean isFlat = highestY - lowestY <= 12;

        int usableHeight = lowestY + ((highestY - lowestY) / 4);

        LOGGER.debug("flat?: {} usable height: {}", isFlat, usableHeight);

        int middleHieght = chunkGen.getFirstOccupiedHeight(
                chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()
        );
        Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(chunkPos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(chunkPos.getMiddleBlockZ()), generationContext.randomState().sampler()
        );

        BlockPos middleBlock = chunkPos.getMiddleBlockPosition(middleHieght + 1);

        HolderSet<Biome> holderset = generationContext.registryAccess().registryOrThrow(Registries.BIOME).getTag(Tags.Biomes.IS_WATER).orElseThrow();
        // LOGGER.debug("Ocean Holderset = {}", holderset);
        Predicate<Holder<Biome>> predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> waterBiomeNearby = chunkGen.getBiomeSource().findBiomeHorizontal(
                middleBlock.getX(), chunkGen.getSeaLevel(), middleBlock.getZ(), 24, predicate, generationContext.random(), generationContext.randomState().sampler()
        );
        LOGGER.debug("Water Biome nearby = {} {}", waterBiomeNearby, waterBiomeNearby == null);

        checkVariant(generationContext, middleBlock);
        // Get a random usable position from the list, otherwise return false
        if (isFlat && generationContext.validBiome().test(biome) && waterBiomeNearby == null) {
            return Pair.of(true, usableHeight);
        }

        if (usableHeight > 215) {
            LOGGER.debug("Terrain to high for Land Tower");
        }

        return Pair.of(false, 0);
    }

    public void checkVariant(GenerationContext context, BlockPos blockpos) {
        // LOGGER.debug("Is Valid Land Tower Biome");
        WorldgenRandom worldgenRandom = context.random();
        worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);

        HolderSet<Biome> overgrownHolderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.LAND_TOWER_OVERGROWN_BIOMES).orElseThrow();
        Predicate<Holder<Biome>> overgrownPredicate = overgrownHolderset::contains;
        Pair<BlockPos, Holder<Biome>> overgrownBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 24, overgrownPredicate, context.random(), context.randomState().sampler());

        HolderSet<Biome> sandyHolderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.LAND_TOWER_SANDY_BIOMES).orElseThrow();
        Predicate<Holder<Biome>> sandyPredicate = sandyHolderset::contains;
        Pair<BlockPos, Holder<Biome>> sandyBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 24, sandyPredicate, context.random(), context.randomState().sampler());

        HolderSet<Biome> snowyHolderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.LAND_TOWER_SNOWY_BIOMES).orElseThrow();
        Predicate<Holder<Biome>> snowyPredicate = snowyHolderset::contains;
        Pair<BlockPos, Holder<Biome>> snowyBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 48, snowyPredicate, context.random(), context.randomState().sampler());

        if (overgrownBiomeNearby != null) {
            // Overgrown
            this.towerType = 1;
        } else if (sandyBiomeNearby != null) {
            // Desert
            this.towerType = 2;
        } else if (snowyBiomeNearby != null) {
            // Snowy
            this.towerType = 3;
        } else {
            // Default || Ruined
            towerType = worldgenRandom.nextInt(50) > 7 ? 0 : 4;
        }
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        super.afterPlace(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
        afterPlaceBT(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.LAND_TOWER.get();
    }
}
