package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;

import com.brass_amber.ba_bt.worldGen.structures.CoreTower;
import com.brass_amber.ba_bt.worldGen.structures.LandTower;
import com.brass_amber.ba_bt.worldGen.structures.OceanTower;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class BTStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_REGISTRY = DeferredRegister.create(Registries.STRUCTURE_TYPE, BABattleTowers.MOD_ID);

    /**
     * Registers the base structure itself and sets what its path is. In this case,
     * this base structure will have the resourcelocation of ba_bt:land_tower.
     */
    public static final RegistryObject<StructureType<LandTower>> LAND_TOWER = STRUCTURE_REGISTRY.register("land_tower", () -> explicitStructureTypeTyping(LandTower.CODEC));
    public static final RegistryObject<StructureType<OceanTower>> OCEAN_TOWER = STRUCTURE_REGISTRY.register("ocean_tower", () -> explicitStructureTypeTyping(OceanTower.CODEC));
    public static final RegistryObject<StructureType<CoreTower>> CORE_TOWER = STRUCTURE_REGISTRY.register("core_tower", () -> explicitStructureTypeTyping(CoreTower.CODEC));

    /**
     * Resource key for referencing these structures like #net.minecraft.world.level.levelgen.structure.BuiltinStructures
     */
    public static final ResourceKey<Structure> LAND_TOWER_KEY = createKey("land_tower");
    public static final ResourceKey<Structure> OCEAN_TOWER_KEY = createKey("ocean_tower");
    public static final ResourceKey<Structure> CORE_TOWER_KEY = createKey("core_tower");

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

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, BABattleTowers.locate(name));
    }

}
