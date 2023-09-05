package com.brass_amber.ba_bt.block.block;


import com.brass_amber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class TowerChestBlock extends GolemChestBlock {

	public TowerChestBlock(BTChestType chestType, Supplier<BlockEntityType<? extends ChestBlockEntity>> chestSupplier, Properties properties) {
		super(chestType, chestSupplier, properties);

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BTBlockEntityTypes.LAND_CHEST.get().create(blockPos, blockState);
	}

}