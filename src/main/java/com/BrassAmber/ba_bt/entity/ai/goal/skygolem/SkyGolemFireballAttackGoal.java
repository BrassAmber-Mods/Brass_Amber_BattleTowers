package com.BrassAmber.ba_bt.entity.ai.goal.skygolem;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.world.World;

public class SkyGolemFireballAttackGoal extends GolemFireballAttackGoal {

	public SkyGolemFireballAttackGoal(BTGolemEntityAbstract golem) {
		super(golem);
	}
	
	@Override
	protected DamagingProjectileEntity createFireBall(World world, double xPower, double yPower, double zPower) {
		DragonFireballEntity fireballentity = new DragonFireballEntity(world, this.golem, xPower, yPower, zPower);
		
		return fireballentity;
	}

}
