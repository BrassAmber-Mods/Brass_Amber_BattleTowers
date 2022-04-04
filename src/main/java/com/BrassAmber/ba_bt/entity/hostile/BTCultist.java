package com.BrassAmber.ba_bt.entity.hostile;


import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BTCultist extends AbstractSkeleton {

    public BTCultist(EntityType<? extends AbstractSkeleton> entityType, Level levelIn) {
        super(entityType, levelIn);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33579_) {
        return SoundEvents.PILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_VILLAGER_STEP;
    }

    protected void dropCustomDeathLoot(DamageSource p_33574_, int p_33575_, boolean p_33576_) {
        super.dropCustomDeathLoot(p_33574_, p_33575_, p_33576_);
        this.spawnAtLocation(Items.SKELETON_SKULL);
        }


}


