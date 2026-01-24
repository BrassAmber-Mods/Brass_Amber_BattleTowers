package com.brass_amber.ba_bt.block.block;

import java.util.Optional;
import java.util.function.Supplier;

import com.brass_amber.ba_bt.block.blockentity.BTChestBlockEntity;

import com.brass_amber.ba_bt.inventory.BTChestMenu;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public class BTChestBlock extends ChestBlock {

	private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<>() {
        public Optional<MenuProvider> acceptDouble(final ChestBlockEntity container1, final ChestBlockEntity chestBlockEntity) {
            final Container container = new CompoundContainer(container1, chestBlockEntity);
            return Optional.of(new MenuProvider() {
                @Nullable
                public AbstractContainerMenu createMenu(int size, Inventory inventory, Player player) {
                    if (container1.canOpen(player) && chestBlockEntity.canOpen(player)) {
                        container1.unpackLootTable(inventory.player);
                        chestBlockEntity.unpackLootTable(inventory.player);
                        return BTChestMenu.eightRows(size, inventory, container);
                    } else {
                        return null;
                    }
                }

                public Component getDisplayName() {
                    if (container1.hasCustomName()) {
                        return container1.getDisplayName();
                    } else {
                        return chestBlockEntity.hasCustomName() ? chestBlockEntity.getDisplayName() : Component.translatable("container.chestDouble");
                    }
                }
            });
        }

        public Optional<MenuProvider> acceptSingle(ChestBlockEntity golemChestBlockEntity) {
            return Optional.of(golemChestBlockEntity);
        }

        public Optional<MenuProvider> acceptNone() {
            return Optional.empty();
        }
    };

	protected final TowerType type;
	protected boolean golemChest;

	public BTChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> chestSupplier, Properties properties, TowerType type) {
		super(properties, chestSupplier);
		this.type = type;
		this.golemChest = true;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		BTChestBlockEntity blockEntity = (BTChestBlockEntity) level.getBlockEntity(blockPos);

		if (itemStack.hasCustomHoverName()) {
			blockEntity.setCustomName(itemStack.getHoverName());
		}

		if (livingEntity instanceof Player player && !player.isCreative()) {
			blockEntity.setUnlocked(true);
		}
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
		return this.combine(blockState, level, blockPos, false).apply(MENU_PROVIDER_COMBINER).orElse(null);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BTChestBlockEntity(blockPos, blockState, type);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		BTChestBlockEntity chestTileEntity = (BTChestBlockEntity) level.getBlockEntity(pos);
        if (chestTileEntity.isUnlocked()) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
		} else if (player != null && player.isCreative()) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
		} else {
			return false;
		}
	}

	public TowerType getType() {
		return type;
	}

	public boolean isGolemChest() {
		return golemChest;
	}
}