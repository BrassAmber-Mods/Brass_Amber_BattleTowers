package com.BrassAmber.ba_bt.entity.hostile;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

public class SkyMinionEntity extends Vex {

	public SkyMinionEntity(EntityType<? extends Vex> entity, Level levelIn) {
		super(entity, levelIn);
	}

//	@Override
//	protected void registerGoals() {
//		// TODO
//	}
}
