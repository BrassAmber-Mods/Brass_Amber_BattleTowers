package com.brass_amber.ba_bt.block.blockentity;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.block.block.BTChestBlock;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;


public class BTChestBlockEntity extends ChestBlockEntity {
	protected boolean unlocked = false;
	protected GolemType golemType;
	protected boolean golemChest;

	protected NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);

	public BTChestBlockEntity(BlockPos blockPos, BlockState blockState, GolemType golemType) {
		this(GolemType.getChestForType(golemType, GolemType.isGolemChest(blockState.getBlock())), blockPos, blockState);
	}

	public BTChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(GolemType.getChestForType(GolemType.getTypeForChest(blockState.getBlock()), GolemType.isGolemChest(blockState.getBlock())), blockPos, blockState);
	}

	protected BTChestBlockEntity(BlockEntityType<? extends BTChestBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.golemType = GolemType.getTypeForChest(blockEntityType);
		this.golemChest = GolemType.isGolemChest(blockState.getBlock());
	}

	@Override
	public int getContainerSize() {
		return 36;
	}

	public GolemType getChestType() {
		return this.golemType;
	}

	/**
	 * Single chest inventory name
	 * @return
	 */
	@Override
	protected Component getDefaultName() {
		if (this.golemChest) {
			return Component.translatable("container.ba_bt." + this.golemType.getSerializedName() + "_golem_chest");
		}
		return Component.translatable("container.ba_bt." + this.golemType.getSerializedName()+ "_chest");
	}

	@Override
	public void load(CompoundTag compoundTag) {
		super.load(compoundTag);
		this.unlocked = compoundTag.getBoolean("Unlocked");
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag) {
		super.saveAdditional(compoundTag);
		compoundTag.putBoolean("Unlocked", this.unlocked);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	protected void setItems(NonNullList<ItemStack> itemStack) {
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < itemStack.size(); i++) {
			if (i < this.items.size()) {
				this.getItems().set(i, itemStack.get(i));
			}
		}
		this.setChanged();
	}

	@Override
	public void setItem(int itemStack, ItemStack stack) {
		// BABTMain.LOGGER.debug(" Set BTChest Item {} {}", itemStack, stack);
		this.unpackLootTable(null);
		this.items.set(itemStack, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}

		this.setChanged();
	}

	protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
		return new ChestMenu(MenuType.GENERIC_9x4, i, inventory, this,4);
	}

	public void setUnlocked(boolean tf) {
		ChestType chesttype = this.getBlockState().getValue(BTChestBlock.TYPE);
		this.unlocked = tf;

        // BABTMain.LOGGER.debug("{} {}", this.unlocked, chesttype);

		// Make sure that if this is a double chest the other half also gets unlocked.
		if (chesttype != ChestType.SINGLE) {
			Direction direction = BTChestBlock.getConnectedDirection(this.getBlockState());
			BTChestBlockEntity chestEntity = null;
			try {
				chestEntity = (BTChestBlockEntity) this.level.getBlockEntity(this.getBlockPos().relative(direction));
			} catch (Exception e) {
				BABattleTowers.LOGGER.debug(e.toString());
			}

			if (chestEntity != null) {
				chestEntity.unlocked = tf;
			}

		}

		//BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.lockKey);
	}

	@Override
	public boolean canOpen(Player player) {
		return super.canOpen(player) && canUnlock(player, this.getDisplayName());
	}

	public boolean canUnlock(Player player, Component component) {
		if (this.golemChest) {
			if (!this.unlocked && !player.isSpectator()) {
				player.displayClientMessage(Component.translatable("container.isLocked", component), true);
				player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
				return false;
			}
			else {
				return true;
			}
		}
		if (!this.unlocked && !player.isSpectator()) {
			player.displayClientMessage(Component.literal(this.getDefaultName().getString() + " is sealed. ").append(Component.translatable("container.ba_bt.tower_chest.isLocked")), true);
			player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
			return false;
		}
		else {
			return true;
		}
	}

	public boolean isUnlocked() {
		return this.unlocked;
	}

	public boolean isGolemChest() {
		return golemChest;
	}
}



