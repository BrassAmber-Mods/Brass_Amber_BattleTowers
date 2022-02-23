package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;


import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTGolem extends BTAbstractGolem {

	public BTGolem(EntityType<? extends BTGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.RED);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		
		this.goalSelector.addGoal(1, new GolemStompAttackGoal(this, 4.0F, 6));
	}
}