package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTNetherGolem extends BTAbstractGolem {

	public BTNetherGolem(EntityType<? extends BTNetherGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.RED);
		this.setGolemName(GolemType.NETHER.getDisplayName());
	}
}