package com.brass_amber.ba_bt.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DepthDropperEffect extends MobEffect {
    public DepthDropperEffect(MobEffectCategory effectCategory, int i) {
        super(effectCategory, i);
    }

    @Override
    public boolean isInstantenous() {
        return super.isInstantenous();
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity p_19462_, @Nullable Entity p_19463_, LivingEntity p_19464_, int p_19465_, double p_19466_) {
        super.applyInstantenousEffect(p_19462_, p_19463_, p_19464_, p_19465_, p_19466_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWater()) {
            Vec3 initVector = entity.getDeltaMovement();
            Vec3 newVector;
            if (initVector.y() > -.45) {
                newVector = initVector.add(0, -.12, 0);
                entity.setDeltaMovement(newVector);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int j = 15 >> amplifier;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }

}
