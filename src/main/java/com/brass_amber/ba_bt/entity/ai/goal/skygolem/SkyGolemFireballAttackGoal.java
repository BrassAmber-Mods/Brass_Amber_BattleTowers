package com.brass_amber.ba_bt.entity.ai.goal.skygolem;

import com.brass_amber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class SkyGolemFireballAttackGoal extends GolemFireballAttackGoal {

	public SkyGolemFireballAttackGoal(BTAbstractGolem golem) {
		super(golem);
	}
	
	@Override
	protected Projectile createFireBall(Level level, double xPower, double yPower, double zPower) {
		return new DragonFireball(level, this.golem, xPower, yPower, zPower);
	}

}
