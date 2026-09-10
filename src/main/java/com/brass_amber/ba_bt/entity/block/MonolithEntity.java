package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.hostile.golem.AbstractGolem;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.registries.EntityData;
import com.brass_amber.ba_bt.registries.TowerData;
import com.brass_amber.ba_bt.sound.BTSoundEvents;


import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/*
 * Test swimming and sounds from Entity
 * 
 * Make particles appear like destroying blocks when removing
 */
public class MonolithEntity extends Entity {
	public static final EntityDataAccessor<Integer> KEYS = SynchedEntityData.defineId(MonolithEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(MonolithEntity.class, EntityDataSerializers.INT);

	public final AnimationState floating = new AnimationState();
	public final AnimationState spawningGolem = new AnimationState();

	public EntityData btEntityData;
	private int livingSoundTime;
	private int nextStageCounter = 0;
	private boolean playedSpawnSound = false;

	public MonolithEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	/*********************************************************** Data ********************************************************/

	public void setBtEntityData(ResourceKey<TowerData> dataKey) {
		Registry<TowerData> towerDataReg = this.level().getServer().registryAccess().registryOrThrow(BTRegistries.Keys.TOWER_DATA);
		this.btEntityData = towerDataReg.getOrThrow(dataKey).getEntityData();
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(KEYS, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.setKeyCountInEntity(compound.getInt("Keys"));
		this.setStateInEntity(compound.getInt("State"));

		DynamicOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, this.level().getServer().registryAccess());
		this.btEntityData = EntityData.CODEC.parse(ops, compound.getCompound("BtEntityData")).getOrThrow(false, BABattleTowers.LOGGER::error);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("Keys", this.getKeyCountInEntity());
		compound.putInt("State", this.getStateInEntity());

		DynamicOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, this.level().getServer().registryAccess());
		compound.put("BtEntityData", EntityData.CODEC.encodeStart(ops, this.btEntityData).getOrThrow(false, BABattleTowers.LOGGER::error));
	}

	@Override
	public Component getDisplayName() {
		Component nameComponent = Component.translatable("entity.ba_bt.land_monolith");

		try {
			nameComponent = Component.translatable("entity.ba_bt.%S_monolith".formatted(this.btEntityData.getName()));
		} catch (Exception e) {
			BABattleTowers.LOGGER.error("Unfindable towerData name of: %S ".formatted(this.btEntityData.getName()));
		}
		return nameComponent;
	}

	/*********************************************************** Ticks ********************************************************/

