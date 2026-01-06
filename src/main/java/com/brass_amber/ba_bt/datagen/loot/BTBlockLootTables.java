package com.brass_amber.ba_bt.datagen.loot;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;


import java.util.Set;

public class BTBlockLootTables extends BlockLootSubProvider {
    public BTBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.LAND_CHEST.get(),
                BTItems.LAND_CHEST_SHARD.get(),
                2, 4
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.LAND_GOLEM_CHEST.get(),
                BTItems.LAND_GOLEM_EYE.get(),
                0, 1
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.OCEAN_CHEST.get(),
                BTItems.OCEAN_CHEST_SHARD.get(),
                2, 4
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.OCEAN_GOLEM_CHEST.get(),
                BTItems.OCEAN_GOLEM_EYE.get(),
                0, 1
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.CORE_CHEST.get(),
                BTItems.CORE_CHEST_SHARD.get(),
                2, 4
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.CORE_GOLEM_CHEST.get(),
                BTItems.CORE_GOLEM_EYE.get(),
                0, 1
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.NETHER_CHEST.get(),
                BTItems.NETHER_CHEST_SHARD.get(),
                2, 4
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.NETHER_GOLEM_CHEST.get(),
                BTItems.NETHER_GOLEM_EYE.get(),
                0, 1
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.END_CHEST.get(),
                BTItems.END_CHEST_SHARD.get(),
                2, 4
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.END_GOLEM_CHEST.get(),
                BTItems.END_GOLEM_EYE.get(),
                0, 1
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.SKY_CHEST.get(),
                BTItems.SKY_CHEST_SHARD.get(),
                2, 4
        );
        this.dropWhenSilkTouchAndDropOtherAmount(
                BTBlocks.SKY_GOLEM_CHEST.get(),
                BTItems.SKY_GOLEM_EYE.get(),
                0, 1
        );
        this.dropSelf(BTBlocks.CORRITE_BLOCK.get());
        this.dropSelf(BTBlocks.CORRITE_SLAB.get());
        this.dropSelf(BTBlocks.CORRITE_STAIR.get());
        this.dropSelf(BTBlocks.CORRITE_WALL.get());
        this.dropSelf(BTBlocks.CORRITE_LADDER.get());
        this.dropSelf(BTBlocks.CORRITE_CHISELED_BOOKSHELF.get());

        this.dropWhenSilkTouch(BTBlocks.CORE_MATTER.get());
        this.dropWhenSilkTouch(BTBlocks.CORE_MATTER_SLAB.get());
        this.dropWhenSilkTouch(BTBlocks.CORE_MATTER_STAIR.get());
        this.dropWhenSilkTouch(BTBlocks.CORE_MATTER_WALL.get());

        this.dropWhenSilkTouch(BTBlocks.ACTIVE_CORRITE_BLOCK.get());
        this.dropWhenSilkTouch(BTBlocks.ACTIVE_CORRITE_SLAB.get());
        this.dropWhenSilkTouch(BTBlocks.ACTIVE_CORRITE_STAIR.get());
        this.dropWhenSilkTouch(BTBlocks.ACTIVE_CORRITE_WALL.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BTBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    protected void dropWhenSilkTouchAndDropOtherAmount(Block block, ItemLike drop, float min, float max) {
        this.add(block, createSilkTouchOnlyTable(block).withPool(
                LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(
                        UniformGenerator.between(min, max)
                ).add(LootItem.lootTableItem(drop)))
        );
    }
}
