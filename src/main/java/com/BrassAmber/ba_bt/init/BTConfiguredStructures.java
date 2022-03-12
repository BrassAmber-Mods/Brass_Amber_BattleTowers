package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.worldGen.BTJigsawConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class BTConfiguredStructures {

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_LAND_TOWER = BTStructures.LAND_BATTLE_TOWER.get().configured(new BTJigsawConfiguration(() -> PlainVillagePools.START, 7));;
    //public static ConfiguredStructureFeature<?, ?> CONFIGURED_OVERGROWN_LAND_TOWER = BTStructures.OVERGROWN_LAND_BATTLE_TOWER.get().configured(new BTJigsawConfiguration(() -> PlainVillagePools.START, 7));;
    /* public static StructureFeature<?, ?> CONFIGURED_SKY_BATTLE_TOWER = BTStructures
            .SKY_BATTLE_TOWER.get().configured(IFeatureConfig.NONE); */

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "bottom_floor_1"), CONFIGURED_LAND_TOWER);
        //Registry.register(registry, new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "overgrown_base"), CONFIGURED_OVERGROWN_LAND_TOWER);

        // Dummy JigsawConfiguration values for now. We will modify the pool at runtime since we cannot get json pool files here at mod init.
        // You can create and register your pools in code, pass in the code create pool here, and delete both newConfig and newContext in RunDownHouseStructure's createPiecesGenerator.
        // Note: JigsawConfiguration only takes 0 - 7 size so that's another reason why we are going to bypass that "codec" by changing size at runtime to get higher sizes.


    }


}
