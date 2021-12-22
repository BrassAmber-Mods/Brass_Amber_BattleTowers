package com.BrassAmber.ba_bt.entity.ai.target;

import java.util.function.Predicate;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class TargetTaskGolemLand<M extends BTGolemEntityAbstract> extends NearestAttackableTargetGoal<PlayerEntity> {

	protected int cooldown = 20;
	
	public TargetTaskGolemLand(M p_i50313_1_) {
		//It does not need to be able to see the target!
		super(p_i50313_1_, PlayerEntity.class, false);
		
		this.targetConditions = (new EntityPredicate()).range(this.getFollowDistance()).selector(new Predicate<LivingEntity>() {

			@Override
			public boolean test(LivingEntity t) {
				if(t instanceof PlayerEntity) {
					return !((PlayerEntity) t).isCreative();
				}
				return true;
			}});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canUse() {
		if(((M)this.mob).getGolemState() == BTGolemEntityAbstract.DORMANT) {
			if(this.cooldown > 0) {
				this.cooldown--;
			}
			//necessary, super.canUse() calls teh findTarget method...
			return this.cooldown <= 0 && super.canUse();
		}
		return super.canUse();
	}
	
	@Override
	protected double getFollowDistance() {
		return 32.0D;
	}
	
	@Override
	protected boolean canReach(LivingEntity p_75295_1_) {
		return Math.sqrt(this.mob.distanceToSqr(p_75295_1_.getX(), this.mob.getY(), p_75295_1_.getZ())) <= ((BTGolemEntityAbstract) this.mob).getTargetingRange() / 2;
	}
	
	@Override
	public void tick() {
		super.tick();
		if(this.target != null && this.mob.distanceTo(this.target) > this.getFollowDistance()) {
			this.stop();
			return;
		}
	}
	
	@Override
	public void stop() {
		this.cooldown = 10;
		super.stop();
	}

}
