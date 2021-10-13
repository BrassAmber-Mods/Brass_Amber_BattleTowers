package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.block.BTTileEntityTypes;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class StoneChestTileEntity extends GolemChestTileEntity {

	public StoneChestTileEntity() {
		super(BTTileEntityTypes.STONE_CHEST);
	}

	/**
	 * Single chest inventory name
	 */
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.ba_bt.stone_chest");
	}
}