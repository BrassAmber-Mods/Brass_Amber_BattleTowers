package com.brass_amber.ba_bt.block.blockentity;



import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.brass_amber.ba_bt.util.BTUtil.getChestEntity;

public class TowerChestBlockEntity extends GolemChestBlockEntity {

	protected TowerChestBlockEntity(BlockEntityType<? extends ChestBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public TowerChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(getChestEntity(blockState.getBlock()), blockPos, blockState);
	}

	/**
	 * Single chest inventory name
	 * @return
	 */
	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.ba_bt." + this.tower_name + "_chest");
	}

	public boolean canUnlock(Player player, Component component) {
		if (!this.unlocked && !player.isSpectator()) {
			player.displayClientMessage(Component.literal(this.getDefaultName().getString() + " is sealed. ").append(Component.translatable("container.ba_bt.tower_chest.isLocked")), true);
			player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
			return false;
		}
		else {
			return true;
		}
	}
}