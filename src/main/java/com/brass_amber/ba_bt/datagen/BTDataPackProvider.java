package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.datagen.datapack.*;
import com.brass_amber.ba_bt.init.BTRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BTDataPackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(BTRegistries.Keys.ITEM_POOLS, BTItemPools::bootstrap)
            .add(BTRegistries.Keys.TOWER_DATA, TowerDataBootstrap::towerBootstrap)
            .add(BTRegistries.Keys.ENTITY_DATA, EntityDataBootstrap::entityBootstrap)
            .add(BTRegistries.Keys.GENERATION_DATA, GenerationDataBootstrap::generationBootstrap)
            .add(Registries.PROCESSOR_LIST, ProcessorListBootstrap::processorListBootstrap);

    public BTDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", BABattleTowers.MOD_ID));
    }
}
