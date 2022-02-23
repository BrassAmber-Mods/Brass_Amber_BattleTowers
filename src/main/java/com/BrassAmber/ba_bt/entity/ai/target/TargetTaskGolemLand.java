package com.BrassAmber.ba_bt.entity.ai.target;


import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class TargetTaskGolemLand<M extends BTGolemEntityAbstract> extends NearestAttackableTargetGoal<Player> {
	protected int cooldown = 20;
	
	public TargetTaskGolemLand(M mobEntity) {
		//It does not need to be able to see the target!
		super(mobEntity, Player.class, false);
		
		this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(livingEntity -> {
			if(livingEntity instanceof Player) {
				// Also doesn't include spectators.
				return !((Player) livingEntity).isCreative();
			}
			return true;
		});
	}
	
	/**
	 * Called when trying to start this Goal.
	 */

	@Override
	public boolean canUse() {
//		BrassAmberBattleTowers.LOGGER.info("Target.canUse()");
		if(((BTGolemEntityAbstract)this.mob).isDormant()) {
			return false;
		}
		return super.canUse();
	}
	
	@Override
	protected double getFollowDistance() {
		return 32.0D;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canContinueToUse() {
//		BrassAmberBattleTowers.LOGGER.info("Target.canContinueToUse()");

		return !((BTGolemEntityAbstract) this.mob).isDormant() && super.canContinueToUse();
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
