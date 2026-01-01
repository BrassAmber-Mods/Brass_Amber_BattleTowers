package com.brass_amber.ba_bt.entity.hostile.golem;

import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import static com.brass_amber.ba_bt.sound.BTMusic.NETHER_GOLEM_FIGHT_MUSIC;

public class NetherGolem extends AbstractGolem {

	public NetherGolem(EntityType<? extends NetherGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.RED);
		this.setGolemName(GolemType.NETHER.getDisplayName());
		this.setBossBarName();
		this.BOSS_MUSIC = NETHER_GOLEM_FIGHT_MUSIC;
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 4020;
		this.golemType = GolemType.NETHER;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return AbstractGolem.createBattleGolemAttributes().add(Attributes.MAX_HEALTH, 550D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 18.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}
}