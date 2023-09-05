package com.brass_amber.ba_bt.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;


/**
 * TODO Maybe look for a way to implement a double attack or something?
 * (It looks okay already if we decide against it. Then we wouldn't need a separate attackGoal.)
 */
public class DualMeleeAttackGoal extends MeleeAttackGoal {
	@SuppressWarnings("unused")
	private final PathfinderMob attacker;

	public DualMeleeAttackGoal(PathfinderMob attacker, double speedModifier, boolean followingTargetEvenIfNotSeen) {
		super(attacker, speedModifier, followingTargetEvenIfNotSeen);
		this.attacker = attacker;
	}
}