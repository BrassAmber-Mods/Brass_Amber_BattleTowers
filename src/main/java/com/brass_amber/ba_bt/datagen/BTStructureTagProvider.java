package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BTStructureTagProvider extends StructureTagsProvider {

    public BTStructureTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BTTags.Structures.BATTLE_TOWERS)
                .add(BTStructures.LAND_TOWER_KEY)
                .add(BTStructures.OCEAN_TOWER_KEY)
                .add(BTStructures.CORE_TOWER_KEY);
    }
}
