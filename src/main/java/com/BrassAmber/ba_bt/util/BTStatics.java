package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.init.BTBlocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.BrassAmber.ba_bt.BattleTowersConfig.landTowerMobs;
import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanTowerMobs;

public class BTStatics {
    public static final List<String> landTowerNames;
    public static final List<List<ResourceKey<Biome>>> landTowerBiomes;
    public static final List<List<Integer>> towerSpawnerAmounts;
    public static final List<List<Integer>> towerChestUnlocking;
    public static final List<List<Block>> towerBlocks;
    public static final List<List<EntityType<?>>> towerMobs;
    public static final List<List<List<Integer>>> towerSpawnerData;
    public static ChunkPos lastLandPostition = ChunkPos.ZERO;
    public static ChunkPos lastOceanPostition = ChunkPos.ZERO;


    static {

        landTowerNames = List.of("Land", "Overgrown", "Sandy", "Icy");

        landTowerBiomes = List.of(
                // Land
                List.of(
                        Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST,
                        Biomes.WINDSWEPT_FOREST, Biomes.MEADOW, Biomes.PLAINS, Biomes.TAIGA,
                        Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.SAVANNA,
                        Biomes.SUNFLOWER_PLAINS, Biomes.GROVE, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS
                ),
                // Overgrown
                List.of(
                        Biomes.SWAMP, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE
                ),
                // Sandy
                List.of(
                        Biomes.DESERT
                )
        );


        towerBlocks = List.of(
                // Land
                List.of(
                        Blocks.COBBLESTONE, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE_STAIRS,
                        Blocks.STONE_BRICK_SLAB, Blocks.STONE_BRICKS, Blocks.STONE_BRICK_STAIRS,
                        Blocks.CHISELED_STONE_BRICKS, Blocks.GRAVEL, Blocks.SAND,
                        Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_STAIRS,
                        Blocks.SANDSTONE, Blocks.SANDSTONE_SLAB, Blocks.SANDSTONE_STAIRS,
                        Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CUT_SANDSTONE_SLAB
                ),
                // Ocean
                List.of(
                        Blocks.PRISMARINE, Blocks.PRISMARINE_SLAB, Blocks.PRISMARINE_STAIRS,
                        Blocks.PRISMARINE_BRICKS, Blocks.PRISMARINE_BRICK_SLAB, Blocks.PRISMARINE_BRICK_STAIRS,
                        Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE, Blocks.DARK_PRISMARINE_STAIRS,
                        Blocks.DARK_PRISMARINE_SLAB, Blocks.SEA_LANTERN, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND,
                        Blocks.SEAGRASS, Blocks.TALL_SEAGRASS, Blocks.KELP_PLANT, Blocks.BRAIN_CORAL,
                        Blocks.BUBBLE_CORAL, Blocks.FIRE_CORAL, Blocks.TUBE_CORAL, Blocks.HORN_CORAL,
                        Blocks.BRAIN_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK,
                        Blocks.TUBE_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK, Blocks.IRON_BARS,
                        BTBlocks.BT_OCEAN_SPAWNER.get(), BTBlocks.OCEAN_CHEST.get(), BTBlocks.OCEAN_GOLEM_CHEST.get()
                )
        );

        towerSpawnerAmounts = List.of(
                Arrays.asList(2, 2, 2, 2, 3, 3, 3, 4), // 21
                Arrays.asList(2, 2, 2, 3, 3, 3, 4, 4), // 23
                Arrays.asList(2, 2, 3, 3, 3, 4, 4, 4), // 25
                Arrays.asList(2, 3, 3, 3, 3, 4, 4, 5), // 27
                Arrays.asList(3, 3, 3, 3, 4, 4, 4, 5), // 29
                Arrays.asList(3, 3, 3, 4, 4, 4, 5, 5) // 31
        );

        towerChestUnlocking = List.of(
                Arrays.asList(6, 14, 21),
                Arrays.asList(9, 23),
                Arrays.asList(10, 25),
                Arrays.asList(11, 27),
                Arrays.asList(12, 29),
                Arrays.asList(13, 31)
        );

        // List of spawner data per 2 floors per tower
        towerSpawnerData = List.of(
                // Land
                List.of(
                        // Floor 1-2 data
                        // minSpawnDelay, maxSpawnDelay, spawnCount, maxNearbyEntities, requiredPlayerRange, spawnRange
                        Arrays.asList(200, 240, 2, 8, 10, 6),
                        // Floor 3-4
                        Arrays.asList(180, 220, 3, 10, 10, 6),
                        // Floor 5-6
                        Arrays.asList(120, 160, 3, 10, 10, 6),
                        // Floor 7-8
                        Arrays.asList(100, 140, 4, 12, 10, 6)
                ),
                // Ocean
                List.of(
                        // Floor 1-2 data
                        Arrays.asList(240, 280, 2, 8, 10, 6),
                        // Floor 3-4
                        Arrays.asList(220, 260, 3, 10, 10, 6),
                        // Floor 5-6
                        Arrays.asList(160, 200, 3, 10, 10, 6),
                        // Floor 7-8
                        Arrays.asList(140, 180, 4, 12, 10, 6)
                )
        );

        towerMobs = List.of(
                new ArrayList<>(),
                new ArrayList<>()
        );

        for (String id : landTowerMobs.get()) {
            towerMobs.get(0).add(EntityType.byString(id).orElseThrow());
        }

        for (String id : oceanTowerMobs.get()) {
            towerMobs.get(1).add(EntityType.byString(id).orElseThrow());
        }

    }
}
