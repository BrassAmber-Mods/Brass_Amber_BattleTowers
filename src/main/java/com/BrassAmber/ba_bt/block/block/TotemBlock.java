package com.BrassAmber.ba_bt.block.block;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.item.BTItems;
import com.BrassAmber.ba_bt.item.GuardianEyeItem;

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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class TotemBlock extends Block {
	public static final EnumProperty<TotemType> TOTEM = EnumProperty.create("totem", TotemType.class);

	public TotemBlock(Properties blockProperties) {
		super(blockProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(TOTEM, TotemType.EMPTY));
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
		if (itemInHand instanceof GuardianEyeItem && state.getValue(TOTEM).equals(TotemType.EMPTY)) {
			GuardianEyeItem guardianEye = (GuardianEyeItem) itemInHand;
			worldIn.setBlockAndUpdate(pos, state.setValue(TOTEM, guardianEye.getTotemType()));
			// Consume a Guardian Eye if the player is not in Creative mode
			if (!player.abilities.instabuild) {
				itemStackInHand.shrink(1);
			}
			return ActionResultType.sidedSuccess(worldIn.isClientSide());
		}
		
		// Player can get the Guardian Eye back by right-clicking with an empty main hand.
		TotemType blockTotemType = state.getValue(TOTEM);
		if (player.getMainHandItem().isEmpty() && !(blockTotemType.equals(TotemType.EMPTY))) {
			worldIn.setBlockAndUpdate(pos, state.setValue(TOTEM, TotemType.EMPTY));
			// Drop a Guardian Eye if the player is not in Creative mode
			if (!player.abilities.instabuild) {
				ItemEntity item = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY()+ 1.1D, pos.getZ() + 0.5D, new ItemStack(blockTotemType.getReturnItem(blockTotemType)));
	            worldIn.addFreshEntity(item);
			}
			return ActionResultType.sidedSuccess(worldIn.isClientSide());
		}

		return ActionResultType.PASS;
	}

	public enum TotemType implements IStringSerializable {
		EMPTY("empty"),
		LAND("land"),
		CORE("core"),
		NETHER("nether"),
		END("end"),
		SKY("sky"),
		OCEAN("ocean");

		private String name;

		TotemType(String name) {
			this.name = name;
		}
		
		@Nullable
		public Item getReturnItem(TotemType totemType) {
			switch (totemType) {
			case EMPTY: default:
				return null;
			case LAND:
				return BTItems.LAND_GUARDIAN_EYE;
			case CORE:
				return BTItems.CORE_GUARDIAN_EYE;
			case NETHER:
				return BTItems.NETHER_GUARDIAN_EYE;
			case END:
				return BTItems.END_GUARDIAN_EYE;
			case SKY:
				return BTItems.SKY_GUARDIAN_EYE;
			case OCEAN:
				return BTItems.OCEAN_GUARDIAN_EYE;
			}
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}