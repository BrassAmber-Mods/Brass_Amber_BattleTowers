package com.BrassAmber.ba_bt.entity.hostile.golem;

import net.minecraft.entity.EntityType;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class EndGolemEntity extends BTGolemEntityAbstract {

	public EndGolemEntity(EntityType<? extends EndGolemEntity> type, World worldIn) {
		super(type, worldIn, BossInfo.Color.PINK);
	}
}