package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABTMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BABTMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packoutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BTRecipeProvider(packoutput));
        generator.addProvider(event.includeServer(), BTLootTableProvider.create(packoutput));
        BlockTagsProvider blockTagsProvider = new BTBlockTagGenerator(packoutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new BTItemtagGenerator(packoutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeClient(), new BTModelProvider(packoutput,existingFileHelper));
        generator.addProvider(event.includeClient(), new BTBlocksStateProvider(packoutput, existingFileHelper));
    }
}
