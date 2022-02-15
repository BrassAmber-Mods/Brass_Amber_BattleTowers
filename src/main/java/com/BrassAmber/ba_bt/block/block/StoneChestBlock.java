package com.BrassAmber.ba_bt.block.block;

import com.BrassAmber.ba_bt.init.BTTileEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class StoneChestBlock extends GolemChestBlock {


	public StoneChestBlock(BTChestType chestType, Properties properties) {
		super(chestType, properties, () -> {
			return BTTileEntityTypes.LAND_CHEST;
		});

	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return BTTileEntityTypes.LAND_CHEST.create();
	}

}