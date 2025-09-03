package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.BTTags;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LandTower extends Structure implements TowerStructure {

    public static final Codec<LandTower> CODEC = RecordCodecBuilder.<LandTower>mapCodec(instance ->
            instance.group(
                    Structure.settingsCodec(instance),
                    Codec.floatRange(0, 1).fieldOf("water_prevent_spawn_threshold").forGetter(codec -> codec.waterBlocksThreshold)
            ).apply(instance, LandTower::new)).codec();

    private final float waterBlocksThreshold;
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

    public LandTower(StructureSettings structureSettings, float waterBlocksThreshold) {
        super(structureSettings);
        this.waterBlocksThreshold = waterBlocksThreshold;
    }

    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        WorldgenRandom worldgenRandom = generationContext.random();
        worldgenRandom.setSeed(generationContext.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(chunkPos.getMiddleBlockPosition(0));

        if (hasNearbyTower(chunkPos)) {
            // BABTMain.LOGGER.debug("Land not outside tower separation " + nextSeperation);
            return Optional.empty();
        }

        // BABattleTowers.LOGGER.debug("Attempting Land Tower Spawn at " + chunkPos.x + " " + chunkPos.z);

        Pair<Boolean, Integer> canSpawn = isSpawnableChunk(generationContext);
        Rotation rotation = Rotation.getRandom(randomSource);

        if (canSpawn.getFirst()) {
            BlockPos spawnPos = chunkPos.getMiddleBlockPosition(canSpawn.getSecond());

            saveTower(spawnPos, rotation);
            return Optional.of(new GenerationStub(spawnPos, (piecesBuilder) -> {
                                this.generatePieces(piecesBuilder, generationContext, spawnPos, rotation);
            }));
        }

        return Optional.empty();
    }

    @Override
    public Pair<Boolean, Integer> isSpawnableChunk(GenerationContext generationContext) {
        // BABattleTowers.LOGGER.debug("Can Spawn Land");
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();

        // Test/Check 3 by 3 square of chunks for possible spawns (x pattern)
        List<ChunkPos> testables = new ArrayList<>(
                List.of(
                        chunkPos,
                        new ChunkPos(chunkPos.x + 1, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)
                )
        );

        // BABTMain.LOGGER.debug("Rquesting chunks to test: " + testables.toString());

        List<ChunkPos> usablePositions =  new ArrayList<>();
        ArrayList<Integer> usableHeights = new ArrayList<>();

        int newLandHeight;
        int lowestY;
        int highestY;
        int minX;
        int minZ;
        int newX;
        int newZ;

        for (ChunkPos pos : testables) {
            // BABattleTowers.LOGGER.debug("Land tower testing at {}", pos);

            lowestY = 215;
            highestY = 0;
            minX = pos.getMinBlockX();
            minZ = pos.getMinBlockZ();

            for (int x = 0; x < 6; x++) {
                for (int z = 0; z < 6; z++) {

                    newX = minX + (x*3);
                    newZ = minZ + (z*3);
                    newLandHeight = chunkGen.getFirstOccupiedHeight(newX, newZ, Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState());

                    lowestY = Math.min(newLandHeight, lowestY);
                    highestY = Math.max(newLandHeight, highestY);

                }
            }

            // 12 Blocks seem to work well with allowing a good number of small cliff spawns, while removing the mountainside spawns
            boolean isFlat = highestY - lowestY <= 12;

            int usableHeight = lowestY + ((highestY - lowestY)/4);

            // BrassAmberBattleTowers.LOGGER.debug("flat?: " + isFlat + " water?: " + watered + " usable height: " + usableHeight);

            if (isFlat) {
                // BrassAmberBattleTowers.LOGGER.debug("Usable position at: " + pos + " " + usableHeight);
                usablePositions.add(pos);
                usableHeights.add(usableHeight);
            }

        }

        int middleHieght = chunkGen.getFirstOccupiedHeight(
                chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()
        );
        Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(chunkPos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(chunkPos.getMiddleBlockZ()), generationContext.randomState().sampler()
        );
        BABattleTowers.LOGGER.debug("Is Valid Land Tower Biome ? {} ", biome);

        // re-check biome for extra chunks skipping to next chunk if not valid

        // Get a random usable position from the list, otherwise return false
        if (!usablePositions.isEmpty() && usableHeights.get(0) < 215 && isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(middleHieght), biome)) {
            return Pair.of(true, usableHeights.get(0));
        }

        if (!usablePositions.isEmpty() && usableHeights.get(0) > 215) {
            BABattleTowers.LOGGER.debug("Terrain to high for Land Tower");

        }

        return Pair.of(false, 0);
    }

    @Override
    public boolean isValidBiome(GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {
        // BABattleTowers.LOGGER.debug("Is Valid Land Tower Biome");
        WorldgenRandom worldgenRandom = context.random();
        worldgenRandom.setSeed(context.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(blockpos);

        HolderSet<Biome> holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(Tags.Biomes.IS_WATER).orElseThrow();
        // BABattleTowers.LOGGER.debug("Ocean Holderset = {}", holderset);
        Predicate<Holder<Biome>> predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> waterBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), context.chunkGenerator().getSeaLevel(), blockpos.getZ(), 64, predicate, context.random(), context.randomState().sampler());
        // BABattleTowers.LOGGER.debug("Water Biome nearby = {} {}", waterBiomeNearby, waterBiomeNearby == null);

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
            towerType = randomSource.nextInt(50) > 7 ? 0 : 4;
        }

        return context.validBiome().test(biomeHolder) && waterBiomeNearby == null;
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
