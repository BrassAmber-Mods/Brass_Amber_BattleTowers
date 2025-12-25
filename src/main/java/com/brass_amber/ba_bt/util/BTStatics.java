package com.brass_amber.ba_bt.util;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;

public class BTStatics {
    public static final List<List<Integer>> towerSpawnerAmounts;
    public static final List<List<Integer>> towerChestUnlocking;
    public static final List<List<Block>> towerBlocks;
    public static final List<BlockState> towerBaseBlocks;
    public static final List<Block> icyOceanBlocks;
    public static List<List<EntityType<?>>> towerMobs;
    public static final List<List<List<Integer>>> towerSpawnerData;
    public static HashMap<String, Pair<List<List<Item>>, List<List<Double>>>> lootMap;
    public static ArrayList<String> lootNames;

    public static final List<Potion> potions;
    public static final List<Item> dyes;

    static {

        towerBlocks = List.of(
                // Land
                List.of(
                        Blocks.COBBLESTONE, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE_STAIRS, Blocks.CRACKED_STONE_BRICKS,
                        Blocks.STONE_BRICK_SLAB, Blocks.STONE_BRICKS, Blocks.STONE_BRICK_STAIRS,
                        Blocks.CHISELED_STONE_BRICKS, Blocks.GRAVEL, Blocks.SAND,
                        Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_STAIRS,
                        Blocks.SANDSTONE, Blocks.SANDSTONE_SLAB, Blocks.SANDSTONE_STAIRS,
                        Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CUT_SANDSTONE_SLAB,
                        BTBlocks.LAND_SPAWNER.get(), BTBlocks.LAND_CHEST.get(), BTBlocks.LAND_GOLEM_CHEST.get(),
                        BTBlocks.SPAWNER_MARKER.get()
                ),
                // Ocean
                List.of(
                        Blocks.PRISMARINE, Blocks.PRISMARINE_SLAB, Blocks.PRISMARINE_STAIRS, Blocks.PACKED_ICE,
                        Blocks.PRISMARINE_BRICKS, Blocks.PRISMARINE_BRICK_SLAB, Blocks.PRISMARINE_BRICK_STAIRS, Blocks.KELP_PLANT,
                        Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE, Blocks.DARK_PRISMARINE_STAIRS, Blocks.ICE,
                        Blocks.DARK_PRISMARINE_SLAB, Blocks.SEA_LANTERN, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND,
                        Blocks.SEAGRASS, Blocks.TALL_SEAGRASS, Blocks.KELP_PLANT, Blocks.BRAIN_CORAL,
                        Blocks.BUBBLE_CORAL, Blocks.FIRE_CORAL, Blocks.TUBE_CORAL, Blocks.HORN_CORAL,
                        Blocks.BRAIN_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK, Blocks.WARPED_HYPHAE,
                        Blocks.TUBE_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK, Blocks.IRON_BARS, Blocks.BONE_BLOCK,
                        Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER, Blocks.OXIDIZED_COPPER, Blocks.WARPED_STEM,
                        BTBlocks.OCEAN_SPAWNER.get(), BTBlocks.OCEAN_CHEST.get(), BTBlocks.OCEAN_GOLEM_CHEST.get(),
                        BTBlocks.SPAWNER_MARKER.get(), Blocks.LAPIS_BLOCK, Blocks.PRISMARINE_SLAB
                ),
                // Core
                List.of(
                        Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.DEEPSLATE_BRICK_WALL, Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_TILE_WALL,
                        Blocks.CHISELED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.DIAMOND_BLOCK, Blocks.CHISELED_NETHER_BRICKS,
                        Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.RED_STAINED_GLASS, Blocks.LAVA, Blocks.SAND,
                        Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.BASALT,
                        BTBlocks.CORRITE_BLOCK.get(), BTBlocks.CORRITE_SLAB.get(), BTBlocks.CORRITE_STAIR.get(), BTBlocks.CORRITE_WALL.get(),
                        BTBlocks.ACTIVE_CORRITE_BLOCK.get(), BTBlocks.ACTIVE_CORRITE_SLAB.get(), BTBlocks.ACTIVE_CORRITE_STAIR.get(),
                        BTBlocks.CORRITE_CHISELED_BOOKSHELF.get(), BTBlocks.CORRITE_LADDER.get(), BTBlocks.CORE_MATTER.get(),
                        BTBlocks.CORE_SPAWNER.get(), BTBlocks.CORE_CHEST.get(), BTBlocks.CORE_GOLEM_CHEST.get(),
                        BTBlocks.SPAWNER_MARKER.get()
                )
        );

        towerBaseBlocks = List.of(Blocks.STONE_BRICKS.defaultBlockState(), Blocks.PRISMARINE_BRICKS.defaultBlockState(), BTBlocks.CORRITE_BLOCK.get().defaultBlockState());

        icyOceanBlocks = List.of(Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.ICE, Blocks.PACKED_ICE);

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
                Arrays.asList(12, 25),
                Arrays.asList(13, 27),
                Arrays.asList(14, 29),
                Arrays.asList(15, 31)
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
                ),
                // Core
                List.of(
                        // Floor 1-2 data
                        Arrays.asList(180, 220, 2, 5, 12, 6),
                        // Floor 3-4
                        Arrays.asList(180, 220, 3, 5, 12, 6),
                        // Floor 5-6
                        Arrays.asList(160, 200, 3, 6, 12, 6),
                        // Floor 7-8
                        Arrays.asList(160, 200, 4, 6, 12, 6)
                )
        );

        towerMobs = List.of(
                landTowerMobs,
                oceanTowerMobs,
                coreTowerMobs
        );

        lootNames = new ArrayList<>(
                List.of(
                        "invalid", // 0
                        "meat", // 1
                        "veggie", // 2
                        "cooked", // 3
                        "gem", // 4
                        "metal", // 5
                        "ore", // 6
                        "land_tower_blocks", // 7
                        "library", // 8
                        "weapon", // 9
                        "armor", // 10
                        "tool", // 11
                        "consumable", // 12
                        "bedside", // 13
                        "plant", // 14
                        "water plant", // 15
                        "tree plant", // 16
                        "ocean_tower_blocks", // 17
                        "core_tower_blocks" // 18
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
