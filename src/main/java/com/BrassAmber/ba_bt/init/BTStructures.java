package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.worldGen.BTJigsawConfiguration;
import com.BrassAmber.ba_bt.worldGen.structures.LandBattleTower;
import com.BrassAmber.ba_bt.worldGen.structures.OvergrownLandTower;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class BTStructures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BrassAmberBattleTowers.MOD_ID);

    public static final RegistryObject<StructureFeature<BTJigsawConfiguration>> LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("land_battle_tower", () -> (new LandBattleTower(BTJigsawConfiguration.CODEC)));
    //public static final RegistryObject<StructureFeature<BTJigsawConfiguration>> OVERGROWN_LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("overgrown_land_battle_tower", () -> (new OvergrownLandTower(BTJigsawConfiguration.CODEC)));
    // public static final RegistryObject<StructureFeature<JigsawConfiguration>> SKY_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("sky_battle_tower", () -> (new SkyBattleTower(JigsawConfiguration.CODEC)));

    /**
     * This is where we set the rarity of your structures and determine if land conforms to it.
     * See the comments in below for more details.
     */
    public static void setupStructures() {
        int landMinimum = Mth.clamp(BattleTowersConfig.landMinimumSeperation.get(), 0 ,1000);
        int landAverage = BattleTowersConfig.landMinimumSeperation.get() + Mth.clamp(BattleTowersConfig.landAverageSeperationModifier.get(), 0 ,1000);
        setupMapSpacingAndLand(
                LAND_BATTLE_TOWER.get(), /* The instance of the structure */
                new StructureFeatureConfiguration(landAverage /* average distance apart in chunks between spawn attempts */,
                        landMinimum /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        774562107 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */
                ),
                true);
        //setupMapSpacingAndLand(
                //OVERGROWN_LAND_BATTLE_TOWER.get(), /* The instance of the structure */
                //new StructureFeatureConfiguration(landAverage /* average distance apart in chunks between spawn attempts */,
                        //landMinimum /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        //774562102 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */
                //),
                //true);
        // setupMapSpacingAndLand(
                // SKY_BATTLE_TOWER.get(),
                // new StructureFeatureConfiguration(26
                        // 22
                        // 1526374890),
                // false);


        // Add more structures here and so on
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            StructureFeatureConfiguration structureFeatureConfiguration,
            boolean transformSurroundingLand)
    {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build();


        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             */
            if(structureMap instanceof ImmutableMap){
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureFeatureConfiguration);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, structureFeatureConfiguration);
            }
        });
    }
}
