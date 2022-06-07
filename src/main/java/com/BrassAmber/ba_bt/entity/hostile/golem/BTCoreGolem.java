package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTCoreGolem extends BTAbstractGolem {

	public BTCoreGolem(EntityType<? extends BTCoreGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.PURPLE);
		this.setGolemName(GolemType.CORE.getDisplayName());
	}
}