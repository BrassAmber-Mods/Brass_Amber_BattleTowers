package com.BrassAmber.ba_bt.block.blockentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
}