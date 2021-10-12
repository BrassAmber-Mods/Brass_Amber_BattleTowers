package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.block.BTTileEntityTypes;

import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GolemChestTileEntity extends ChestTileEntity {

	public GolemChestTileEntity() {
		this(BTTileEntityTypes.GOLEM_CHEST);
	}
	

	protected <TE extends ChestTileEntity> GolemChestTileEntity(TileEntityType<TE> chestTileEntity) {
		super(chestTileEntity);
	}

	/**
	 * Single chest inventory name
	 */
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.ba_bt.golem_chest");
	}
}