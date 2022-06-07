package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTEndGolem extends BTAbstractGolem {

	public BTEndGolem(EntityType<? extends BTEndGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.PINK);
		this.setGolemName(GolemType.END.getDisplayName());
	}
}