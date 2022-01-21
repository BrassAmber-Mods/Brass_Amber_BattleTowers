package com.BrassAmber.ba_bt.entity.hostile.golem;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.BTTileEntityTypes;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.StoneChestTileEntity;
import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.DestroyTowerEntity;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.BrassAmber.ba_bt.entity.ai.target.TargetTaskGolemLand;
import com.BrassAmber.ba_bt.item.BTItems;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Level;

/**
 * @author Xrated_junior, DerToaster
 * @TODO
 * FIXME Can still be pushed by players. (Doesn't really matter in Survival, because he will become awake. However you can still bump him off an edge while fighting)
 * 
 * DONE Break blocks in his way like enderdragen, also explode blocks players hide behind.
 * TODO If players splash the Golem with potions before the fight they I want to clear all effects.
 * 
 * DONE? Pathfind to the player when not able to see the player.
 * 
 * TODO Can see invisible players
 * TODO Fix pathfinding to last known target location after golem reset. (Rare bug)
 */
public abstract class BTGolemEntityAbstract extends MonsterEntity {
	private static final DataParameter<BlockPos> SPAWN_POS = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Float> SPAWN_DIRECTION = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.FLOAT);
	private static final DataParameter<Byte> GOLEM_STATE = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.BYTE);
	private static final DataParameter<Boolean> DATA_IS_CHARGING = EntityDataManager.defineId(BTGolemEntityAbstract.class, DataSerializers.BOOLEAN);
	public static final CreatureAttribute BATTLE_GOLEM = new CreatureAttribute();
	public static final byte DORMANT = 0, AWAKE = 1, SPECIAL = 2;
	public static final float SCALE = 0.9F; // Old scale: 1.8
	private final ServerBossInfo bossbar;
	private int explosionPower = 1;
	private DestroyTowerEntity destroyTower;


	// Data Strings
	private final String spawnPosName = "SpawnPos";
	private final String spawnDirectionName = "SpawnDirection";
	private final String golemStateName = "GolemState";
	private final String explosionPowerName = "ExplosionPower";
	private BlockPos chestTileEntityPos;

	protected BTGolemEntityAbstract(EntityType<? extends MonsterEntity> type, World worldIn, BossInfo.Color bossbarColor) {
		super(type, worldIn);
		// Initializes the bossbar with the correct color.
		this.bossbar = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), bossbarColor, BossInfo.Overlay.PROGRESS)).setCreateWorldFog(false);
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 500;

		// TODO Delete, testing purposes
		// String clientOrServer = worldIn.isClientSide() ? "Client" : "Server";
		// BrassAmberBattleTowers.LOGGER.info(clientOrServer + ", Create Entity!");
		
		this.maxUpStep = 1.5F;
	}
	
	/**
	 * @return Maximum horizontal distance from the tower.
	 */
	public int getAllowedTowerRange() {
		return 32;
	}
	
	/**
	 * @return Maximum distance between player and Golem to become awake.
	 */
	public int getWakeUpRange() {
		return 6;
	}
	
	public int getTargetingRange() {
		return this.getAllowedTowerRange();
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
		this.entityData.define(DATA_IS_CHARGING, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.put(this.spawnPosName, this.newDoubleList(this.getSpawnPos().getX(), this.getSpawnPos().getY(), this.getSpawnPos().getZ()));
		compound.putFloat(this.spawnDirectionName, this.getSpawnDirection());
		compound.putByte(this.golemStateName, this.getGolemState());
		compound.putInt(this.explosionPowerName, this.explosionPower);
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		ListNBT spawnPos = compound.getList(this.spawnPosName, 6);
		double x = spawnPos.getDouble(0);
		double y = spawnPos.getDouble(1);
		double z = spawnPos.getDouble(2);
		this.setSpawnPos(new BlockPos(x, y, z));
		this.setSpawnDirection(compound.getFloat(this.spawnDirectionName));
		this.setGolemState(compound.getByte(this.golemStateName));
		if (compound.contains(this.explosionPowerName, 99)) {
			this.explosionPower = compound.getInt("ExplosionPower");
		}
	}

	/*********************************************************** Ticks ********************************************************/

	/*
	 * Gets called every tick the entity exists. (Alive or dead)
	 */
	@Override
	public void tick() {
		super.tick();

		// Update the bossbar to display the health correctly.
		this.bossbar.setPercent(this.getHealth() / this.getMaxHealth());

		// Set the golemState to enraged when health drops below 1/3.
		if (this.getHealth() / this.getMaxHealth() < 0.33 && !this.isEnraged() && this.isAwake()) {
			// TODO Maybe stand still for a moment with hands up growling?
			this.playSoundEvent(BTSoundEvents.ENTITY_GOLEM_SPECIAL, 0.5f); // LOUD AF (Still? I adjusted the volume)
			this.setGolemState(SPECIAL);
		}

		// When the Golem is dormant, check for a player within 6 blocks to become awake.
		if (this.isDormant()) {
			if (!this.level.isClientSide()) {
				PlayerEntity entityplayer = this.level.getNearestPlayer(this, this.getWakeUpRange());
				// Must be able to see the player and the player mustn't be in Creative mode.
				if (entityplayer != null && !entityplayer.isCreative() && this.canSee(entityplayer)) {
					this.wakeUpGolem();
					this.setTarget(entityplayer);
				}
			}
		} else {
			if (this.tickCount > 0 && this.tickCount % 20 == 0 && !this.level.isClientSide) {
				this.destroyBlocksNearby();
			}
		}

		// Reset the Golem if its to far from it's spawn location and not close to a
		// player.
		this.resetGolemTick();

		// Heal the Golem if its dormant and not at max health.
		this.healGolemTick();
	}
	
	/**
	 * Set LivingEntity to be targeted by the Golem.
	 * (Used in TargetGoals)
	 * 
	 * Note: Finds the nearest player/loaded entity.
	 */
	@Override
	public void setTarget(@Nullable LivingEntity livingEntity) {
		if (this.getTarget() instanceof PlayerEntity && ((PlayerEntity) this.getTarget()).isCreative()) {
			super.setTarget(null);
		}
		if (livingEntity instanceof PlayerEntity && !((PlayerEntity) livingEntity).isSpectator()) {
			super.setTarget(livingEntity);
			return;
		}
		
		// TODO Re-implement
//		if (livingEntity == null && this.getTarget() != null) {
//			double horizontalDistanceToTarget = this.horizontalDistanceToSqr(this.getTarget().getX(), this.getTarget().getZ());
//			if (horizontalDistanceToTarget <= (this.getTargetingRange() * this.getTargetingRange()) && this.getTarget().isAlive()
//					&& !(this.getTarget() instanceof PlayerEntity && ((PlayerEntity) this.getTarget()).isSpectator())) {
//				// DOn't reset the target when it is still in reach!
//				return;
//			} else {
//				this.resetGolem();
//			}
//		}
		super.setTarget(livingEntity);
	}
	
	/**
	 * Returns the horizontal distance.
	 */
	public double horizontalDistanceToSqr(double targetX, double targetZ) {
		double dX = this.getX() - targetX;
		double dZ = this.getZ() - targetZ;
		return dX * dX + dZ * dZ;
	}

	/**
	 * Reset the Golem if its to far from it's spawn location and not close to a player.
	 */
	protected void resetGolemTick() {
		// Reset to spawn position if the golem is too far away
		int maxDistanceFromSpawn = this.getAllowedTowerRange();
		// We need the max distance squared.
		maxDistanceFromSpawn *= maxDistanceFromSpawn;
		BlockPos spawnPos = this.getSpawnPos();
		// Check for the distance to the X and Z coordinates on the current Y level.
		// This way we get only the lateral distance
		if (this.distanceToSqr(spawnPos.getX(), this.getY(), spawnPos.getZ()) > maxDistanceFromSpawn && !this.level.isClientSide()) {
			this.resetGolem();
		}
		
		// Also check to see if there's any players within 32 blocks, otherwise reset.
		// Doesn't include Spectators or Creative mode. (Does include Creative mode)
		PlayerEntity nearestPlayer = this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), this.getTargetingRange(), true);
		// We compare the position and direction of the Golem to prevent resetting the Golem every tick when a player is not nearby.
		if (nearestPlayer == null && (!this.blockPosition().equals(spawnPos) || this.yBodyRot != this.getSpawnDirection())) {
			this.resetGolem();
		}
		
		// TODO Resets too often.
