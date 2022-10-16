package com.BrassAmber.ba_bt;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class BattleTowersConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> firstTowerDistance;
    public static final ForgeConfigSpec.ConfigValue<Integer> landAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> landMinimumSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanMinimumSeperation;

    public static final ForgeConfigSpec.ConfigValue<Integer> landTimeBeforeCollapse;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanTimeBeforeCollapse;
    public static final ForgeConfigSpec.ConfigValue<Double> landTowerCrumblePercent;
    public static final ForgeConfigSpec.ConfigValue<Double> oceanTowerCrumblePercent;

    public static final ForgeConfigSpec.ConfigValue<Double> landGolemHP;
    public static final ForgeConfigSpec.ConfigValue<Double> oceanGolemHP;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerMobs;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerMobs;

    public static final ForgeConfigSpec.ConfigValue<Integer> bookLevelEnchant;
    public static final ForgeConfigSpec.ConfigValue<Boolean> enchantArmor;
    public static final ForgeConfigSpec.ConfigValue<Boolean> enchantTools;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalBadLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> generalBadLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalFillerLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> generalFillerLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalDecentLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> generalDecentLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalGoodLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> generalGoodLootCounts;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerBadLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerFillerLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerDecentLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerGoodLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerGolemLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> landBadLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> landFillerLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> landDecentLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> landGoodLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> landGolemLootCounts;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerBadLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerFillerLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerDecentLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerGoodLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerGolemLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> oceanBadLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> oceanFillerLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> oceanDecentLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> oceanGoodLootCounts;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> oceanGolemLootCounts;

    private static final Predicate<Object> listValidator = (Objects::nonNull);


    static {

        BUILDER.push("General Settings -- Negative values are ignored");
        BUILDER.comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                "Several other factors (Land height/other structures) can affect whether the structure actually spawns.");
        landMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 9 chunk minimum. " +
                                "Default: 16 Chunks")
                .define("Land minimum separation", 16);
        oceanMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Ocean Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 7 chunk minimum. " +
                                "Default: 16 Chunks")
                        .define("Ocean minimum separation", 16);
        firstTowerDistance = BUILDER.comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 25 chunks ")
                .define("First Tower Distance", 25);
        BUILDER.pop();

        BUILDER.push("Advanced Settings -- take note of the range for each value, values outside the ranges will be discarded");
        landGolemHP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("Total health of the Land Golem", 250D, 200, 1800);
        landAverageSeperationModifier = BUILDER.comment("This value is added to the Land Tower minimum separation"
                                + " above to get the average separation between Land Towers for spawning measured in chunks.",
                        "I.E. if you leave the minimum separation at 16, and change this value to 8 then Land Towers would spawn"
                                + " at:  minimum = 16 chunks | average = 24 chunks (16 + 8) | maximum = 32 chunks (16 + 16)")
                .defineInRange("Land average separation modifier", 8, 1, 100);
        landTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Land Tower collapses")
                        .defineInRange("Land Collapse Timer", 30, 30, 60);
        landTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 83% of tower.")
                        .defineInRange("Percent of Land Tower to destroy", .83D, 0,1);


        oceanGolemHP =
                BUILDER.comment("The total health of the Ocean Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("Total health of the Ocean Golem", 350D, 250, 2000);
        oceanAverageSeperationModifier = BUILDER.comment("This value is added to the Ocean Tower minimum separation"
                                + " above to get the average separation between Ocean Towers for spawning measured in chunks.",
                        "See Land Tower Average Separation for explanation of use.")
                .defineInRange("Ocean average separation modifier", 12, 1, 100);
        oceanTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Ocean Tower crumbles")
                        .defineInRange("Ocean Collapse Timer", 30, 30, 60);
        oceanTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 100% of tower.")
                        .defineInRange("Percent of Ocean Tower to destroy", 1D, .5D,1D);

        BUILDER.pop();


        BUILDER.push("Crash-able settings -- If you edit these, and the game crashes, its on you");
        landTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the Land tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Land Tower Mobs"), () -> List.of("minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), listValidator);
        oceanTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the Ocean tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Ocean Tower Mobs"), () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:pufferfish"), listValidator);
        BUILDER.pop();


        BUILDER.push("Tower loot -- Loot rolls for tower chests are generated from these tables: EACH TABLE MUST CONTAIN AT LEAST ONE ITEM ");
        bookLevelEnchant = BUILDER.comment("The number of xp levels books are enchanted with in loot, " +
                "Ie a 20 here means that books that appear in loot will contain enchants as if enchanted with 20 levels of xp  (if from a bad loot pool) up to 40 (from a good loot pool)" )
                .defineInRange("Book XP Levels", 10, 0, 40);
        enchantArmor = BUILDER.comment("Whether or not armor in loot should be enchanted").define("Enchanted Armor", true);
        enchantTools = BUILDER.comment("Whether or not tools/weapons in loot should be enchanted").define("Enchanted Tools", true);
        generalBadLoot =
                BUILDER.comment("A list of item ids of possible bad items.")
                        .defineListAllowEmpty(List.of("General Bad Loot"), () -> List.of("minecraft:string", "minecraft:rotten_flesh"), listValidator);
        generalBadLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the loot list above, if the number is 25 that item will generate in groups of 2-5 a single integer like 3 is treated as a range of 0-3")
                        .defineListAllowEmpty(List.of("General Bad Loot Counts"), () -> List.of(13, 25), listValidator);
        generalFillerLoot =
                BUILDER.comment("A list of item ids of possible filler items.")
                        .defineListAllowEmpty(List.of("General Filler Loot"), () -> List.of("minecraft:sugar", "minecraft:gold_nugget", "minecraft:glass_bottle",
                                "minecraft:clay_ball", "minecraft:flower_pot", "minecraft:arrow", "minecraft:cookie", "minecraft:iron_nugget", "minecraft:iron_ingot"), listValidator);
        generalFillerLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the loot list above")
                        .defineListAllowEmpty(List.of("General Filler Loot Counts"), () -> List.of(14, 26, 11, 24, 11, 26, 24, 36, 12), listValidator);
        generalDecentLoot =
                BUILDER.comment("A list of item ids of possible decent items.")
                        .defineListAllowEmpty(List.of("General Decent Loot"), () -> List.of("minecraft:gunpowder", "minecraft:obsidian", "minecraft:bone",
                                "minecraft:iron_ingot", "minecraft:bucket", "minecraft:honey_bottle", "minecraft:oak_wood", "minecraft:gold_ingot",
                                "minecraft:redstone", "minecraft:crosbow", "minecraft:lava_bucket", "minecraft:paper", "minecraft:item_frame", "minecraft:sugar_cane",
                                "minecraft:cauldron", "minecraft:diamond_hoe", "minecraft:iron_axe", "minecraft:iron_pickaxe", "minecraft:iron_shovel",
                                "minecraft:iron_sword", "minecraft:arrow"), listValidator);
        generalDecentLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the loot list above")
                        .defineListAllowEmpty(List.of("General Decent Loot Counts"), () -> List.of(24, 13, 14, 23, 11, 11, 15, 12, 14, 11, 11,
                                3, 11, 13, 11, 11, 11, 11, 11, 11, 48), listValidator);
        generalGoodLoot =
                BUILDER.comment("A list of item ids of possible good items.")
                        .defineListAllowEmpty(List.of("General Good Loot"), () -> List.of("minecraft:golden_carrot", "minecraft:golden_apple", "minecraft:enchanted_book",
                                "minecraft:ender_pearl", "minecraft:diamond", "minecraft:diamond_axe", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel",
                                "minecraft:diamond_sword", "minecraft:experience_bottle", "minecraft:emerald", "minecraft:music_disc_11", "minecraft:music_disc_13",
                                "minecraft:music_disc_blocks", "minecraft:music_disc_cat", "minecraft:music_disc_chirp", "minecraft:music_disc_far", "minecraft:amethyst_shard",
                                "minecraft:music_disc_mall", "minecraft:music_disc_mellohi", "minecraft:music_disc_otherside", "minecraft:music_disc_stal", "minecraft:diamond"), listValidator);
        generalGoodLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the loot list above")
                        .defineListAllowEmpty(List.of("General Good Loot Counts"), () -> List.of(13, 11, 11, 25, 13, 11, 11, 11, 11, 37, 14,
                                11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 13), listValidator);


        landTowerBadLoot =
                BUILDER.comment("A list of item ids of possible bad items to spawn inside the Land tower. " +
                                "Each tower chest has a defined number of rolls per category (bad/filler/decent/good).")
                        .defineListAllowEmpty(List.of("Land Tower Bad Loot"), () -> List.of("minecraft:cobblestone", "minecraft:iron_nugget", "minecraft:apple"), listValidator);
        landBadLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the land bad loot list above")
                        .defineListAllowEmpty(List.of("Land Tower Bad Loot Counts"), () -> List.of(25, 48, 13), listValidator);
        landTowerFillerLoot =
                BUILDER.comment("A list of item ids of possible filler items to spawn inside the Land tower.")
                        .defineListAllowEmpty(List.of("Land Tower Filler Loot"), () -> List.of("minecraft:sugar", "minecraft:flint", "minecraft:ladder",
                                "minecraft:white_wool", "minecraft:torch", "minecraft:egg", "minecraft:rabbit", "minecraft:beef", "minecraft:porkchop",
                                "minecraft:chicken", "minecraft:mutton"), listValidator);
        landFillerLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the land filler loot list above")
                        .defineListAllowEmpty(List.of("Land Tower Filler Loot Counts"), () -> List.of(13, 35, 26, 14, 35, 12, 13, 13, 13, 13, 13), listValidator);
        landTowerDecentLoot =
                BUILDER.comment("A list of item ids of possible decent items to spawn inside the Land tower.")
                        .defineListAllowEmpty(List.of("Land Tower Decent Loot"), () -> List.of("minecraft:gunpowder", "minecraft:saddle",
                                "minecraft:cake", "minecraft:beetroot_soup", "minecraft:cookie", "minecraft:mushroom_stew", "minecraft:cooked_rabbit",
                                "minecraft:cooked_beef", "minecraft:cooked_porkchop", "minecraft:cooked_chicken", "minecraft:cooked_mutton"), listValidator);
        landDecentLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the land decent loot list above")
                        .defineListAllowEmpty(List.of("Land Tower Decent Loot Counts"), () -> List.of(14, 11, 11, 11, 36, 11, 13, 13, 13, 13, 13), listValidator);
        landTowerGoodLoot =
                BUILDER.comment("A list of item ids of possible good items to spawn inside the Land tower.")
                        .defineListAllowEmpty(List.of("Land Tower Good Loot"), () -> List.of("minecraft:golden_carrot", "minecraft:map", "minecraft:jukebox"), listValidator);
        landGoodLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the land good loot list above")
                        .defineListAllowEmpty(List.of("Land Tower Good Loot Counts"), () -> List.of(24, 11, 11), listValidator);
        landTowerGolemLoot =
                BUILDER.comment("A list of item ids of possible items to add to the land Golem chest item pool. A single value of air means no items are added")
                        .defineListAllowEmpty(List.of("Land Tower Golem Loot"), () -> List.of("minecraft:air"), listValidator);
        landGolemLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the loot list above")
                        .defineListAllowEmpty(List.of("Land Golem Loot Counts"), () -> List.of(11), listValidator);


        oceanTowerBadLoot =
                BUILDER.comment("A list of item ids of possible bad items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Bad Loot"), () -> List.of("minecraft:dirt", "minecraft:seagrass", "minecraft:gravel"), listValidator);
        oceanBadLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the ocean bad loot list above")
                        .defineListAllowEmpty(List.of("Ocean Tower Bad Loot Counts"), () -> List.of(18, 25, 25), listValidator);
        oceanTowerFillerLoot =
                BUILDER.comment("A list of item ids of possible filler items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Filler Loot"), () -> List.of("minecraft:lily_pad", "minecraft:kelp", "minecraft:salmon",
                                "minecraft:cod", "minecraft:prismarine_shard", "minecraft:copper_ingot"), listValidator);
        oceanFillerLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the ocean filler loot list above")
                        .defineListAllowEmpty(List.of("Ocean Tower Filler Loot Counts "), () -> List.of(14, 25, 13, 13, 24, 13), listValidator);
        oceanTowerDecentLoot =
                BUILDER.comment("A list of item ids of possible decent items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Decent Loot"), () -> List.of("minecraft:soul_sand", "minecraft:oak_boat", "minecraft:birch_boat",
                                "minecraft:glow_item_frame", "minecraft:glow_ink_sac", "minecraft:glowstone", "minecraft:magma_block", "minecraft:gold_ingot", "minecraft:copper_ingot"), listValidator);
        oceanDecentLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the ocean decent loot list above")
                        .defineListAllowEmpty(List.of("Ocean Tower Decent Loot Counts"), () -> List.of(14, 11, 11, 11, 23, 25, 14, 14, 14), listValidator);
        oceanTowerGoodLoot =
                BUILDER.comment("A list of item ids of possible good items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Good Loot"), () -> List.of("minecraft:trident", "minecraft:trident", "minecraft:axolotl_bucket", "minecraft:sponge",
                                "minecraft:heart_of_the_sea", "minecraft:brewing_stand", "minecraft:prismarine_crystals", "minecraft:copper_ingot", "minecraft:gold_block"), listValidator);
        oceanGoodLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the ocean filler loot list above")
                        .defineListAllowEmpty(List.of("Ocean Tower Good Loot Counts"), () -> List.of(11, 11, 11, 35, 11, 11, 24, 26, 11), listValidator);
        oceanTowerGolemLoot =
                BUILDER.comment("A list of item ids of possible items to add to the land Golem chest item pool, A single value of air means no items are added")
                        .defineListAllowEmpty(List.of("Ocean Tower Golem Loot"), () -> List.of("minecraft:air"), listValidator);
        oceanGolemLootCounts =
                BUILDER.comment("A list of combined minimum and maximum counts of each item in the ocean golem loot list above")
                        .defineListAllowEmpty(List.of("Ocean Golem Loot Counts"), () -> List.of(11), listValidator);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
