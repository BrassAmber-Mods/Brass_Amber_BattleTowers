package com.BrassAmber.ba_bt.entity.hostile;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

public abstract class BTGolemEntityAbstract extends MonsterEntity {
	private final static DataParameter<Boolean> AWAKE = EntityDataManager.<Boolean>defineId(BTGolemEntityAbstract.class, DataSerializers.BOOLEAN);
	private final ServerBossInfo bossbar;
	public static final float scale = 1.8F;

	protected BTGolemEntityAbstract(EntityType<? extends MonsterEntity> type, World worldIn, BossInfo.Color bossbarColor) {
		super(type, worldIn);
		// Initializes the bossbar with the correct color.
		this.bossbar = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), bossbarColor, BossInfo.Overlay.PROGRESS)).setCreateWorldFog(false);
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 500;
	}

	/*********************************************************** Data ********************************************************/

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("isAwaken", this.isAwake());
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("isAwaken")) {
			this.setAwake(compound.getBoolean("isAwaken"));
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(AWAKE, false);
	}

	/*********************************************************** Awake / Dormant ********************************************************/

	public boolean isAwake() {
		return this.entityData.get(AWAKE);
	}

	public void setAwake(boolean isAwaken) {
		this.entityData.set(AWAKE, isAwaken);
	}

	/*********************************************************** Bossbar ********************************************************/

	/**
	 * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in order
	 * to view its associated boss bar.
	 */
	@Override
	public void startSeenByPlayer(ServerPlayerEntity player) {
		super.startSeenByPlayer(player);
		this.bossbar.addPlayer(player);
	}

	/**
	 * Removes the given player from the list of players tracking this entity. See {@link Entity#startSeenByPlayer} for
	 * more information on tracking.
	 */
	@Override
	public void stopSeenByPlayer(ServerPlayerEntity player) {
		super.stopSeenByPlayer(player);
		this.bossbar.removePlayer(player);
	}

	@Override
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		// Update the bossbar to display the correct name.
		this.bossbar.setName(this.getDisplayName());
	}

	/*********************************************************** Properties ********************************************************/

	/**
	 * Prevents despawning in Peaceful difficulty.
	 * I don't want him to despawn in Peaceful, just stand still invincible.
	 */
	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	/**
	 * This seems to be the standard way to prevent despawning.
	 */
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	/**
	 * Return false if this Entity is a boss, true otherwise.
	 */
	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public boolean causeFallDamage(float damage, float multiplier) {
		return false;
	}

	/**
	 * Prevents Golems from being pushed and entering Boats and such.
	 */
	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.88F;
	}

	/*********************************************************** Ticks ********************************************************/

	@Override
	public void tick() {
		super.tick();
		// Update the bossbar to display the health correctly.
		this.bossbar.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		this.spawnGolemDrops();
	}

	protected void spawnGolemDrops() {
		if (!this.level.isClientSide()) {
			int dropsAmount = 5;
			for (int j = 0; j < dropsAmount; j++) {
				spawnAtLocation(new ItemStack(Items.DIAMOND));
				spawnAtLocation(new ItemStack(Items.REDSTONE));
			}

			dropsAmount = this.random.nextInt(4) + 8;
			for (int k = 0; k < dropsAmount; k++) {
				spawnAtLocation(new ItemStack(Blocks.CLAY));
			}
		}
	}

	/*********************************************************** TESTING ********************************************************/

	@Override
	protected void registerGoals() {
	}

	/*********************************************************** Sounds ********************************************************/

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.8F;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	@Override
	protected float getVoicePitch() {
		return super.getVoicePitch();
	}

	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	@Override
	public int getAmbientSoundInterval() {
		return 400;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isAwake() ? BTSoundEvents.ENTITY_GOLEM_AMBIENT : SoundEvents.AMBIENT_CAVE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return BTSoundEvents.ENTITY_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BTSoundEvents.ENTITY_GOLEM_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, this.getSoundVolume(), this.getVoicePitch());
	}
}