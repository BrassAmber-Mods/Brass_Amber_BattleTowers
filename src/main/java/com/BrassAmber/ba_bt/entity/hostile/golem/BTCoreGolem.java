package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTCoreGolem extends BTAbstractGolem {

	public BTCoreGolem(EntityType<? extends BTCoreGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.PURPLE);
		this.setGolemName(GolemType.CORE.getDisplayName());
		this.setBossBarName();
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 2045;
		this.golemType = GolemType.CORE;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 500D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 18.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}
}