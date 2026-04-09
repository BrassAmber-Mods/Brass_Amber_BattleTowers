package com.brass_amber.ba_bt.item.item;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuardianEyeItem extends Item {
	private TowerType towerType;

	public GuardianEyeItem(TowerType towerType, Item.Properties builder) {
		super(builder);
		this.towerType = towerType;
	}
	
	public TowerType getGolemType() {
		return this.towerType;
	}

	@Override

	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("tooltip.ba_bt." + this.towerType.getLowercaseName() + "_eye").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(BABattleTowers.HOLD_SHIFT_TOOLTIP);
		}
	}
}
