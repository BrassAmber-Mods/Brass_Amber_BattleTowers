package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.block.block.TotemBlock.TotemType;

import net.minecraft.item.Item;

public class GuardianEyeItem extends Item {
	private TotemType totemType;

	public GuardianEyeItem(TotemType totemType, Item.Properties builder) {
		super(builder);
		this.totemType = totemType;
	}
	
	public TotemType getTotemType() {
		return this.totemType;
	}
}
