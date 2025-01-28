package com.brass_amber.ba_bt.block.blockentity;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.block.block.GolemChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;

import static com.brass_amber.ba_bt.util.BTUtil.getChestEntity;
import static com.brass_amber.ba_bt.util.BTUtil.getTowerName;

public class GolemChestBlockEntity extends ChestBlockEntity {
	protected final String tower_name;
	private LockCode lockKey = new LockCode("bt_spawner");
	protected boolean unlocked = false;

	private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);

	protected GolemChestBlockEntity(BlockEntityType<? extends ChestBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.tower_name = getTowerName(blockEntityType);
	}

	public GolemChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(getChestEntity(blockState.getBlock()), blockPos, blockState);
	}

	@Override
	public int getContainerSize() {
		return 36;
	}

	@Override
	protected AbstractContainerMenu createMenu(int p_59082_, Inventory p_59083_) {
		return ChestMenu.fourRows(p_59082_, p_59083_);
	}

	/**
	 * Single chest inventory name
	 * @return
	 */
	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.ba_bt." + this.tower_name + "_golem_chest");
	}

	@Override
	public void load(CompoundTag compoundTag) {
		super.load(compoundTag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compoundTag)) {
			ContainerHelper.loadAllItems(compoundTag, this.items);
		}
		this.unlocked = compoundTag.getBoolean("Unlocked");

	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag) {
		super.saveAdditional(compoundTag);
		if (!this.trySaveLootTable(compoundTag)) {
			ContainerHelper.saveAllItems(compoundTag, this.items);
		}
		compoundTag.putBoolean("Unlocked", this.unlocked);
	}

	private net.minecraftforge.items.IItemHandlerModifiable createHandler() {
		BlockState state = this.getBlockState();
		if (!(state.getBlock() instanceof GolemChestBlock)) {
			return new net.minecraftforge.items.wrapper.InvWrapper(this);
		}
		Container inv = GolemChestBlock.getContainer((GolemChestBlock) state.getBlock(), state, getLevel(), getBlockPos(), true);
		return new net.minecraftforge.items.wrapper.InvWrapper(inv == null ? this : inv);
	}

	public void setUnlocked(boolean tf) {
		ChestType chesttype = this.getBlockState().getValue(ChestBlock.TYPE);
		this.unlocked = tf;

		// BrassAmberBattleTowers.LOGGER.info(this.unlocked + " " + chesttype);

		// Make sure that if this is a double chest the other half also gets unlocked.
		if (chesttype != ChestType.SINGLE) {
			Direction direction = ChestBlock.getConnectedDirection(this.getBlockState());
			GolemChestBlockEntity chestEntity = null;
			try {
				chestEntity = (GolemChestBlockEntity) this.level.getBlockEntity(this.getBlockPos().relative(direction));
			} catch (Exception e) {
				BABTMain.LOGGER.info(e.toString());
			}

			if (chestEntity != null) {
				chestEntity.unlocked = tf;
			}

		}

		//BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.lockKey);
	}

	@Override
	public boolean canOpen(Player player) {
		return canUnlock(player, this.getDisplayName());
	}

	public boolean canUnlock(Player player, Component component) {
		if (!this.unlocked && !player.isSpectator()) {
			player.displayClientMessage(Component.translatable("container.isLocked", component), true);
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

	@Override
	public @NotNull BlockPos getBlockPos() {
		return this.worldPosition;
	}


	@Override
	public void setItems(@NotNull NonNullList<ItemStack> items) {
		super.setItems(items);
	}
}



