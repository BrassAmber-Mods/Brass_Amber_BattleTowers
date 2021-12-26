package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.DestroyTowerEntity;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.system.CallbackI;

/*
 * Test swimming and sounds from Entity
 * 
 * Make particles appear like destroying blocks when removing
 */
public class MonolithEntity extends Entity {
	public static final DataParameter<Integer> KEYS = EntityDataManager.defineId(MonolithEntity.class, DataSerializers.INT);
	private final EntityType<?> monolithType;
	private final GolemType golemType;
	private final Item correctMonolithKey;
	private final Item correctGuardianEye;
	private boolean displayEye = false;
	private int nextStageCounter = 0;
	private int livingSoundTime;
	private int floatingRotation;
	private boolean playedSpawnSound = false;

	public MonolithEntity(EntityType<? extends MonolithEntity> type, World world) {
		super(type, world);
		this.blocksBuilding = true;
		this.floatingRotation = this.random.nextInt(100_000);
		this.monolithType = this.getType();
		this.golemType = GolemType.getTypeForMonolith(this);
		this.correctMonolithKey = GolemType.getKeyFor(this.golemType);
		this.correctGuardianEye = GolemType.getEyeFor(GolemType.getPreviousGolemType(this.golemType));
	}

	public MonolithEntity(EntityType<MonolithEntity> monolithEntityType, World worldIn, double x, double y, double z) {
		this(monolithEntityType, worldIn);
		this.setPos(x, y, z);
	}

	/*********************************************************** Data ********************************************************/

