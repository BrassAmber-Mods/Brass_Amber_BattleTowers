package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.TowerChestBlock;
import com.BrassAmber.ba_bt.client.inventory.BTChestItemRenderer;
import com.BrassAmber.ba_bt.init.BTBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.RenderProperties;

import javax.annotation.Nullable;
import java.util.List;
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

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        if (Screen.hasShiftDown()) {
            if (this.getBlock() instanceof TowerChestBlock) {
                tooltip.add(new TranslatableComponent("tooltip.ba_bt.tower_chest").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(new TranslatableComponent("tooltip.ba_bt.golem_chest").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(BrassAmberBattleTowers.HOLD_SHIFT_TOOLTIP);
        }
    }
}

class ChestProperty implements IItemRenderProperties {
    public static ChestProperty INSTANCE = new ChestProperty();

    @Override
    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        return BTChestItemRenderer.INSTANCE;
    }
}
