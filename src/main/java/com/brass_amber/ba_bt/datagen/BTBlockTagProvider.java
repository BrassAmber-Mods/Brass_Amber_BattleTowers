package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.brass_amber.ba_bt.util.BTTags.Blocks.*;
import static net.minecraft.tags.BlockTags.CLIMBABLE;
import static net.minecraft.tags.BlockTags.WALLS;
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

        this.tag(WALLS).add(
                BTBlocks.CORRITE_WALL.get(),
                BTBlocks.ACTIVE_CORRITE_WALL.get(),
                BTBlocks.CORE_MATTER_WALL.get()
        );
        this.tag(CLIMBABLE)
                .add(BTBlocks.CORRITE_LADDER.get())
                .addTag(BT_CORE_MATTER_BLOCKS);

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

        this.tag(BT_CORRITE_BLOCKS).add(
                BTBlocks.CORRITE_BLOCK.get(),
                BTBlocks.CORRITE_SLAB.get(),
                BTBlocks.CORRITE_STAIR.get(),
                BTBlocks.CORRITE_WALL.get(),
                BTBlocks.CORRITE_CHISELED_BOOKSHELF.get(),
                BTBlocks.CORRITE_LADDER.get()
        );

        this.tag(BT_ACTIVE_CORRITE_BLOCKS).add(
                BTBlocks.ACTIVE_CORRITE_BLOCK.get(),
                BTBlocks.ACTIVE_CORRITE_SLAB.get(),
                BTBlocks.ACTIVE_CORRITE_STAIR.get(),
                BTBlocks.ACTIVE_CORRITE_WALL.get()
        );

        this.tag(BT_CORE_MATTER_BLOCKS).add(
                BTBlocks.CORE_MATTER.get(),
                BTBlocks.CORE_MATTER_SLAB.get(),
                BTBlocks.CORE_MATTER_STAIR.get(),
                BTBlocks.CORE_MATTER_WALL.get()
        );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BT_CHESTS)
                .addTag(BT_SPAWNERS)
                .addTag(BT_CORRITE_BLOCKS)
                .addTag(BT_ACTIVE_CORRITE_BLOCKS)
                .addTag(BT_CORE_MATTER_BLOCKS);

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

        this.tag(IS_MAGMA_BLOCK)
                .add(Blocks.MAGMA_BLOCK)
                .addTag(BT_ACTIVE_CORRITE_BLOCKS);
    }

    @Override
    public String getName() {
        return "Block Tags";
    }
}
