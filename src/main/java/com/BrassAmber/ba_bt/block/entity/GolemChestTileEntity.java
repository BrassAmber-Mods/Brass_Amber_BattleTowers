package com.BrassAmber.ba_bt.block.entity;

import com.BrassAmber.ba_bt.block.BTTileEntityTypes;

import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GolemChestTileEntity extends ChestTileEntity {

	public GolemChestTileEntity() {
		super(BTTileEntityTypes.GOLEM_CHEST);
	}

	/**
	 * Single chest inventory name
	 */
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.ba_bt.golem_chest");
	}
}