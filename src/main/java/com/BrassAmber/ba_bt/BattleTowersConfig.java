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

    public static final ForgeConfigSpec.ConfigValue<Integer> landAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> landMinimumSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> oceanMinimumSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> firstTowerDistance;
    public static final ForgeConfigSpec.ConfigValue<Double> landTowerCrumblePercent;
    public static final ForgeConfigSpec.ConfigValue<Double> landGolemHP;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> landTowerMobs;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> oceanTowerMobs;
    private static final Predicate<Object> stringListValidator = (Objects::nonNull);

    static {

        BUILDER.push("General Settings -- Negative values are ignored");
        BUILDER.comment("Tower Separation values below only change how often the game tries to spawn the structure. " +
                "Several other factors (Land height/other structures) can affect whether the structure actually spawns.");
        landMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 20 chunk minimum. " +
                                "Default and Minimum: 20 Chunks")
                .define("Land minimum separation", 20);
        oceanMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Ocean Towers measured in chunks. " +
                                "(due to structure changes in 1.18.2 there is now a self imposed 16 chunk minimum. " +
                                "Default and Minimum: 16 Chunks")
                        .define("Ocean minimum separation", 16);
        firstTowerDistance = BUILDER.comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 35 chunks ")
                .define("First Tower Distance", 35);
        BUILDER.pop();

        BUILDER.push("Advanced Settings -- take note of the range for each value, values outside the ranges will be discarded");
        landGolemHP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("Total health of the Land Golem", 250D, 200, 1800);
        // BUILDER.comment("Capped at 1800 because more than 900 hearts is absurd.");
        landTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 83% of tower.")
                        .defineInRange("Percent of Land Tower to destroy", .83D, 0,1);
        landAverageSeperationModifier = BUILDER.comment("This value is added to the Land Tower minimum separation"
                + " above to get the average separation between Land Towers for spawning measured in chunks.",
                "I.E. if you leave the minimum separation at 20, and change this value to 10 then Land Towers would spawn"
                        + " a maximum of 30 chunks (20 + 10) apart and a minimum of 20 chunks apart.")
                .defineInRange("Land average separation modifier", 12, 1, 100);
        oceanAverageSeperationModifier = BUILDER.comment("This value is added to the Ocean Tower minimum separation"
                                + " above to get the average separation between Ocean Towers for spawning measured in chunks.",
                        "See Land Tower Average Separation for explanation of use.")
                .defineInRange("Ocean average separation modifier", 24, 1, 100);
        BUILDER.pop();


        BUILDER.push("Crash-able settings -- If you edit these, and the game crashes, its on you");
        landTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the land tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Land Tower Mobs"), () -> List.of("minecraft:zombie", "minecraft:skeleton", "minecraft:spider"), stringListValidator);
        oceanTowerMobs =
                BUILDER.comment("A list of mob ids of possible mobs to spawn in spawners inside the land tower. Must contain at least one value")
                        .defineListAllowEmpty(List.of("Ocean Tower Mobs"), () -> List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:pufferfish"), stringListValidator);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
