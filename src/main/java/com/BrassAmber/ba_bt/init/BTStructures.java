package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.worldGen.structures.LandBattleTower;
import com.BrassAmber.ba_bt.worldGen.structures.OvergrownLandTower;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class BTStructures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BrassAmberBattleTowers.MOD_ID);

    public static final RegistryObject<StructureFeature<?>> LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("land_battle_tower", LandBattleTower::new);
    public static final RegistryObject<StructureFeature<?>> OVERGROWN_LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("overgrown_land_battle_tower", OvergrownLandTower::new);

    /**
     * This is where we set the rarity of your structures and determine if land conforms to it.
     * See the comments in below for more details.
     */
}
