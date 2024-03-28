package com.brass_amber.ba_bt;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BattleTowersConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> firstTowerDistance;
    public static final ModConfigSpec.ConfigValue<Integer> landAverageSeperationModifier;
    public static final ModConfigSpec.ConfigValue<Integer> landMinimumSeperation;
    public static final ModConfigSpec.ConfigValue<Integer> oceanAverageSeperationModifier;
    public static final ModConfigSpec.ConfigValue<Integer> oceanMinimumSeperation;
    public static final ModConfigSpec.ConfigValue<Boolean> terralithBiomeSpawning;
    public static final ModConfigSpec.ConfigValue<Boolean> biomesOfPlentyBiomeSpawning;
    public static final ModConfigSpec.ConfigValue<Boolean> biomesYoullGoBiomeSpawning;

    public static final ModConfigSpec.ConfigValue<Integer> landTimeBeforeCollapse;
    public static final ModConfigSpec.ConfigValue<Integer> oceanTimeBeforeCollapse;
    public static final ModConfigSpec.ConfigValue<Double> landTowerCrumblePercent;
    public static final ModConfigSpec.ConfigValue<Double> oceanTowerCrumblePercent;

    public static final ModConfigSpec.ConfigValue<Double> landGolemHP;
    public static final ModConfigSpec.ConfigValue<Double> oceanGolemHP;

    public static final ModConfigSpec.ConfigValue<Integer> landObeliskSpawnDistance;
    public static final ModConfigSpec.ConfigValue<Integer> landFloorHeight;
    public static final ModConfigSpec.ConfigValue<Boolean> useOldSpawnerAmounts;
    public static final ModConfigSpec.ConfigValue<Boolean> minimalOceanCarving;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> landTowerMobs;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> oceanTowerMobs;

    public static final ModConfigSpec.ConfigValue<Integer> bookLevelEnchant;
    public static final ModConfigSpec.ConfigValue<Boolean> enchantArmor;
    public static final ModConfigSpec.ConfigValue<Boolean> enchantTools;

    private static boolean validateEntityName(final Object obj)
    {
        return obj instanceof String entityName && BuiltInRegistries.ENTITY_TYPE.containsKey(new ResourceLocation(entityName));
    }

    static {

        BUILDER.push("General Settings -- Negative values are ignored");
        BUILDER.comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                "Several other factors (Land height/other structures) can affect whether the structure actually spawns.");

        landMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 9 chunk minimum. " +
                                "Default: 20 Chunks")
                .define("Land minimum separation", 20);
        oceanMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Ocean Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 6 chunk minimum. " +
                                "Default: 16 Chunks")
                        .define("Ocean minimum separation", 16);
        firstTowerDistance = BUILDER.comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 25 chunks ")
                .define("First Tower Distance", 25);
        terralithBiomeSpawning = BUILDER.comment("Whether to include acceptable Terralith biomes during the tower's Biome check.")
                .define("Terralith Biomes", false);
        biomesOfPlentyBiomeSpawning = BUILDER.comment("Whether to include acceptable Biomes of Plenty biomes during the tower's Biome check.")
                .define("Biomes of Plenty Biomes", false);
        biomesYoullGoBiomeSpawning = BUILDER.comment("Whether to include acceptable Oh The Biomes You'll Go biomes during the tower's Biome check.")
                .define("Oh The Biomes You'll Go Biomes", false);
        BUILDER.pop();

        BUILDER.push("Advanced Settings -- take note of the range for each value, values outside the ranges will be discarded");
        landGolemHP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("Total health of the Land Golem", 250D, 200, 1800);
        landAverageSeperationModifier = BUILDER.comment("This value is added to the Land Tower minimum separation"
                                + " above to get the average separation between Land Towers for spawning measured in chunks.",
                        "I.E. if you leave the minimum separation at 20, and change this value to 8 then Land Towers would spawn"
                                + " at:  minimum = 20 chunks | average = 28 chunks (20 + 8) | maximum = 36 chunks (20 + 16)")
                .defineInRange("Land average separation modifier", 4, 1, 100);
        landTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Land Tower collapses")
                        .defineInRange("Land Collapse Timer", 30, 30, 60);
        landTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 83% of tower.")
                        .defineInRange("Percent of Land Tower to destroy", .83D, 0,1);
        landObeliskSpawnDistance =
                BUILDER.comment("Distance below Monolith the obelisk spawns, Only to be used in tandem " +
                        "with floor height for creation of own 'Towers'")
                        .defineInRange("Land Obelisk Spawn Distance", 90, 32, 200);
        landFloorHeight =
                BUILDER.comment("Distance between Land tower floors, only to be used in tandem with Land Obelisk Spawn Distance")
                        .defineInRange("Land Tower Floor Height", 11, 4, 24);
        useOldSpawnerAmounts =
                BUILDER.comment("Whether to use the new spawner amounts for each floor, or old 2 spawners per floor")
                        .define("Old Spawner Amounts", false);

        oceanGolemHP =
                BUILDER.comment("The total health of the Ocean Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("Total health of the Ocean Golem", 350D, 250, 2000);
        oceanAverageSeperationModifier = BUILDER.comment("This value is added to the Ocean Tower minimum separation"
                                + " above to get the average separation between Ocean Towers for spawning measured in chunks.",
                        "See Land Tower Average Separation for explanation of use.")
                .defineInRange("Ocean average separation modifier", 8, 1, 100);
        oceanTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Ocean Tower crumbles")
                        .defineInRange("Ocean Collapse Timer", 30, 30, 60);
        oceanTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 100% of tower.")
                        .defineInRange("Percent of Ocean Tower to destroy", 1D, .5D,1D);
        minimalOceanCarving =
                BUILDER.comment("Makes the Ocean trench around the Ocean tower much smaller, reducing the lag on load")
                        .define("Small Ocean Trench T/F", false);



        BUILDER.pop();


        BUILDER.push("Crash-able settings -- If you edit these, and the game crashes, its on you");
        landTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the Land tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Land Tower Mobs"), () -> List.of("minecraft:zombie", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), BattleTowersConfig::validateEntityName);
        oceanTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the Ocean tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Ocean Tower Mobs"), () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:drowned", "minecraft:drowned", "minecraft:pufferfish"), BattleTowersConfig::validateEntityName);
        BUILDER.pop();


        BUILDER.push("Tower loot -- Loot rolls for tower chests are generated from these tables: EACH TABLE MUST CONTAIN AT LEAST ONE ITEM ");
        bookLevelEnchant = BUILDER.comment("The number of xp levels books and tools are enchanted with in loot, " +
                "Ie a 20 here means that books and tools that appear in loot will contain enchants as if enchanted with 20 levels of xp  up to 41 (3 extra per tower floor)" )
                .defineInRange("Book XP Levels", 10, 0, 40);
        enchantArmor = BUILDER.comment("Whether or not armor in loot should be enchanted").define("Enchanted Armor", true);
        enchantTools = BUILDER.comment("Whether or not tools/weapons in loot should be enchanted").define("Enchanted Tools", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
