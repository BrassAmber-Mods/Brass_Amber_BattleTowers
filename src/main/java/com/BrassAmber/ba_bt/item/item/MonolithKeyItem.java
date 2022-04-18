package com.BrassAmber.ba_bt.item.item;

import java.util.List;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


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
			tooltip.add(new TranslatableComponent("tooltip.ba_bt.monolith_key").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(BrassAmberBattleTowers.HOLD_SHIFT_TOOLTIP);
		}
	}
}
