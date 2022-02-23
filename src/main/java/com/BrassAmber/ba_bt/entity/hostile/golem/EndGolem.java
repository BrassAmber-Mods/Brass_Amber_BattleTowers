package com.BrassAmber.ba_bt.entity.hostile.golem;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EndGolem extends BTAbstractGolem {

	public EndGolem(EntityType<? extends EndGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.PINK);
	}
}