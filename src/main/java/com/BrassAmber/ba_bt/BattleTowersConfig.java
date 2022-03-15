package com.BrassAmber.ba_bt;

import net.minecraftforge.common.ForgeConfigSpec;

public class BattleTowersConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> landAverageSeperationModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> landMinimumSeperation;
    public static final ForgeConfigSpec.ConfigValue<Integer> firstTowerDistance;
    public static final ForgeConfigSpec.ConfigValue<Double> landTowerCrumblePercent;
    public static final ForgeConfigSpec.ConfigValue<Double> landGolemHP;



    static {
        BUILDER.push("Config for Brass-Amber BattleTowers");
        BUILDER.pop();

        BUILDER.push("General Settings -- Negative values are ignored");
        landMinimumSeperation =
                BUILDER.comment("The minimum possible distance between Land Towers measured in chunks. Default: 32 chunks")
                .define("Land minimum separation", 32);
        firstTowerDistance = BUILDER.comment("Minimum distance from spawn a Tower can be measured in chunks (Applies to X and Z). Default: 96 chunks ")
                .define("First Tower Distance", 64);

        BUILDER.pop();

        BUILDER.push("Advanced Settings -- take note of the range for each value, values outside the ranges will be discarded");
        landGolemHP =
                BUILDER.comment("The total health of the Land Golem, divide by two per heart. I.E a value of 300 is 150 hearts")
                        .defineInRange("Total health of the Land Golem", 250D, 200, 1800);
        BUILDER.comment("Capped at 1800 because more than 900 hearts is absurd.");
        landTowerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the Golem. Default: 75% of tower.")
                        .defineInRange("Percent of Land Tower to destroy", .83D, 0,1);
        landAverageSeperationModifier = BUILDER.comment("This value is added to the Land Tower minimum separation"
                + " above to get the average separation between Land Towers for spawning measured in chunks.",
                "I.E. if you leave the minimum separation at 16, and change this value to 10 then Land Towers would spawn"
                        + " on average 26 chunks apart and a minimum of 20 chunks apart.")
                .defineInRange("Land average separation modifier", 16, 1, 100);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
