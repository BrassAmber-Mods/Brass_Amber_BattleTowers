package com.BrassAmber.ba_bt.entity.ai.target;


import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class TargetTaskGolemLand<M extends BTAbstractGolem> extends NearestAttackableTargetGoal<Player> {
	
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
		if(((BTAbstractGolem)this.mob).isDormant()) {
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

		return !((BTAbstractGolem) this.mob).isDormant() && super.canContinueToUse();
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
		}
	}

}
