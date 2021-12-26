package com.BrassAmber.ba_bt.entity.ai.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

/**
 * TODO Maybe look for a way to implement a double attack or something?
 * (It looks okay already if we decide against it. Then we wouldn't need a separate attackGoal.)
 */
public class DualMeleeAttackGoal extends MeleeAttackGoal {
	@SuppressWarnings("unused")
	private final CreatureEntity attacker;

	public DualMeleeAttackGoal(CreatureEntity attacker, double speedModifier, boolean followingTargetEvenIfNotSeen) {
		super(attacker, speedModifier, followingTargetEvenIfNotSeen);
		this.attacker = attacker;
	}
}