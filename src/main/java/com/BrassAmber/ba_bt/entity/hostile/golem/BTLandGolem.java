package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemLeapGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;


import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTLandGolem extends BTAbstractGolem {

	public BTLandGolem(EntityType<? extends BTLandGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.GREEN);
		this.setGolemName(GolemType.LAND.getDisplayName());
	}

	@Override
	protected void addBehaviorGoals() {
		super.addBehaviorGoals();
		this.goalSelector.addGoal(1, new GolemStompAttackGoal(this, 4.0F, 6));
		this.goalSelector.addGoal(3, new GolemLeapGoal(this,  8F, 8F, 16F));
	}


}