package com.brass_amber.ba_bt.item;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTRegistries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;

import java.util.List;

public class BTItemPools {

    public static final ResourceKey<ItemPool> INVALID = registerItemPoolKey("invalid");
    public static final ResourceKey<ItemPool> MEAT = registerItemPoolKey("meat");
    public static final ResourceKey<ItemPool> VEGGIE = registerItemPoolKey("veggie");
    public static final ResourceKey<ItemPool> COOKED = registerItemPoolKey("cooked");
    public static final ResourceKey<ItemPool> GEM = registerItemPoolKey("gem");
    public static final ResourceKey<ItemPool> METAL = registerItemPoolKey("metal");
    public static final ResourceKey<ItemPool> ORE = registerItemPoolKey("ore");
    public static final ResourceKey<ItemPool> LAND_TOWER_BLOCKS = registerItemPoolKey("land_tower_blocks");
    public static final ResourceKey<ItemPool> LIBRARY = registerItemPoolKey("library");
    public static final ResourceKey<ItemPool> WEAPON = registerItemPoolKey("weapon");
    public static final ResourceKey<ItemPool> ARMOR = registerItemPoolKey("armor");
    public static final ResourceKey<ItemPool> TOOL = registerItemPoolKey("tool");
    public static final ResourceKey<ItemPool> CONSUMABLE = registerItemPoolKey("consumable");
    public static final ResourceKey<ItemPool> BEDSIDE = registerItemPoolKey("bedside");
    public static final ResourceKey<ItemPool> PLANT = registerItemPoolKey("plant");
    public static final ResourceKey<ItemPool> WATER_PLANT = registerItemPoolKey("water_plant");
    public static final ResourceKey<ItemPool> TREE_PLANT = registerItemPoolKey("tree_plant");
    public static final ResourceKey<ItemPool> OCEAN_TOWER_BLOCKS = registerItemPoolKey("ocean_tower_blocks");
    public static final ResourceKey<ItemPool> CORE_TOWER_BLOCKS = registerItemPoolKey("core_tower_blocks");


