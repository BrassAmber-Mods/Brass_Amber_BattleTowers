package com.brass_amber.ba_bt;

import java.util.Collections;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;


public class BattleTowersConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> firstTowerDistance;
    public static final ForgeConfigSpec.ConfigValue<Integer> landAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> landMinimumSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanMinimumSeperation;
    public static final ForgeConfigSpec.ConfigValue<Boolean> terralithBiomeSpawning;
    public static final ForgeConfigSpec.ConfigValue<Boolean> biomesOfPlentyBiomeSpawning;
    public static final ForgeConfigSpec.ConfigValue<Boolean> biomesYoullGoBiomeSpawning;

    public static final ForgeConfigSpec.ConfigValue<Integer> landTimeBeforeCollapse;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanTimeBeforeCollapse;
    public static final ForgeConfigSpec.ConfigValue<Double> landTowerCrumblePercent;
    public static final ForgeConfigSpec.ConfigValue<Double> oceanTowerCrumblePercent;

    public static final ForgeConfigSpec.ConfigValue<Double> landGolemHP;
    public static final ForgeConfigSpec.ConfigValue<Double> oceanGolemHP;

    public static final ForgeConfigSpec.ConfigValue<Boolean> minimalOceanCarving;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerMobs;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerMobs;

    public static final ForgeConfigSpec.ConfigValue<Integer> bookLevelEnchant;
    public static final ForgeConfigSpec.ConfigValue<Boolean> enchantArmor;
    public static final ForgeConfigSpec.ConfigValue<Boolean> enchantTools;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> extraContainerTypes;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> extraContainerBlocks;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> meatPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> veggiePoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> cookedPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> gemsPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> metalsPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> orePoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> buildingBlocksPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> libraryPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> weaponPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> armorPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> toolPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> consumablePoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> bedsidePoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> plantsPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> waterPlantsPoolExtra;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> treePlantsPoolExtra;

    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> meatPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> veggiePoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> cookedPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> gemsPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> metalsPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> orePoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> buildingBlocksPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> libraryPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> weaponPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> armorPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> toolPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> consumablePoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bedsidePoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> plantsPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> waterPlantsPoolRarity;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> treePlantsPoolRarity;

    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> meatPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> veggiePoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> cookedPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> gemsPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> metalsPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> orePoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> buildingBlocksPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> libraryPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> weaponPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> armorPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> toolPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> consumablePoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> bedsidePoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> plantsPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> waterPlantsPoolAmount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Float>> treePlantsPoolAmount;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerChestPools;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerChestPools;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> coreTowerChestPools;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> netherTowerChestPools;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> endTowerChestPools;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> skyTowerChestPools;

    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }
    private static boolean validateInteger(final Object obj) {
        return obj instanceof Integer x && x > 0;
    }

    private static boolean validateFloat(final Object obj) {
        return obj instanceof Float x && x > 0.0f;
    }
    
    private static boolean validateRarity(final Object obj) {
        return validateInteger(obj) && (Integer) obj < 5;
    }
    
    private static boolean validateEntityName(final Object obj) {
        return obj instanceof String entityName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(entityName));
    }
    
    private static boolean validateBlock(final Object obj) {
        return obj instanceof String blockName && ForgeRegistries.BLOCKS.containsKey((new ResourceLocation(blockName)));
    }

    private static boolean validateItem(final Object obj) {
        return obj instanceof String blockName && ForgeRegistries.ITEMS.containsKey((new ResourceLocation(blockName)));
    }
    


    static {
        BUILDER.comment("General Settings -- Negative values are ignored");
        BUILDER.push("general");
        BUILDER.comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                "Several other factors (Land height/other structures) can affect whether the structure actually spawns.");

        landMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. " +
                                "(9 chunk minimum. Default: 20 Chunks")
                        .defineInRange("landMinimumSeparation", 30, 9, 999999999);
        oceanMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Ocean Towers measured in chunks. " +
                                "(6 chunk minimum. Default: 16 Chunks")
                        .defineInRange("oceanMinimumSeparation", 35, 9, 999999999);
        firstTowerDistance = BUILDER.comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 25 chunks ")
                .define("firstTowerDistance", 25);
        terralithBiomeSpawning = BUILDER.comment("Whether to include acceptable Terralith biomes during the tower's Biome check.")
                .define("terralith", false);
        biomesOfPlentyBiomeSpawning = BUILDER.comment("Whether to include acceptable Biomes of Plenty biomes during the tower's Biome check.")
                .define("biomesofPlenty", false);
        biomesYoullGoBiomeSpawning = BUILDER.comment("Whether to include acceptable Oh The Biomes You'll Go biomes during the tower's Biome check.")
                .define("ohTheBiomesYoullGo", false);
        BUILDER.pop();

        BUILDER.comment("Advanced Settings -- take note of the range for each value, values outside the ranges will be discarded");
        BUILDER.push("advanced");
        landGolemHP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("landGolemHealth", 250D, 200, 100000);
        landAverageSeperationModifier = BUILDER.comment("This value is added to the Land Tower minimum separation"
                                + " above to get the average separation between Land Towers for spawning measured in chunks.",
                        "I.E. if you leave the minimum separation at 20, and change this value to 8 then Land Towers would spawn"
                                + " at:  minimum = 20 chunks | average = 28 chunks (20 + 8) | maximum = 36 chunks (20 + 16)")
                .defineInRange("landAverageSeparationModifier", 4, 1, 100);
        landTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Land Tower collapses")
                        .defineInRange("landCollapseTimer", 30, 30, 60);
        landTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 85% of tower.")
                        .defineInRange("landTowerDestruction", .85D, 0,1);

        oceanGolemHP =
                BUILDER.comment("The total health of the Ocean Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("oceanGolemHealth", 350D, 250, 100000);
        oceanAverageSeperationModifier = BUILDER.comment("This value is added to the Ocean Tower minimum separation"
                                + " above to get the average separation between Ocean Towers for spawning measured in chunks.",
                        "See Land Tower Average Separation for explanation of use.")
                .defineInRange("oceanAverageSeparationModifier", 8, 1, 100);
        oceanTimeBeforeCollapse =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Ocean Tower crumbles")
                        .defineInRange("oceanCollapseTimer", 30, 30, 60);
        oceanTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 100% of tower.")
                        .defineInRange("oceanTowerDestruction", 1D, .5D,1D);
        minimalOceanCarving =
                BUILDER.comment("Makes the Ocean trench around the Ocean tower much smaller, reducing the lag on load")
                        .define("smallOceanTrench", false);
        BUILDER.pop();

        BUILDER.comment("Crashable settings -- If you edit these, and the game crashes, its on you");
        BUILDER.push("crashable");

        BUILDER.push("towerMobs");
        BUILDER.comment("Lists of mob ids of possible mobs to spawn in spawners inside each Tower. Each list must contain at least one value");
        landTowerMobs =
                BUILDER.defineListAllowEmpty(List.of("landTowerMobs"), () -> List.of("minecraft:zombie", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), BattleTowersConfig::validateEntityName);
        oceanTowerMobs =
                BUILDER.defineListAllowEmpty(List.of("oceanTowerMobs"), () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:drowned", "minecraft:drowned", "minecraft:pufferfish"), BattleTowersConfig::validateEntityName);

        BUILDER.pop();


        BUILDER.push("towerLootOptions");
        bookLevelEnchant = BUILDER.comment("The number of xp levels books and tools are enchanted with in loot, " +
                "Ie a 20 here means that books and tools that appear in loot will contain enchants as if enchanted with 20 levels of xp  up to 41 (3 extra per tower floor)" )
                .defineInRange("bookXPLevels", 10, 0, 40);
        enchantArmor = BUILDER.comment("Whether or not armor in loot should be enchanted").define("enchantedArmor", true);
        enchantTools = BUILDER.comment("Whether or not tools/weapons in loot should be enchanted").define("enchantedTools", true);
        extraContainerTypes = BUILDER.comment("List of extra specifiable container types for use in custom tower floors").defineList("extraChestTypes", () -> List.of("White Shulker"), BattleTowersConfig::validateString);
        extraContainerBlocks = BUILDER.comment("List of extra container blocks for placing in custom tower floors").defineList("extraChestBlocks", () -> List.of("minecraft:white_shulker_box"), BattleTowersConfig::validateBlock);
        BUILDER.pop();

        BUILDER.push("towerExtendableLootPools");
        BUILDER.comment("Rarity starts at zero on floor 1 of any tower and increases by 1 every 2 floors.");
        BUILDER.comment("A Rarity 0 item for the meat pool would be rotten flesh, while a rarity 4 item would be rabbit stew (as seen below)");
        BUILDER.comment("Every item you add to the 'Pool Extra' needs a matching entry in the rarity and amount sections");
        BUILDER.comment("The amount section is a float with the '.' separating the min amount of the item from the max amount. I.E 1.3 is a range of 1-3");
        
        meatPoolExtra = BUILDER.defineListAllowEmpty("meatLootPool", () -> List.of("minecraft:rabbit_stew"), BattleTowersConfig::validateItem);
        meatPoolRarity = BUILDER.defineListAllowEmpty("meatLootRarity", () -> List.of(4), BattleTowersConfig::validateRarity);
        meatPoolAmount = BUILDER.defineListAllowEmpty("meatLootAmounts", () -> List.of(1.1f), BattleTowersConfig::validateFloat);

        veggiePoolExtra = BUILDER.defineListAllowEmpty("veggieLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        veggiePoolRarity = BUILDER.defineListAllowEmpty("veggieLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        veggiePoolAmount = BUILDER.defineListAllowEmpty("veggieLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        cookedPoolExtra = BUILDER.defineListAllowEmpty("cookedLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        cookedPoolRarity = BUILDER.defineListAllowEmpty("cookedLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        cookedPoolAmount = BUILDER.defineListAllowEmpty("cookedLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        gemsPoolExtra = BUILDER.defineListAllowEmpty("gemsLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        gemsPoolRarity = BUILDER.defineListAllowEmpty("gemsLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        gemsPoolAmount = BUILDER.defineListAllowEmpty("gemsLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        metalsPoolExtra = BUILDER.defineListAllowEmpty("metalsLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        metalsPoolRarity = BUILDER.defineListAllowEmpty("metalsLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        metalsPoolAmount = BUILDER.defineListAllowEmpty("metalsLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        orePoolExtra = BUILDER.defineListAllowEmpty("oreLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        orePoolRarity = BUILDER.defineListAllowEmpty("oreLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        orePoolAmount = BUILDER.defineListAllowEmpty("oreLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        buildingBlocksPoolExtra = BUILDER.defineListAllowEmpty("buildingBlocksLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        buildingBlocksPoolRarity = BUILDER.defineListAllowEmpty("buildingBlocksLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        buildingBlocksPoolAmount = BUILDER.defineListAllowEmpty("buildingBlocksLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        libraryPoolExtra = BUILDER.defineListAllowEmpty("libraryLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        libraryPoolRarity = BUILDER.defineListAllowEmpty("libraryLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        libraryPoolAmount = BUILDER.defineListAllowEmpty("libraryLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        weaponPoolExtra = BUILDER.defineListAllowEmpty("weaponLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        weaponPoolRarity = BUILDER.defineListAllowEmpty("weaponLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        weaponPoolAmount = BUILDER.defineListAllowEmpty("weaponLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        armorPoolExtra = BUILDER.defineListAllowEmpty("armorLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        armorPoolRarity = BUILDER.defineListAllowEmpty("armorLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        armorPoolAmount = BUILDER.defineListAllowEmpty("armorLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        toolPoolExtra = BUILDER.defineListAllowEmpty("toolLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        toolPoolRarity = BUILDER.defineListAllowEmpty("toolLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        toolPoolAmount = BUILDER.defineListAllowEmpty("toolLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        consumablePoolExtra = BUILDER.defineListAllowEmpty("consumableLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        consumablePoolRarity = BUILDER.defineListAllowEmpty("consumableLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        consumablePoolAmount = BUILDER.defineListAllowEmpty("consumableLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        bedsidePoolExtra = BUILDER.defineListAllowEmpty("bedsideLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        bedsidePoolRarity = BUILDER.defineListAllowEmpty("bedsideLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        bedsidePoolAmount = BUILDER.defineListAllowEmpty("bedsideLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        plantsPoolExtra = BUILDER.defineListAllowEmpty("plantsLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        plantsPoolRarity = BUILDER.defineListAllowEmpty("plantsLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        plantsPoolAmount = BUILDER.defineListAllowEmpty("plantsLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        waterPlantsPoolExtra = BUILDER.defineListAllowEmpty("waterPlantsLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        waterPlantsPoolRarity = BUILDER.defineListAllowEmpty("waterPlantsLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        waterPlantsPoolAmount = BUILDER.defineListAllowEmpty("waterPlantsLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        treePlantsPoolExtra = BUILDER.defineListAllowEmpty("treePlantsLootPool", Collections.emptyList(), BattleTowersConfig::validateItem);
        treePlantsPoolRarity = BUILDER.defineListAllowEmpty("treePlantsLootRarity", Collections.emptyList(), BattleTowersConfig::validateRarity);
        treePlantsPoolAmount = BUILDER.defineListAllowEmpty("treePlantsLootAmounts", Collections.emptyList(), BattleTowersConfig::validateFloat);

        BUILDER.pop();

        BUILDER.push("towerChestLootTableReplacements");
        BUILDER.comment("Adding a resource location of a custom loot-table to one of these lists replaces the loot generated ");
        BUILDER.comment("    using the Tower Extendable Loot Pools above  for floor/golem chests with loot from the supplied loot-table.");
        BUILDER.comment("Each list is in floor order with the first item being the table for floor 1 and the last item being the table for the Golem (Boss) chest");

        landTowerChestPools = BUILDER.defineList("landTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
        oceanTowerChestPools = BUILDER.defineList("oceanTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
        coreTowerChestPools = BUILDER.defineList("coreTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
        netherTowerChestPools = BUILDER.defineList("netherTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
        endTowerChestPools = BUILDER.defineList("endTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
        skyTowerChestPools = BUILDER.defineList("sskyTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
