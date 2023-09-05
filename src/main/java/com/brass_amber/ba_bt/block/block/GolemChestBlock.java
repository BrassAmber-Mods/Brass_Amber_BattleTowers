package com.brass_amber.ba_bt.block.block;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.brass_amber.ba_bt.block.blockentity.GolemChestBlockEntity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import static com.brass_amber.ba_bt.util.BTUtil.getChestEntity;

public class GolemChestBlock extends ChestBlock {
	private final BTChestType chestType;

	private final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
		public Optional<MenuProvider> acceptDouble(ChestBlockEntity chestBlockEntity1, ChestBlockEntity chestBlockEntity2) {
			final Container iinventory = new CompoundContainer(chestBlockEntity1, chestBlockEntity2);
			return Optional.of(new MenuProvider() {
				@Nullable
				public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player playerIn) {
					if (chestBlockEntity1.canOpen(playerIn) && chestBlockEntity2.canOpen(playerIn)) {
						chestBlockEntity1.unpackLootTable(playerInventory.player);
						chestBlockEntity2.unpackLootTable(playerInventory.player);
						return ChestMenu.sixRows(containerId, playerInventory, iinventory);
					} else {
						return null;
					}
				}

				public Component getDisplayName() {
					if (chestBlockEntity1.hasCustomName()) {
						return chestBlockEntity1.getDisplayName();
					} else {
						return chestBlockEntity2.hasCustomName() ? chestBlockEntity2.getDisplayName() : Component.translatable("container.ba_bt."+ getChestType().getName() +"_chest_double");
					}
				}
			});
		}

		public Optional<MenuProvider> acceptSingle(ChestBlockEntity chestBlockEntity) {
			return Optional.of(chestBlockEntity);
		}

		public Optional<MenuProvider> acceptNone() {
			return Optional.empty();
		}
	};

	public GolemChestBlock(BTChestType chestType, Supplier<BlockEntityType<? extends ChestBlockEntity>> chestSupplier, Properties properties) {
		super(properties, chestSupplier);
		this.chestType = chestType;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return getChestEntity(this).create(blockPos, blockState);
	}

	public BTChestType getChestType() {
		return this.chestType;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		super.setPlacedBy(world, blockPos, blockState, livingEntity, itemStack);
		try {
			Player player = (Player) livingEntity;
			if (!player.isCreative()) {
				GolemChestBlockEntity chestTileEntity = (GolemChestBlockEntity) world.getBlockEntity(blockPos);
				chestTileEntity.setUnlocked(true);
			}
		} catch (Exception ignored) {

		}
	}

	public enum BTChestType implements StringRepresentable {
		GOLEM("golem"),
		TOWER("tower");
		
		private String typeName;

		private static final BTChestType[] ALL_CHEST_TYPES = values();

		BTChestType(String typeName) {
			this.typeName = typeName;
		}

		public String getName() {
			return this.typeName;
		}

		public static BTChestType getRandomChestType(RandomSource rand) {
			return Util.getRandom(ALL_CHEST_TYPES, rand);
		}

		@Override
		public String getSerializedName() {
			return typeName;
		}
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		GolemChestBlockEntity chestTileEntity = (GolemChestBlockEntity) world.getBlockEntity(pos);
		if (chestTileEntity.isUnlocked()) {
			return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
		} else if (player != null && player.isCreative()) {
			return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
		} else {
			return false;
		}

	}
}