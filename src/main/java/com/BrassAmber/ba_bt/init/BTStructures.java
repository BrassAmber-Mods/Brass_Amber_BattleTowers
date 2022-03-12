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

    public static final RegistryObject<StructureFeature<?>> LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("land_battle_tower", LandBattleTower::new));
    public static final RegistryObject<StructureFeature<?>> OVERGROWN_LAND_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("overgrown_land_battle_tower", OvergrownLandTower::new));
    // public static final RegistryObject<StructureFeature<JigsawConfiguration>> SKY_BATTLE_TOWER = DEFERRED_REGISTRY_STRUCTURE.register("sky_battle_tower", () -> (new SkyBattleTower(JigsawConfiguration.CODEC)));

    /**
     * This is where we set the rarity of your structures and determine if land conforms to it.
     * See the comments in below for more details.
     */
}
