package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.item.BTItemPools;
import com.brass_amber.ba_bt.worldGen.structures.BTStructureGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BTWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.STRUCTURE, BTStructureGen::bootstrap)
            .add(Registries.STRUCTURE_SET, BTStructureGen::setBootstrap)
            .add(BTRegistries.Keys.ITEM_POOLS, BTItemPools::bootstrap);

    public BTWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", BABattleTowers.MOD_ID));
    }
}
