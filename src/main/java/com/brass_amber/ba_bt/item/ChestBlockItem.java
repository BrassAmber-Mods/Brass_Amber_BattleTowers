package com.brass_amber.ba_bt.item;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.block.block.BTChestBlock;
import com.brass_amber.ba_bt.block.blockentity.inventory.BTChestItemRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;


public class ChestBlockItem extends BlockItem {


    public ChestBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);

        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BTChestItemRenderer.INSTANCE;
            }
        });
    }
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            if (((BTChestBlock)this.getBlock()).isGolemChest()) {
                tooltip.add(Component.translatable("tooltip.ba_bt.golem_chest").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.translatable("tooltip.ba_bt.tower_chest").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(BABTMain.HOLD_SHIFT_TOOLTIP);
        }
    }
}

