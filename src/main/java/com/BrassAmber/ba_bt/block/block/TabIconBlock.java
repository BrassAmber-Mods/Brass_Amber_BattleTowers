package com.BrassAmber.ba_bt.block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * TODO(OLD) Prevent placement in the world.
 * TODO I think I want to keep placement in the world, but make the model flat against the wall so it can be used for decoration.
 */
public class TabIconBlock extends Block {

	public TabIconBlock(Properties blockProperties) {
		super(blockProperties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
		return Block.box(0.0D, 0.0D, 7.5D, 16.0D, 16.0D, 8.5D);
	}
}