//		if(this.getTarget() != null && (!this.getTarget().isAlive() || (this.getTarget() instanceof PlayerEntity && ((PlayerEntity)this.getTarget()).isSpectator()))) {
//			this.resetGolem();
//			return;
//		}
//		if (this.getTarget() == null || (this.getTarget() != null && this.distanceToSqr(this.getTarget().getX(), this.getY(), this.getTarget().getZ()) >= this.getTargetingRange() && (!this.blockPosition().equals(spawnPos) || this.yBodyRot != this.getSpawnDirection()))) {
//			this.resetGolem();
//		}
	}

	/**
	 * Heal the Golem if its dormant and not at max health.
	 */
	protected void healGolemTick() {
		// Do something per second
		boolean isEachSecond = this.tickCount % 20 == 0;

		// If the golem is dormant and not at full health, slowly heal per second.
		if (this.getHealth() < this.getMaxHealth() && this.isDormant() && isEachSecond) {
			// Heal the golem. (Will be clamped to max health)
			this.heal(this.getMaxHealth() / 200);
		}
	}

	private void destroyBlocksNearby() {
		if(net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) && this.getTarget() != null && this.isAwake()) {
			final Vector3d offset = this.getLookAngle().normalize().scale(0.5);
			
			int ox = MathHelper.floor(this.getX() + offset.x);
			int oy = MathHelper.floor(this.getY() + 0.25);
			int oz = MathHelper.floor(this.getZ() + offset.z);
			
			int width = MathHelper.ceil(this.getBbWidth() / 2);
			int height = MathHelper.ceil(this.getBbHeight());
			boolean playEffectFlag = false;
			for(int ix = ox - width; ix <= ox + width; ix++) {
				for(int iy = oy; iy <= oy + height; iy++) {
					for(int iz = oz - width; iz <= oz + width; iz++) {
						BlockPos pos = new BlockPos(ix, iy, iz);
						BlockState state = this.level.getBlockState(pos);
						if(state.canEntityDestroy(this.level, pos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, pos, state)) {
							playEffectFlag |= this.level.destroyBlock(pos, true, this);
						}
					}
				}
			}
			
			if(playEffectFlag) {
				this.level.levelEvent(Constants.WorldEvents.WITHER_BREAK_BLOCK_SOUND, this.blockPosition(), 0);
			}
		}
	}
	
	public void setCharging(boolean setCharging) {
		this.entityData.set(DATA_IS_CHARGING, setCharging);
	}

	public int getExplosionPower() {
		return this.explosionPower;
	}

	/*********************************************************** Hurt / Die ********************************************************/

	/**
	 * TODO Maybe let players do more damage if they are close to the Golem compared to shooting it from far away to encourage close combat?
	 */
	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (!this.isDormant()) {
			// this.reassessGolemGoals(); // Used for testing
			int multiplier = this.isEnraged() ? 2 : 1;
			return super.hurt(source, damage * multiplier);
		} else if (source.isCreativePlayer() && this.isDormant()) {
			this.setGolemState(AWAKE);
		}
		// This makes sure the /kill command works.
		return source.equals(DamageSource.OUT_OF_WORLD) ? super.hurt(source, damage) : false;
	}

	@Override
	public void die(DamageSource source) {

		this.destroyTower = (DestroyTowerEntity) this.level.getEntities(null,
				new AxisAlignedBB(this.getSpawnPos().getX()-1,this.getSpawnPos().getY() + 4,
						this.getSpawnPos().getZ()-1, this.getSpawnPos().getX() + 1,
						this.getSpawnPos().getY() + 7, this.getSpawnPos().getZ() + 1)).get(0);
		this.destroyTower.setGolemDead(true);

		if (!this.level.isClientSide()) {
			BlockPos spawnPos = this.getSpawnPos();
			BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, spawnPos);

			checkPos(spawnPos.north(12).below());
			checkPos(spawnPos.east(12).below());
			checkPos(spawnPos.south(12).below());
			checkPos(spawnPos.west(12).below());
			try {
				TileEntity entity = this.level.getBlockEntity(this.chestTileEntityPos);
				if (entity != null) {
					GolemChestTileEntity chestEntity = (GolemChestTileEntity) this.level.getBlockEntity(this.chestTileEntityPos);
					BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Chest " + entity);
					chestEntity.setNoLockKey();
				}

			} catch (Exception ignored) {

			}
		}

		super.die(source);
	}

	public void checkPos(BlockPos pos) {
		TileEntity posEntity = this.level.getBlockEntity(pos);

		if (posEntity != null && posEntity.getType() == BTTileEntityTypes.GOLEM_CHEST) {
			this.chestTileEntityPos = pos;
		}
		else {
			BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, pos + " - " + posEntity);
		}
	}

	/*********************************************************** AI Goals ********************************************************/

	/**
	 * Called in the {@link MobEntity} constructor.
	 */
	@Override
	protected void registerGoals() {
		this.addGolemGoal(0, new SwimGoal(this));
		this.addGolemGoal(5, new MeleeAttackGoal(this, 1.0D, true) {
			@Override
			public boolean canUse() {
				if (BTGolemEntityAbstract.this.isDormant()) {
					return false;
				}
//				BrassAmberBattleTowers.LOGGER.info("Melee: " +getTarget());
				return super.canUse();
			}
			
			@Override
			public boolean canContinueToUse() {
//				BrassAmberBattleTowers.LOGGER.info("Melee canContinueToUse():" +getTarget());
				return BTGolemEntityAbstract.this.isDormant() ? false : super.canContinueToUse();
			}
		});
		this.addGolemGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F) {
			@Override
			public boolean canUse() {
				if (BTGolemEntityAbstract.this.isDormant()) {
					return false;
				}
//				BrassAmberBattleTowers.LOGGER.info("Look");
				return super.canUse();
			}
			
			@Override
			public boolean canContinueToUse() {
//				BrassAmberBattleTowers.LOGGER.info("Look canContinueToUse()");
				return BTGolemEntityAbstract.this.isDormant() ? false : super.canContinueToUse();
			}
		});

		this.addGolemGoal(6, this.createFireballAttackGoal());
		// Ignore damage from non-player entities
		this.addGolemTargetGoal(1, new HurtByTargetGoal(this));
		// this.addGolemTargetGoal(2, new NearestAttackableTargetGoal<>(this,
		// PlayerEntity.class, false /*mustSee*/, false /*mustReach*/));
		this.addGolemTargetGoal(2, new TargetTaskGolemLand<BTGolemEntityAbstract>(this));
	}

	protected GolemFireballAttackGoal createFireballAttackGoal() {
		return new GolemFireballAttackGoal(this);
	}
	
	protected void addGolemGoal(int priority, Goal goal) {
		this.goalSelector.addGoal(priority, goal);
	}

	protected void addGolemTargetGoal(int priority, Goal goal) {
		this.targetSelector.addGoal(priority, goal);
	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		// TODO Delete, Testing
		// BrassAmberBattleTowers.LOGGER.info("SPAWN GOLEM");

		// Set spawn position and direction centered on the spawning Block.
		this.setSpawnPos(this.blockPosition());
		this.setSpawnDirection(this.yRot);
		return spawnDataIn;
	}

	/*********************************************************** Properties ********************************************************/

	public static AttributeModifierMap.MutableAttribute createBattleGolemAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
	}

	@Override
	public CreatureAttribute getMobType() {
		return BATTLE_GOLEM;
	}

	/**
	 * Returns whether this Entity is on the same team as the given Entity.
	 * TODO Will be useful with the Sky golem and her minions.
	 */
	@Override
	public boolean isAlliedTo(Entity entityIn) {
		if (super.isAlliedTo(entityIn)) {
			return true;
		} else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getMobType() == this.getMobType()) {
			return this.getTeam() == null && entityIn.getTeam() == null;
		} else {
			return false;
		}
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
	
	/**
	 * Affects the movement speed?
	 */
	@Override
	protected boolean isAffectedByFluids() {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.88F;
	}

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 *
	 * @param target The full target the player is looking at
	 * @return An ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 */
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		EntityType<?> entityType = this.getEntity().getType();
		if (entityType != null) {
			if (entityType.equals(BTEntityTypes.LAND_GOLEM)) {
				return new ItemStack(BTItems.LAND_MONOLITH);
			} else if (entityType.equals(BTEntityTypes.CORE_GOLEM)) {
				return new ItemStack(BTItems.CORE_MONOLITH);
			} else if (entityType.equals(BTEntityTypes.NETHER_GOLEM)) {
				return new ItemStack(BTItems.NETHER_MONOLITH);
			} else if (entityType.equals(BTEntityTypes.END_GOLEM)) {
				return new ItemStack(BTItems.END_MONOLITH);
			} else if (entityType.equals(BTEntityTypes.SKY_GOLEM)) {
				return new ItemStack(BTItems.SKY_MONOLITH);
			} else if (entityType.equals(BTEntityTypes.OCEAN_GOLEM)) {
				return new ItemStack(BTItems.OCEAN_MONOLITH);
			}
		}

		// Couldn't get EntityType
		return ItemStack.EMPTY;
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
		if (golemStateID != DORMANT && !this.bossbar.isVisible()) {
			this.bossbar.setVisible(true);
		} else if (golemStateID == DORMANT && this.bossbar.isVisible()) {
			this.bossbar.setVisible(false);
		}
	}
	
	/**
	 * Set Golem state to awake.
	 */
	protected void wakeUpGolem() {
		this.setGolemState(AWAKE);
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

	/*********************************************************** Spawn Position / Reset ********************************************************/

	/*
	 * Reset the Golem to its defined spawn location.
	 */
	private void resetGolem() {
		if(this.isDormant()) {
			return;
		}
		final double x = this.getX();
		final double y = this.getY();
		final double z = this.getZ();
		BlockPos spawnPos = this.getSpawnPos();
		if(this.distanceToSqr(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()) <= 4) {
			return;
		}
		this.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		this.faceSpawnDirection();
		this.setGolemState(DORMANT);
		
		this.getNavigation().stop();
		
		// TODO Stop running goals
		
		LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.level);
		lightning.setPos(x, y, z);
		lightning.setDamage(0.0F);
		this.level.addFreshEntity(lightning);
	}

	/*
	 * Make the Golem face the defined spawn direction.
	 */
	private void faceSpawnDirection() {
		this.faceDirection(this.getSpawnDirection());
	}

	/*
	 * Make the Golem face the defined spawn direction.
	 */
	public void faceDirection(float direction) {
		this.yRot = direction;
		this.setYHeadRot(direction);
		this.setYBodyRot(direction);
	}

	/**
	 * Define the spawn position.
	 */
	public void setSpawnPos(BlockPos spawnPos) {
		this.entityData.set(SPAWN_POS, spawnPos);
	}

	/**
	 * Get the defined spawn position.
	 */
	protected BlockPos getSpawnPos() {
		return this.entityData.get(SPAWN_POS);
	}

	public Float getSpawnDirection() {
		return this.entityData.get(SPAWN_DIRECTION);
	}

	public void setSpawnDirection(float yRot) {
		// TODO Delete, Testing
		// BrassAmberBattleTowers.LOGGER.info("Set Spawn Direction: " + yRot);
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

	/**
	 * Play a SoundEvent from this entity with adjusted volume.
	 */
	public void playSoundEvent(SoundEvent soundEvent, float volume) {
		this.playSound(soundEvent, volume, this.getVoicePitch());
	}
	
	/**
	 * Play a SoundEvent from this entity
	 */
	public void playSoundEvent(SoundEvent soundEvent) {
		this.playSoundEvent(soundEvent, this.getSoundVolume());
	}

	public void playSoundEventWithVariation(SoundEvent soundEvent) {
		this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch() + this.getSmallPitchVariation());
	}

	private float getSmallPitchVariation() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
	}

	public boolean isEnragedBasedOnHP() {
		return this.getHealth() / this.getMaxHealth() < 0.33F || this.isEnraged();
	}
	
}