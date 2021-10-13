package com.BrassAmber.ba_bt.entity.golem;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.item.BTItems;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

public abstract class BTGolemEntityAbstract extends MonsterEntity {
	private static final DataParameter<BlockPos> SPAWN_POS = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Float> SPAWN_DIRECTION = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.FLOAT);
	protected static final DataParameter<Byte> GOLEM_STATE = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.BYTE);
	public static final CreatureAttribute BATTLE_GOLEM = new CreatureAttribute();
	public static final byte DORMANT = 0, AWAKE = 1, SPECIAL = 2;
	public static final float SCALE = 0.9F; // Old scale: 1.8
	private final ServerBossInfo bossbar;

	// Data Strings
	private final String spawnPos = "SpawnPos";
	private final String spawnDirection = "SpawnDirection";
	private final String golemState = "GolemState";

	protected BTGolemEntityAbstract(EntityType<? extends MonsterEntity> type, World worldIn, BossInfo.Color bossbarColor) {
		super(type, worldIn);
		// Initializes the bossbar with the correct color.
		this.bossbar = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), bossbarColor, BossInfo.Overlay.PROGRESS)).setCreateWorldFog(false);
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 500;

		String clientOrServer = worldIn.isClientSide() ? "Client" : "Server";
		BrassAmberBattleTowers.LOGGER.info(clientOrServer + ", Create Entity!");
	}

	/*********************************************************** Data ********************************************************/

	/**
	 * Define DataParameters
	 */
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SPAWN_POS, BlockPos.ZERO);
		this.entityData.define(SPAWN_DIRECTION, 0.0f);
		this.entityData.define(GOLEM_STATE, (byte) 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.put(this.spawnPos, this.newDoubleList(this.getSpawnPos().getX(), this.getSpawnPos().getY(), this.getSpawnPos().getZ()));
		compound.putFloat(this.spawnDirection, this.getSpawnDirection());
		compound.putByte(this.golemState, this.getGolemState());
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		ListNBT spawnPos = compound.getList(this.spawnPos, 6);
		double x = spawnPos.getDouble(0);
		double y = spawnPos.getDouble(1);
		double z = spawnPos.getDouble(2);
		this.setSpawnPos(new BlockPos(x, y, z));
		this.setSpawnDirection(compound.getFloat(this.spawnDirection));
		this.setGolemState(compound.getByte(this.golemState));
	}

	/*********************************************************** Ticks ********************************************************/

	/*
	 * Gets called every tick the entity exists. (Alive or dead)
	 * 
	 * TODO Reset if there's no target for some time
	 * TODO Shoot fireballs
	 * TODO Destroy floors and blocks to get to the player
	 */
	@Override
	public void tick() {
		super.tick();
		//		BrassAmberBattleTowers.LOGGER.info("TICK TICK TICK!");

		// Update the bossbar to display the health correctly.
		this.bossbar.setPercent(this.getHealth() / this.getMaxHealth());

		// Set the golemState to enraged when health drops below 1/3.
		if (this.getHealth() / this.getMaxHealth() < 0.33) {
			this.setGolemState(SPECIAL);
		}

		// Reset to spawn position if the golem is too far away
		int maxDistanceFromSpawn = 16;
		maxDistanceFromSpawn *= maxDistanceFromSpawn;
		BlockPos spawnPos = this.getSpawnPos();
		if (this.distanceToSqr(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()) > maxDistanceFromSpawn && !this.getCommandSenderWorld().isClientSide()) {
			this.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
			this.yRot = this.getSpawnDirection();
			this.setGolemState(DORMANT);
			//			this.setInvisible(true);
		}

		// Do something per second
		boolean isEachSecond = this.tickCount % 20 == 0;

		// If the golem is dormant and not at full health, slowly heal per second.
		if (this.getHealth() < this.getMaxHealth() && this.isDormant() && isEachSecond) {
			// Heal the golem. (Will be clamped to max health)
			this.heal(this.getMaxHealth() / 200);
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

	/*********************************************************** AI Goals ********************************************************/

	/**
	 * Called in {@link MobEntity} Constructor
	 */
	@Override
	protected void registerGoals() {
		this.reassessGolemGoals();
	}

	private SwimGoal swimGoal = new SwimGoal(this);
	private MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.0D, true);
	private LookAtGoal lookAtGoal = new LookAtGoal(this, PlayerEntity.class, 8.0F);
	private HurtByTargetGoal hurtByTargetGoal = new HurtByTargetGoal(this);
	private NearestAttackableTargetGoal<PlayerEntity> nearestAttackablePlayer = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, true);

	/**
	 * Method for adding/removing AI when changing GolemState.
	 * (Not sure if this is the best way to do this, but lets see where we end up in the end)
	 */
	protected void reassessGolemGoals() {
//		BrassAmberBattleTowers.LOGGER.info("ReassesGoals");
		if (this.isDormant()) {
			this.goalSelector.removeGoal(this.swimGoal);
			this.goalSelector.removeGoal(this.meleeAttackGoal);
			this.goalSelector.removeGoal(this.lookAtGoal);
			this.targetSelector.removeGoal(this.hurtByTargetGoal);
			this.targetSelector.removeGoal(this.nearestAttackablePlayer);
		} else if (this.isAwake()) {
			this.goalSelector.addGoal(0, this.swimGoal);
			this.goalSelector.addGoal(1, this.meleeAttackGoal);
			this.goalSelector.addGoal(2, this.lookAtGoal);
			this.targetSelector.addGoal(1, this.hurtByTargetGoal);
			this.targetSelector.addGoal(2, this.nearestAttackablePlayer);
		}
	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		BrassAmberBattleTowers.LOGGER.info("SPAWN GOLEM");

		// Start coordinates centered on the spawning Block
		// Seems to initialize the server-side values and then it takes 3? ticks to update the client
		this.setSpawnPos(this.blockPosition());
		this.setSpawnDirection(this.yRot);

		return spawnDataIn;
	}

	/*********************************************************** Properties ********************************************************/

	public static AttributeModifierMap.MutableAttribute createBattleGolemAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D);
	}

	@Override
	public CreatureAttribute getMobType() {
		return BATTLE_GOLEM;
	}

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

	/**
	 * Prevents entity being pushed by water
	 */
	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.88F;
	}

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 * @return An ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 */
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(BTItems.MONOLITH);
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
		this.reassessGolemGoals();
	}

	/**
	 * Check to see if the golem is dormant.
	 */
	public boolean isDormant() {
		return this.getGolemState() == DORMANT;
	}

	/**
	 * Check to see if the golem is awake, but not enraged.
	 */
	public boolean isAwake() {
		return this.getGolemState() == AWAKE;
	}

	/**
	 * Check to see if the golem is enraged.
	 */
	public boolean isEnraged() {
		return this.getGolemState() == SPECIAL;
	}

	/*********************************************************** Spawn Position ********************************************************/

	/**
	 * Define the spawn position.
	 */
	public void setSpawnPos(BlockPos spawnPos) {
		this.entityData.set(SPAWN_POS, spawnPos);
	}

	/**
	 * Get the defined spawn position.
	 */
	private BlockPos getSpawnPos() {
		return this.entityData.get(SPAWN_POS);
	}

	public Float getSpawnDirection() {
		return this.entityData.get(SPAWN_DIRECTION);
	}

	public void setSpawnDirection(float yRot) {
		BrassAmberBattleTowers.LOGGER.info("Set Spawn Direction: " + yRot);
		this.entityData.set(SPAWN_DIRECTION, yRot);
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