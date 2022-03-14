package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlocks;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BTCreativeTab extends CreativeModeTab {

	public BTCreativeTab() {
		super(BrassAmberBattleTowers.MOD_ID);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BTBlocks.TAB_ICON.get());
	}
}