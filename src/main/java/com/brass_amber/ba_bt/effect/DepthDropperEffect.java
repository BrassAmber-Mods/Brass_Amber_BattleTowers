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
    public void applyEffectTick(LivingEntity entity, int amplifier) {

        if (entity.isInWater()) {

            Vec3 motion = entity.getDeltaMovement();
            boolean jumping = entity.jumping;
            entity.setOnGround(entity.onGround() || entity.verticalCollision);

            if (entity.isSwimming() && entity instanceof Player) {
                entity.setSwimming(false);
            }

            if (!(entity instanceof Player)) {
                motion = motion.add(0, -0.01f, 0);
                entity.setDeltaMovement(motion);

            } else if (jumping && entity.onGround()) {
                motion = motion.add(0, .5f, 0);
                entity.setOnGround(false);
            } else {
                motion = motion.add(0, -0.05f, 0);
            }

            float multiplier = 1.3f;
            if (motion.multiply(1, 0, 1)
                    .length() < 0.145f && (entity.zza > 0 || entity.xxa != 0) && !entity.isShiftKeyDown()) {
                motion = motion.multiply(multiplier, 1, multiplier);
            }

            entity.setDeltaMovement(motion);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
