package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.worldGen.structures.LandBattleTower;
import com.BrassAmber.ba_bt.worldGen.structures.OceanBattleTower;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_REGISTRY = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BrassAmberBattleTowers.MOD_ID);

    public static final RegistryObject<StructureFeature<?>> LAND_BATTLE_TOWER = STRUCTURE_REGISTRY.register("bt_land_tower", LandBattleTower::new);
    public static final RegistryObject<StructureFeature<?>> OCEAN_BATTLE_TOWER = STRUCTURE_REGISTRY.register("bt_ocean_tower", OceanBattleTower::new);

}
