package com.BrassAmber.ba_bt.entity.hostile.golem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class OceanGolemEntity extends BTGolemEntityAbstract {

	public OceanGolemEntity(EntityType<? extends OceanGolemEntity> type, World worldIn) {
		super(type, worldIn, BossInfo.Color.BLUE);
	}
}