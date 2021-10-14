package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.util.GolemType;

import net.minecraft.item.Item;

public class GuardianEyeItem extends Item {
	private GolemType golemType;

	public GuardianEyeItem(GolemType golemType, Item.Properties builder) {
		super(builder);
		this.golemType = golemType;
	}
	
	public GolemType getGolemType() {
		return this.golemType;
	}
}
