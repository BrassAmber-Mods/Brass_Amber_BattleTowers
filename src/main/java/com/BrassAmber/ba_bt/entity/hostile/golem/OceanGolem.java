package com.BrassAmber.ba_bt.entity.hostile.golem;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class OceanGolem extends BTAbstractGolem {

	public OceanGolem(EntityType<? extends EndGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.BLUE);
	}
}