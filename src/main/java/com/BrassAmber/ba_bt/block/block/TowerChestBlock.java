package com.BrassAmber.ba_bt.block.block;


import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TowerChestBlock extends GolemChestBlock {

	public TowerChestBlock(BTChestType chestType, Properties properties) {
		super(chestType, properties, BTBlockEntityTypes.LAND_CHEST::get);

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BTBlockEntityTypes.LAND_CHEST.get().create(blockPos, blockState);
	}

}