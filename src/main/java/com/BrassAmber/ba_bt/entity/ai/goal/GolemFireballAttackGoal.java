package com.BrassAmber.ba_bt.entity.ai.goal;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Reference: {@link Ghast.GhastShootFireballGoal}
 * 
 * TODO Doesn't seem to work together with {@link MeleeAttackGoal}
 */
public class GolemFireballAttackGoal extends Goal {
	protected final BTAbstractGolem golem;
	public int chargeTime;

	public GolemFireballAttackGoal(BTAbstractGolem golem) {
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
		if (targetLivingEntity != null) {
			this.golem.getLookControl().setLookAt(targetLivingEntity, 30.0F, 30.0F);

			// Set the max shooting distance
			double maxShootingDistance = 64.0D;
			// We need to square it since we're using that value for calculations.
			maxShootingDistance *= maxShootingDistance;

			// Check if the target is within range and if the Golem is able to see the target.
			if (targetLivingEntity.distanceToSqr(this.golem) < maxShootingDistance && this.golem.hasLineOfSight(targetLivingEntity)) {
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
					Vec3 vec3 = this.golem.getViewVector(1.0F);
					double xPower = targetLivingEntity.getX() - this.golem.getX();
					double yPower = targetLivingEntity.getY(0.5D) - (0.5D + this.golem.getY(0.5D));
					double zPower = targetLivingEntity.getZ() - this.golem.getZ();

					// Get golem world
					Level level = this.golem.level;

					// Play shooting sound
					if (!this.golem.isSilent()) {
						level.levelEvent(null, 1016, this.golem.blockPosition(), 0);
					}

					// Create fireball
					Projectile fireballentity = this.createFireBall(level, xPower, yPower, zPower);
					// Set fireball initial position
					double lateralSpawnPositionOffset = 1.2D;
					double verticalSpawnPositionOffset = 0.5D;
					fireballentity.setPos(this.golem.getX() + vec3.x * lateralSpawnPositionOffset, this.golem.getY(0.5D) + verticalSpawnPositionOffset, fireballentity.getZ() + vec3.z * lateralSpawnPositionOffset);
					// Add fireball to the world
					level.addFreshEntity(fireballentity);

					// set firing timeout between shoots
					this.chargeTime = this.golem.isEnraged() ? -20 : -40;
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
	}
	
	protected Projectile createFireBall(Level level, double xPower, double yPower, double zPower) {
		Fireball fb = new LargeFireball(level, this.golem, xPower, yPower, zPower, this.golem.getExplosionPower());
		return fb;
	}
}
