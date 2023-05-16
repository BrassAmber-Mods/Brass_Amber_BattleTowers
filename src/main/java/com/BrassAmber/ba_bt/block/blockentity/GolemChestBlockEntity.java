package com.BrassAmber.ba_bt.block.blockentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.GolemChestBlock;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;

import static com.BrassAmber.ba_bt.util.BTUtil.getChestEntity;
import static com.BrassAmber.ba_bt.util.BTUtil.getTowerName;

public class GolemChestBlockEntity extends ChestBlockEntity {
	protected final String tower_name;
	private LockCode lockKey = new LockCode("bt_spawner");
	protected boolean unlocked = false;

	private static final int EVENT_SET_OPEN_COUNT = 1;
	private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
			GolemChestBlockEntity.playSound(level, blockPos, blockState, SoundEvents.CHEST_OPEN);
		}

		protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
			GolemChestBlockEntity.playSound(level, blockPos, blockState, SoundEvents.CHEST_CLOSE);
		}

		protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int i1) {
			GolemChestBlockEntity.this.signalOpenCount(level, blockPos, blockState, i, i1);
		}

		protected boolean isOwnContainer(Player player) {
			if (!(player.containerMenu instanceof ChestMenu)) {
				return false;
			} else {
				Container container = ((ChestMenu) player.containerMenu).getContainer();
				return container == GolemChestBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(GolemChestBlockEntity.this);
			}
		}
	};

	private final ChestLidController chestLidController = new ChestLidController();

	protected GolemChestBlockEntity(BlockEntityType<? extends ChestBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.tower_name = getTowerName(blockEntityType);
	}

	public GolemChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(getChestEntity(blockState.getBlock()), blockPos, blockState);
	}

	public int getContainerSize() {
		return 36;
	}

	/**
	 * Single chest inventory name
	 * @return
	 */
	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.ba_bt." + this.tower_name + "_golem_chest");
	}

	public void load(CompoundTag compoundTag) {
		super.load(compoundTag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compoundTag)) {
			ContainerHelper.loadAllItems(compoundTag, this.items);
		}
		this.unlocked = compoundTag.getBoolean("Unlocked");

	}

	protected void saveAdditional(CompoundTag compoundTag) {
		super.saveAdditional(compoundTag);
		if (!this.trySaveLootTable(compoundTag)) {
			ContainerHelper.saveAllItems(compoundTag, this.items);
		}
		compoundTag.putBoolean("Unlocked", this.unlocked);
	}

	static void playSound(Level p_155339_, BlockPos p_155340_, BlockState p_155341_, SoundEvent p_155342_) {
		ChestType chesttype = p_155341_.getValue(ChestBlock.TYPE);
		if (chesttype != ChestType.LEFT) {
			double d0 = (double)p_155340_.getX() + 0.5D;
			double d1 = (double)p_155340_.getY() + 0.5D;
			double d2 = (double)p_155340_.getZ() + 0.5D;
			if (chesttype == ChestType.RIGHT) {
				Direction direction = ChestBlock.getConnectedDirection(p_155341_);
				d0 += (double)direction.getStepX() * 0.5D;
				d2 += (double)direction.getStepZ() * 0.5D;
			}

			p_155339_.playSound((Player)null, d0, d1, d2, p_155342_, SoundSource.BLOCKS, 0.5F, p_155339_.random.nextFloat() * 0.1F + 0.9F);
		}
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
				BrassAmberBattleTowers.LOGGER.info(e);
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
			player.displayClientMessage(new TranslatableComponent("container.isLocked", component), true);
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
	public float getOpenNess(float p_59604_) {
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return items;
	}

	@Override
	public void setItems(@NotNull NonNullList<ItemStack> items) {
		for (int i = 0; i < items.size(); i++) {
			this.items.set(i, items.get(i));
		}
	}
}



