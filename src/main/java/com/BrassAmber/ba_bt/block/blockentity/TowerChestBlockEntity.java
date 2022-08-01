package com.BrassAmber.ba_bt.block.blockentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TowerChestBlockEntity extends GolemChestBlockEntity {

	private Double SpawnersDestroyed = 0D;

	protected TowerChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public TowerChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(BTBlockEntityTypes.LAND_CHEST.get(), blockPos, blockState);
	}

	/**
	 * Single chest inventory name
	 * @return
	 */
	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.ba_bt.land_chest");
	}

	public void spawnerDestroyed() {
		this.SpawnersDestroyed = this.SpawnersDestroyed + 1D;
		if (this.SpawnersDestroyed == 2) {
			setUnlocked(true);
		}
		BrassAmberBattleTowers.LOGGER.info(this.SpawnersDestroyed);
	}

	public boolean canUnlock(Player player, Component component) {
		if (!this.unlocked && !player.isSpectator()) {
			player.displayClientMessage(new TextComponent(this.getDefaultName().getString() + "is sealed. ").append(new TranslatableComponent("container.ba_bt.tower_chest.isLocked", component)), true);
			player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
			return false;
		}
		else {
			return true;
		}
	}
}