	@Override
	protected void defineSynchedData() {
		this.entityData.define(KEYS, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.setKeyCountInEntity(compound.getInt("Keys"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putInt("Keys", this.getKeyCountInEntity());
	}

	/*********************************************************** Ticks ********************************************************/

	/**
	 * Gets called from: {@link Entity#tick()}.
	 */
	@Override
	public void baseTick() {
		super.baseTick();

		boolean isLandMonolith = this.monolithType.equals(BTEntityTypes.LAND_MONOLITH);
		if (this.getKeyCountInEntity() == 2 && !isLandMonolith) {
			this.nextStageCounter++;
			int seconds = 2;
			// Hold this stage for 2 seconds before the Eye slot becomes visible.
			if (this.nextStageCounter >= (seconds * 20)) {
				// Display the Eye slot on the Monolith.
				this.setEyeSlotDisplayed();
				// Reset counter.
				this.nextStageCounter = 0;
			}
		}
		// Checks for keys.
		else if (this.getKeyCountInEntity() >= 3 && (this.isEyeSlotDisplayed() || isLandMonolith)) {
			if (!this.playedSpawnSound) {
				this.playSpawnSound();
				this.playedSpawnSound = true;
			}
			// Hold this for 5 seconds before Golem Spawns
			int seconds = 5;
			if (this.nextStageCounter >= (seconds * 20)) {
				//	Spawn Golem and remove this entity
				this.spawnGolem();
				this.remove();
			}
			this.nextStageCounter++;
		}

		// Checks if there are any blocks inside the hit-box and deletes them.
		this.checkBlocksInEntity();

		// Handles the floating animation
		++this.floatingRotation;
		// Animate particles.
		this.animateTick();

		// Play ambient sounds at random intervals.
		if (this.isAlive() && this.random.nextInt(1000) < this.livingSoundTime++) {
			this.resetMinSoundInterval();
			this.playAmbientSound();
		}
	}

	/*********************************************************** Interaction ********************************************************/

	/**
	 * Applies the given player interaction to this Entity.
	 */
	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (this.monolithType != null) {
			// Get the item in the player's hand.
			Item itemInHand = player.getItemInHand(hand).getItem();
			if (itemInHand.equals(this.correctMonolithKey)) {
				// Need 3 keys for the Land Monolith and 2 for the others.
				int keysNeeded = this.monolithType.equals(BTEntityTypes.LAND_MONOLITH) ? 3 : 2;
				// Check how many keys are already in.
				if (this.getKeyCountInEntity() < keysNeeded) {
					// Increase Keys by one.
					this.increaseKeyCount(player, hand);
					return ActionResultType.sidedSuccess(this.getCommandSenderWorld().isClientSide());
				}
			}

			// Test for a chance to insert a Guardian Eye.
			else if (itemInHand.equals(this.correctGuardianEye) && this.getKeyCountInEntity() == 2 && !this.monolithType.equals(BTEntityTypes.LAND_MONOLITH) && this.isEyeSlotDisplayed()) {
				this.increaseKeyCount(player, hand);
				return ActionResultType.sidedSuccess(this.getCommandSenderWorld().isClientSide());
			}
		}

		// Pass
		return super.interact(player, hand);
	}

	/**
	 * Increase the amount of keys by 1.
	 */
	private void increaseKeyCount(PlayerEntity player, Hand hand) {
		this.setKeyCountInEntity(this.getKeyCountInEntity() + 1);
		this.playKeyInteractionSound();
		if (!player.isCreative()) {
			player.getItemInHand(hand).shrink(1);
		}
	}

	/*********************************************************** Golem Spawning ********************************************************/

	/**
	 * Helper method to spawn a new Golem.
	 */
	private void spawnGolem() {
		if (!this.level.isClientSide()) {
			ServerWorld serverworld = (ServerWorld) this.level;

			// Spawn visual lightning
			LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(serverworld);
			lightningboltentity.moveTo(this.getX(), this.getY(), this.getZ());
			lightningboltentity.setVisualOnly(true);
			serverworld.addFreshEntity(lightningboltentity);

			this.level.explode(null, this.getX(), this.getY() + 2, this.getZ(), 1.6F, Explosion.Mode.BREAK);
			this.level.explode(null, this.getX(), this.getY() + 1, this.getZ(), 1.4F, Explosion.Mode.BREAK);

			// Get the correct GolemEntityType.
			EntityType<?> golemEntityType = GolemType.getGolemFor(this.golemType);
			// Create a new GolemEntity.
			Entity entity = golemEntityType.create(this.level);
			if (entity instanceof BTGolemEntityAbstract) {
				BTGolemEntityAbstract newGolemEntity = (BTGolemEntityAbstract) entity;
				// Set the position for the new Golem to the current position of the Monolith.
				newGolemEntity.setPos(this.getX(), this.getY(), this.getZ());
				// Set the Golem to be invulnerable for x amount of ticks.
				newGolemEntity.invulnerableTime = 60;
				// Set the Golem to spawn Dormant.
				newGolemEntity.setGolemState(BTGolemEntityAbstract.DORMANT);
				// Spawn the Golem facing the same direction as the Monolith.
				newGolemEntity.faceDirection(this.getGolemSpawnDirection(this.yRot));

				newGolemEntity.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.TRIGGERED, (ILivingEntityData) null, (CompoundNBT) null);
				serverworld.addFreshEntity(newGolemEntity);
			}

			Entity entity1 = new DestroyTowerEntity(this.golemType, this.blockPosition(), this.level, 0.75D);
			entity1.setPos(this.getX(), this.getY() + 6, this.getZ());
			entity1.invulnerableTime = 999999999;
			serverworld.addFreshEntity(entity1);
		}
	}

	/**
	 * Returns the correct spawn rotation for the Golem.
	 */
	private float getGolemSpawnDirection(float monolithRotation) {
		// Invert placement facing north and south.
		return monolithRotation == 0 ? 180 : monolithRotation == 180 ? 0 : monolithRotation;
	}

	/*********************************************************** Check Blocks ********************************************************/

	/**
	 * Checks if there are any Blocks in the way.
	 */
	@SuppressWarnings("deprecation")
	private void checkBlocksInEntity() {
		for (int height = 0; height < 3; height++) {
			BlockPos monolithPos = this.blockPosition().offset(0, height, 0);
			BlockState testBlock = this.level.getBlockState(monolithPos);
			if (!testBlock.isAir()) {
				this.level.setBlockAndUpdate(monolithPos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	/*********************************************************** Keys ********************************************************/

	/**
	 * counts the amount of keys in the entity. used in rendering.
	 */
	public final int getKeyCountInEntity() {
		return MathHelper.clamp(this.entityData.get(KEYS), 0, 3);
	}

	/**
	 * sets the amount of keys in the entity. used for rendering.
	 */
	public final void setKeyCountInEntity(int count) {
		this.entityData.set(KEYS, count);
	}

	/*********************************************************** Golem Type Helpers ********************************************************/

	public EntityType<?> getMonolithType() {
		return this.monolithType;
	}

	public void setEyeSlotDisplayed() {
		this.displayEye = true;
	}

	public boolean isEyeSlotDisplayed() {
		return this.displayEye;
	}

	/*********************************************************** Characteristics & Properties ********************************************************/

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 * @return An ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 * (Empty ItemStack is an ItemStack of '(Item) null')
	 */
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(GolemType.getMonolithItemFor(this.golemType));
	}

	/**
	 * {@link PushReaction.IGNORE} is the only valid option for an entity I think to stop piston interaction
	 * TODO I want this to Block the pistons movement
	 * 
	 * Used in: {@link PistonTileEntity.moveCollidedEntities method}
	 */
	@SuppressWarnings("JavadocReference")
	@Override
	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 * (Like arrows and stuff.)
	 */
	@Override
	public boolean isPickable() {
		return this.isAlive();
	}

	/**
	 * Block movement through this entity
	 */
	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	/*********************************************************** Breaking ********************************************************/

	/**
	 * Called by the /kill command.
	 */
	@Override
	public void kill() {
		// Do nothing to prevent people deleting a Monolith by accident.
		BrassAmberBattleTowers.LOGGER.info("Used the /kill command. However, a Monolith has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!(source.getMsgId().equals("player"))) {
			return false;
		} else {
			if (this.isAlive() && !this.level.isClientSide() && source.isCreativePlayer()) {
				this.playDestroySound();
				this.remove();
			}
			return true;
		}
	}

	/*********************************************************** Client ********************************************************/

	@OnlyIn(Dist.CLIENT)
	public float getFloatingRotation() {
		return this.floatingRotation;
	}

	@OnlyIn(Dist.CLIENT)
	private void animateTick() {
		// TODO Add blue portal like particle
		if (/*!config.disableParticles.get() && */this.random.nextFloat() < 0.2f) {
			//			this.world.addParticle(ParticleTypes.PORTAL, this.getPosX() + (this.rand.nextDouble() - 0.5) * 1.5, this.getPosY() + 0.8, this.getPosZ() + (this.rand.nextDouble() - 0.5) * 1.5, 0, 0, 0);
			this.level.addParticle(ParticleTypes.ENCHANT, this.getX() + (this.random.nextDouble() - 0.5) * 1.5, this.getY() + 1 + this.random.nextDouble(), this.getZ() + (this.random.nextDouble() - 0.5) * 1.5, this.random.nextDouble() - 0.5, this.random.nextDouble(), this.random.nextDouble() - 0.5);
		}
	}

	/*********************************************************** Networking?? ********************************************************/
	//	TODO Check the networking section on the Forge Docs

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/*********************************************************** Sounds ********************************************************/
	// TODO Fix subtitles for all sounds.

	@Override
	public SoundCategory getSoundSource() {
		return SoundCategory.BLOCKS;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	private float getSoundVolume() {
		return 0.6F;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	private float getSoundPitch() {
		float avaragePitch = 0.5F;
		return avaragePitch;
	}

	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	private int getAmbientSoundInterval() {
		return 400;
	}

	/**
	 * Reset the interval for ambient sounds.
	 */
	private void resetMinSoundInterval() {
		this.livingSoundTime = -this.getAmbientSoundInterval();
	}

	/**
	 * Plays a sound at its position
	 */
	private void playAmbientSound() {
		this.playSound(SoundEvents.RESPAWN_ANCHOR_AMBIENT, this.getSoundVolume() - 0.2F, this.getSoundPitch());
	}

	private void playKeyInteractionSound() {
		if (this.getKeyCountInEntity() == 3) {
			this.playSound(SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, this.getSoundVolume() - 0.5F, this.getSoundPitch());
		}
		float pitchModifier = this.getKeyCountInEntity() / 6.5F;
		this.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, this.getSoundVolume(), this.getSoundPitch() + pitchModifier);
	}

	private void playDestroySound() {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, this.getSoundVolume() + 2.0F, this.getSoundPitch() + 1.0F);
		this.playSound(SoundEvents.RESPAWN_ANCHOR_DEPLETE, this.getSoundVolume(), this.getSoundPitch() + 1.5F);
	}

	private void playSpawnSound() {
		this.playSound(BTSoundEvents.MONOLITH_SPAWN_GOLEM, this.getSoundVolume(), 1F);
	}
}