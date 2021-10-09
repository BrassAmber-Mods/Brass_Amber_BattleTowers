package com.BrassAmber.ba_bt.entity.hostile;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

public abstract class BTGolemEntityAbstract extends MonsterEntity {
	protected static final DataParameter<Byte> GOLEM_STATE = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.BYTE);
	public static final byte DORMANT = 0, AWAKE = 1, ENRAGED = 2;
	public static final float SCALE = 1.8F;
	private final ServerBossInfo bossbar;

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
		compound.putByte("golemState", this.getGolemState());
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setGolemState(compound.getByte("golemState"));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(GOLEM_STATE, (byte) 0);
	}

	/*********************************************************** Awake / Dormant ********************************************************/

	/**
	 * 0 = Dormant, 1 = Awake, 2 = Enraged
	 */
	public byte getGolemState() {
		return (byte) MathHelper.clamp(this.entityData.get(GOLEM_STATE), 0, 2);
	}

	public void setGolemState(byte golemStateID) {
		this.entityData.set(GOLEM_STATE, golemStateID);
	}

	/**
	 * Check to see if the golem is dormant.
	 */
	public boolean isDormant() {
		return this.entityData.get(GOLEM_STATE).equals(DORMANT);
	}

	/**
	 * Check to see if the golem is awake, but not enraged.
	 */
	public boolean isAwake() {
		return this.entityData.get(GOLEM_STATE).equals(AWAKE);
	}

	/**
	 * Check to see if the golem is enraged.
	 */
	public boolean isEnraged() {
		return this.entityData.get(GOLEM_STATE).equals(ENRAGED);
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

		// Set the golemState to enraged when health drops below 1/3.
		if (this.getHealth() / this.getMaxHealth() < 0.33) {
			this.setGolemState(ENRAGED);

		}
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		boolean isHurt = super.hurt(source, damage);
		if (!this.isAwake()) {
			this.setGolemState(AWAKE);
		}
		return isHurt;
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
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