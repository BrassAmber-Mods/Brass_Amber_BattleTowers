package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.worldGen.AvoidStructuresStructurePlacement;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BTStructurePlacements {

    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPE_REGISTRY = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, BABattleTowers.MOD_ID);

    public static final RegistryObject<StructurePlacementType<AvoidStructuresStructurePlacement>> AVOID_STRUCTURES_STRUCTURE_PLACEMENT = STRUCTURE_PLACEMENT_TYPE_REGISTRY.register("avoid_structures_structure_placement", () -> explicitStructureTypeTyping(AvoidStructuresStructurePlacement.CODEC));

    private static <T extends StructurePlacement> StructurePlacementType<T> explicitStructureTypeTyping(Codec<T> structurePlacementTypeCodec) {
        return () -> structurePlacementTypeCodec;
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_PLACEMENT_TYPE_REGISTRY.register(eventBus);
    }

}
