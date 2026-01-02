package com.brass_amber.ba_bt.entity.ai.goal;

import com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem.FIREBALL_DURATION_TICKS;
import static com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem.MELEE_DURATION_TICKS;

public class CoreGolemFireballAttackGoal extends Goal {
	protected final CoreGolem golem;
	private int attackDelay = FIREBALL_DURATION_TICKS / 2;
	private int ticksUntilNextAttack = FIREBALL_DURATION_TICKS / 2;
	private boolean shouldCountTillNextAttack = false;
	private double maxShootingDistance = 64d;
	private double maxShootingDistanceSqr;

	private long lastCanUseCheck;

	public CoreGolemFireballAttackGoal(CoreGolem golem) {
		this.golem = golem;
		this.maxShootingDistanceSqr = maxShootingDistance * maxShootingDistance;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this method as well.
	 */
	public boolean canUse() {
		return this.golem.getTarget() != null && !this.golem.isDormant();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse();
	}

	/**
	 * Execute a one shot task or start executing a continuous task.
	 */
	public void start() {
		this.attackDelay = FIREBALL_DURATION_TICKS / 2;
		this.ticksUntilNextAttack = attackDelay * 4;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one.
	 */
	public void stop() {
		this.golem.setFireball(false);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started.
	 */
	public void tick() {
		if (this.shouldCountTillNextAttack) {
			this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
		}
		if (this.ticksUntilNextAttack >= FIREBALL_DURATION_TICKS) {
			this.golem.setFireball(false);
		}
		LivingEntity targetLivingEntity = this.golem.getTarget();
		// Look at the target.
		if (targetLivingEntity != null) {
			this.golem.getLookControl().setLookAt(targetLivingEntity, 30.0F, 30.0F);
			checkAndPerformAttack(targetLivingEntity, targetLivingEntity.distanceToSqr(this.golem));
		}
	}

	protected void checkAndPerformAttack(LivingEntity enemy, double distanceToSqr) {
		// Check if the target is within range and if the Golem is able to see the target.
		if (distanceToSqr < this.maxShootingDistanceSqr && this.golem.hasLineOfSight(enemy)) {
			// We need to square it since we're using that value for calculations.
			this.shouldCountTillNextAttack = true;

			if (isTimeToStartFireballAnimation()) {
				this.golem.setFireball(true);
				this.golem.fireballAnimationTimeout = FIREBALL_DURATION_TICKS;

			}


			if (isTimeToAttack()) {
				performAttack(enemy);
				this.resetAttackCooldown();
				this.golem.setFireball(false);
			}
		}
		// If target is too far away or the golem can't see the target
		else {
			// Increase time to next attack
			resetAttackCooldown();
			this.shouldCountTillNextAttack = false;
			this.golem.setFireball(false);
		}
	}
	
	protected Projectile createFireBall(Level level, double xPower, double yPower, double zPower) {
		return new LargeFireball(level, this.golem, xPower, yPower, zPower, this.golem.getExplosionPower());
	}

	protected void performAttack(LivingEntity enemy) {
		this.resetAttackCooldown();
		// Calculation for fireball trajectory and positioning.
		Vec3 vec3 = this.golem.getViewVector(1.0F);
		double xPower = enemy.getX() - this.golem.getX();
		double yPower = enemy.getY(0.5D) - (0.5D + this.golem.getY(0.5D));
		double zPower = enemy.getZ() - this.golem.getZ();

		// Get golem world
		Level level = this.golem.level();

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
	}

	private boolean isTimeToStartFireballAnimation() {
		return this.ticksUntilNextAttack <= attackDelay;
	}

	protected void resetAttackCooldown() {
		this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 4);
	}

	protected boolean isTimeToAttack() {
		return this.ticksUntilNextAttack <= 0;
	}

	protected int getTicksUntilNextAttack() {
		return this.ticksUntilNextAttack;
	}
}
