package com.BrassAmber.ba_bt.block.block;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;

public class GolemChestBlock extends ChestBlock {

	public static final BTChestType BT_CHEST_TYPE = BTChestType.GOLEM;
	private final BTChestType chestType;

	private static final DoubleBlockCombiner.Combiner<GolemChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<GolemChestBlockEntity, Optional<Container>>() {
		public Optional<Container> acceptDouble(GolemChestBlockEntity p_51591_, GolemChestBlockEntity p_51592_) {
			return Optional.of(new CompoundContainer(p_51591_, p_51592_));
		}

		public Optional<Container> acceptSingle(GolemChestBlockEntity p_51589_) {
			return Optional.of(p_51589_);
		}

		public Optional<Container> acceptNone() {
			return Optional.empty();
		}
	};
	private static final DoubleBlockCombiner.Combiner<GolemChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<GolemChestBlockEntity, Optional<MenuProvider>>() {
		public Optional<MenuProvider> acceptDouble(final GolemChestBlockEntity p_51604_, final GolemChestBlockEntity p_51605_) {
			final Container container = new CompoundContainer(p_51604_, p_51605_);
			return Optional.of(new MenuProvider() {
				@Nullable
				public AbstractContainerMenu createMenu(int p_51622_, Inventory p_51623_, Player p_51624_) {
					if (p_51604_.canOpen(p_51624_) && p_51605_.canOpen(p_51624_)) {
						p_51604_.unpackLootTable(p_51623_.player);
						p_51605_.unpackLootTable(p_51623_.player);
						return ChestMenu.sixRows(p_51622_, p_51623_, container);
					} else {
						return null;
					}
				}

				public Component getDisplayName() {
					if (p_51604_.hasCustomName()) {
						return p_51604_.getDisplayName();
					} else {
						return p_51605_.hasCustomName() ? p_51605_.getDisplayName() : new TranslatableComponent("container.ba_bt."+BT_CHEST_TYPE.getName()+"_chest_double");
					}
				}
			});
		}

		public Optional<MenuProvider> acceptSingle(GolemChestBlockEntity p_51602_) {
			return Optional.of(p_51602_);
		}

		public Optional<MenuProvider> acceptNone() {
			return Optional.empty();
		}
	};


	
	public GolemChestBlock(BTChestType chestType, Properties properties) {
		this(chestType, properties, () -> BTBlockEntityTypes.LAND_GOLEM_CHEST);
	}

	public GolemChestBlock(BTChestType chestType, Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> chestSupplier) {
		super(properties, chestSupplier);
		this.chestType = chestType;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BTBlockEntityTypes.LAND_GOLEM_CHEST.create(blockPos, blockState);
	}

	@Override
	public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
		super.entityInside(p_60495_, p_60496_, p_60497_, p_60498_);
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

		public static BTChestType getRandomChestType(Random rand) {
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
		}
		else {
			return false;
		}

	}
}