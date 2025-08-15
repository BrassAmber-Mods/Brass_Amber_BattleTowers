package com.brass_amber.ba_bt.util;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;

public class BTStatics {
    public static final List<List<ResourceKey<Biome>>> landTowerBiomes;
    public static final List<ResourceKey<Biome>> oceanTowerBiomes;
    public static final List<List<Integer>> towerSpawnerAmounts;
    public static final List<List<Integer>> towerChestUnlocking;
    public static final List<List<BlockState>> towerBlocks;
    public static final List<BlockState> towerBaseBlocks;
    public static final List<Block> icyOceanBlocks;
    public static List<List<EntityType<?>>> towerMobs;
    public static final List<List<List<Integer>>> towerSpawnerData;
    public static HashMap<String, Pair<List<List<Item>>, List<List<Double>>>> lootMap;
    public static ArrayList<String> lootNames;

    public static final List<Integer> minimumSeperations;
    public static final List<Integer> averageSeperations;

    public static final List<Potion> potions;
    public static final List<Item> dyes;

    static {

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

        oceanTowerBiomes = List.of(Biomes.DEEP_FROZEN_OCEAN, Biomes.FROZEN_OCEAN);

        towerBlocks = List.of(
                // Land
                List.of(
                        Blocks.COBBLESTONE.defaultBlockState(), Blocks.COBBLESTONE_SLAB.defaultBlockState(), Blocks.COBBLESTONE_STAIRS.defaultBlockState(), Blocks.CRACKED_STONE_BRICKS.defaultBlockState(),
                        Blocks.STONE_BRICK_SLAB.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICK_STAIRS.defaultBlockState(),
                        Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), Blocks.GRAVEL.defaultBlockState(), Blocks.SAND.defaultBlockState(),
                        Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState(), Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState(),
                        Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE_SLAB.defaultBlockState(), Blocks.SANDSTONE_STAIRS.defaultBlockState(),
                        Blocks.CHISELED_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE_SLAB.defaultBlockState(),
                        BTBlocks.LAND_SPAWNER.get().defaultBlockState(), BTBlocks.LAND_CHEST.get().defaultBlockState(), BTBlocks.LAND_GOLEM_CHEST.get().defaultBlockState(),
                        BTBlocks.SPAWNER_MARKER.get().defaultBlockState()
                ),
                // Ocean
                List.of(
                        Blocks.PRISMARINE.defaultBlockState(), Blocks.PRISMARINE_SLAB.defaultBlockState(), Blocks.PRISMARINE_STAIRS.defaultBlockState(), Blocks.PACKED_ICE.defaultBlockState(),
                        Blocks.PRISMARINE_BRICKS.defaultBlockState(), Blocks.PRISMARINE_BRICK_SLAB.defaultBlockState(), Blocks.PRISMARINE_BRICK_STAIRS.defaultBlockState(), Blocks.KELP_PLANT.defaultBlockState(),
                        Blocks.SEA_LANTERN.defaultBlockState(), Blocks.DARK_PRISMARINE.defaultBlockState(), Blocks.DARK_PRISMARINE_STAIRS.defaultBlockState(), Blocks.ICE.defaultBlockState(),
                        Blocks.DARK_PRISMARINE_SLAB.defaultBlockState(), Blocks.SEA_LANTERN.defaultBlockState(), Blocks.MAGMA_BLOCK.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(),
                        Blocks.SEAGRASS.defaultBlockState(), Blocks.TALL_SEAGRASS.defaultBlockState(), Blocks.KELP_PLANT.defaultBlockState(), Blocks.BRAIN_CORAL.defaultBlockState(),
                        Blocks.BUBBLE_CORAL.defaultBlockState(), Blocks.FIRE_CORAL.defaultBlockState(), Blocks.TUBE_CORAL.defaultBlockState(), Blocks.HORN_CORAL.defaultBlockState(),
                        Blocks.BRAIN_CORAL_BLOCK.defaultBlockState(), Blocks.BUBBLE_CORAL_BLOCK.defaultBlockState(), Blocks.FIRE_CORAL_BLOCK.defaultBlockState(), Blocks.WARPED_HYPHAE.defaultBlockState(),
                        Blocks.TUBE_CORAL_BLOCK.defaultBlockState(), Blocks.HORN_CORAL_BLOCK.defaultBlockState(), Blocks.IRON_BARS.defaultBlockState(), Blocks.BONE_BLOCK.defaultBlockState(),
                        Blocks.COPPER_BLOCK.defaultBlockState(), Blocks.EXPOSED_COPPER.defaultBlockState(), Blocks.OXIDIZED_COPPER.defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(),
                        BTBlocks.OCEAN_SPAWNER.get().defaultBlockState(), BTBlocks.OCEAN_CHEST.get().defaultBlockState(), BTBlocks.OCEAN_GOLEM_CHEST.get().defaultBlockState(),
                        BTBlocks.SPAWNER_MARKER.get().defaultBlockState(), Blocks.LAPIS_BLOCK.defaultBlockState(), Blocks.PRISMARINE_SLAB.defaultBlockState()
                )
        );

        towerBaseBlocks = List.of(Blocks.STONE_BRICKS.defaultBlockState(), Blocks.PRISMARINE_BRICKS.defaultBlockState());

        icyOceanBlocks = List.of(Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.ICE, Blocks.PACKED_ICE);

        minimumSeperations = List.of(landMinimumSeperation, oceanMinimumSeperation);
        averageSeperations = List.of(landAverageSeperationModifier, oceanAverageSeperationModifier);

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
                        Arrays.asList(180, 220, 1, 4, 11, 6),
                        // Floor 3-4
                        Arrays.asList(180, 220, 2, 4, 12, 6),
                        // Floor 5-6
                        Arrays.asList(160, 200, 3, 4, 13, 6),
                        // Floor 7-8
                        Arrays.asList(160, 200, 3, 4, 12, 6)
                ),
                // Ocean
                List.of(
                        // Floor 1-2 data
                        Arrays.asList(240, 280, 2, 3, 12, 8),
                        // Floor 3-4
                        Arrays.asList(240, 280, 3, 3, 13, 8),
                        // Floor 5-6
                        Arrays.asList(160, 200, 3, 4, 14, 8),
                        // Floor 7-8
                        Arrays.asList(160, 200, 4, 4, 13, 8)
                )
        );

        towerMobs = List.of(
                landTowerMobs,
                oceanTowerMobs
        );

        lootNames = new ArrayList<>(
                List.of(
                        "Invalid", // 0
                        "Meat", // 1
                        "Veggie", // 2
                        "Cooked", // 3
                        "Gem", // 4
                        "Metal", // 5
                        "Ore", // 6
                        "Land Tower Blocks", // 7
                        "Library", // 8
                        "Weapon", // 9
                        "Armor", // 10
                        "Tool", // 11
                        "Consumable", // 12
                        "Bedside", // 13
                        "Plant", // 14
                        "Water Plant", // 15
                        "Tree Plant", // 16
                        "Ocean Tower Blocks", // 17
                        "Core Tower Blocks" // 18
                )
        );


        potions = ForgeRegistries.POTIONS.getValues().stream().toList();
        dyes = List.of(
                Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE, Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE,
                Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE, Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE,
                Items.BLUE_DYE, Items.BROWN_DYE, Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE
        );
    };
    
}
