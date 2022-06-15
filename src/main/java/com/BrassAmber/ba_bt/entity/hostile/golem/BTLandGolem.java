package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemLeapGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;


import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import static com.BrassAmber.ba_bt.BattleTowersConfig.landGolemHP;
import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTLandGolem extends BTAbstractGolem {

	public BTLandGolem(EntityType<? extends BTLandGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.GREEN);
		this.setGolemName(GolemType.LAND.getDisplayName());
		this.setBossBarName();
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, landGolemHP.get()).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}


	@Override
	protected void addBehaviorGoals() {
		super.addBehaviorGoals();
		this.goalSelector.addGoal(1, new GolemStompAttackGoal(this, 4.0F, 6));
		this.goalSelector.addGoal(3, new GolemLeapGoal(this,  8F, 8F, 16F));
		this.goalSelector.addGoal(6, new GolemFireballAttackGoal(this));
	}


}