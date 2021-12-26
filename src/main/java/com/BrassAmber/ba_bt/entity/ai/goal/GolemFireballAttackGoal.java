package com.BrassAmber.ba_bt.entity.ai.goal;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Reference: {@link GhastEntity.FireballAttackGoal}
 * 
 * TODO Doesn't seem to work together with {@link MeleeAttackGoal}
 */
public class GolemFireballAttackGoal extends Goal {
	protected final BTGolemEntityAbstract golem;
	public int chargeTime;

	public GolemFireballAttackGoal(BTGolemEntityAbstract golem) {
		this.golem = golem;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this method as well.
	 */
	public boolean canUse() {
		return this.golem.getTarget() != null && !this.golem.isDormant();
	}

	/**
	 * Execute a one shot task or start executing a continuous task.
	 */
	public void start() {
		this.chargeTime = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one.
	 */
	public void stop() {
		this.golem.setCharging(false);
	}

	/**
	 * Keep ticking a continuous task that has already been started.
	 */
	public void tick() {
		LivingEntity targetLivingEntity = this.golem.getTarget();
		// Look at the target.
		this.golem.getLookControl().setLookAt(targetLivingEntity, 30.0F, 30.0F);

		// Set the max shooting distance
		double maxShootingDistance = 64.0D;
		// We need to square it since we're using that value for calculations.
		maxShootingDistance *= maxShootingDistance;

		// Check if the target is within range and if the Golem is able to see the target.
		if (targetLivingEntity.distanceToSqr(this.golem) < maxShootingDistance && this.golem.canSee(targetLivingEntity)) {
			// Increase attack time
			++this.chargeTime;

			// Prepare to shoot. (10 more ticks, or 0.5 seconds)
			if (this.chargeTime == 10 && !this.golem.isSilent()) {
				// Play charging sound
				this.golem.playSoundEventWithVariation(BTSoundEvents.ENTITY_GOLEM_CHARGE);
			}

			// Shoot fireball
			if (this.chargeTime >= 20) {
				// Calculation for fireball trajectory and positioning.
				Vector3d vector3d = this.golem.getViewVector(1.0F);
				double xPower = targetLivingEntity.getX() - this.golem.getX();
				double yPower = targetLivingEntity.getY(0.5D) - (0.5D + this.golem.getY(0.5D));
				double zPower = targetLivingEntity.getZ() - this.golem.getZ();

				// Get golem world
				World world = this.golem.level;

				// Play shooting sound
				if (!this.golem.isSilent()) {
					world.levelEvent((PlayerEntity) null, 1016, this.golem.blockPosition(), 0);
				}

				// Create fireball
				DamagingProjectileEntity fireballentity = this.createFireBall(world, xPower, yPower, zPower);
				// Set fireball initial position
				double lateralSpawnPositionOffset = 1.2D;
				double verticalSpawnPositionOffset = 0.5D;
				fireballentity.setPos(this.golem.getX() + vector3d.x * lateralSpawnPositionOffset, this.golem.getY(0.5D) + verticalSpawnPositionOffset, fireballentity.getZ() + vector3d.z * lateralSpawnPositionOffset);
				// Add fireball to the world
				world.addFreshEntity(fireballentity);

				// set firing timeout between shoots
				this.chargeTime = this.golem.getGolemState() == BTGolemEntityAbstract.SPECIAL ? -20 : -40;
			}
		}

		// If target is too far away or the golem can't see the target and the current charge time is more than 0.
		else if (this.chargeTime > 0) {
			// Decrease charge time until 0. (Which is the default)
			--this.chargeTime;
		}

		// 10 ticks before shooting, so while preparing to shoot, set the DataParameter charging to 'true'.
		this.golem.setCharging(this.chargeTime > 10);
	}
	
	protected DamagingProjectileEntity createFireBall(World world, double xPower, double yPower, double zPower) {
		FireballEntity fb = new FireballEntity(world, this.golem, xPower, yPower, zPower);
		fb.explosionPower = this.golem.getExplosionPower();
		
		return fb;
	}
}
