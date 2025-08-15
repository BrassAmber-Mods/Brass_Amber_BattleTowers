package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.Map;

public class BTStructureGen {

    public static final ResourceKey<Structure> LAND_TOWER = registerStructureKey("land_tower");
    public static final ResourceKey<Structure> OCEAN_TOWER = registerStructureKey("ocean_tower");
    public static final ResourceKey<Structure> CORE_TOWER = registerStructureKey("core_tower");

    public static final ResourceKey<StructureSet> LAND_TOWER_SET = registerSetKey("land_tower_set");
    public static final ResourceKey<StructureSet> OCEAN_TOWER_SET = registerSetKey("ocean_tower_set");
    public static final ResourceKey<StructureSet> CORE_TOWER_SET = registerSetKey("core_tower_set");


    public static void bootstrap(BootstapContext<Structure> context) {
        var biomes = context.lookup(Registries.BIOME);


        context.register(LAND_TOWER,
                new LandTower(
                        new Structure.StructureSettings(
                                biomes.getOrThrow(BTTags.Biomes.LAND_TOWER_BIOMES),
                                Map.of(),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.NONE
                        ),
                        new TowerStructure.BTStructureSettings(3),
                        0.17f
                )
        );

        context.register(OCEAN_TOWER,
                new OceanTower(
                        new Structure.StructureSettings(
                                biomes.getOrThrow(BTTags.Biomes.OCEAN_TOWER_BIOMES),
                                Map.of(),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.NONE
                        ),
                        new TowerStructure.BTStructureSettings(4)
                )
        );

        context.register(CORE_TOWER,
                new OceanTower(
                        new Structure.StructureSettings(
                                biomes.getOrThrow(BTTags.Biomes.CORE_TOWER_BIOMES),
                                Map.of(),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.NONE
                        ),
                        new TowerStructure.BTStructureSettings(5)
                )
        );
    }


    public static void setBootstrap(BootstapContext<StructureSet> context) {
        var structures = context.lookup(Registries.STRUCTURE);

        context.register(LAND_TOWER_SET,
                new StructureSet(
                        structures.getOrThrow(LAND_TOWER),
                        new RandomSpreadStructurePlacement(9, 5, RandomSpreadType.LINEAR,121144999)
                )
        );

        context.register(OCEAN_TOWER_SET,
                new StructureSet(
                        structures.getOrThrow(OCEAN_TOWER),
                        new RandomSpreadStructurePlacement(9, 5, RandomSpreadType.LINEAR,153511499)
                )
        );

        context.register(CORE_TOWER_SET,
                new StructureSet(
                        structures.getOrThrow(CORE_TOWER),
                        new RandomSpreadStructurePlacement(9, 5, RandomSpreadType.LINEAR,315185999)
                )
        );
    }

    public static ResourceKey<Structure> registerStructureKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, BABattleTowers.locate(name));
    }

    public static ResourceKey<StructureSet> registerSetKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, BABattleTowers.locate(name));
    }

}
