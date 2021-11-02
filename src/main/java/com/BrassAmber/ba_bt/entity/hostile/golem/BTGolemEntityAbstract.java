package com.BrassAmber.ba_bt.entity.hostile.golem;

import java.util.Map;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.item.BTItems;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

/**
 * FIXME Can still be pushed by players. (Doesn't really matter in Survival, because he will become awake. However you can still bump him off an edge while fighting)
 * 
 * TODO Break blocks in his way like enderdragen, also explode blocks players hide behind.
 * TODO If players splash the Golem with potions before the fight they I want to clear all effects.
 * 
 * TODO Pathfind to the player when not able to see the player.
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
	private int fireballChargeTime = 0;
	protected Map<Integer, Tuple<Integer, Goal>> golemGoals;
	protected Map<Integer, Tuple<Integer, Goal>> golemTargets;

	// Data Strings
	private final String spawnPosName = "SpawnPos";
	private final String spawnDirectionName = "SpawnDirection";
	private final String golemStateName = "GolemState";
	private final String explosionPowerName = "ExplosionPower";

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
		if (this.getHealth() / this.getMaxHealth() < 0.33 && !this.isDormant()) {
			// TODO Maybe stand still for a moment with hands up growling?
			// this.playSoundEvent(BTSoundEvents.ENTITY_GOLEM_CHARGE); // LOUD AF
			this.setGolemState(SPECIAL);
		}

		// When the Golem is dormant, check for a player within 6 blocks to become awake.
		if (this.isDormant()) {
			if (!this.level.isClientSide()) {
				double maxDistanceToPlayer = 6.0D;
				PlayerEntity entityplayer = this.level.getNearestPlayer(this, maxDistanceToPlayer);
				// Must be able to see the player and the player mustn't be in Creative mode.
				if (entityplayer != null && !entityplayer.isCreative() && this.canSee(entityplayer)) {
					this.setGolemState(AWAKE);
					this.setTarget(entityplayer);
				}
			}
		}

		// Reset the Golem if its to far from it's spawn location and not close to a player.
		this.resetGolemTick();

		// Heal the Golem if its dormant and not at max health.
		this.healGolemTick();

		// Handles Golem attacks.
		this.golemAttackTick();
	}

	/**
	 * Reset the Golem if its to far from it's spawn location and not close to a player.
	 */
	protected void resetGolemTick() {
		// Reset to spawn position if the golem is too far away
		int maxDistanceFromSpawn = 32;
		// We need the max distance squared.
		maxDistanceFromSpawn *= maxDistanceFromSpawn;
		BlockPos spawnPos = this.getSpawnPos();
		// Check for the distance to the X and Z coordinates on the current Y level.
		// This way we get only the lateral distance
		if (this.distanceToSqr(spawnPos.getX(), this.getY(), spawnPos.getZ()) > maxDistanceFromSpawn && !this.level.isClientSide()) {
			this.resetGolem();
		}

		// Also check to see if there's any players within 32 blocks, otherwise reset.
		int maxDistanceToNearestPlayer = 32;
		// Doesn't include Spectators. (Does include Creative mode)
		PlayerEntity nearestPlayer = this.level.getNearestPlayer(this, maxDistanceToNearestPlayer);
		// We compare the position and direction of the Golem to prevent resetting the Golem every tick when a player is not nearby.
		if (nearestPlayer == null && (!this.blockPosition().equals(spawnPos) || this.yBodyRot != this.getSpawnDirection())) {
			this.resetGolem();
		}
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

	/*********************************************************** Attack Ticks ********************************************************/

	/**
	 * Handles Golem attacks.
	 * Can be overridden by other Golems with different functionalities.
	 * 
	 * TODO Reset if there's no target for some time.
	 * TODO Destroy floors and blocks to get to the player.
	 */
	protected void golemAttackTick() {
		if (this.getTarget() instanceof LivingEntity && !this.level.getDifficulty().equals(Difficulty.PEACEFUL)) {
			this.doFireballAttack();
		}

		// TODO Destroy block stuff
		if (!this.level.isClientSide()) {
			// TODO delete
			//			this.checkWalls(this.getBoundingBox());

			// TODO Note: Golem now keeps track of target through walls and stuff.
			// We can start to use that to destroy blocks and stuff.
			if (this.getTarget() != null) {
				for (Object goal : this.targetSelector.getRunningGoals().toArray()) {
					PrioritizedGoal prioGoal = (PrioritizedGoal) goal;
					if (prioGoal.getGoal() instanceof TargetGoal) {
						// TargetGoal targetGoal = (TargetGoal) prioGoal.getGoal();
						this.canReach(this.getTarget());
						// BrassAmberBattleTowers.LOGGER.info("Can reach: " + this.canReach(this.getTarget()));
						// If the Golem is not creating new Paths and can't reach the target.
						if (!this.isPathFinding() && !this.canReach(this.getTarget())) {
							this.checkWalls(this.getBoundingBox());
						}
					}
				}
			}

			// Check if the Golem is looking for a path to reach the target.
			// Is false for example when able to attack or not able to see the player.
			// BrassAmberBattleTowers.LOGGER.info(this.isPathFinding());
			//			

			//			BrassAmberBattleTowers.LOGGER.info(this.goalSelector.getRunningGoals().count());
			//			this.goalSelector.getRunningGoals().forEach((goal) -> BrassAmberBattleTowers.LOGGER.info(goal.getGoal()));
			//			
			//			BrassAmberBattleTowers.LOGGER.info(this.targetSelector.getRunningGoals().count());
			//			this.targetSelector.getRunningGoals().forEach((goal) -> BrassAmberBattleTowers.LOGGER.info(goal.getGoal()));
		}
	}

	/*********************************************************** Attacks ********************************************************
	
	/**
	 * Check if the Golem can physically walk close enough to the target's position.
	 */
	private boolean canReach(LivingEntity targetEntity) {
		//	      this.reachCacheTime = 10 + this.getRandom().nextInt(5);
		Path path = this.getNavigation().createPath(targetEntity, 0);
		if (path == null) {
			return false;
		} else {
			PathPoint pathpoint = path.getEndNode();
			if (pathpoint == null) {
				// Check if there is even a path.
				return false;
			} else if (pathpoint.y - MathHelper.floor(targetEntity.getY()) > 2.5D) {
				// Target is too far on Y coordinate.
				return false;
			} else {
				int x = pathpoint.x - MathHelper.floor(targetEntity.getX());
				int z = pathpoint.z - MathHelper.floor(targetEntity.getZ());
				// Checks if target is too far horizontally.
				return (double) (x * x + z * z) <= 2.25D;
			}
		}
	}

	/**
	 * Referenced from {@link EnderDragonEntity#checkWalls}
	 */
	@SuppressWarnings("deprecation")
	private boolean checkWalls(AxisAlignedBB boundingBox) {
		int minX = MathHelper.floor(boundingBox.minX);
		int minY = MathHelper.floor(boundingBox.minY);
		int minZ = MathHelper.floor(boundingBox.minZ);
		int maxX = MathHelper.floor(boundingBox.maxX);
		int maxY = MathHelper.floor(boundingBox.maxY);
		int maxZ = MathHelper.floor(boundingBox.maxZ);
		boolean isStuckInWall = false;
		boolean canDestroyBlock = false;

		for (int x = minX - 1; x <= maxX + 1; ++x) {
			for (int y = minY; y <= maxY + 1; ++y) {
				for (int z = minZ - 1; z <= maxZ + 1; ++z) {
					BlockPos blockpos = new BlockPos(x, y, z);
					BlockState blockstate = this.level.getBlockState(blockpos);
					Block block = blockstate.getBlock();
					if (!blockstate.isAir(this.level, blockpos) && blockstate.getMaterial() != Material.FIRE) {
						if (net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.level, blockpos, this) && !BlockTags.DRAGON_IMMUNE.contains(block)) {
							canDestroyBlock = this.level.destroyBlock(blockpos, false) || canDestroyBlock;
						} else {
							isStuckInWall = true;
						}
					}
				}
			}
		}

		if (canDestroyBlock) {
			BlockPos blockpos1 = new BlockPos(minX + this.random.nextInt(maxX - minX + 1), minY + this.random.nextInt(maxY - minY + 1), minZ + this.random.nextInt(maxZ - minZ + 1));
			// Block destroy/explode particles.
			this.level.levelEvent(2008, blockpos1, 0);
		}

		return isStuckInWall;
	}

	/*********************************************************** Fireball Attack ********************************************************/

	/**
	 * Shoot fireballs.
	 * TODO Maybe shoot a fireball at the target's last known location?
	 */
	private void doFireballAttack() {
		LivingEntity targetLivingEntity = this.getTarget();
		// Look at the target.
		this.getLookControl().setLookAt(targetLivingEntity, 30.0F, 30.0F);

		// Set the max shooting distance
		double maxShootingDistance = 64.0D;
		// We need to square it since we're using that value for calculations.
		maxShootingDistance *= maxShootingDistance;

		// Check if the target is within range and if the Golem is able to see the target.
		if (targetLivingEntity.distanceToSqr(this) < maxShootingDistance && this.canSee(targetLivingEntity)) {
			// Increase attack time
			++this.fireballChargeTime;

			// Prepare to shoot. (10 more ticks, or 0.5 seconds)
			if (this.fireballChargeTime == 10 && !this.isSilent()) {
				// Play charging sound
				this.playSoundEventWithVariation(BTSoundEvents.ENTITY_GOLEM_CHARGE);
			}

			// Shoot fireball
			if (this.fireballChargeTime == 20) {
				// Calculation for fireball trajectory and positioning.
				Vector3d vector3d = this.getViewVector(1.0F);
				double xPower = targetLivingEntity.getX() - this.getX();
				double yPower = targetLivingEntity.getY(0.5D) - (0.5D + this.getY(0.5D));
				double zPower = targetLivingEntity.getZ() - this.getZ();

				// Get golem world
				World world = this.level;

				// Play shooting sound
				if (!this.isSilent()) {
					world.levelEvent((PlayerEntity) null, 1016, this.blockPosition(), 0);
				}

				// Create fireball
				FireballEntity fireballentity = new FireballEntity(world, this, xPower, yPower, zPower);
				// Set explosion power
				fireballentity.explosionPower = this.getExplosionPower();
				// Set fireball initial position
				double lateralSpawnPositionOffset = 1.2D;
				double verticalSpawnPositionOffset = 0.5D;
				fireballentity.setPos(this.getX() + vector3d.x * lateralSpawnPositionOffset, this.getY(0.5D) + verticalSpawnPositionOffset, fireballentity.getZ() + vector3d.z * lateralSpawnPositionOffset);
				// Add fireball to the world
				world.addFreshEntity(fireballentity);

				// set firing timeout between shoots. (Doubles if the Golem is enraged)
				this.fireballChargeTime = this.isEnraged() ? -20 : -40;
			}
		}

		// If target is too far away or the golem can't see the target and the current charge time is more than 0.
		else if (this.fireballChargeTime > 0) {
			// Decrease charge time until 0. (Which is the default)
			--this.fireballChargeTime;
		}

		// 10 ticks before shooting, so while preparing to shoot, set the DataParameter charging to 'true'.
		this.setCharging(this.fireballChargeTime > 10);
	}

	/*********************************************************** Fireball Attack Data ********************************************************/

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
		} else if (source.isCreativePlayer()) {
			this.setGolemState(AWAKE);
		}
		// This makes sure the /kill command works.
		return source.equals(DamageSource.OUT_OF_WORLD) ? super.hurt(source, damage) : false;
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
	}

	/*********************************************************** AI Goals ********************************************************/

	/**
	 * Called in the {@link MobEntity} constructor.
	 */
	@Override
	protected void registerGoals() {
		// Initialize new HashMaps and register Golem goals.
		this.golemGoals = Maps.newHashMap();
		this.golemTargets = Maps.newHashMap();
		this.registerGolemGoals();
		this.reassessGolemGoals();
	}

	/**
	 * Method for adding/removing AI when changing GolemState.
	 * (Not sure if this is the best way to do this, but lets see where we end up in the end)
	 */
	protected void reassessGolemGoals() {
		// Goals get registered on the server-side in the constructor.
		if (!this.level.isClientSide()) {
			// Prevent stupid crashes.
			if (this.golemGoals == null || this.golemTargets == null) {
				BrassAmberBattleTowers.LOGGER.error("Golem goal was \"null\". Preventing crash by not assigning any goals.");
			} else {
				if (this.isDormant()) {
					this.golemGoals.values().forEach(tuple -> this.goalSelector.removeGoal(tuple.getB()));
					this.golemTargets.values().forEach(tuple -> this.targetSelector.removeGoal(tuple.getB()));
				} else if (!this.isDormant()) {
					this.golemGoals.values().forEach(tuple -> this.goalSelector.addGoal(tuple.getA(), tuple.getB()));
					this.golemTargets.values().forEach(tuple -> this.targetSelector.addGoal(tuple.getA(), tuple.getB()));
				}
			}
		}
	}

	/**
	 * Register all goals that a Golem should have. Called in the {@link MobEntity} constructor.
	 */
	protected void registerGolemGoals() {
		this.addGolemGoal(0, new SwimGoal(this));
		/**
		 * TODO Work in progress -->
		 * Is also movement AI
		 * Pathfinding sucks, has problems with the walls on top of the tower if the player is behind them.
		 * (Should maybe be no problem after the explosion of blocks is added?)
		 */
		this.addGolemGoal(1, new MeleeAttackGoal(this, 1.0D, true));
		this.addGolemGoal(2, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.addGolemTargetGoal(1, new HurtByTargetGoal(this));
		this.addGolemTargetGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false /*mustSee*/, false /*mustReach*/));
	}

	protected void addGolemGoal(int priority, Goal goal) {
		if (this.golemGoals != null) {
			this.golemGoals.put(golemGoals.size(), new Tuple<>(priority, goal));
		}
	}

	protected void addGolemTargetGoal(int priority, Goal goal) {
		if (this.golemTargets != null) {
			this.golemTargets.put(this.golemTargets.size(), new Tuple<>(priority, goal));
		}
	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		BrassAmberBattleTowers.LOGGER.info("SPAWN GOLEM");

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
		this.reassessGolemGoals();
		if (golemStateID != DORMANT && !this.bossbar.isVisible()) {
			this.bossbar.setVisible(true);
		} else if (golemStateID == DORMANT && this.bossbar.isVisible()) {
			this.bossbar.setVisible(false);
		}
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
		BlockPos spawnPos = this.getSpawnPos();
		this.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		this.faceSpawnDirection();
		this.setGolemState(DORMANT);
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

	/**
	 * Play a SoundEvent from this entity
	 */
	public void playSoundEvent(SoundEvent soundEvent) {
		this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
	}

	public void playSoundEventWithVariation(SoundEvent soundEvent) {
		this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch() + this.getSmallPitchVariation());
	}

	private float getSmallPitchVariation() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
	}
}