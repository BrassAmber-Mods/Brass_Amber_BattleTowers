package com.BrassAmber.ba_bt.entity.ai.goal;

import java.util.EnumSet;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion.Mode;

public class GolemStompAttackGoal extends Goal {

	protected final BTGolemEntityAbstract golem;
	protected final float explosionStrength;
	protected final double minVertDistance;
	
	protected boolean jumpingInProgress = false;
	
	protected static final int WARMUP_TICKS = 10;
	
	protected int warmup = WARMUP_TICKS;
	
	public GolemStompAttackGoal(BTGolemEntityAbstract golem, final float explosionStrength, final double minVertDistToTarget) {
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
		if(this.golem.getTarget() != null && !this.golem.isDormant() && this.golem.isOnGround()) {
			final LivingEntity target = this.golem.getTarget();
			final Vector3d targetPos = target.position();
			final Vector3d golemPos = this.golem.position();
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
		
		this.golem.setDeltaMovement(Vector3d.ZERO);
		this.golem.setDeltaMovement(Vector3d.ZERO.add(0, this.explosionStrength / 3, 0));
		
		this.jumpingInProgress = true;
	}
	
	
	
	@Override
	public void tick() {
		super.tick();
		
		if(!this.golem.isOnGround() && !this.jumpingInProgress) {
			this.jumpingInProgress = true;
		}
		else if(this.jumpingInProgress && this.golem.isOnGround()) {
			this.jumpingInProgress = false;
			
			AxisAlignedBB aabb = new AxisAlignedBB(this.golem.position().add(this.golem.getBbWidth(), 1, this.golem.getBbWidth()), this.golem.position().subtract(this.golem.getBbWidth(), 2, this.golem.getBbWidth()));
			BlockPos.Mutable.betweenClosedStream(aabb).forEach((position) -> {
				this.golem.level.destroyBlock(position, true);
			});
			final Vector3d position = this.golem.position();
			this.golem.level.explode(this.golem, position.x, position.y - 1, position.z, this.explosionStrength, Mode.BREAK);
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
