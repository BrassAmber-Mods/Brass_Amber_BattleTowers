package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.brass_amber.ba_bt.util.BTTags.Blocks.*;
import static net.minecraftforge.common.Tags.Blocks.CHESTS;

public class BTBlockTagProvider extends BlockTagsProvider  {
    public BTBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BABattleTowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BT_CHESTS).add(
                BTBlocks.LAND_CHEST.get(),
                BTBlocks.LAND_CHEST.get(),
                BTBlocks.OCEAN_CHEST.get(),
                BTBlocks.OCEAN_CHEST.get(),
                BTBlocks.CORE_CHEST.get(),
                BTBlocks.CORE_CHEST.get(),
                BTBlocks.NETHER_CHEST.get(),
                BTBlocks.NETHER_CHEST.get(),
                BTBlocks.END_CHEST.get(),
                BTBlocks.END_CHEST.get(),
                BTBlocks.SKY_CHEST.get(),
                BTBlocks.SKY_CHEST.get()
        );

        this.tag(CHESTS).addTag(BT_CHESTS);

        this.tag(BT_SPAWNERS).add(
                BTBlocks.LAND_SPAWNER.get(),
                BTBlocks.OCEAN_SPAWNER.get(),
                BTBlocks.CORE_SPAWNER.get(),
                BTBlocks.NETHER_SPAWNER.get(),
                BTBlocks.END_SPAWNER.get(),
                BTBlocks.SKY_SPAWNER.get()
        );

        this.tag(BASE_PROTECTED_TAG).add(
                Blocks.DIRT,
                Blocks.GRASS_BLOCK,
                Blocks.COARSE_DIRT,
                Blocks.STONE,
                Blocks.GRAVEL,
                Blocks.SAND
        );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BT_CHESTS)
                .addTag(BT_SPAWNERS);

        this.tag(BlockTags.DRAGON_IMMUNE)
                .addTag(BT_CHESTS)
                .addTag(BT_SPAWNERS);

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .addTag(BT_CHESTS);

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .addTag(BT_SPAWNERS);

        this.tag(BlockTags.WITHER_IMMUNE)
                .addTag(BT_CHESTS)
                .addTag(BT_SPAWNERS);
    }

    @Override
    public String getName() {
        return "Block Tags";
    }
}
