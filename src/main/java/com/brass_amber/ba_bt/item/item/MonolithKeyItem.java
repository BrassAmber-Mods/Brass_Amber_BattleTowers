package com.brass_amber.ba_bt.item.item;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;


public class MonolithKeyItem extends Item {

	public MonolithKeyItem(Item.Properties builder) {
		super(builder);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("tooltip.ba_bt.monolith_key").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(BrassAmberBattleTowers.HOLD_SHIFT_TOOLTIP);
		}
	}
}
