package com.brass_amber.ba_bt;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BABattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BattleTowersConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<Integer> FIRST_TOWER_DISTANCE = BUILDER.comment("General Settings -- Negative values are ignored").push("general")
            .comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                "Several other factors (Land height/other structures) can affect whether the structure actually spawns.")
            .comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 30 chunks ")
                .define("firstTowerDistance", 30);
    
    private static final ForgeConfigSpec.ConfigValue<Integer> LAND_MINIMUM_SEPERATION = BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. " +
                                "(9 chunk minimum. Default: 45 Chunks")
                        .defineInRange("landMinimumSeparation", 45, 9, 999999999);;
    private static final ForgeConfigSpec.ConfigValue<Integer> OCEAN_MINIMUM_SEPERATION = BUILDER.comment("The minimum possible distance between Ocean Towers measured in chunks. " +
                                "(11 chunk minimum. Default: 60 Chunks")
                        .defineInRange("oceanMinimumSeparation", 60, 9, 999999999);
    private static final ForgeConfigSpec.ConfigValue<Integer> CORE_MINIMUM_SEPERATION = BUILDER.comment("The minimum possible distance between Core Towers measured in chunks. " +
                    "(15 chunk minimum. Default: 75 Chunks")
            .defineInRange("coreMinimumSeparation", 75, 15, 999999999);

    private static final ForgeConfigSpec.ConfigValue<Boolean> DEPTH_DROPPER_AFFECTS_MOBS = BUILDER.comment("Whether the Depth Dropper effect given by the Ocean Tower"
                                + " affects mobs.")
                .define("depthDropperAffectsMobs", false);


    private static final ForgeConfigSpec.ConfigValue<Integer> LAND_TIME_BEFORE_COLLAPSE =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Land Tower collapses")
                        .defineInRange("landCollapseTimer", 30, 30, 60);

    private static final ForgeConfigSpec.ConfigValue<Integer> OCEAN_TIME_BEFORE_COLLAPSE =
                BUILDER.comment("Length of time in seconds after Golem is defeated before the Ocean Tower crumbles")
                        .defineInRange("oceanCollapseTimer", 30, 30, 60);

    private static final ForgeConfigSpec.ConfigValue<Double> LAND_TOWER_CRUMBLE_PERCENT =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 85% of tower.")
                        .defineInRange("landTowerDestruction", .85D, 0,1);

    private static final ForgeConfigSpec.ConfigValue<Double> OCEAN_TOWER_CRUMBLE_PERCENT =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 100% of tower.")
                        .defineInRange("oceanTowerDestruction", 1D, .5D,1D);


    private static final ForgeConfigSpec.ConfigValue<Double> LAND_GOLEM_HP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("landGolemHealth", 250D, 200, 100000);
    private static final ForgeConfigSpec.ConfigValue<Double> OCEAN_GOLEM_HP =
                BUILDER.comment("The total health of the Ocean Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("oceanGolemHealth", 300D, 250, 100000);


    private static final ForgeConfigSpec.ConfigValue<Boolean> MINIMAL_OCEAN_CARVING =
                BUILDER.comment("Makes the Ocean trench around the Ocean tower much smaller, reducing the lag on load")
                        .define("smallOceanTrench", false);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LAND_TOWER_MOBS =
                BUILDER.pop().comment("Crashable settings -- If you edit these, and the game crashes, its on you").push("crashable")
                        .push("towerMobs").comment("Lists of mob ids of possible mobs to spawn in spawners inside each Tower. Each list must contain at least one value")
                        .defineListAllowEmpty("landTowerMobs", () -> List.of("minecraft:zombie", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> OCEAN_TOWER_MOBS =
                BUILDER.defineListAllowEmpty("oceanTowerMobs", () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:drowned", "minecraft:drowned", "minecraft:pufferfish"), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CORE_TOWER_MOBS =
            BUILDER.defineListAllowEmpty("coreTowerMobs", () -> List.of("minecraft:magma_cube", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider"), BattleTowersConfig::validateEntityName);


    private static final ForgeConfigSpec.ConfigValue<Integer> BOOK_LEVEL_ENCHANT = BUILDER.pop().push("towerLootOptions")
            .comment("The number of xp levels books and tools are enchanted with in loot, " +
                "Ie a 20 here means that books and tools that appear in loot will contain enchants as if enchanted with 20 levels of xp  up to 41 (3 extra per tower floor)" )
                .defineInRange("bookXPLevels", 10, 0, 40);
    private static final ForgeConfigSpec.ConfigValue<Boolean> ENCHANT_ARMOR = BUILDER.comment("Whether or not armor in loot should be enchanted").define("enchantedArmor", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> ENCHANT_TOOLS = BUILDER.comment("Whether or not tools/weapons in loot should be enchanted").define("enchantedTools", true);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_CONTAINER_TYPES = BUILDER.comment("List of extra specifiable container types for use in custom tower floors").defineList("extraChestTypes", () -> List.of("White Shulker"), BattleTowersConfig::validateString);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_CONTAINER_BLOCKS = BUILDER.comment("List of extra container blocks for placing in custom tower floors").defineList("extraChestBlocks", () -> List.of("minecraft:white_shulker_box"), BattleTowersConfig::validateBlock);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LAND_TOWER_CHEST_LOOT_TABLES = BUILDER.pop().pop().push("towerChestLootTableReplacements")
            .comment("Adding a resource location of a custom loot-table to one of these lists replaces the loot generated ")
            .comment("    using the Tower Extendable Loot Pools above  for floor/golem chests with loot from the supplied loot-table.")
            .comment("Each list is in floor order with the first item being the table for floor 1 and the last item being the table for the Golem (Boss) chest")
            .defineList("landTowerChestPools", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> OCEAN_TOWER_CHEST_LOOT_TABLES = BUILDER.defineList("oceanTowerChestLootTables", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CORE_TOWER_CHEST_LOOT_TABLES = BUILDER.defineList("coreTowerChestLootTables", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> NETHER_TOWER_CHEST_LOOT_TABLES = BUILDER.defineList("netherTowerChestLootTables", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> END_TOWER_CHEST_LOOT_TABLES = BUILDER.defineList("endTowerChestLootTables", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SKY_TOWER_CHEST_LOOT_TABLES = BUILDER.defineList("skyTowerChestLootTables", List.of("", "", "", "", "", "", "", "", ""), BattleTowersConfig::validateString);
    
    public static final ForgeConfigSpec SPEC = BUILDER.pop().build();;
    
    public static int firstTowerDistance;
    public static int landMinimumSeperation;
    public static int oceanMinimumSeperation;
    public static int coreMinimumSeperation;
    public static boolean depthDropperAffectsMobs;

    public static int landTimeBeforeCollapse;
    public static int oceanTimeBeforeCollapse;
    public static double landTowerCrumblePercent;
    public static double oceanTowerCrumblePercent;

    public static double landGolemHP;
    public static double oceanGolemHP;

    public static boolean minimalOceanCarving;;

    public static List<EntityType<?>> landTowerMobs;
    public static List<EntityType<?>> oceanTowerMobs;
    public static List<EntityType<?>> coreTowerMobs;

    public static int bookLevelEnchant;
    public static boolean enchantArmor;
    public static boolean enchantTools;

    public static List<String> extraContainerTypes;
    public static List<String> extraContainerBlocks;

    public static List<String> landTowerChestLootTables;
    public static List<String> oceanTowerChestLootTables;
    public static List<String> coreTowerChestLootTables;
    public static List<String> netherTowerChestLootTables;
    public static List<String> endTowerChestLootTables;
    public static List<String> skyTowerChestLootTables;
    

    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }
    private static boolean validateInteger(final Object obj) {
        return obj instanceof Integer x && x > 0;
    }

    private static boolean validateDoubleOrFloat(final Object obj) {
        return (obj instanceof Double x && x > 0d) || (obj instanceof Float z && z > 0f);
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

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        
        firstTowerDistance = FIRST_TOWER_DISTANCE.get();
        landMinimumSeperation = LAND_MINIMUM_SEPERATION.get();
        oceanMinimumSeperation = OCEAN_MINIMUM_SEPERATION.get();
        coreMinimumSeperation = CORE_MINIMUM_SEPERATION.get();

        depthDropperAffectsMobs = DEPTH_DROPPER_AFFECTS_MOBS.get();

        landGolemHP = LAND_GOLEM_HP.get();

        landTimeBeforeCollapse = LAND_TIME_BEFORE_COLLAPSE.get();
        landTowerCrumblePercent =  LAND_TOWER_CRUMBLE_PERCENT.get();
        oceanGolemHP = OCEAN_GOLEM_HP.get();
        oceanTimeBeforeCollapse =  OCEAN_TIME_BEFORE_COLLAPSE.get();
        oceanTowerCrumblePercent = OCEAN_TOWER_CRUMBLE_PERCENT.get();
        minimalOceanCarving = MINIMAL_OCEAN_CARVING.get();
        
        landTowerMobs = LAND_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        oceanTowerMobs = OCEAN_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        coreTowerMobs = CORE_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());

        bookLevelEnchant =  BOOK_LEVEL_ENCHANT.get();
        enchantArmor = ENCHANT_ARMOR.get();
        enchantTools = ENCHANT_TOOLS.get();
        extraContainerTypes = EXTRA_CONTAINER_TYPES.get().stream().collect(Collectors.toUnmodifiableList());
        extraContainerBlocks = EXTRA_CONTAINER_BLOCKS.get().stream().collect(Collectors.toUnmodifiableList());

        landTowerChestLootTables = LAND_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        oceanTowerChestLootTables = OCEAN_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        coreTowerChestLootTables = CORE_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        netherTowerChestLootTables = NETHER_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        endTowerChestLootTables = END_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        skyTowerChestLootTables = SKY_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
    }
}
