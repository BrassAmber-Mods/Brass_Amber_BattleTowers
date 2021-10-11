package com.BrassAmber.ba_bt.block.block;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class GolemChestBlock extends ChestBlock {
	private final TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>> menu_provider_combiner = new TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>>() {
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
						return (ITextComponent) (chestTileEntity2.hasCustomName() ? chestTileEntity2.getDisplayName() : new TranslationTextComponent("container.ba_bt."+getChestType().getName()+"_chest_double"));
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

//	protected static TileEntityType<? extends GolemChestTileEntity> CHEST_TILE_ENTITY_TYPE = BTTileEntityTypes.GOLEM_CHEST;
	private final BTChestType chestType;
	
	public GolemChestBlock(BTChestType chestType, Properties properties) {
		this(chestType, properties, () -> {
			return BTTileEntityTypes.GOLEM_CHEST;
		});
	}
	
	public GolemChestBlock(BTChestType chestType, Properties properties, Supplier<TileEntityType<? extends ChestTileEntity>> chestSupplier) {
		super(properties, chestSupplier);
		this.chestType = chestType;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return BTTileEntityTypes.GOLEM_CHEST.create();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
		return this.combine(state, worldIn, pos, false).<Optional<INamedContainerProvider>>apply(this.menu_provider_combiner).orElse((INamedContainerProvider) null);
	}
	
	public BTChestType getChestType() {
		return this.chestType;
	}

	public enum BTChestType {
		GOLEM("golem"),
		STONE("stone");
		
		private String typeName;

		private static final BTChestType[] ALL_CHEST_TYPES = values();

		BTChestType(String typeName) {
			this.typeName = typeName;
		}

		public String getName() {
			return this.typeName;
		}

		public static BTChestType getRandomChestType(Random rand) {
			return Util.getRandom(ALL_CHEST_TYPES, rand);
		}
	}
}