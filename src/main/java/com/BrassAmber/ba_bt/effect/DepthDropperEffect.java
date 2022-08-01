package com.BrassAmber.ba_bt.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DepthDropperEffect extends MobEffect {
    public DepthDropperEffect(MobEffectCategory effectCategory, int i) {
        super(effectCategory, i);
    }

    @Override
    public boolean isInstantenous() {
        return super.isInstantenous();
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWater()) {
            Vec3 initVector = entity.getDeltaMovement();
            Vec3 newVector;
            if (initVector.y() > -.35) {
                newVector = initVector.add(0, -.08, 0);
                entity.setDeltaMovement(newVector);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int j = 40 >> amplifier;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }

}
