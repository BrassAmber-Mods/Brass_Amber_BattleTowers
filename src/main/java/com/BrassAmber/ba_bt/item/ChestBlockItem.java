package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.client.inventory.BTChestItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.RenderProperties;

import java.util.function.Consumer;

public class ChestBlockItem extends BlockItem {
    public ChestBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(ChestProperty.INSTANCE);
    }
}

class ChestProperty implements IItemRenderProperties {
    public static ChestProperty INSTANCE = new ChestProperty();

    @Override
    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        return BTChestItemRenderer.INSTANCE;
    }
}
