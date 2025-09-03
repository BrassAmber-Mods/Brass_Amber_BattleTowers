package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BABattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new BTModelProvider(packOutput,existingFileHelper));
        generator.addProvider(event.includeClient(), new BTBlocksStateProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(), new BTRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), BTLootTableProvider.create(packOutput));
        BlockTagsProvider blockTagsProvider = new BTBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new BTItemtagGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new BTStructureTagProvider(packOutput, lookupProvider, BABattleTowers.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeServer(), new BTStructureSetTagProvider(packOutput, lookupProvider, BABattleTowers.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeServer(), new BTBiomeTagProvider(packOutput, lookupProvider, BABattleTowers.MOD_ID,existingFileHelper));

        generator.addProvider(event.includeServer(), new BTWorldGenProvider(packOutput, lookupProvider));
    }
}
