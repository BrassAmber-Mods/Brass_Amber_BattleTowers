package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.golem.BTGolemEntity;
import com.BrassAmber.ba_bt.entity.golem.BTGolemEntityAbstract;
import com.BrassAmber.ba_bt.item.BTItems;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

/*
 * Test swimming and sounds from Entity
 * 
 * Make particles appear like destroying blocks when removing
 */
public class MonolithEntity extends Entity {
	public static final DataParameter<Integer> KEYS = EntityDataManager.defineId(MonolithEntity.class, DataSerializers.INT);
	public int livingSoundTime;
	public int floatingRotation;

	public MonolithEntity(EntityType<? extends MonolithEntity> type, World world) {
		super(type, world);
		this.blocksBuilding = true;
		this.floatingRotation = this.random.nextInt(100_000);
	}

	public MonolithEntity(World worldIn, double x, double y, double z) {
		this(BTEntityTypes.MONOLITH, worldIn);
		this.setPos(x, y, z);
	}

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 *
	 * @param target The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 */
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(BTItems.MONOLITH);
	}

	/*********************************************************** NBT ********************************************************/

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

	/*********************************************************** Interaction ********************************************************/

	/**
	 * Applies the given player interaction to this Entity.
	 * 
	 * TODO use {@link Entity.interact}
	 */
	@Override
	public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
		if (player.getItemInHand(hand).getItem().equals(BTItems.LAND_MONOLOITH_KEY)) {
			if (this.getKeyCountInEntity() < 3) {
				this.setKeyCountInEntity(this.getKeyCountInEntity() + 1);
				this.playKeyInteractionSound();
				if (!player.isCreative()) {
					player.getItemInHand(hand).shrink(1);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	/*********************************************************** Ticks ********************************************************/

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		++this.floatingRotation; // Handles the floating animation

		if (this.getKeyCountInEntity() >= 3) {
			//	Spawn Golem and remove this entity
			this.spawnGolem();
			this.remove();
		}

		this.checkBlocksInEntity();

		this.animateTick();
	}

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

	/**
	 * Helper method to spawn a new Golem.
	 */
	protected void spawnGolem() {
		if (!this.level.isClientSide()) {
			ServerWorld serverworld = (ServerWorld) this.level;

			// Spawn visual lightning
			LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(serverworld);
			lightningboltentity.moveTo(this.getX(), this.getY(), this.getZ());
			lightningboltentity.setVisualOnly(true);
			serverworld.addFreshEntity(lightningboltentity);

			// Create a new GolemEntity
			BTGolemEntity newGolemEntity = BTEntityTypes.LAND.create(this.level);
			// Set the position for the new Golem to the current position of the Monolith.
			newGolemEntity.setPos(this.getX(), this.getY(), this.getZ());
			// Set the Golem to be invulnerable for x amount of ticks.
			newGolemEntity.invulnerableTime = 60;
			// Set the Golem to spawn Dormant.
			newGolemEntity.setGolemState(BTGolemEntityAbstract.DORMANT);
			// Spawn the Golem facing the same direction as the Monolith.
			newGolemEntity.yRot = this.yRot;
			newGolemEntity.setYHeadRot(this.yRot);
			newGolemEntity.setYBodyRot(this.yRot);
			
			newGolemEntity.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.TRIGGERED, (ILivingEntityData) null, (CompoundNBT) null);
			serverworld.addFreshEntity(newGolemEntity);
		}
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAlive() && this.random.nextInt(1000) < this.livingSoundTime++) {
			this.resetMinSoundInterval();
			this.playAmbientSound();
		}
	}

	private void resetMinSoundInterval() {
		this.livingSoundTime = -this.getTalkInterval();
	}

	/*********************************************************** Characteristics ********************************************************/

	/**
	 * {@link PushReaction.IGNORE} is the only valid option for an entity I think to stop piston interaction
	 * TODO I want this to Block the pistons movement
	 * 
	 * Used in: {@link PistonTileEntity.moveCollidedEntities method}
	 */
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
	@SuppressWarnings("deprecation")
	@Override
	public boolean isPickable() {
		return !this.removed;
	}

	/**
	 * Block movement through this entity
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean canBeCollidedWith() {
		return !this.removed;
	}

	/*********************************************************** Breaking ********************************************************/

	/**
	 * Called by the /kill command.
	 */
	@Override
	public void kill() {
		// Do nothing to prevent people deleting the Monoliths by accident.
		BrassAmberBattleTowers.LOGGER.info("Used the /kill command. However, one Monolith has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
	}

	/**
	 * Called when the entity is attacked.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!(source.getMsgId().equals("player"))) {
			return false;
		} else {
			if (!this.removed && !this.level.isClientSide() && source.isCreativePlayer()) {
				this.playDestroySound();
				this.remove();
			}
			return true;
		}
	}

	/*********************************************************** Sounds ********************************************************/

	@Override
	public SoundCategory getSoundSource() {
		return SoundCategory.BLOCKS;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	private float getSoundVolume() {
		return 0.8F;
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
	private int getTalkInterval() {
		return 400;
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

	/*********************************************************** Client ********************************************************/

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
}