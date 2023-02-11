package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import jdk.dynalink.beans.StaticClass;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class BTConfigurables {
    public static final BTConfigValue<Integer> landAverageSeperationModifier;
    public static final BTConfigValue<Integer> landMinimumSeperation;
    public static final BTConfigValue<Integer> oceanAverageSeperationModifier;
    public static final BTConfigValue<Integer> oceanMinimumSeperation;
    public static final BTConfigValue<Integer> firstTowerDistance;
    public static final BTConfigValue<Double> landTowerCrumblePercent;
    public static final BTConfigValue<Double> landGolemHP;
    public static final BTConfigListValue<String> landTowerMobs;
    public static final BTConfigListValue<String> oceanTowerMobs;

    private static final Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());

    static {
        landAverageSeperationModifier = new BTConfigValue<>(12, 1, 100);
        landMinimumSeperation =  new BTConfigValue<>(20, 4, 1000);
        oceanAverageSeperationModifier = new BTConfigValue<>(24, 1, 100) ;
        oceanMinimumSeperation = new BTConfigValue<>(16, 4, 1000);
        firstTowerDistance = new BTConfigValue<>(35, 1 , 10000);
        landTowerCrumblePercent = new BTConfigValue<>(.83D, 0D, 1D);
        landGolemHP = new BTConfigValue<>(250D, 200D, 1800D);
        landTowerMobs = new BTConfigListValue<>(
                List.of("minecraft:zombie", "minecraft:skeleton", "minecraft:spider"),
                1, 42
        );
        oceanTowerMobs = new BTConfigListValue<>(
                List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:pufferfish"),
                1, 26
        );

        Path configpath = defaultConfigPath.resolve(String.format(Locale.ROOT, "%s-%s.toml", BrassAmberBattleTowers.MOD_ID, ModConfig.Type.COMMON.extension()));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("ba-battletowers-config"));
            writer.write("Writing");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class BTConfigValue<T> {

        public final T defaultValue;
        public T currentValue;
        public final T minimumValue;
        public final T maximumValue;

        BTConfigValue(T defaultV, T minimum, T maximum) {
            this.defaultValue = defaultV;
            this.currentValue = defaultV;
            this.minimumValue = minimum;
            this.maximumValue = maximum;
        }

        public void setCurrentValue(T newDefault) {
            this.currentValue = newDefault;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public T getMinimumValue() {
            return minimumValue;
        }

        public T getMaximumValue() {
            return maximumValue;
        }
    }

    public static class BTConfigListValue<T> extends BTConfigValue<List<T>> {

        public final int minimumEntries;
        public final int maximumEntries;

        BTConfigListValue(List<T> defaultV, int minimumEntries, int maximumEntries) {

            super(defaultV, null, null);
            this.minimumEntries = minimumEntries;
            this.maximumEntries = maximumEntries;
        }

        @Override
        public void setCurrentValue(List<T> newDefault) {
            int listSize = newDefault.size();
            // Check that the new list has enough entries, if not fill from default list
            if (listSize < this.minimumEntries) {
                for (int i = 0; i < this.minimumEntries - listSize; i++) {
                    newDefault.add(this.defaultValue.get(i));
                }
                // Check that the list does not have too many entries, if so remove until equal to maximum
            } else if (listSize > this.maximumEntries) {
                for (int i = listSize; i > this.maximumEntries; i--) {
                    newDefault.remove(this.defaultValue.get(i-1));
                }
            }
            super.setCurrentValue(newDefault);
        }
    }
    
}
