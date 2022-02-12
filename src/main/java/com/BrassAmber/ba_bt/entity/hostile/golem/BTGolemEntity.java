package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
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

	/*********************************************************** Properties ********************************************************/

	public static AttributeModifierMap.MutableAttribute createBattleGolemAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, BattleTowersConfig.landGolemHP.get()).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
	}
}