    public static void bootstrap(BootstapContext<ItemPool> itemPoolContext) {
        itemPoolContext.register(
                INVALID,
                new ItemPool(
                        "invalid",
                        List.of(ItemPool.singlePoolItem(Items.AIR)),
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of()
                )
        );

        itemPoolContext.register(
                MEAT,
                new ItemPool(
                        "meat",
                        List.of(
                                ItemPool.singlePoolItem(Items.PUFFERFISH),
                                ItemPool.singlePoolItem(Items.TROPICAL_FISH),
                                ItemPool.offsetRangePoolItem(Items.ROTTEN_FLESH, 2, 4),
                                ItemPool.rangePoolItem(Items.SPIDER_EYE, 2)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.SALMON),
                                ItemPool.singlePoolItem(Items.COD),
                                ItemPool.singlePoolItem(Items.MUTTON),
                                ItemPool.singlePoolItem(Items.CHICKEN)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.BEEF),
                                ItemPool.singlePoolItem(Items.RABBIT),
                                ItemPool.singlePoolItem(Items.PORKCHOP),
                                ItemPool.singlePoolItem(Items.COOKED_RABBIT),
                                ItemPool.singlePoolItem(Items.COOKED_COD)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.COOKED_CHICKEN),
                                ItemPool.singlePoolItem(Items.COOKED_MUTTON),
                                ItemPool.singlePoolItem(Items.COOKED_SALMON)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.COOKED_PORKCHOP),
                                ItemPool.singlePoolItem(Items.COOKED_BEEF)
                                )
                )
        );

        itemPoolContext.register(
                VEGGIE,
                new ItemPool(
                        "veggie",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.DRIED_KELP, 3, 6),
                                ItemPool.singlePoolItem(Items.POISONOUS_POTATO),
                                ItemPool.rangePoolItem(Items.POTATO, 3),
                                ItemPool.rangePoolItem(Items.BEETROOT, 3)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.SWEET_BERRIES, 4, 8),
                                ItemPool.offsetRangePoolItem(Items.GLOW_BERRIES, 4, 8),
                                ItemPool.offsetRangePoolItem(Items.MELON_SLICE, 2, 4)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.CHORUS_FRUIT, 3),
                                ItemPool.offsetRangePoolItem(Items.CARROT, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.APPLE, 2, 4),
                                ItemPool.rangePoolItem(Items.BAKED_POTATO, 3)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.GOLDEN_CARROT, 2),
                                ItemPool.rangePoolItem(Items.HONEY_BOTTLE, 2)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.GOLDEN_APPLE),
                                ItemPool.singlePoolItem(Items.ENCHANTED_GOLDEN_APPLE)
                                )
                )
        );

        itemPoolContext.register(
                COOKED,
                new ItemPool(
                        "cooked",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.COOKIE, 4, 8)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.BREAD, 2, 5)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.BEETROOT_SOUP),
                                ItemPool.singlePoolItem(Items.MUSHROOM_STEW)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.PUMPKIN_PIE),
                                ItemPool.singlePoolItem(Items.CAKE)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.RABBIT_STEW)
                                )
                )
        );

        itemPoolContext.register(
                GEM,
                new ItemPool(
                        "gem",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.GLOWSTONE_DUST, 2, 8),
                                ItemPool.offsetRangePoolItem(Items.REDSTONE, 2, 8)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.QUARTZ, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.AMETHYST_SHARD, 2, 4)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.ENDER_PEARL, 3),
                                ItemPool.rangePoolItem(Items.EMERALD, 4)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.GHAST_TEAR),
                                ItemPool.offsetRangePoolItem(Items.PRISMARINE_SHARD, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.PRISMARINE_CRYSTALS, 2, 4)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.ENDER_EYE, 2),
                                ItemPool.rangePoolItem(Items.ECHO_SHARD, 3)
                                )
                )
        );

        itemPoolContext.register(
                METAL,
                new ItemPool(
                        "metal",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.RAW_IRON, 2, 8),
                                ItemPool.offsetRangePoolItem(Items.RAW_GOLD, 2, 8),
                                ItemPool.offsetRangePoolItem(Items.RAW_COPPER, 2, 8)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.IRON_INGOT, 2, 6),
                                ItemPool.offsetRangePoolItem(Items.GOLD_INGOT, 2, 6),
                                ItemPool.offsetRangePoolItem(Items.COPPER_INGOT, 2, 6),
                                ItemPool.singlePoolItem(Items.RAW_IRON_BLOCK),
                                ItemPool.singlePoolItem(Items.RAW_GOLD_BLOCK)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.IRON_BLOCK),
                                ItemPool.singlePoolItem(Items.GOLD_BLOCK),
                                ItemPool.singlePoolItem(Items.COPPER_BLOCK),
                                ItemPool.singlePoolItem(Items.RAW_COPPER_BLOCK)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.DIAMOND, 2),
                                ItemPool.singlePoolItem(Items.NETHERITE_SCRAP)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.NETHERITE_INGOT),
                                ItemPool.singlePoolItem(Items.DIAMOND_BLOCK)
                                )
                )
        );

        itemPoolContext.register(
                ORE,
                new ItemPool(
                        "ore",
                        List.of(
                                ItemPool.rangePoolItem(Items.COAL_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_COAL_ORE, 3),
                                ItemPool.rangePoolItem(Items.REDSTONE_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_REDSTONE_ORE, 3)
                                ),
                        List.of(

                                ItemPool.rangePoolItem(Items.IRON_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_IRON_ORE, 3),
                                ItemPool.rangePoolItem(Items.COPPER_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_COPPER_ORE, 3)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.GOLD_ORE, 3),
                                ItemPool.rangePoolItem(Items.EMERALD_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_GOLD_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_EMERALD_ORE, 3)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.DIAMOND_ORE, 2),
                                ItemPool.rangePoolItem(Items.LAPIS_ORE, 3),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_DIAMOND_ORE, 2),
                                ItemPool.rangePoolItem(Items.DEEPSLATE_LAPIS_ORE, 3)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.ANCIENT_DEBRIS)
                                )
                )
        );

        itemPoolContext.register(
                LAND_TOWER_BLOCKS,
                new ItemPool(
                        "land_tower_blocks",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of()
                )
        );

        itemPoolContext.register(
                LIBRARY,
                new ItemPool(
                        "library",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.LEATHER, 2, 6),
                                ItemPool.offsetRangePoolItem(Items.FEATHER, 2, 4),
                                ItemPool.rangePoolItem(Items.INK_SAC, 4)
                        ),
                        List.of(

                                ItemPool.offsetRangePoolItem(Items.STRING, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.PAPER, 3, 6)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.BOOK, 2, 3),
                                ItemPool.singlePoolItem(Items.WRITABLE_BOOK)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.EMERALD, 4),
                                ItemPool.rangePoolItem(Items.BOOKSHELF, 3)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.DIAMOND, 2, 3),
                                ItemPool.rangePoolItem(Items.CHISELED_BOOKSHELF, 3)
                                )
                )
        );

        itemPoolContext.register(
                WEAPON,
                new ItemPool(
                        "weapon",
                        List.of(
                                ItemPool.singlePoolItem(Items.WOODEN_SWORD),
                                ItemPool.singlePoolItem(Items.WOODEN_AXE),
                                ItemPool.singlePoolItem(Items.ARROW)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.GOLDEN_SWORD),
                                ItemPool.singlePoolItem(Items.GOLDEN_AXE),
                                ItemPool.singlePoolItem(Items.SPECTRAL_ARROW)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.IRON_SWORD),
                                ItemPool.singlePoolItem(Items.IRON_AXE),
                                ItemPool.singlePoolItem(Items.BOW),
                                ItemPool.singlePoolItem(Items.CROSSBOW)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.DIAMOND_SWORD),
                                ItemPool.singlePoolItem(Items.DIAMOND_AXE),
                                ItemPool.singlePoolItem(Items.TRIDENT)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.NETHERITE_SWORD),
                                ItemPool.singlePoolItem(Items.NETHERITE_AXE)
                                )
                )
        );

        itemPoolContext.register(
                ARMOR,
                new ItemPool(
                        "armor",
                        List.of(
                                ItemPool.singlePoolItem(Items.LEATHER_HELMET),
                                ItemPool.singlePoolItem(Items.LEATHER_CHESTPLATE),
                                ItemPool.singlePoolItem(Items.LEATHER_LEGGINGS),
                                ItemPool.singlePoolItem(Items.LEATHER_BOOTS),
                                ItemPool.singlePoolItem(Items.GOLDEN_HELMET),
                                ItemPool.singlePoolItem(Items.GOLDEN_CHESTPLATE),
                                ItemPool.singlePoolItem(Items.GOLDEN_LEGGINGS),
                                ItemPool.singlePoolItem(Items.GOLDEN_BOOTS)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.IRON_HELMET),
                                ItemPool.singlePoolItem(Items.IRON_CHESTPLATE),
                                ItemPool.singlePoolItem(Items.IRON_LEGGINGS),
                                ItemPool.singlePoolItem(Items.IRON_BOOTS),
                                ItemPool.singlePoolItem(Items.GOLDEN_HORSE_ARMOR),
                                ItemPool.singlePoolItem(Items.SHIELD)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.CHAINMAIL_HELMET),
                                ItemPool.singlePoolItem(Items.CHAINMAIL_CHESTPLATE),
                                ItemPool.singlePoolItem(Items.CHAINMAIL_LEGGINGS),
                                ItemPool.singlePoolItem(Items.CHAINMAIL_BOOTS),
                                ItemPool.singlePoolItem(Items.IRON_HORSE_ARMOR),
                                ItemPool.singlePoolItem(Items.TURTLE_HELMET)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.DIAMOND_HELMET),
                                ItemPool.singlePoolItem(Items.DIAMOND_CHESTPLATE),
                                ItemPool.singlePoolItem(Items.DIAMOND_LEGGINGS),
                                ItemPool.singlePoolItem(Items.DIAMOND_BOOTS),
                                ItemPool.singlePoolItem(Items.DIAMOND_HORSE_ARMOR)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.NETHERITE_HELMET),
                                ItemPool.singlePoolItem(Items.NETHERITE_CHESTPLATE),
                                ItemPool.singlePoolItem(Items.NETHERITE_LEGGINGS),
                                ItemPool.singlePoolItem(Items.NETHERITE_BOOTS),
                                ItemPool.singlePoolItem(Items.ELYTRA)
                                )
                )
        );

        itemPoolContext.register(
                TOOL,
                new ItemPool(
                        "tool",
                        List.of(
                                ItemPool.singlePoolItem(Items.WOODEN_AXE),
                                ItemPool.singlePoolItem(Items.WOODEN_PICKAXE),
                                ItemPool.singlePoolItem(Items.WOODEN_SHOVEL),
                                ItemPool.singlePoolItem(Items.WOODEN_HOE),
                                ItemPool.singlePoolItem(Items.STONE_HOE),
                                ItemPool.singlePoolItem(Items.FISHING_ROD),
                                ItemPool.singlePoolItem(Items.CARROT_ON_A_STICK),
                                ItemPool.singlePoolItem(Items.FLINT_AND_STEEL)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.STONE_AXE),
                                ItemPool.singlePoolItem(Items.STONE_PICKAXE),
                                ItemPool.singlePoolItem(Items.STONE_SHOVEL),
                                ItemPool.singlePoolItem(Items.GOLDEN_HOE),
                                ItemPool.singlePoolItem(Items.COMPASS),
                                ItemPool.singlePoolItem(Items.WARPED_FUNGUS_ON_A_STICK),
                                ItemPool.singlePoolItem(Items.CLOCK),
                                ItemPool.singlePoolItem(Items.SHEARS)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.GOLDEN_AXE),
                                ItemPool.singlePoolItem(Items.GOLDEN_PICKAXE),
                                ItemPool.singlePoolItem(Items.GOLDEN_SHOVEL),
                                ItemPool.singlePoolItem(Items.GOLDEN_HOE),
                                ItemPool.singlePoolItem(Items.IRON_AXE),
                                ItemPool.singlePoolItem(Items.IRON_PICKAXE),
                                ItemPool.singlePoolItem(Items.IRON_SHOVEL),
                                ItemPool.singlePoolItem(Items.DIAMOND_HOE),
                                ItemPool.rangePoolItem(Items.LEAD, 3)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.GOLDEN_AXE),
                                ItemPool.singlePoolItem(Items.GOLDEN_PICKAXE),
                                ItemPool.singlePoolItem(Items.GOLDEN_SHOVEL),
                                ItemPool.singlePoolItem(Items.NETHERITE_HOE)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.NETHERITE_AXE),
                                ItemPool.singlePoolItem(Items.NETHERITE_PICKAXE),
                                ItemPool.singlePoolItem(Items.NETHERITE_SHOVEL),
                                ItemPool.singlePoolItem(Items.SPYGLASS)
                                )
                )
        );

        itemPoolContext.register(
                CONSUMABLE,
                new ItemPool(
                        "consumable",
                        List.of(
                                ItemPool.rangePoolItem(Items.GLASS_BOTTLE, 3),
                                ItemPool.offsetRangePoolItem(Items.BOWL, 2, 3),
                                ItemPool.singlePoolItem(Items.POTION)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.WHITE_DYE, 2, 5),
                                ItemPool.offsetRangePoolItem(Items.COOKIE, 3, 7),
                                ItemPool.singlePoolItem(Items.FLINT_AND_STEEL),
                                ItemPool.singlePoolItem(Items.WRITABLE_BOOK)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.BEETROOT_SOUP),
                                ItemPool.offsetRangePoolItem(Items.HONEY_BOTTLE, 2, 4),
                                ItemPool.singlePoolItem(Items.MUSHROOM_STEW),
                                ItemPool.singlePoolItem(Items.MAP)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.GOLDEN_CARROT, 2),
                                ItemPool.singlePoolItem(Items.NAME_TAG),
                                ItemPool.singlePoolItem(Items.RABBIT_STEW),
                                ItemPool.rangePoolItem(Items.GOLDEN_APPLE, 2)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.EXPERIENCE_BOTTLE, 2, 5),
                                ItemPool.singlePoolItem(Items.SPLASH_POTION)
                                )
                )
        );

        itemPoolContext.register(
                BEDSIDE,
                new ItemPool(
                        "bedside",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.PAPER, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.STICK, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.LEATHER, 2, 4),
                                ItemPool.offsetRangePoolItem(Items.FEATHER, 2, 4)
                                ),
                        List.of(
                                ItemPool.rangePoolItem(Items.BOWL, 2),
                                ItemPool.offsetRangePoolItem(Items.TORCH, 3, 7),
                                ItemPool.rangePoolItem(Items.INK_SAC, 2),
                                ItemPool.singlePoolItem(Items.FLOWER_POT)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.WRITABLE_BOOK),
                                ItemPool.offsetRangePoolItem(Items.GLOW_INK_SAC, 2, 4),
                                ItemPool.rangePoolItem(Items.BOOK, 3)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.RABBIT_FOOT),
                                ItemPool.singlePoolItem(Items.CLOCK),
                                ItemPool.singlePoolItem(Items.BRUSH),
                                ItemPool.singlePoolItem(Items.COMPASS)
                                ),
                        List.of(
                                ItemPool.singlePoolItem(Items.AXOLOTL_BUCKET),
                                ItemPool.rangePoolItem(Items.PHANTOM_MEMBRANE, 3),
                                ItemPool.singlePoolItem(Items.SPYGLASS)
                                )
                )
        );

        itemPoolContext.register(
                PLANT,
                new ItemPool(
                        "plant",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.ALLIUM, 2,4),
                                ItemPool.offsetRangePoolItem(Items.AZURE_BLUET, 2,4),
                                ItemPool.offsetRangePoolItem(Items.CACTUS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.CORNFLOWER, 2,4),
                                ItemPool.offsetRangePoolItem(Items.DANDELION, 2,4),
                                ItemPool.offsetRangePoolItem(Items.OXEYE_DAISY, 2,4),
                                ItemPool.offsetRangePoolItem(Items.POPPY, 2,4),
                                ItemPool.offsetRangePoolItem(Items.GRASS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.TALL_GRASS, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.BAMBOO, 2,4),
                                ItemPool.offsetRangePoolItem(Items.BLUE_ORCHID, 2,4),
                                ItemPool.offsetRangePoolItem(Items.FERN, 2,4),
                                ItemPool.offsetRangePoolItem(Items.GLOW_LICHEN, 2,4),
                                ItemPool.offsetRangePoolItem(Items.LILY_OF_THE_VALLEY, 2,4),
                                ItemPool.offsetRangePoolItem(Items.ORANGE_TULIP, 2,4),
                                ItemPool.offsetRangePoolItem(Items.WHITE_TULIP, 2,4),
                                ItemPool.offsetRangePoolItem(Items.RED_TULIP, 2,4),
                                ItemPool.offsetRangePoolItem(Items.PINK_TULIP, 2,4),
                                ItemPool.offsetRangePoolItem(Items.PEONY, 2,4),
                                ItemPool.offsetRangePoolItem(Items.ROSE_BUSH, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SUGAR_CANE, 2,4),
                                ItemPool.offsetRangePoolItem(Items.VINE, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.DEAD_BUSH, 2,4),
                                ItemPool.offsetRangePoolItem(Items.HANGING_ROOTS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.LARGE_FERN, 2,4),
                                ItemPool.offsetRangePoolItem(Items.LILAC, 2,4),
                                ItemPool.offsetRangePoolItem(Items.MOSS_BLOCK, 2,4),
                                ItemPool.offsetRangePoolItem(Items.MOSS_CARPET, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SUNFLOWER, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.CRIMSON_FUNGUS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.CRIMSON_ROOTS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.NETHER_WART, 2,4),
                                ItemPool.offsetRangePoolItem(Items.NETHER_SPROUTS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SHROOMLIGHT, 2,4),
                                ItemPool.offsetRangePoolItem(Items.TWISTING_VINES, 2,4),
                                ItemPool.offsetRangePoolItem(Items.WARPED_FUNGUS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.WARPED_ROOTS, 2,4),
                                ItemPool.offsetRangePoolItem(Items.WEEPING_VINES, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.CHORUS_FLOWER, 2,4),
                                ItemPool.offsetRangePoolItem(Items.PITCHER_PLANT, 2,4),
                                ItemPool.offsetRangePoolItem(Items.PITCHER_POD, 2,4),
                                ItemPool.offsetRangePoolItem(Items.TORCHFLOWER, 2,4),
                                ItemPool.offsetRangePoolItem(Items.WITHER_ROSE, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SPORE_BLOSSOM, 2,4)
                                )
                )
        );

        itemPoolContext.register(
                WATER_PLANT,
                new ItemPool(
                        "water_plant",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.KELP, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SEAGRASS, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.LILY_PAD, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SEA_PICKLE, 2,4),
                                ItemPool.offsetRangePoolItem(Items.MANGROVE_ROOTS, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.SMALL_DRIPLEAF, 2,4),
                                ItemPool.offsetRangePoolItem(Items.BIG_DRIPLEAF, 2,4),
                                ItemPool.offsetRangePoolItem(Items.MUDDY_MANGROVE_ROOTS, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.BRAIN_CORAL, 2,4),
                                ItemPool.offsetRangePoolItem(Items.BUBBLE_CORAL, 2,4),
                                ItemPool.offsetRangePoolItem(Items.HORN_CORAL, 2,4),
                                ItemPool.offsetRangePoolItem(Items.TUBE_CORAL, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.BRAIN_CORAL_FAN, 2,4),
                                ItemPool.offsetRangePoolItem(Items.BUBBLE_CORAL_FAN, 2,4),
                                ItemPool.offsetRangePoolItem(Items.HORN_CORAL_FAN, 2,4),
                                ItemPool.offsetRangePoolItem(Items.TUBE_CORAL_FAN, 2,4)
                                )
                )
        );

        itemPoolContext.register(
                TREE_PLANT,
                new ItemPool(
                        "tree_plant",
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.BIRCH_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.BIRCH_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.OAK_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.OAK_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.SPRUCE_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.SPRUCE_LEAVES, 2,8)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.ACACIA_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.ACACIA_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.DARK_OAK_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.DARK_OAK_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.JUNGLE_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.JUNGLE_LEAVES, 2,8)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.AZALEA, 2,4),
                                ItemPool.offsetRangePoolItem(Items.AZALEA_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.MUSHROOM_STEM, 2,4),
                                ItemPool.offsetRangePoolItem(Items.MANGROVE_ROOTS, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.FLOWERING_AZALEA, 2,4),
                                ItemPool.offsetRangePoolItem(Items.FLOWERING_AZALEA_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.MUDDY_MANGROVE_ROOTS, 2,4)
                                ),
                        List.of(
                                ItemPool.offsetRangePoolItem(Items.CHERRY_SAPLING, 2,4),
                                ItemPool.offsetRangePoolItem(Items.CHERRY_LEAVES, 2,8),
                                ItemPool.offsetRangePoolItem(Items.MANGROVE_PROPAGULE, 2,4),
                                ItemPool.offsetRangePoolItem(Items.MANGROVE_LEAVES, 2,8)
                                )
                )
        );

    }

    private static ResourceKey<ItemPool> registerItemPoolKey(String name) {
        return ResourceKey.create(BTRegistries.Keys.ITEM_POOLS, BABattleTowers.locate(name));
    }
}
