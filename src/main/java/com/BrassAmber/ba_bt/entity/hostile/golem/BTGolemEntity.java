package com.BrassAmber.ba_bt.entity.hostile.golem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class BTGolemEntity extends BTGolemEntityAbstract {

	public BTGolemEntity(EntityType<? extends BTGolemEntity> type, World worldIn) {
		super(type, worldIn, BossInfo.Color.RED);
	}
}