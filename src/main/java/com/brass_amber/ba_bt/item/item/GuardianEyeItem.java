package com.brass_amber.ba_bt.item.item;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuardianEyeItem extends Item {
	private GolemType golemType;

	public GuardianEyeItem(GolemType golemType, Item.Properties builder) {
		super(builder);
		this.golemType = golemType;
	}
	
	public GolemType getGolemType() {
		return this.golemType;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("tooltip.ba_bt." + this.golemType.getSerializedName() + "_eye").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(BrassAmberBattleTowers.HOLD_SHIFT_TOOLTIP);
		}
	}
}
