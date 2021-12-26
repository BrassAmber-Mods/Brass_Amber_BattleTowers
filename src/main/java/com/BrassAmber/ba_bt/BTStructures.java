package com.BrassAmber.ba_bt;

import com.BrassAmber.ba_bt.structures.LandBattleTower;
import com.BrassAmber.ba_bt.structures.OceanBattleTower;
import com.BrassAmber.ba_bt.structures.SkyBattleTower;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class BTStructures {

    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BrassAmberBattleTowers.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("land_battle_tower", () -> (new LandBattleTower(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> OCEAN_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("ocean_battle_tower", () -> (new OceanBattleTower(NoFeatureConfig.CODEC)));
    public static final RegistryObject<Structure<NoFeatureConfig>> SKY_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("sky_battle_tower", () -> (new SkyBattleTower(NoFeatureConfig.CODEC)));

    /**
     * This is where we set the rarity of your structures and determine if land conforms to it.
     * See the comments in below for more details.
     */
    public static void setupStructures() {
        setupMapSpacingAndLand(
                LAND_BATTLE_TOWER.get(), /* The instance of the structure */
                new StructureSeparationSettings(16 /* average distance apart in chunks between spawn attempts */,
                        12 /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        1234567890 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);
        setupMapSpacingAndLand(
                OCEAN_BATTLE_TOWER.get(), /* The instance of the structure */
                new StructureSeparationSettings(6 /* average distance apart in chunks between spawn attempts */,
                        5 /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        242556778 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);
        setupMapSpacingAndLand(
                SKY_BATTLE_TOWER.get(), /* The instance of the structure */
                new StructureSeparationSettings(26 /* average distance apart in chunks between spawn attempts */,
                        22 /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        652637489 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);


        // Add more structures here and so on
    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();


        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             */
            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
}
