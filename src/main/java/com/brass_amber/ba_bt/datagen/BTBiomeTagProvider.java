package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BTBiomeTagProvider extends BiomeTagsProvider {

    public BTBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BTTags.Biomes.LAND_TOWER_BIOMES)
                .addTag(BiomeTags.IS_OVERWORLD);

        this.tag(BTTags.Biomes.OCEAN_TOWER_BIOMES)
                .addTag(BiomeTags.IS_OCEAN);

        this.tag(BTTags.Biomes.CORE_TOWER_BIOMES)
                .addTag(Tags.Biomes.IS_CAVE)
                .addTag(Tags.Biomes.IS_UNDERGROUND);
    }


}
