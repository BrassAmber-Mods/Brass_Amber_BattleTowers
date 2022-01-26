package com.BrassAmber.ba_bt;

import net.minecraftforge.common.ForgeConfigSpec;
import org.lwjgl.system.CallbackI;

public class BattleTowersConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> landAverageDistanceModifier;
    public static final ForgeConfigSpec.ConfigValue<Integer> landMinimumDistance;
    public static final ForgeConfigSpec.ConfigValue<Double> towerCrumblePercent;


    static {
        BUILDER.push("Config for Brass-Amber BattleTowers");
        BUILDER.pop();

        BUILDER.push("General Settings");
        landMinimumDistance =
                BUILDER.comment("The minimum distance apart in chunks that land towers can spawn. Default: 16 chunks")
                .define("Land minimum distance", 20);

        BUILDER.pop();

        BUILDER.push("Advanced Settings --take note of the range for each value. Values outside the ranges will be discarded");
        towerCrumblePercent =
                BUILDER.comment("How much of the tower is destroyed after defeating the golem. Default: 75% of tower.")
                        .defineInRange("Percent of Tower to destroy", .75D, 0,1);
        landAverageDistanceModifier = BUILDER.comment("This value is added to the Land Tower minimum distance value"
                + " above to get the average distance between Land Towers for spawning",
                "I.E. if you leave the minimum distance at 20, and change this value to 10 then Land Towers would spawn"
                        + " a minimum of 20 chunks apart, and on average 30 chunks apart")
                .defineInRange("Land average modifier", 5, 1, 100);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
