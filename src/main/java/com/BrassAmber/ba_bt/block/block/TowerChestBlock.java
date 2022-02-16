package com.BrassAmber.ba_bt.block.block;


import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class TowerChestBlock extends GolemChestBlock {

	public static final BTChestType BT_CHEST_TYPE = BTChestType.TOWER;


	private static final DoubleBlockCombiner.Combiner<TowerChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<TowerChestBlockEntity, Optional<Container>>() {
		public Optional<Container> acceptDouble(TowerChestBlockEntity p_51591_, TowerChestBlockEntity p_51592_) {
			return Optional.of(new CompoundContainer(p_51591_, p_51592_));
		}

		public Optional<Container> acceptSingle(TowerChestBlockEntity p_51589_) {
			return Optional.of(p_51589_);
		}

		public Optional<Container> acceptNone() {
			return Optional.empty();
		}
	};
	private static final DoubleBlockCombiner.Combiner<TowerChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<TowerChestBlockEntity, Optional<MenuProvider>>() {
		public Optional<MenuProvider> acceptDouble(final TowerChestBlockEntity p_51604_, final TowerChestBlockEntity p_51605_) {
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

		public Optional<MenuProvider> acceptSingle(TowerChestBlockEntity p_51602_) {
			return Optional.of(p_51602_);
		}

		public Optional<MenuProvider> acceptNone() {
			return Optional.empty();
		}
	};

	public TowerChestBlock(BTChestType chestType, Properties properties) {
		this(chestType, properties, () -> BTBlockEntityTypes.LAND_CHEST);

	}

	public TowerChestBlock(BTChestType chestType, Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> chestSupplier) {
		super(chestType, properties, chestSupplier);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BTBlockEntityTypes.LAND_CHEST.create(blockPos, blockState);
	}

}