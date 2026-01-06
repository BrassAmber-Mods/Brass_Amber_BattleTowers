package com.brass_amber.ba_bt;

import java.util.Collections;
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

    private static final ForgeConfigSpec.ConfigValue<Integer> FIRST_TOWER_DISTANCE =
            BUILDER.comment("General Settings -- Negative values are ignored").push("general")
                    .comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                            "Several other factors (Land height/other structures) can affect whether the structure actually spawns.")
                    .comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 30 chunks ")
                    .define("firstTowerDistance", 30);


    private static final ForgeConfigSpec.ConfigValue<Integer> LAND_TIME_BEFORE_COLLAPSE =
            BUILDER.comment("Length of time in seconds after Golem is defeated before the Land Tower collapses")
                    .defineInRange("landCollapseTimer", 30, 5, 60);

    private static final ForgeConfigSpec.ConfigValue<Integer> OCEAN_TIME_BEFORE_COLLAPSE =
            BUILDER.comment("Length of time in seconds after Golem is defeated before the Ocean Tower crumbles")
                    .defineInRange("oceanCollapseTimer", 45, 5, 60);

    private static final ForgeConfigSpec.ConfigValue<Integer> CORE_TIME_BEFORE_COLLAPSE =
            BUILDER.comment("Length of time in seconds after Golem is defeated before the Core Tower crumbles")
                    .defineInRange("coreCollapseTimer", 45, 5, 60);

    private static final ForgeConfigSpec.ConfigValue<Integer> NETHER_TIME_BEFORE_COLLAPSE =
            BUILDER.comment("Length of time in seconds after Golem is defeated before the Core Tower crumbles")
                    .defineInRange("netherCollapseTimer", 45, 5, 60);

    private static final ForgeConfigSpec.ConfigValue<Integer> END_TIME_BEFORE_COLLAPSE =
            BUILDER.comment("Length of time in seconds after Golem is defeated before the Core Tower crumbles")
                    .defineInRange("endCollapseTimer", 45, 5, 60);

    private static final ForgeConfigSpec.ConfigValue<Integer> SKY_TIME_BEFORE_COLLAPSE =
            BUILDER.comment("Length of time in seconds after Golem is defeated before the Core Tower crumbles")
                    .defineInRange("skyCollapseTimer", 45, 5, 60);


    private static final ForgeConfigSpec.ConfigValue<Double> LAND_TOWER_CRUMBLE_PERCENT =
                BUILDER.comment("How much of the Land tower remains after defeating the Golem. Default: 7% of tower.")
                        .defineInRange("landTowerDestruction", .07D, 0,1);

    private static final ForgeConfigSpec.ConfigValue<Double> OCEAN_TOWER_CRUMBLE_PERCENT =
                BUILDER.comment("How much of the Ocean tower remains after defeating the Golem. Default: 5% of tower.")
                        .defineInRange("oceanTowerDestruction", .05D, 0,1D);

    private static final ForgeConfigSpec.ConfigValue<Double> CORE_TOWER_CRUMBLE_PERCENT =
            BUILDER.comment("How much of the Core tower remains after defeating the Golem. Default: 0% of tower.")
                    .defineInRange("coreTowerDestruction", 0.00D, 0, 1D);

    private static final ForgeConfigSpec.ConfigValue<Double> NETHER_TOWER_CRUMBLE_PERCENT =
            BUILDER.comment("How much of the Nether tower remains after defeating the Golem. Default: 0% of tower.")
                    .defineInRange("netherTowerDestruction", 0D, 0, 1D);

    private static final ForgeConfigSpec.ConfigValue<Double> END_TOWER_CRUMBLE_PERCENT =
            BUILDER.comment("How much of the End tower remains after defeating the Golem. Default: 0% of tower.")
                    .defineInRange("endTowerDestruction", 0D, 0, 1D);

    private static final ForgeConfigSpec.ConfigValue<Double> SKY_TOWER_CRUMBLE_PERCENT =
            BUILDER.comment("How much of the Sky tower remains after defeating the Golem. Default: 0% of tower.")
                    .defineInRange("skyTowerDestruction", 0D, 0, 1D);

    private static final ForgeConfigSpec.ConfigValue<Boolean> MINIMAL_OCEAN_CARVING =
            BUILDER.comment("Makes the Ocean trench around the Ocean tower much smaller, reducing the lag on load")
                    .define("smallOceanTrench", false);

    private static final ForgeConfigSpec.ConfigValue<Boolean> OCEAN_TOWER_VOID_HOLE =
            BUILDER.comment("Whether the ocean tower destruction leaves a hole into the void. Default: true.")
                    .define("oceanTowerVoidHole", true);

    private static final ForgeConfigSpec.ConfigValue<Boolean> DEPTH_DROPPER_AFFECTS_MOBS =
            BUILDER.comment("Whether the Depth Dropper effect given by the Ocean Tower affects mobs.")
            .define("depthDropperAffectsMobs", false);



    private static final ForgeConfigSpec.ConfigValue<Double> LAND_GOLEM_HP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("landGolemHealth", 250D, 200, 100000);

    private static final ForgeConfigSpec.ConfigValue<Double> OCEAN_GOLEM_HP =
                BUILDER.comment("The total health of the Ocean Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("oceanGolemHealth", 300D, 250, 100000);

    private static final ForgeConfigSpec.ConfigValue<Double> CORE_GOLEM_HP =
            BUILDER.comment("The total health of the Core Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                    .defineInRange("coreGolemHealth", 350D, 300, 100000);

    private static final ForgeConfigSpec.ConfigValue<Double> NETHER_GOLEM_HP =
            BUILDER.comment("The total health of the Nether Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                    .defineInRange("netherGolemHealth", 400D, 350, 100000);

    private static final ForgeConfigSpec.ConfigValue<Double> END_GOLEM_HP =
            BUILDER.comment("The total health of the End Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                    .defineInRange("endGolemHealth", 450D, 400, 100000);

    private static final ForgeConfigSpec.ConfigValue<Double> SKY_GOLEM_HP =
            BUILDER.comment("The total health of the Sky Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                    .defineInRange("skyGolemHealth", 500D, 450, 100000);


    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LAND_TOWER_MOBS =
                BUILDER.pop().comment("Crashable settings -- If you edit these, and the game crashes, its on you").push("crashable")
                        .push("towerMobs").comment("Lists of mob ids of possible mobs to spawn in spawners inside each Tower. Each list must contain at least one value")
                        .defineListAllowEmpty("landTowerMobs", () -> List.of("minecraft:zombie", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> OCEAN_TOWER_MOBS =
                BUILDER.defineListAllowEmpty("oceanTowerMobs", () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:drowned", "minecraft:drowned", "minecraft:pufferfish"), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CORE_TOWER_MOBS =
            BUILDER.defineListAllowEmpty("coreTowerMobs", () -> List.of("minecraft:magma_cube", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider"), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> NETHER_TOWER_MOBS =
            BUILDER.defineListAllowEmpty("netherTowerMobs", Collections.emptyList(), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> END_TOWER_MOBS =
            BUILDER.defineListAllowEmpty("endTowerMobs", Collections.emptyList(), BattleTowersConfig::validateEntityName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SKY_TOWER_MOBS =
            BUILDER.defineListAllowEmpty("skyTowerMobs", Collections.emptyList(), BattleTowersConfig::validateEntityName);

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
    
    public static final ForgeConfigSpec SPEC = BUILDER.pop().build();
    
    public static int firstTowerDistance;

    public static int landTimeBeforeCollapse;
    public static int oceanTimeBeforeCollapse;
    public static int coreTimeBeforeCollapse;
    public static int netherTimeBeforeCollapse;
    public static int endTimeBeforeCollapse;
    public static int skyTimeBeforeCollapse;

    public static double landTowerCrumblePercent;
    public static double oceanTowerCrumblePercent;
    public static double coreTowerCrumblePercent;
    public static double netherTowerCrumblePercent;
    public static double endTowerCrumblePercent;
    public static double skyTowerCrumblePercent;

    public static boolean oceanTowerVoidHole;
    public static boolean minimalOceanCarving;
    public static boolean depthDropperAffectsMobs;

    public static double landGolemHP;
    public static double oceanGolemHP;
    public static double coreGolemHP;
    public static double netherGolemHP;
    public static double endGolemHP;
    public static double skyGolemHP;

    public static List<EntityType<?>> landTowerMobs;
    public static List<EntityType<?>> oceanTowerMobs;
    public static List<EntityType<?>> coreTowerMobs;
    public static List<EntityType<?>> netherTowerMobs;
    public static List<EntityType<?>> endTowerMobs;
    public static List<EntityType<?>> skyTowerMobs;

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

        landTimeBeforeCollapse = LAND_TIME_BEFORE_COLLAPSE.get();
        oceanTimeBeforeCollapse = OCEAN_TIME_BEFORE_COLLAPSE.get();
        coreTimeBeforeCollapse = CORE_TIME_BEFORE_COLLAPSE.get();
        netherTimeBeforeCollapse = NETHER_TIME_BEFORE_COLLAPSE.get();
        endTimeBeforeCollapse = END_TIME_BEFORE_COLLAPSE.get();
        skyTimeBeforeCollapse = SKY_TIME_BEFORE_COLLAPSE.get();

        landTowerCrumblePercent = LAND_TOWER_CRUMBLE_PERCENT.get();
        oceanTowerCrumblePercent = OCEAN_TOWER_CRUMBLE_PERCENT.get();
        coreTowerCrumblePercent = CORE_TOWER_CRUMBLE_PERCENT.get();
        netherTowerCrumblePercent = NETHER_TOWER_CRUMBLE_PERCENT.get();
        endTowerCrumblePercent = END_TOWER_CRUMBLE_PERCENT.get();
        skyTowerCrumblePercent = SKY_TOWER_CRUMBLE_PERCENT.get();

        oceanTowerVoidHole = OCEAN_TOWER_VOID_HOLE.get();
        minimalOceanCarving = MINIMAL_OCEAN_CARVING.get();
        depthDropperAffectsMobs = DEPTH_DROPPER_AFFECTS_MOBS.get();

        landGolemHP = LAND_GOLEM_HP.get();
        oceanGolemHP = OCEAN_GOLEM_HP.get();
        coreGolemHP = CORE_GOLEM_HP.get();
        netherGolemHP = NETHER_GOLEM_HP.get();
        endGolemHP = END_GOLEM_HP.get();
        skyGolemHP = SKY_GOLEM_HP.get();
        
        landTowerMobs = LAND_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        oceanTowerMobs = OCEAN_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        coreTowerMobs = CORE_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        netherTowerMobs = NETHER_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        endTowerMobs = END_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());
        skyTowerMobs = SKY_TOWER_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toList());

        landTowerChestLootTables = LAND_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        oceanTowerChestLootTables = OCEAN_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        coreTowerChestLootTables = CORE_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        netherTowerChestLootTables = NETHER_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        endTowerChestLootTables = END_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
        skyTowerChestLootTables = SKY_TOWER_CHEST_LOOT_TABLES.get().stream().collect(Collectors.toUnmodifiableList());
    }
}
