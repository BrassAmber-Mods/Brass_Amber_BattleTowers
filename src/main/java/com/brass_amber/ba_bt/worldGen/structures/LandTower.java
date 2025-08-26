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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LandTower extends TowerStructure {

    private final float waterBlocksThreshold;

    public static final Codec<LandTower> CODEC = RecordCodecBuilder.<LandTower>mapCodec(instance ->
            instance.group(
                    TowerStructure.settingsCodec(instance),
                    TowerStructure.extraSettingsCodec(),
                    Codec.floatRange(0, 1).fieldOf("water_prevent_spawn_threshold").forGetter(codec -> codec.waterBlocksThreshold)
            ).apply(instance, LandTower::new)).codec();



    public LandTower(StructureSettings structureSettings, BTStructureSettings extraSettings, float waterBlocksThreshold) {
        super(structureSettings, extraSettings);

        this.waterBlocksThreshold = waterBlocksThreshold;
        this.towerId = 0;
        this.towerName = "land_tower";
        this.towerTypeConversion = new String[]{"normal", "overgrown", "sandy", "icy", "ruined"};
    }

    @Override
    protected Pair<Boolean, Integer> isSpawnableChunk(GenerationContext generationContext) {
        BABattleTowers.LOGGER.debug("Can Spawn Land");
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
            int middleHieght = chunkGen.getFirstOccupiedHeight(
                    pos.getMiddleBlockX(), pos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()
            );
            Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                    QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(pos.getMiddleBlockZ()), generationContext.randomState().sampler()
            );

            // re-check biome for extra chunks skipping to next chunk if not valid
            if (!isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(middleHieght), biome)) {
                usablePositions.clear();
                usableHeights.clear();
                break;
            }

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

        // Get a random usable position from the list, otherwise return false
        if (!usablePositions.isEmpty() && usableHeights.get(0) < 215) {
            return Pair.of(true, usableHeights.get(0));
        }

        if (!usablePositions.isEmpty() && usableHeights.get(0) > 215) {
            BABattleTowers.LOGGER.debug("Terrain to high for Land Tower");

        }

        return Pair.of(false, 0);
    }

    @Override
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {
        BABattleTowers.LOGGER.debug("Is Valid Land Tower Biome");
        WorldgenRandom worldgenRandom = context.random();
        worldgenRandom.setSeed(context.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(blockpos);

        HolderSet<Biome> holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(Tags.Biomes.IS_WATER).orElseThrow();
        Predicate<Holder<Biome>> predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> waterBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 64, predicate, context.random(), context.randomState().sampler());

        HolderSet<Biome> overgrownHolderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.LAND_TOWER_OVERGROWN_BIOMES).orElseThrow();
        Predicate<Holder<Biome>> overgrownPredicate = overgrownHolderset::contains;
        Pair<BlockPos, Holder<Biome>> overgrownBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 24, overgrownPredicate, context.random(), context.randomState().sampler());


        HolderSet<Biome> sandyHolderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.LAND_TOWER_SANDY_BIOMES).orElseThrow();
        Predicate<Holder<Biome>> sandyPredicate = sandyHolderset::contains;
        Pair<BlockPos, Holder<Biome>> sandyBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 24, sandyPredicate, context.random(), context.randomState().sampler());


        HolderSet<Biome> snowyHolderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.LAND_TOWER_SNOWY_BIOMES).orElseThrow();
        Predicate<Holder<Biome>> snowyPredicate = snowyHolderset::contains;
        Pair<BlockPos, Holder<Biome>> snowyBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 24, snowyPredicate, context.random(), context.randomState().sampler());

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
    public StructureType<?> type() {
        return BTStructures.LAND_TOWER.get();
    }
}
