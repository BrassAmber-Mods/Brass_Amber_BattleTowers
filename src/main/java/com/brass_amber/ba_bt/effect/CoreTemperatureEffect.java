package com.brass_amber.ba_bt.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CoreTemperatureEffect extends MobEffect {
    protected CoreTemperatureEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(entity.level().damageSources().onFire(), 1);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
