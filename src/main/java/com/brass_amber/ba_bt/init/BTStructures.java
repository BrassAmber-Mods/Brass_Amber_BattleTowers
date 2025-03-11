package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABTMain;

import com.brass_amber.ba_bt.worldGen.structures.LandTower;
import com.brass_amber.ba_bt.worldGen.structures.OceanTower;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class BTStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_REGISTRY = DeferredRegister.create(Registries.STRUCTURE_TYPE, BABTMain.MODID);

    /**
     * Registers the base structure itself and sets what its path is. In this case,
     * this base structure will have the resourcelocation of babt:land_tower.
     */
    public static final RegistryObject<StructureType<LandTower>> LAND_TOWER = STRUCTURE_REGISTRY.register("land_tower", () -> explicitStructureTypeTyping(LandTower.CODEC));
    public static final RegistryObject<StructureType<OceanTower>> OCEAN_TOWER = STRUCTURE_REGISTRY.register("ocean_tower", () -> explicitStructureTypeTyping(OceanTower.CODEC));

    /**
     * This method explicitly states what the return type
     * is so that the IDE can put it into the DeferredRegistry properly. (Telepathic Grunt)
     */
    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_REGISTRY.register(eventBus);
    }

}
