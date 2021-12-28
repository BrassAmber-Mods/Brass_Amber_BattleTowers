package com.BrassAmber.ba_bt.entity.ai.target;

import java.util.function.Predicate;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class TargetTaskGolemLand<M extends BTGolemEntityAbstract> extends NearestAttackableTargetGoal<PlayerEntity> {
	protected int cooldown = 20;
	
	public TargetTaskGolemLand(M mobEntity) {
		//It does not need to be able to see the target!
		super(mobEntity, PlayerEntity.class, false);
		
		this.targetConditions = (new EntityPredicate()).range(this.getFollowDistance()).selector(new Predicate<LivingEntity>() {

			@Override
			public boolean test(LivingEntity livingEntity) {
				if(livingEntity instanceof PlayerEntity) {
					// Also doesn't include spectators.
					return !((PlayerEntity) livingEntity).isCreative();
				}
				return true;
			}});
	}
	
	/**
	 * Called when trying to start this Goal.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean canUse() {
//		BrassAmberBattleTowers.LOGGER.info("Target.canUse()");
		if(((M)this.mob).isDormant()) {
			return false;
		}
		return super.canUse();
	}
	
	@Override
	protected double getFollowDistance() {
		return 32.0D;
	}
	
	@Override
	protected boolean canReach(LivingEntity livingEntity) {
		return Math.sqrt(this.mob.distanceToSqr(livingEntity.getX(), this.mob.getY(), livingEntity.getZ())) <= ((BTGolemEntityAbstract) this.mob).getTargetingRange() / 2;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canContinueToUse() {
//		BrassAmberBattleTowers.LOGGER.info("Target.canContinueToUse()");

		return ((M)this.mob).isDormant() ? false : super.canContinueToUse();
	}
	
	/**
	 * TODO switch targets when another player is much closer. 
	 * Or maybe if another player is visible and the current target is not.
	 * (Switch when another target is favorable)
	 */
	@Override
	public void tick() {
//		BrassAmberBattleTowers.LOGGER.info("Target.tick()");
//		BrassAmberBattleTowers.LOGGER.info(this.target);
		super.tick();
		// Stop targeting if the target is lost, or the target is too far away.
		if(this.target != null && this.mob.distanceTo(this.target) > this.getFollowDistance()) {
			this.stop();
			return;
		}
	}
	
	@Override
	public void stop() {
//		BrassAmberBattleTowers.LOGGER.info("Target.stop()");
		this.cooldown = 10;
		super.stop();
	}

}
