package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTOceanGolem extends BTAbstractGolem {

	public BTOceanGolem(EntityType<? extends BTOceanGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.BLUE);
		this.setGolemName(GolemType.OCEAN.getDisplayName());
	}
}