package com.BrassAmber.ba_bt;

import net.minecraftforge.common.ForgeConfigSpec;

import java.io.InputStreamReader;
import java.util.Collections;
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

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalBadLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalFillerLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalDecentLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> generalGoodLoot;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerBadLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerFillerLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerDecentLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerGoodLoot;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerBadLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerFillerLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerDecentLoot;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerGoodLoot;

    private static final Predicate<Object> stringListValidator = (Objects::nonNull);

    static {

        BUILDER.push("General Settings -- Negative values are ignored");
        BUILDER.comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                "Several other factors (Land height/other structures) can affect whether the structure actually spawns.");
        landMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 5 chunk minimum. " +
                                "Default: 16 Chunks")
                .define("Land minimum separation", 16);
        oceanMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Ocean Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 5 chunk minimum. " +
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
        landTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 83% of tower.")
                        .defineInRange("Percent of Land Tower to destroy", .83D, 0,1);
        landTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Land Tower collapses")
                        .defineInRange("Land Collapse Timer", 30, 30, 60);

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
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 95% of tower.")
                        .defineInRange("Percent of Ocean Tower to destroy", .95D, .45D,1);

        BUILDER.pop();


        BUILDER.push("Crash-able settings -- If you edit these, and the game crashes, its on you");
        landTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the Land tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Land Tower Mobs"), () -> List.of("minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), stringListValidator);
        oceanTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the Ocean tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Ocean Tower Mobs"), () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:pufferfish"), stringListValidator);
        BUILDER.pop();


        BUILDER.push("Tower loot -- Loot rolls for tower chests are generated from these tables. EACH TABLE MUST CONTAIN AT LEAST ONE ITEM ");
        generalBadLoot =
                BUILDER.comment("A list of item ids of possible bad items to spawn in any tower chests.")
                        .defineListAllowEmpty(List.of("General Bad Loot"), () -> List.of("minecraft:string", "minecraft:rotten_flesh"), stringListValidator);
        generalFillerLoot =
                BUILDER.comment("A list of item ids of possible filler items to spawn in any tower chests.")
                        .defineListAllowEmpty(List.of("General Filler Loot"), () -> List.of("minecraft:sugar", "minecraft:gold_nugget", "minecraft:glass_bottle",
                                "minecraft:clay_ball", "minecraft:flower_pot", "minecraft:iron_hoe", "minecraft:arrow", "minecraft:cookie"), stringListValidator);
        generalDecentLoot =
                BUILDER.comment("A list of item ids of possible decent items to spawn in any tower chests.")
                        .defineListAllowEmpty(List.of("General Decent Loot"), () -> List.of("minecraft:gunpowder", "minecraft:obsidian", "minecraft:bone",
                                "minecraft:iron_ingot", "minecraft:bucket", "minecraft:honey_bottle", "minecraft:chest", "minecraft:oak_wood", "minecraft:gold_ingot",
                                "minecraft:redstone", "minecraft:crosbow", "minecraft:lava_bucket", "minecraft:paper", "minecraft:item_frame", "minecraft:sugar_cane",
                                "minecraft:cauldron", "minecraft:diamond_hoe", "minecraft:iron_axe", "minecraft:iron_pickaxe", "minecraft:iron_shovel",
                                "minecraft:iron_sword", "minecraft:arrow"), stringListValidator);
        generalGoodLoot =
                BUILDER.comment("A list of item ids of possible good items to spawn in any tower chests.")
                        .defineListAllowEmpty(List.of("General Good Loot"), () -> List.of("minecraft:golden_carrot", "minecraft:golden_apple", "minecraft:enchanted_book",
                                "minecraft:ender_pearl", "minecraft:diamond", "minecraft:diamond_axe", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel",
                                "minecraft:diamond_sword", "minecraft:experience_bottle", "minecraft:emerald", "minecraft:music_disc_11", "minecraft:music_disc_13",
                                "minecraft:music_disc_blocks", "minecraft:music_disc_cat", "minecraft:music_disc_chirp", "minecraft:music_disc_far",
                                "minecraft:music_disc_mall", "minecraft:music_disc_mellohi", "minecraft:music_disc_otherside", "minecraft:music_disc_stal"), stringListValidator);

        landTowerBadLoot =
                BUILDER.comment("A list of item ids of possible bad items to spawn inside the Land tower. " +
                                "Each tower chest has a defined number of rolls per category (bad/filler/decent/good).")
                        .defineListAllowEmpty(List.of("Land Tower Bad Loot"), () -> List.of("minecraft:cobblestone", "minecraft:iron_nugget", "minecraft:apple"), stringListValidator);
        landTowerFillerLoot =
                BUILDER.comment("A list of item ids of possible filler items to spawn inside the Land tower.")
                        .defineListAllowEmpty(List.of("Land Tower Filler Loot"), () -> List.of("minecraft:sugar", "minecraft:flint", "minecraft:ladder",
                                "minecraft:white_wool", "minecraft:torch", "minecraft:egg", "minecraft:rabbit", "minecraft:beef", "minecraft:porkchop",
                                "minecraft:chicken", "minecraft:mutton"), stringListValidator);
        landTowerDecentLoot =
                BUILDER.comment("A list of item ids of possible decent items to spawn inside the Land tower.")
                        .defineListAllowEmpty(List.of("Land Tower Decent Loot"), () -> List.of("minecraft:gunpowder", "minecraft:saddle", "minecraft:book",
                                "minecraft:cake", "minecraft:beetroot_soup", "minecraft:cookie", "minecraft:mushroom_stew", "minecraft:cooked_rabbit",
                                "minecraft:cooked_beef", "minecraft:cooked_porkchop", "minecraft:cooked_chicken", "minecraft:cooked_mutton"), stringListValidator);
        landTowerGoodLoot =
                BUILDER.comment("A list of item ids of possible good items to spawn inside the Land tower.")
                        .defineListAllowEmpty(List.of("Land Tower Good Loot"), () -> List.of("minecraft:golden_carrot", "minecraft:map", "minecraft:blaze_rod",
                                "minecraft:jukebox"), stringListValidator);


        oceanTowerBadLoot =
                BUILDER.comment("A list of item ids of possible bad items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Bad Loot"), () -> List.of("minecraft:dirt", "minecraft:seagrass", "minecraft:gravel"), stringListValidator);
        oceanTowerFillerLoot =
                BUILDER.comment("A list of item ids of possible filler items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Filler Loot"), () -> List.of("minecraft:lily_pad", "minecraft:kelp", "minecraft:salmon",
                                "minecraft:cod", "minecraft:prismarine_shard"), stringListValidator);
        oceanTowerDecentLoot =
                BUILDER.comment("A list of item ids of possible decent items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Decent Loot"), () -> List.of("minecraft:soul_sand", "minecraft:oak_boat", "minecraft:birch_boat",
                                "minecraft:glow_item_frame", "minecraft:glow_ink_sac", "minecraft:glowstone", "minecraft:magma_block"), stringListValidator);
        oceanTowerGoodLoot =
                BUILDER.comment("A list of item ids of possible good items to spawn inside the Ocean tower.")
                        .defineListAllowEmpty(List.of("Ocean Tower Good Loot"), () -> List.of("minecraft:trident", "minecraft:axolotl_bucket", "minecraft:sponge",
                                "minecraft:heart_of_the_sea", "minecraft:brewing_stand", "minecraft:prismarine_crystals"), stringListValidator);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
