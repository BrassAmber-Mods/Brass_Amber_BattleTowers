package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BTItemGroup extends ItemGroup {

	public BTItemGroup() {
		super(BrassAmberBattleTowers.MOD_ID);
	}

	/**
	 * TODO Takes an Item for now. I want this just to be a texture.
	 * (Not sure if we should keep the block maybe? It doesn't really work with the new texture. Or we should make the texture 3D.)
	 */
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BTBlocks.TAB_ICON);
	}
}