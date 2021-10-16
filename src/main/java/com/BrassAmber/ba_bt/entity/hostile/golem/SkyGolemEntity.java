package com.BrassAmber.ba_bt.entity.hostile.golem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class SkyGolemEntity extends BTGolemEntityAbstract {

	public SkyGolemEntity(EntityType<? extends SkyGolemEntity> type, World worldIn) {
		super(type, worldIn, BossInfo.Color.WHITE);
	}
}