	/**
	 * Gets called from: {@link Entity#tick()}.
	 */
	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.spawningGolem.isStarted()) {
			this.floating.startIfStopped(this.tickCount);
		}

		if (this.getKeyCountInEntity() == this.btEntityData.getKeyMax(this.getStateInEntity()) && !this.playedSpawnSound) {
			this.nextStageCounter++;

			// Hold the stage for 2 seconds before going to the next
			if (this.nextStageCounter >= 40) {
				// Display next state
				this.setStateInEntity(this.entityData.get(STATE) + 1);
				this.setKeyCountInEntity(0);
				this.nextStageCounter = 0;
			}
		}

		if (this.getStateInEntity() == this.btEntityData.getMultiStateKeys().getMaxState()) {
			if (!this.playedSpawnSound) {
				this.playSpawnSound();
				this.playedSpawnSound = true;
			}
			this.floating.stop();
			this.spawningGolem.start(this.tickCount);
			// Hold this for 5 seconds before Golem Spawns
			int seconds = 5;
			if (this.nextStageCounter >= (seconds * 20)) {
				//	Spawn Golem and remove this entity
				this.spawnGolem();
				this.updateObelisk();
				this.nextStageCounter = 0;
				this.remove(RemovalReason.DISCARDED);
			}
			this.nextStageCounter++;
		}

		// Checks if there are any blocks inside the hit-box and deletes them.
		this.checkBlocksInEntity();

		if (this.level().isClientSide()) {
			// Animate particles.
			this.animateTick();
		}

		// Play ambient sounds at random intervals.
		if (this.isAlive() && this.random.nextInt(1000) < this.livingSoundTime++) {
			this.resetMinSoundInterval();
			this.playAmbientSound();
			// BrassAmberBattleTowers.LOGGER.debug("Spawned Obelisk? : " + this.spawnedObelisk);
		}
	}

	public void updateObelisk() {
		if (this.level().isClientSide()) {
			return;
		}
		try {
			BTAbstractObelisk obelisk = this.level().getEntitiesOfClass(BTAbstractObelisk.class, this.getBoundingBox().inflate(15, 115, 15)).get(0);
			obelisk.setGolemSpawned(true);
		} catch (Exception ignored) {
		}
	}

	/*********************************************************** Interaction ********************************************************/

	/**
	 * Applies the given player interaction to this Entity.
	 */
	@Override
	public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
		EntityData.MultiStateKeys keys = this.btEntityData.getMultiStateKeys();
		// Get the item in the player's hand.
		ItemStack itemInHand = player.getItemInHand(hand);
		if (keys.keyMatch(itemInHand, this.getStateInEntity(), this.getKeyCountInEntity())) {
			this.increaseKeyCount(player, hand);
			return InteractionResult.sidedSuccess(this.getCommandSenderWorld().isClientSide());
		}

		// Pass
		return super.interact(player, hand);
	}

	/**
	 * Increase the amount of keys by 1.
	 */
	private void increaseKeyCount(Player player, InteractionHand hand) {
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
		if (!this.level().isClientSide()) {
			ServerLevel serverworld = (ServerLevel) this.level();
			// Spawn visual lightning
			LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(serverworld);
			lightningbolt.moveTo(this.getX(), this.getY(), this.getZ());
			lightningbolt.setVisualOnly(true);
			serverworld.addFreshEntity(lightningbolt);

			this.level().explode(null, this.getX(), this.getY() + 2, this.getZ(), 1.6F, Level.ExplosionInteraction.BLOCK);
			this.level().explode(null, this.getX(), this.getY() + 1, this.getZ(), 1.4F, Level.ExplosionInteraction.BLOCK);

			// Get the correct GolemEntityType.
			EntityType<?> golemEntityType = this.btEntityData.getGolemEntity();
			// Create a new GolemEntity.
			Entity entity = golemEntityType.create(this.level());
			if (entity instanceof AbstractGolem newGolemEntity) {
				// Set the position for the new Golem to the current position of the Monolith.
				newGolemEntity.setPos(this.getX(), this.getY(), this.getZ());
				// Set the Golem to be invulnerable for x amount of ticks.
				newGolemEntity.invulnerableTime = 60;
				// Set the Golem to spawn Dormant.
				newGolemEntity.setGolemState(AbstractGolem.DORMANT);
				// Spawn the Golem facing the same direction as the Monolith.
				newGolemEntity.faceDirection(this.getGolemSpawnDirection(this.getYRot()));

				newGolemEntity.setBossBarName();

				newGolemEntity.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.TRIGGERED, null, null);
				serverworld.addFreshEntity(newGolemEntity);
			}
		}
	}

	/**
	 * Returns the correct spawn rotation for the Golem.
	 */
	private float getGolemSpawnDirection(float monolithRotation) {
		// Invert placement facing north and south.
		return monolithRotation == 0 ? 180 : monolithRotation == 180 ? 0 : monolithRotation;
	}

	/************************************************ Check Blocks ***************************************************/

	/**
	 * Checks if there are any Blocks in the way.
	 */
	private void checkBlocksInEntity() {
		Block setBlock = this.btEntityData.getMonolithFillBlock();
		for (int height = 0; height < 3; height++) {
			BlockPos monolithPos = this.blockPosition().offset(0, height, 0);
			BlockState testBlock = this.level().getBlockState(monolithPos);
			if (!testBlock.is(setBlock)) {
				this.level().setBlockAndUpdate(monolithPos, setBlock.defaultBlockState());
			}
		}
	}

	/******************************************************* Keys ****************************************************/

	/**
	 * counts the amount of keys in the entity. used in rendering.
	 */
	public final int getKeyCountInEntity() {
		return Mth.clamp(this.entityData.get(KEYS), 0, 3);
	}

	/**
	 * sets the amount of keys in the entity. used for rendering.
	 */
	public final void setKeyCountInEntity(int count) {
		this.entityData.set(KEYS, count);
	}

	/**
	 * counts the state  in the entity. used in rendering.
	 */
	public final int getStateInEntity() {
		return Mth.clamp(this.entityData.get(STATE), 0, 3);
	}

	/**
	 * sets the state in the entity. used for rendering.
	 */
	public final void setStateInEntity(int count) {
		// Ensure State never goes past final so that final state is shown visually until monolith is removed
		this.entityData.set(STATE, Math.min(count, this.btEntityData.getMultiStateKeys().getMaxState()));
	}

	/*************************************** Characteristics & Properties *******************************************/

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 * @return An ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 * (Empty ItemStack is an ItemStack of '(Item) null')
	 */
	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(this.btEntityData.getMonolithItem());
	}


	/**
	 * {@link PushReaction.IGNORE} is the only valid option for an entity I think to stop piston interaction
	 * <p>
	 * Used in: {@link PistonTileEntity.moveCollidedEntities method}
	 */
	@SuppressWarnings("JavadocReference")
	@Override
	public @NotNull PushReaction getPistonPushReaction() {
		return PushReaction.BLOCK;
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

	/***************************************************** Breaking *************************************************/

	/**
	 * Called by the /kill command.
	 */
	@Override
	public void kill() {
		// Do nothing to prevent people deleting a Monolith by accident.
		BABattleTowers.LOGGER.debug("Used the /kill command. However, a Monolith has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(@NotNull DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!(source.getMsgId().equals("player"))) {
			return false;
		} else {
			if (this.isAlive() && !this.level().isClientSide() && source.isCreativePlayer()) {
				this.playDestroySound();
				this.remove(RemovalReason.KILLED);
			}
			return true;
		}
	}

	/****************************************************** Client ***************************************************/


	private void animateTick() {
		// TODO Add blue portal like particle
		if (/*!config.disableParticles.get() && */this.random.nextFloat() < 0.2f) {
			//			this.world.addParticle(ParticleTypes.PORTAL, this.getPosX() + (this.rand.nextDouble() - 0.5) * 1.5, this.getPosY() + 0.8, this.getPosZ() + (this.rand.nextDouble() - 0.5) * 1.5, 0, 0, 0);
			this.level().addParticle(ParticleTypes.ENCHANT, this.getX() + (this.random.nextDouble() - 0.5) * 1.5, this.getY() + 1 + this.random.nextDouble(), this.getZ() + (this.random.nextDouble() - 0.5) * 1.5, this.random.nextDouble() - 0.5, this.random.nextDouble(), this.random.nextDouble() - 0.5);
		}
	}

	/*********************************************************** Sounds ********************************************************/

	@Override
	public @NotNull SoundSource getSoundSource() {
		return SoundSource.BLOCKS;
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
		return 0.5F;
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
		this.playSound(SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), this.getSoundVolume(), this.getSoundPitch() + 1.5F);
	}

	private void playSpawnSound() {
		this.playSound(BTSoundEvents.MONOLITH_SPAWN_GOLEM.get(), this.getSoundVolume() -.4F, 1F);
	}

	@Override
	public void playSound(@NotNull SoundEvent p_19938_, float p_19939_, float p_19940_) {
		if (!this.isSilent()) {
			this.level().playSound(null, this, p_19938_, this.getSoundSource(), p_19939_, p_19940_);
		}
	}
}