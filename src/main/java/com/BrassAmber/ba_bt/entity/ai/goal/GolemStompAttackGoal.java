package com.BrassAmber.ba_bt.entity.ai.goal;

import java.util.EnumSet;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GolemStompAttackGoal extends Goal {

	protected final BTAbstractGolem golem;
	protected final float explosionStrength;
	protected final double minVertDistance;
	
	protected boolean jumpingInProgress = false;
	
	protected static final int WARMUP_TICKS = 10;
	
	protected int warmup = WARMUP_TICKS;
	
	public GolemStompAttackGoal(BTAbstractGolem golem, final float explosionStrength, final double minVertDistToTarget) {
		super();
		
		this.golem = golem;
		this.explosionStrength = explosionStrength;
		this.minVertDistance = minVertDistToTarget;
		
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}
	
	@Override
	public boolean isInterruptable() {
		return false;
	}
	
	@Override
	public boolean canUse() {
		if(this.golem.getTarget() != null && !this.golem.isDormant() && this.golem.onGround()) {
			final LivingEntity target = this.golem.getTarget();
			final Vec3 targetPos = target.position();
			final Vec3 golemPos = this.golem.position();
			final double distHorizontal = Math.abs(((targetPos.x - golemPos.x) * ((targetPos.x - golemPos.x)) + (targetPos.z - golemPos.z) * (targetPos.z - golemPos.z)));
			final double distVertical = golemPos.y - targetPos.y;
			
			if(distVertical <= this.minVertDistance) {
				return false;
			}
			
			if(distHorizontal > this.minVertDistance * 5) {
				return false;
			}

			this.warmup--;
			
			return this.warmup <= 0;
		}
		return false;
	}
	
	@Override
	public void start() {
		super.start();
		
		this.golem.setDeltaMovement(Vec3.ZERO);
		this.golem.setDeltaMovement(Vec3.ZERO.add(0, this.explosionStrength / 3, 0));
		
		this.jumpingInProgress = true;
	}
	
	
	
	@Override
	public void tick() {
		super.tick();
		
		if(!this.golem.onGround() && !this.jumpingInProgress) {
			this.jumpingInProgress = true;
		}
		else if(this.jumpingInProgress && this.golem.onGround()) {
			this.jumpingInProgress = false;
			
			AABB aabb = new AABB(this.golem.position().add(this.golem.getBbWidth(), 1, this.golem.getBbWidth()), this.golem.position().subtract(this.golem.getBbWidth(), 2, this.golem.getBbWidth()));
			BlockPos.MutableBlockPos.betweenClosedStream(aabb).forEach((position) -> {
				this.golem.level().destroyBlock(position, true);
			});
			final Vec3 position = this.golem.position();
			this.golem.level().explode(this.golem, position.x, position.y - 1, position.z, this.explosionStrength, Level.ExplosionInteraction.BLOCK);
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		this.warmup = WARMUP_TICKS;
	}
	
	@Override
	public boolean canContinueToUse() {
		return this.jumpingInProgress;
	}

}
