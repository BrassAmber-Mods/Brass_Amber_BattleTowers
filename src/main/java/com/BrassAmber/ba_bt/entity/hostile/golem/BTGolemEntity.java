package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;

import net.minecraft.entity.EntityType;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class BTGolemEntity extends BTGolemEntityAbstract {

	public BTGolemEntity(EntityType<? extends BTGolemEntity> type, World worldIn) {
		super(type, worldIn, BossInfo.Color.RED);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		
		this.goalSelector.addGoal(1, new GolemStompAttackGoal(this, 4.0F, 6));
	}
}