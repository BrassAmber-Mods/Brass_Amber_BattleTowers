package com.BrassAmber.ba_bt;

import com.BrassAmber.ba_bt.block.BTBlocks;

import com.BrassAmber.ba_bt.item.BTItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BTItemGroup extends ItemGroup {

	public BTItemGroup() {
		super(BrassAmberBattleTowers.MOD_ID);
	}

	/**
	 * TODO Takes an OperatorOnlyItem for now. (Which means it can only be accessed using commands) I want this just to be a texture.
	 */
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BTItems.TAB_ICON_ITEM);
	}
}
