package com.BrassAmber.ba_bt.block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * TODO Prevent placement in the world. 
 */
public class TabIconBlock extends Block {

	public TabIconBlock(Properties blockProperties) {
		super(blockProperties);
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return Block.box(0.0D, 0.0D, 7.5D, 16.0D, 16.0D, 8.5D);
	}
}