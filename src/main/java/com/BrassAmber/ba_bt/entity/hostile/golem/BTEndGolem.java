package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTEndGolem extends BTAbstractGolem {

	public BTEndGolem(EntityType<? extends BTEndGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.PINK);
		this.setGolemName(GolemType.END.getDisplayName());
		this.setBossBarName();
		this.BOSS_MUSIC = BTSoundEvents.END_GOLEM_FIGHT_MUSIC;
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.golemType = GolemType.END;
		this.xpReward = 8670;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return BTAbstractGolem.createBattleGolemAttributes().add(Attributes.MAX_HEALTH, 600D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 21.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}
}