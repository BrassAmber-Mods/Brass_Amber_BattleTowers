package com.BrassAmber.ba_bt.entity.ai.target;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class TargetTaskGolemLand<M extends BTGolemEntityAbstract> extends NearestAttackableTargetGoal<PlayerEntity> {

	protected int cooldown = 5;
	
	public TargetTaskGolemLand(M p_i50313_1_) {
		//It does not need to be able to see the target!
		super(p_i50313_1_, PlayerEntity.class, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canUse() {
		if(((M)this.mob).getGolemState() == BTGolemEntityAbstract.DORMANT) {
			if(this.cooldown > 0) {
				this.cooldown--;
			}
			return this.cooldown <= 0;
		}
		return super.canUse();
	}
	
	@Override
	protected double getFollowDistance() {
		return 32.0D;
	}
	
	@Override
	protected boolean canReach(LivingEntity p_75295_1_) {
		return super.canReach(p_75295_1_);
	}
	
	@Override
	public void tick() {
		if(this.target == null) {
			return;
		}
		if(this.mob.distanceTo(this.target) > this.getFollowDistance()) {
			this.stop();
			return;
		}
		
		super.tick();
	}
	
	@Override
	public void stop() {
		this.cooldown = 10;
		super.stop();
	}

}
