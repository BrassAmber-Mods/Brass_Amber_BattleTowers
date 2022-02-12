package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.init.BTTileEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LockCode;

public class GolemChestTileEntity extends ChestTileEntity {

	protected boolean unlocked = false;

	public GolemChestTileEntity() {
		this(BTTileEntityTypes.LAND_GOLEM_CHEST);
	}
	

	protected <TE extends ChestTileEntity> GolemChestTileEntity(TileEntityType<TE> chestTileEntity) {
		super(chestTileEntity);
		this.lockKey = new LockCode("bt_spawner");
	}

	public void setNoLockKey() {
		this.lockKey = LockCode.NO_LOCK;
		this.unlocked = true;
		//BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.lockKey);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.unlocked && this.lockKey != LockCode.NO_LOCK) {
			this.setNoLockKey();

		} else if (!this.unlocked && this.lockKey == LockCode.NO_LOCK){
			this.lockKey = new LockCode(("BTSpawner"));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbtIn) {
		CompoundNBT nbt = super.save(nbtIn);
		nbt.putBoolean("Unlocked", this.unlocked);
		return nbt;


	}

	@Override
	public void load(BlockState blockState, CompoundNBT nbt) {
		super.load(blockState, nbt);
		this.unlocked = nbt.getBoolean("Unlocked");
	}

	/**
	 * Single chest inventory name
	 */
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.ba_bt.land_golem_chest");
	}

	public boolean isUnlocked() {
		return this.unlocked;
	}

}