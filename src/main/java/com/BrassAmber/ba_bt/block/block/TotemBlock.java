package com.BrassAmber.ba_bt.block.block;

import com.BrassAmber.ba_bt.item.item.GuardianEyeItem;
import com.BrassAmber.ba_bt.util.GolemType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class TotemBlock extends Block {
	public static final EnumProperty<GolemType> TOTEM = EnumProperty.create("totem", GolemType.class);

	public TotemBlock(Properties blockProperties) {
		super(blockProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(TOTEM, GolemType.EMPTY));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TOTEM);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		ItemStack itemStackInHand = player.getItemInHand(hand);
		Item itemInHand = itemStackInHand.getItem();

		// Update the totem if a player right-clicks with a Guardian Eye
		if (itemInHand instanceof GuardianEyeItem && state.getValue(TOTEM).equals(GolemType.EMPTY)) {
			GuardianEyeItem guardianEye = (GuardianEyeItem) itemInHand;
			worldIn.setBlockAndUpdate(pos, state.setValue(TOTEM, guardianEye.getGolemType()));
			// Consume a Guardian Eye if the player is not in Creative mode
			if (!player.abilities.instabuild) {
				itemStackInHand.shrink(1);
			}
			return ActionResultType.sidedSuccess(worldIn.isClientSide());
		}

		// Player can get the Guardian Eye back by right-clicking with an empty main hand.
		GolemType blockTotemType = state.getValue(TOTEM);
		if (player.getMainHandItem().isEmpty() && !(blockTotemType.equals(GolemType.EMPTY))) {
			worldIn.setBlockAndUpdate(pos, state.setValue(TOTEM, GolemType.EMPTY));
			// Drop a Guardian Eye if the player is not in Creative mode
			if (!player.abilities.instabuild) {
				ItemEntity item = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, new ItemStack(GolemType.getEyeFor(blockTotemType)));
				worldIn.addFreshEntity(item);
			}
			return ActionResultType.sidedSuccess(worldIn.isClientSide());
		}

		return ActionResultType.PASS;
	}
}