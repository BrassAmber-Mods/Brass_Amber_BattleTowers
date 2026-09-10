package com.brass_amber.ba_bt.datagen.datapack;

import com.brass_amber.ba_bt.BABattleTowers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerProcessors.*;

public class ProcessorListBootstrap {

    public static final ResourceKey<StructureProcessorList> LAND_SHELL_DEGRADATION = registerStructureProcessorListKey("land_shell_degradation");
    public static final ResourceKey<StructureProcessorList> LAND_FLOOR_DEGRADATION = registerStructureProcessorListKey("land_floor_degradation");
    public static final ResourceKey<StructureProcessorList> LAND_WALL_DEGRADATION = registerStructureProcessorListKey("land_wall_degradation");
    public static final ResourceKey<StructureProcessorList> LAND_CROP_PLACEMENT = registerStructureProcessorListKey("land_crop_placement");
    public static final ResourceKey<StructureProcessorList> LAND_CARPET_FIXER = registerStructureProcessorListKey("land_carpet_fixer");

    public static final ResourceKey<StructureProcessorList> SAND_DEGRADATION = registerStructureProcessorListKey("sand_remove_7");
    public static final ResourceKey<StructureProcessorList> SANDSTONE_REPLACER = registerStructureProcessorListKey("sandstone_replacer");

    public static final ResourceKey<StructureProcessorList> OCEAN_SHELL_DEGRADATION = registerStructureProcessorListKey("ocean_shell_degradation");
    public static final ResourceKey<StructureProcessorList> OCEAN_FLOOR_DEGRADATION = registerStructureProcessorListKey("ocean_floor_degradation");
    public static final ResourceKey<StructureProcessorList> OCEAN_WALL_DEGRADATION = registerStructureProcessorListKey("ocean_wall_degradation");

    public static final ResourceKey<StructureProcessorList> CORE_SHELL_DEGRADATION = registerStructureProcessorListKey("core_shell_degradation");
    public static final ResourceKey<StructureProcessorList> CORE_FLOOR_DEGRADATION = registerStructureProcessorListKey("ocean_floor_degradation");
    public static final ResourceKey<StructureProcessorList> CORE_ORE_PLACEMENT = registerStructureProcessorListKey("core_ore_placement");
    public static final ResourceKey<StructureProcessorList> CORE_ROOF_DEGRADATION = registerStructureProcessorListKey("core_roof_degradation");

    public static void processorListBootstrap(BootstapContext<StructureProcessorList> structureProcessorContext) {
        structureProcessorContext.register(
                LAND_SHELL_DEGRADATION,
                new StructureProcessorList(List.of(LAND_WALL, LAND_NORMAL_STAIRS, LAND_NORMAL_FLOOR))
        );
        structureProcessorContext.register(
                LAND_FLOOR_DEGRADATION,
                new StructureProcessorList(List.of(LAND_NORMAL_FLOOR))
        );
        structureProcessorContext.register(
                LAND_WALL_DEGRADATION,
                new StructureProcessorList(List.of(LAND_WALL))
        );
        structureProcessorContext.register(
                LAND_CROP_PLACEMENT,
                new StructureProcessorList(List.of(LAND_CROP_PLACER))
        );
        structureProcessorContext.register(
                LAND_CARPET_FIXER,
                new StructureProcessorList(List.of(LAND_CARPET_PLACER))
        );


        structureProcessorContext.register(
                SAND_DEGRADATION,
                new StructureProcessorList(List.of(SAND_REMOVE_7))
        );
        structureProcessorContext.register(
                SANDSTONE_REPLACER,
                new StructureProcessorList(List.of(SAND_REMOVE_7, SANDSTONE))
        );

        structureProcessorContext.register(
                OCEAN_SHELL_DEGRADATION,
                new StructureProcessorList(List.of(OCEAN_NORMAL, NORMAL_STAIRS_OCEAN, OCEAN_NORMAL_FLOOR, WATERLOGGED))
        );
        structureProcessorContext.register(
                OCEAN_FLOOR_DEGRADATION,
                new StructureProcessorList(List.of(OCEAN_NORMAL_FLOOR))
        );
        structureProcessorContext.register(
                OCEAN_WALL_DEGRADATION,
                new StructureProcessorList(List.of(OCEAN_NORMAL))
        );

    }

    private static ResourceKey<StructureProcessorList> registerStructureProcessorListKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, BABattleTowers.locate(name));
    }

}
