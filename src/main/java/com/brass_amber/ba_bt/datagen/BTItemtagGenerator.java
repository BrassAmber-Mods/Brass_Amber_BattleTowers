package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BTItemtagGenerator extends ItemTagsProvider {
    public BTItemtagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture,
                              CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, lookupCompletableFuture, BABattleTowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.MUSIC_DISCS)
                .add(BTItems.LAND_RESONANCE_CRYSTAL.get(),
                        BTItems.OCEAN_RESONANCE_CRYSTAL.get(),
                        BTItems.CORE_RESONANCE_CRYSTAL.get(),
                        BTItems.NETHER_RESONANCE_CRYSTAL.get(),
                        BTItems.END_RESONANCE_CRYSTAL.get(),
                        BTItems.SKY_RESONANCE_CRYSTAL.get(),
                        BTItems.CITY_RESONANCE_CRYSTAL.get()
                );
    }

    @Override
    public String getName() {
        return "Item Tags";
    }
}
