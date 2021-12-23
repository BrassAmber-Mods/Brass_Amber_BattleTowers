package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.BTTileEntityTypes;

import com.BrassAmber.ba_bt.block.block.StoneChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LockCode;
import org.apache.logging.log4j.Level;

public class StoneChestTileEntity extends GolemChestTileEntity {

	private Double SpawnersDestroyed = 0D;
	private ChestType type;
	private Direction connectedDirection;


	public StoneChestTileEntity() {
		super(BTTileEntityTypes.STONE_CHEST);
		this.lockKey = new LockCode("BTSpawner");
		if (this.hasLevel()) {
			BlockState state = this.level.getBlockState(this.getBlockPos());
			this.type = state.getValue(BlockStateProperties.CHEST_TYPE);
			StoneChestBlock block = (StoneChestBlock) state.getBlock();
			this.connectedDirection = block.getConnectedDirection(state);
		}
	}

	public void setNoLockKey() {
		this.lockKey = LockCode.NO_LOCK;
		//BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.lockKey);
	}

	/**
	 * Single chest inventory name
	 */
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.ba_bt.stone_chest");
	}

	public void spawnerDestroyed() {
		this.SpawnersDestroyed = this.SpawnersDestroyed + 1D;
		if (this.SpawnersDestroyed == 2) {
			setNoLockKey();
		}
		BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.SpawnersDestroyed);
	}
}