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
    public static HashMap<String, Pair<List<List<Item>>, List<List<Float>>>> lootMap;
    public static ArrayList<String> lootNames;

    public static final List<List<Item>> meatPool;
    public static final List<List<Item>> veggiePool;
    public static final List<List<Item>> cookedPool;
    public static final List<List<Item>> gemsPool;
    public static final List<List<Item>> metalsPool;
    public static final List<List<Item>> orePool;
    public static final List<List<Item>> buildingBlocksPool;
    public static final List<List<Item>> libraryPool;
    public static final List<List<Item>> weaponPool;
    public static final List<List<Item>> armorPool;
    public static final List<List<Item>> toolPool;
    public static final List<List<Item>> consumablePool;
    public static final List<List<Item>> bedsidePool;
    public static final List<List<Item>> plantsPool;
    public static final List<List<Item>> waterPlantsPool;
    public static final List<List<Item>> treePlantsPool;

    public static final List<List<Float>> meatPoolAmounts;
    public static final List<List<Float>> veggiePoolAmounts;
    public static final List<List<Float>> cookedPoolAmounts;
    public static final List<List<Float>> gemsPoolAmounts;
    public static final List<List<Float>> metalsPoolAmounts;
    public static final List<List<Float>> orePoolAmounts;
    public static final List<List<Float>> buildingBlocksPoolAmounts;
    public static final List<List<Float>> libraryPoolAmounts;
    public static final List<List<Float>> weaponPoolAmounts;
    public static final List<List<Float>> armorPoolAmounts;
    public static final List<List<Float>> toolPoolAmounts;
    public static final List<List<Float>> consumablePoolAmounts;
    public static final List<List<Float>> bedsidePoolAmounts;
    public static final List<List<Float>> plantsPoolAmounts;
    public static final List<List<Float>> waterPlantsPoolAmounts;
    public static final List<List<Float>> treePlantsPoolAmounts;
    

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
                landTowerMobs.get().stream()
                        .map(entityName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityName)))
                        .collect(Collectors.toList()),
                oceanTowerMobs.get().stream()
                        .map(entityName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityName)))
                        .collect(Collectors.toList())
        );

        meatPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.PUFFERFISH, Items.TROPICAL_FISH, Items.ROTTEN_FLESH, Items.SPIDER_EYE));
                    addAll(meatPoolExtra.get().stream().filter(itemName -> (meatPoolRarity.get().get(meatPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.SALMON, Items.COD, Items.MUTTON, Items.CHICKEN));
                    addAll(meatPoolExtra.get().stream().filter(itemName -> (meatPoolRarity.get().get(meatPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BEEF, Items.RABBIT, Items.PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_COD));
                    addAll(meatPoolExtra.get().stream().filter(itemName -> (meatPoolRarity.get().get(meatPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_SALMON));
                    addAll(meatPoolExtra.get().stream().filter(itemName -> (meatPoolRarity.get().get(meatPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.COOKED_PORKCHOP, Items.COOKED_BEEF));
                    addAll(meatPoolExtra.get().stream().filter(itemName -> (meatPoolRarity.get().get(meatPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4   -Rabbit Stew added via config
        );
        
        meatPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 2.4f, 1.2f));
                    addAll(meatPoolAmount.get().stream().filter(itemRange -> (meatPoolRarity.get().get(meatPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(meatPoolAmount.get().stream().filter(itemRange -> (meatPoolRarity.get().get(meatPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(meatPoolAmount.get().stream().filter(itemRange -> (meatPoolRarity.get().get(meatPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f));
                    addAll(meatPoolAmount.get().stream().filter(itemRange -> (meatPoolRarity.get().get(meatPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f));
                    addAll(meatPoolAmount.get().stream().filter(itemRange -> (meatPoolRarity.get().get(meatPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        veggiePool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DRIED_KELP, Items.POISONOUS_POTATO, Items.POTATO, Items.BEETROOT));
                    addAll(veggiePoolExtra.get().stream().filter(itemName -> (veggiePoolRarity.get().get(veggiePoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.MELON_SLICE));
                    addAll(veggiePoolExtra.get().stream().filter(itemName -> (veggiePoolRarity.get().get(veggiePoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CHORUS_FRUIT, Items.CARROT, Items.APPLE, Items.BAKED_POTATO));
                    addAll(veggiePoolExtra.get().stream().filter(itemName -> (veggiePoolRarity.get().get(veggiePoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GOLDEN_CARROT, Items.HONEY_BOTTLE));
                    addAll(veggiePoolExtra.get().stream().filter(itemName -> (veggiePoolRarity.get().get(veggiePoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE));
                    addAll(veggiePoolExtra.get().stream().filter(itemName -> (veggiePoolRarity.get().get(veggiePoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        veggiePoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(3.6f, 1.1f, 1.3f, 1.3f));
                    addAll(veggiePoolAmount.get().stream().filter(itemRange -> (veggiePoolRarity.get().get(veggiePoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(4.8f, 4.8f, 2.4f));
                    addAll(veggiePoolAmount.get().stream().filter(itemRange -> (veggiePoolRarity.get().get(veggiePoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.3f, 2.4f, 2.4f, 1.3f));
                    addAll(veggiePoolAmount.get().stream().filter(itemRange -> (veggiePoolRarity.get().get(veggiePoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 1.2f));
                    addAll(veggiePoolAmount.get().stream().filter(itemRange -> (veggiePoolRarity.get().get(veggiePoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f));
                    addAll(veggiePoolAmount.get().stream().filter(itemRange -> (veggiePoolRarity.get().get(veggiePoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        cookedPool = List.of(
                new ArrayList<>() {{
                    add(Items.COOKIE);
                    addAll(cookedPoolExtra.get().stream().filter(itemName -> (cookedPoolRarity.get().get(cookedPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    add(Items.BREAD);
                    addAll(cookedPoolExtra.get().stream().filter(itemName -> (cookedPoolRarity.get().get(cookedPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BEETROOT_SOUP, Items.MUSHROOM_STEW));
                    addAll(cookedPoolExtra.get().stream().filter(itemName -> (cookedPoolRarity.get().get(cookedPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.PUMPKIN_PIE, Items.CAKE));
                    addAll(cookedPoolExtra.get().stream().filter(itemName -> (cookedPoolRarity.get().get(cookedPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    add(Items.RABBIT_STEW);
                    addAll(cookedPoolExtra.get().stream().filter(itemName -> (cookedPoolRarity.get().get(cookedPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        cookedPoolAmounts = List.of(
                new ArrayList<>() {{
                    add(4.8f);
                    addAll(cookedPoolAmount.get().stream().filter(itemRange -> (cookedPoolRarity.get().get(cookedPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    add(2.5f);
                    addAll(cookedPoolAmount.get().stream().filter(itemRange -> (cookedPoolRarity.get().get(cookedPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f));
                    addAll(cookedPoolAmount.get().stream().filter(itemRange -> (cookedPoolRarity.get().get(cookedPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f));
                    addAll(cookedPoolAmount.get().stream().filter(itemRange -> (cookedPoolRarity.get().get(cookedPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    add(1.1f);
                    addAll(cookedPoolAmount.get().stream().filter(itemRange -> (cookedPoolRarity.get().get(cookedPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        gemsPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GLOWSTONE, Items.REDSTONE));
                    addAll(gemsPoolExtra.get().stream().filter(itemName -> (gemsPoolRarity.get().get(gemsPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.QUARTZ, Items.AMETHYST_SHARD));
                    addAll(gemsPoolExtra.get().stream().filter(itemName -> (gemsPoolRarity.get().get(gemsPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.ENDER_PEARL, Items.EMERALD));
                    addAll(gemsPoolExtra.get().stream().filter(itemName -> (gemsPoolRarity.get().get(gemsPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GHAST_TEAR, Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS));
                    addAll(gemsPoolExtra.get().stream().filter(itemName -> (gemsPoolRarity.get().get(gemsPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.ENDER_EYE, Items.ECHO_SHARD));
                    addAll(gemsPoolExtra.get().stream().filter(itemName -> (gemsPoolRarity.get().get(gemsPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        gemsPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.8f, 2.8f));
                    addAll(gemsPoolAmount.get().stream().filter(itemRange -> (gemsPoolRarity.get().get(gemsPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f));
                    addAll(gemsPoolAmount.get().stream().filter(itemRange -> (gemsPoolRarity.get().get(gemsPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.3f, 1.4f));
                    addAll(gemsPoolAmount.get().stream().filter(itemRange -> (gemsPoolRarity.get().get(gemsPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 2.4f, 2.4f));
                    addAll(gemsPoolAmount.get().stream().filter(itemRange -> (gemsPoolRarity.get().get(gemsPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 1.3f));
                    addAll(gemsPoolAmount.get().stream().filter(itemRange -> (gemsPoolRarity.get().get(gemsPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        metalsPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.RAW_IRON, Items.RAW_GOLD, Items.RAW_COPPER));
                    addAll(metalsPoolExtra.get().stream().filter(itemName -> (metalsPoolRarity.get().get(metalsPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.IRON_INGOT, Items.GOLD_INGOT, Items.COPPER_INGOT, Items.RAW_IRON_BLOCK, Items.RAW_GOLD_BLOCK));
                    addAll(metalsPoolExtra.get().stream().filter(itemName -> (metalsPoolRarity.get().get(metalsPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.COPPER_BLOCK, Items.IRON_BLOCK, Items.GOLD_BLOCK, Items.RAW_COPPER_BLOCK));
                    addAll(metalsPoolExtra.get().stream().filter(itemName -> (metalsPoolRarity.get().get(metalsPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DIAMOND, Items.NETHERITE_SCRAP));
                    addAll(metalsPoolExtra.get().stream().filter(itemName -> (metalsPoolRarity.get().get(metalsPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.NETHERITE_INGOT, Items.DIAMOND_BLOCK));
                    addAll(metalsPoolExtra.get().stream().filter(itemName -> (metalsPoolRarity.get().get(metalsPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        metalsPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.8f, 2.8f, 2.8f));
                    addAll(metalsPoolAmount.get().stream().filter(itemRange -> (metalsPoolRarity.get().get(metalsPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.8f, 2.8f, 2.8f, 1.2f, 1.2f));
                    addAll(metalsPoolAmount.get().stream().filter(itemRange -> (metalsPoolRarity.get().get(metalsPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 1.2f, 1.2f, 1.2f));
                    addAll(metalsPoolAmount.get().stream().filter(itemRange -> (metalsPoolRarity.get().get(metalsPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 1.1f));
                    addAll(metalsPoolAmount.get().stream().filter(itemRange -> (metalsPoolRarity.get().get(metalsPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f));
                    addAll(metalsPoolAmount.get().stream().filter(itemRange -> (metalsPoolRarity.get().get(metalsPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        orePool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.COAL_ORE, Items.DEEPSLATE_COAL_ORE, Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE));
                    addAll(orePoolExtra.get().stream().filter(itemName -> (orePoolRarity.get().get(orePoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.REDSTONE_ORE, Items.DEEPSLATE_REDSTONE_ORE));
                    addAll(orePoolExtra.get().stream().filter(itemName -> (orePoolRarity.get().get(orePoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GOLD_ORE, Items.EMERALD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.DEEPSLATE_EMERALD_ORE));
                    addAll(orePoolExtra.get().stream().filter(itemName -> (orePoolRarity.get().get(orePoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DIAMOND_ORE, Items.LAPIS_ORE, Items.DEEPSLATE_LAPIS_ORE, Items.DEEPSLATE_DIAMOND_ORE));
                    addAll(orePoolExtra.get().stream().filter(itemName -> (orePoolRarity.get().get(orePoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    add(Items.ANCIENT_DEBRIS);
                    addAll(orePoolExtra.get().stream().filter(itemName -> (orePoolRarity.get().get(orePoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        orePoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.3f, 1.3f, 1.3f, 1.3f));
                    addAll(orePoolAmount.get().stream().filter(itemRange -> (orePoolRarity.get().get(orePoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.3f, 1.3f));
                    addAll(orePoolAmount.get().stream().filter(itemRange -> (orePoolRarity.get().get(orePoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.3f, 1.3f, 1.3f, 1.3f));
                    addAll(orePoolAmount.get().stream().filter(itemRange -> (orePoolRarity.get().get(orePoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 1.3f,1.3f, 1.2f));
                    addAll(orePoolAmount.get().stream().filter(itemRange -> (orePoolRarity.get().get(orePoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    add(1.1f);
                    addAll(orePoolAmount.get().stream().filter(itemRange -> (orePoolRarity.get().get(orePoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        buildingBlocksPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(
                            Items.STONE_BRICKS, Items.STONE_BRICK_SLAB, Items.STONE_BRICK_STAIRS, Items.STONE_BRICK_WALL,
                            Items.GRANITE, Items.GRANITE_SLAB, Items.GRANITE_STAIRS, Items.GRANITE_WALL,
                            Items.DIORITE, Items.DIORITE_SLAB, Items.DIORITE_STAIRS, Items.DIORITE_WALL,
                            Items.ANDESITE, Items.ANDESITE_SLAB, Items.ANDESITE_STAIRS, Items.ANDESITE_WALL
                    ));
                    addAll(buildingBlocksPoolExtra.get().stream().filter(itemName -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(
                            Items.MOSSY_STONE_BRICKS, Items.MOSSY_STONE_BRICK_SLAB, Items.MOSSY_STONE_BRICK_STAIRS, Items.MOSSY_STONE_BRICK_WALL,
                            Items.POLISHED_GRANITE, Items.POLISHED_GRANITE_SLAB, Items.POLISHED_GRANITE_STAIRS,
                            Items.POLISHED_DIORITE, Items.POLISHED_DIORITE_SLAB, Items.POLISHED_DIORITE_STAIRS,
                            Items.POLISHED_ANDESITE, Items.POLISHED_ANDESITE_SLAB, Items.POLISHED_ANDESITE_STAIRS
                    ));
                    addAll(buildingBlocksPoolExtra.get().stream().filter(itemName -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CRACKED_STONE_BRICKS, Items.CHISELED_STONE_BRICKS, Items.SMOOTH_STONE, Items.BLACKSTONE, Items.BLACKSTONE_SLAB, Items.BLACKSTONE_STAIRS));
                    addAll(buildingBlocksPoolExtra.get().stream().filter(itemName -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.OBSIDIAN, Items.QUARTZ_BLOCK, Items.QUARTZ_BRICKS, Items.QUARTZ_SLAB, Items.QUARTZ_PILLAR, Items.QUARTZ_STAIRS));
                    addAll(buildingBlocksPoolExtra.get().stream().filter(itemName -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CRYING_OBSIDIAN, Items.GILDED_BLACKSTONE, Items.SMOOTH_QUARTZ, Items.SMOOTH_QUARTZ_SLAB, Items.SMOOTH_QUARTZ_STAIRS));
                    addAll(buildingBlocksPoolExtra.get().stream().filter(itemName -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        buildingBlocksPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(
                            4.8f, 3.6f, 3.6f, 3.6f,
                            4.8f, 3.6f, 3.6f, 3.6f,
                            4.8f, 3.9f, 3.6f, 3.6f,
                            4.8f, 3.6f, 3.6f, 3.6f
                    ));
                    addAll(buildingBlocksPoolAmount.get().stream().filter(itemRange -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(
                        4.8f, 3.6f, 3.6f, 3.6f,
                        4.8f, 3.6f, 3.6f,
                        4.8f, 3.6f, 3.6f,
                        4.8f, 3.6f, 3.6f
                    ));
                    addAll(buildingBlocksPoolAmount.get().stream().filter(itemRange -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(4.8f, 4.8f, 4.12f, 2.6f, 2.6f,  2.6f));
                    addAll(buildingBlocksPoolAmount.get().stream().filter(itemRange -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.6f, 2.6f, 2.6f, 2.6f));
                    addAll(buildingBlocksPoolAmount.get().stream().filter(itemRange -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.4f, 1.4f, 2.6f, 2.6f, 2.6f));
                    addAll(buildingBlocksPoolAmount.get().stream().filter(itemRange -> (buildingBlocksPoolRarity.get().get(buildingBlocksPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );


        libraryPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.LEATHER, Items.FEATHER, Items.INK_SAC));
                    addAll(libraryPoolExtra.get().stream().filter(itemName -> (libraryPoolRarity.get().get(libraryPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.STRING, Items.PAPER));
                    addAll(libraryPoolExtra.get().stream().filter(itemName -> (libraryPoolRarity.get().get(libraryPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BOOK, Items.WRITABLE_BOOK));
                    addAll(libraryPoolExtra.get().stream().filter(itemName -> (libraryPoolRarity.get().get(libraryPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.EMERALD, Items.BOOKSHELF));
                    addAll(libraryPoolExtra.get().stream().filter(itemName -> (libraryPoolRarity.get().get(libraryPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DIAMOND, Items.CHISELED_BOOKSHELF));
                    addAll(libraryPoolExtra.get().stream().filter(itemName -> (libraryPoolRarity.get().get(libraryPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        libraryPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.6f, 2.4f, 1.4f));
                    addAll(libraryPoolAmount.get().stream().filter(itemRange -> (libraryPoolRarity.get().get(libraryPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 3.6f));
                    addAll(libraryPoolAmount.get().stream().filter(itemRange -> (libraryPoolRarity.get().get(libraryPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(3.3f, 1.1f));
                    addAll(libraryPoolAmount.get().stream().filter(itemRange -> (libraryPoolRarity.get().get(libraryPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.4f, 1.3f));
                    addAll(libraryPoolAmount.get().stream().filter(itemRange -> (libraryPoolRarity.get().get(libraryPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.3f, 1.3f));
                    addAll(libraryPoolAmount.get().stream().filter(itemRange -> (libraryPoolRarity.get().get(libraryPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        weaponPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.WOODEN_SWORD, Items.WOODEN_AXE, Items.ARROW));
                    addAll(weaponPoolExtra.get().stream().filter(itemName -> (weaponPoolRarity.get().get(weaponPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GOLDEN_SWORD, Items.GOLDEN_AXE, Items.SPECTRAL_ARROW));
                    addAll(weaponPoolExtra.get().stream().filter(itemName -> (weaponPoolRarity.get().get(weaponPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.IRON_SWORD, Items.IRON_AXE, Items.BOW, Items.CROSSBOW));
                    addAll(weaponPoolExtra.get().stream().filter(itemName -> (weaponPoolRarity.get().get(weaponPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DIAMOND_SWORD, Items.DIAMOND_AXE));
                    addAll(weaponPoolExtra.get().stream().filter(itemName -> (weaponPoolRarity.get().get(weaponPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.NETHERITE_SWORD, Items.NETHERITE_AXE, Items.TRIDENT));
                    addAll(weaponPoolExtra.get().stream().filter(itemName -> (weaponPoolRarity.get().get(weaponPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        weaponPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f));
                    addAll(weaponPoolAmount.get().stream().filter(itemRange -> (weaponPoolRarity.get().get(weaponPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f));
                    addAll(weaponPoolAmount.get().stream().filter(itemRange -> (weaponPoolRarity.get().get(weaponPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(weaponPoolAmount.get().stream().filter(itemRange -> (weaponPoolRarity.get().get(weaponPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f));
                    addAll(weaponPoolAmount.get().stream().filter(itemRange -> (weaponPoolRarity.get().get(weaponPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f));
                    addAll(weaponPoolAmount.get().stream().filter(itemRange -> (weaponPoolRarity.get().get(weaponPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        armorPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS));
                    addAll(armorPoolExtra.get().stream().filter(itemName -> (armorPoolRarity.get().get(armorPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.SHIELD));
                    addAll(armorPoolExtra.get().stream().filter(itemName -> (armorPoolRarity.get().get(armorPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS, Items.TURTLE_HELMET, Items.IRON_HORSE_ARMOR));
                    addAll(armorPoolExtra.get().stream().filter(itemName -> (armorPoolRarity.get().get(armorPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.DIAMOND_HORSE_ARMOR));
                    addAll(armorPoolExtra.get().stream().filter(itemName -> (armorPoolRarity.get().get(armorPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS, Items.ELYTRA));
                    addAll(armorPoolExtra.get().stream().filter(itemName -> (armorPoolRarity.get().get(armorPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        armorPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(armorPoolAmount.get().stream().filter(itemRange -> (armorPoolRarity.get().get(armorPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(armorPoolAmount.get().stream().filter(itemRange -> (armorPoolRarity.get().get(armorPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(armorPoolAmount.get().stream().filter(itemRange -> (armorPoolRarity.get().get(armorPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(armorPoolAmount.get().stream().filter(itemRange -> (armorPoolRarity.get().get(armorPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(armorPoolAmount.get().stream().filter(itemRange -> (armorPoolRarity.get().get(armorPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        toolPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.WOODEN_AXE, Items.WOODEN_PICKAXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.STONE_HOE, Items.FISHING_ROD, Items.CARROT_ON_A_STICK, Items.FLINT_AND_STEEL));
                    addAll(toolPoolExtra.get().stream().filter(itemName -> (toolPoolRarity.get().get(toolPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.STONE_AXE, Items.STONE_PICKAXE, Items.STONE_SWORD, Items.STONE_SHOVEL, Items.GOLDEN_HOE, Items.COMPASS,  Items.WARPED_FUNGUS_ON_A_STICK, Items.CLOCK, Items.SHEARS));
                    addAll(toolPoolExtra.get().stream().filter(itemName -> (toolPoolRarity.get().get(toolPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.IRON_HOE,Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SWORD, Items.IRON_SHOVEL, Items.DIAMOND_HOE, Items.LEAD));
                    addAll(toolPoolExtra.get().stream().filter(itemName -> (toolPoolRarity.get().get(toolPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.NETHERITE_HOE));
                    addAll(toolPoolExtra.get().stream().filter(itemName -> (toolPoolRarity.get().get(toolPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.NETHERITE_AXE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SWORD, Items.NETHERITE_SHOVEL, Items.SPYGLASS));
                    addAll(toolPoolExtra.get().stream().filter(itemName -> (toolPoolRarity.get().get(toolPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        toolPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(toolPoolAmount.get().stream().filter(itemRange -> (toolPoolRarity.get().get(toolPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(toolPoolAmount.get().stream().filter(itemRange -> (toolPoolRarity.get().get(toolPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.3f));
                    addAll(toolPoolAmount.get().stream().filter(itemRange -> (toolPoolRarity.get().get(toolPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(toolPoolAmount.get().stream().filter(itemRange -> (toolPoolRarity.get().get(toolPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.1f, 1.1f, 1.1f));
                    addAll(toolPoolAmount.get().stream().filter(itemRange -> (toolPoolRarity.get().get(toolPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        consumablePool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GLASS_BOTTLE, Items.BOWL, Items.POTION)); // Potion = Bottle of Water
                    addAll(consumablePoolExtra.get().stream().filter(itemName -> (consumablePoolRarity.get().get(consumablePoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.WHITE_DYE, Items.COOKIE, Items.FLINT_AND_STEEL, Items.WRITABLE_BOOK));
                    addAll(consumablePoolExtra.get().stream().filter(itemName -> (consumablePoolRarity.get().get(consumablePoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BEETROOT_SOUP, Items.HONEY_BOTTLE, Items.MUSHROOM_STEW, Items.MAP));
                    addAll(consumablePoolExtra.get().stream().filter(itemName -> (consumablePoolRarity.get().get(consumablePoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.GOLDEN_CARROT, Items.NAME_TAG, Items.RABBIT_STEW, Items.GOLDEN_APPLE));
                    addAll(consumablePoolExtra.get().stream().filter(itemName -> (consumablePoolRarity.get().get(consumablePoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.EXPERIENCE_BOTTLE, Items.SPLASH_POTION));
                    addAll(consumablePoolExtra.get().stream().filter(itemName -> (consumablePoolRarity.get().get(consumablePoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        consumablePoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.3f, 2.3f, 1.2f));
                    addAll(consumablePoolAmount.get().stream().filter(itemRange -> (consumablePoolRarity.get().get(consumablePoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.5f, 3.7f, 1.2f, 1.2f));
                    addAll(consumablePoolAmount.get().stream().filter(itemRange -> (consumablePoolRarity.get().get(consumablePoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 2.4f, 1.1f, 1.1f));
                    addAll(consumablePoolAmount.get().stream().filter(itemRange -> (consumablePoolRarity.get().get(consumablePoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 1.1f, 1.1f, 1.2f));
                    addAll(consumablePoolAmount.get().stream().filter(itemRange -> (consumablePoolRarity.get().get(consumablePoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.5f, 1.2f));
                    addAll(consumablePoolAmount.get().stream().filter(itemRange -> (consumablePoolRarity.get().get(consumablePoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        bedsidePool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.PAPER, Items.STICK, Items.LEATHER, Items.FEATHER));
                    addAll(bedsidePoolExtra.get().stream().filter(itemName -> (bedsidePoolRarity.get().get(bedsidePoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BOWL, Items.TORCH, Items.INK_SAC, Items.FLOWER_POT));
                    addAll(bedsidePoolExtra.get().stream().filter(itemName -> (bedsidePoolRarity.get().get(bedsidePoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.WRITABLE_BOOK, Items.GLOW_INK_SAC, Items.BOOK));
                    addAll(bedsidePoolExtra.get().stream().filter(itemName -> (bedsidePoolRarity.get().get(bedsidePoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.RABBIT_FOOT, Items.CLOCK, Items.BRUSH, Items.COMPASS));
                    addAll(bedsidePoolExtra.get().stream().filter(itemName -> (bedsidePoolRarity.get().get(bedsidePoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.AXOLOTL_BUCKET, Items.PHANTOM_MEMBRANE,Items.SPYGLASS));
                    addAll(bedsidePoolExtra.get().stream().filter(itemName -> (bedsidePoolRarity.get().get(bedsidePoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        bedsidePoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(bedsidePoolAmount.get().stream().filter(itemRange -> (bedsidePoolRarity.get().get(bedsidePoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 3.7f, 1.2f, 1.1f));
                    addAll(bedsidePoolAmount.get().stream().filter(itemRange -> (bedsidePoolRarity.get().get(bedsidePoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.2f, 2.4f, 1.3f));
                    addAll(bedsidePoolAmount.get().stream().filter(itemRange -> (bedsidePoolRarity.get().get(bedsidePoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.1f, 1.2f, 1.1f));
                    addAll(bedsidePoolAmount.get().stream().filter(itemRange -> (bedsidePoolRarity.get().get(bedsidePoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(1.1f, 1.3f, 1.1f));
                    addAll(bedsidePoolAmount.get().stream().filter(itemRange -> (bedsidePoolRarity.get().get(bedsidePoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        plantsPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.ALLIUM, Items.AZURE_BLUET, Items.CACTUS, Items.CORNFLOWER, Items.DANDELION, Items.OXEYE_DAISY, Items.POPPY, Items.GRASS, Items.TALL_GRASS));
                    addAll(plantsPoolExtra.get().stream().filter(itemName -> (plantsPoolRarity.get().get(plantsPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BAMBOO, Items.BLUE_ORCHID, Items.FERN, Items.GLOW_LICHEN, Items.LILY_OF_THE_VALLEY, Items.ORANGE_TULIP, Items.WHITE_TULIP, Items.RED_TULIP, Items.WHITE_TULIP, Items.PEONY, Items.ROSE_BUSH, Items.SUGAR_CANE, Items.VINE));
                    addAll(plantsPoolExtra.get().stream().filter(itemName -> (plantsPoolRarity.get().get(plantsPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.DEAD_BUSH, Items.HANGING_ROOTS, Items.LARGE_FERN, Items.LILAC, Items.MOSS_BLOCK, Items.MOSS_CARPET, Items.SUNFLOWER));
                    addAll(plantsPoolExtra.get().stream().filter(itemName -> (plantsPoolRarity.get().get(plantsPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CRIMSON_FUNGUS, Items.CRIMSON_ROOTS, Items.NETHER_WART, Items.NETHER_SPROUTS, Items.SHROOMLIGHT, Items.TWISTING_VINES, Items.WARPED_FUNGUS, Items.WARPED_ROOTS, Items.WEEPING_VINES));
                    addAll(plantsPoolExtra.get().stream().filter(itemName -> (plantsPoolRarity.get().get(plantsPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CHORUS_FLOWER, Items.PITCHER_PLANT, Items.PITCHER_POD, Items.TORCHFLOWER, Items.WITHER_ROSE , Items.SPORE_BLOSSOM));
                    addAll(plantsPoolExtra.get().stream().filter(itemName -> (plantsPoolRarity.get().get(plantsPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        plantsPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(plantsPoolAmount.get().stream().filter(itemRange -> (plantsPoolRarity.get().get(plantsPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(plantsPoolAmount.get().stream().filter(itemRange -> (plantsPoolRarity.get().get(plantsPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(plantsPoolAmount.get().stream().filter(itemRange -> (plantsPoolRarity.get().get(plantsPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(plantsPoolAmount.get().stream().filter(itemRange -> (plantsPoolRarity.get().get(plantsPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(plantsPoolAmount.get().stream().filter(itemRange -> (plantsPoolRarity.get().get(plantsPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        waterPlantsPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.KELP, Items.SEAGRASS));
                    addAll(waterPlantsPoolExtra.get().stream().filter(itemName -> (waterPlantsPoolRarity.get().get(waterPlantsPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.LILY_PAD, Items.SEA_PICKLE, Items.MANGROVE_ROOTS));
                    addAll(waterPlantsPoolExtra.get().stream().filter(itemName -> (waterPlantsPoolRarity.get().get(waterPlantsPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BIG_DRIPLEAF, Items.SMALL_DRIPLEAF, Items.MUDDY_MANGROVE_ROOTS));
                    addAll(waterPlantsPoolExtra.get().stream().filter(itemName -> (waterPlantsPoolRarity.get().get(waterPlantsPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BRAIN_CORAL, Items.BUBBLE_CORAL, Items.HORN_CORAL, Items.TUBE_CORAL));
                    addAll(waterPlantsPoolExtra.get().stream().filter(itemName -> (waterPlantsPoolRarity.get().get(waterPlantsPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BRAIN_CORAL_FAN, Items.BUBBLE_CORAL_FAN, Items.HORN_CORAL_FAN, Items.TUBE_CORAL_FAN));
                    addAll(waterPlantsPoolExtra.get().stream().filter(itemName -> (waterPlantsPoolRarity.get().get(waterPlantsPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        waterPlantsPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.6f, 2.6f));
                    addAll(waterPlantsPoolAmount.get().stream().filter(itemRange -> (waterPlantsPoolRarity.get().get(waterPlantsPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.6f));
                    addAll(waterPlantsPoolAmount.get().stream().filter(itemRange -> (waterPlantsPoolRarity.get().get(waterPlantsPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.6f, 2.4f, 2.4f));
                    addAll(waterPlantsPoolAmount.get().stream().filter(itemRange -> (waterPlantsPoolRarity.get().get(waterPlantsPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(waterPlantsPoolAmount.get().stream().filter(itemRange -> (waterPlantsPoolRarity.get().get(waterPlantsPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.4f, 2.4f, 2.4f));
                    addAll(waterPlantsPoolAmount.get().stream().filter(itemRange -> (waterPlantsPoolRarity.get().get(waterPlantsPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        treePlantsPool = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.BIRCH_SAPLING, Items.BIRCH_LEAVES, Items.OAK_SAPLING, Items.OAK_LEAVES, Items.SPRUCE_SAPLING, Items.SPRUCE_LEAVES));
                    addAll(treePlantsPoolExtra.get().stream().filter(itemName -> (treePlantsPoolRarity.get().get(treePlantsPoolExtra.get().indexOf(itemName)) == 0)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 0
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.ACACIA_SAPLING, Items.ACACIA_LEAVES, Items.DARK_OAK_SAPLING, Items.DARK_OAK_LEAVES, Items.JUNGLE_SAPLING, Items.JUNGLE_LEAVES));
                    addAll(treePlantsPoolExtra.get().stream().filter(itemName -> (treePlantsPoolRarity.get().get(treePlantsPoolExtra.get().indexOf(itemName)) == 1)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 1
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.AZALEA, Items.AZALEA_LEAVES, Items.MUSHROOM_STEM, Items.MANGROVE_ROOTS));
                    addAll(treePlantsPoolExtra.get().stream().filter(itemName -> (treePlantsPoolRarity.get().get(treePlantsPoolExtra.get().indexOf(itemName)) == 2)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 2
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.FLOWERING_AZALEA, Items.FLOWERING_AZALEA_LEAVES, Items.MUDDY_MANGROVE_ROOTS));
                    addAll(treePlantsPoolExtra.get().stream().filter(itemName -> (treePlantsPoolRarity.get().get(treePlantsPoolExtra.get().indexOf(itemName)) == 3)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }}, // Rarity 3
                new ArrayList<>() {{
                    addAll(Arrays.asList(Items.CHERRY_SAPLING, Items.CHERRY_LEAVES, Items.MANGROVE_PROPAGULE, Items.MANGROVE_LEAVES));
                    addAll(treePlantsPoolExtra.get().stream().filter(itemName -> (treePlantsPoolRarity.get().get(treePlantsPoolExtra.get().indexOf(itemName)) == 4)).map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).toList());
                }} // Rarity 4
        );

        treePlantsPoolAmounts = List.of(
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.8f, 2.4f, 2.8f, 2.4f, 2.8f));
                    addAll(treePlantsPoolAmount.get().stream().filter(itemRange -> (treePlantsPoolRarity.get().get(treePlantsPoolAmount.get().indexOf(itemRange)) == 0)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.8f, 2.4f, 2.8f, 2.4f, 2.8f));
                    addAll(treePlantsPoolAmount.get().stream().filter(itemRange -> (treePlantsPoolRarity.get().get(treePlantsPoolAmount.get().indexOf(itemRange)) == 1)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.8f, 2.8f, 2.8f));
                    addAll(treePlantsPoolAmount.get().stream().filter(itemRange -> (treePlantsPoolRarity.get().get(treePlantsPoolAmount.get().indexOf(itemRange)) == 2)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.8f, 2.8f));
                    addAll(treePlantsPoolAmount.get().stream().filter(itemRange -> (treePlantsPoolRarity.get().get(treePlantsPoolAmount.get().indexOf(itemRange)) == 3)).toList());
                }},
                new ArrayList<>() {{
                    addAll(Arrays.asList(2.4f, 2.8f, 2.4f, 2.8f));
                    addAll(treePlantsPoolAmount.get().stream().filter(itemRange -> (treePlantsPoolRarity.get().get(treePlantsPoolAmount.get().indexOf(itemRange)) == 4)).toList());
                }}
        );

        lootMap = new HashMap<>();
        lootMap.put("Invalid", Pair.of(Collections.emptyList(), Collections.emptyList()));
        lootMap.put("Meat", Pair.of(meatPool, meatPoolAmounts)); // 1
        lootMap.put("Veggie", Pair.of(veggiePool,veggiePoolAmounts)); // 2
        lootMap.put("Cooked", Pair.of(cookedPool, cookedPoolAmounts)); // 3
        lootMap.put("Gem", Pair.of(gemsPool, gemsPoolAmounts)); // 4
        lootMap.put("Metal", Pair.of(metalsPool, metalsPoolAmounts)); // 5
        lootMap.put("Ore", Pair.of(orePool, orePoolAmounts)); // 6
        lootMap.put("Building Block", Pair.of(buildingBlocksPool, buildingBlocksPoolAmounts));
        lootMap.put("Library", Pair.of(libraryPool, libraryPoolAmounts)); // 8
        lootMap.put("Weapon", Pair.of(weaponPool, weaponPoolAmounts)); // 9
        lootMap.put("Armor", Pair.of(armorPool, armorPoolAmounts)); // 10
        lootMap.put("Tool", Pair.of(toolPool, toolPoolAmounts)); // 11
        lootMap.put("Consumable", Pair.of(consumablePool, consumablePoolAmounts));
        lootMap.put("Bedside", Pair.of(bedsidePool, bedsidePoolAmounts)); // 13
        lootMap.put("Plant", Pair.of(plantsPool, plantsPoolAmounts)); // 14
        lootMap.put("Water Plant", Pair.of(waterPlantsPool, waterPlantsPoolAmounts)); // 15
        lootMap.put("Tree Plant", Pair.of(treePlantsPool, treePlantsPoolAmounts)); // 16

        lootNames = new ArrayList<>(
                List.of(
                        "Invalid", "Meat", "Veggie", "Cooked", "Gem", "Metal",
                    "Ore", "Building Block", "Library", "Weapon",
                    "Armor", "Tool", "Consumable", "Bedside",
                    "Plant", "Water Plant", "Tree Plant"
                )
        );


        potions = ForgeRegistries.POTIONS.getValues().stream().toList();
        dyes = List.of(
                Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE, Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE,
                Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE, Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE,
                Items.BLUE_DYE, Items.BROWN_DYE, Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE
        );
    }
}
