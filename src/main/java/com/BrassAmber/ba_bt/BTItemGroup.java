package com.BrassAmber.ba_bt;

import com.BrassAmber.ba_bt.block.BTBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BTItemGroup extends ItemGroup {

	public BTItemGroup() {
		super(BrassAmberBattleTowers.MOD_ID);
	}

	/**
	 * TODO Takes an Item for now. I want this just to be a texture.
	 */
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BTBlocks.TAB_ICON);
	}
}
