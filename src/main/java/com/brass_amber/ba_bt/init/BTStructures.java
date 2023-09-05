package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.worldGen.structures.BattleTowerStructure;
import com.brass_amber.ba_bt.worldGen.structures.LandBattleTower;
import com.brass_amber.ba_bt.worldGen.structures.OceanBattleTower;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BTStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_REGISTRY = DeferredRegister.create(Registries.STRUCTURE_TYPE, BrassAmberBattleTowers.MOD_ID);

    public static final RegistryObject<StructureType<LandBattleTower>> LAND_BATTLE_TOWER = STRUCTURE_REGISTRY.register("bt_land_tower", () -> explicitStructureTypeTyping(LandBattleTower.CODEC));
    public static final RegistryObject<StructureType<BattleTowerStructure>> OCEAN_BATTLE_TOWER = STRUCTURE_REGISTRY.register("bt_ocean_tower", () -> explicitStructureTypeTyping(OceanBattleTower.CODEC));


    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }
}
