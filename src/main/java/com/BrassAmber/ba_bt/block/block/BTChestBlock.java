package com.BrassAmber.ba_bt.block.block;

import java.util.Optional;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.block.BTTileEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BTChestBlock extends ChestBlock {
	private static final TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>> MENU_PROVIDER_COMBINER = new TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>>() {
		public Optional<INamedContainerProvider> acceptDouble(final ChestTileEntity chestTileEntity1, final ChestTileEntity chestTileEntity2) {
			final IInventory iinventory = new DoubleSidedInventory(chestTileEntity1, chestTileEntity2);
			return Optional.of(new INamedContainerProvider() {
				@Nullable
				public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity playerIn) {
					if (chestTileEntity1.canOpen(playerIn) && chestTileEntity2.canOpen(playerIn)) {
						chestTileEntity1.unpackLootTable(playerInventory.player);
						chestTileEntity2.unpackLootTable(playerInventory.player);
						return ChestContainer.sixRows(containerId, playerInventory, iinventory);
					} else {
						return null;
					}
				}

				// Naming of double chests
				public ITextComponent getDisplayName() {
					if (chestTileEntity1.hasCustomName()) {
						return chestTileEntity1.getDisplayName();
					} else {
						return (ITextComponent) (chestTileEntity2.hasCustomName() ? chestTileEntity2.getDisplayName() : new TranslationTextComponent("container.ba_bt.golem_chest_double"));
					}
				}
			});
		}

		public Optional<INamedContainerProvider> acceptSingle(ChestTileEntity chestTileEntity) {
			return Optional.of(chestTileEntity);
		}

		public Optional<INamedContainerProvider> acceptNone() {
			return Optional.empty();
		}
	};

	public BTChestBlock(Properties properties) {
		super(properties, () -> {
			return BTTileEntityTypes.GOLEM_CHEST;
		});
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return BTTileEntityTypes.GOLEM_CHEST.create();
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
		return this.combine(state, worldIn, pos, false).<Optional<INamedContainerProvider>>apply(MENU_PROVIDER_COMBINER).orElse((INamedContainerProvider) null);
	}
}