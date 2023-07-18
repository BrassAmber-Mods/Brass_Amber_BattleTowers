package com.BrassAmber.ba_bt.entity.ai.goal;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTLandGolem;
import com.BrassAmber.ba_bt.util.BTUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static java.lang.Math.abs;

public class GolemLeapGoal extends Goal {
    private final BTLandGolem golem;
    private LivingEntity target;
    private final float minleap;
    private final float maxleap;

    protected static final int WARMUP_TICKS = 40;

    private int warmup = WARMUP_TICKS;

    public GolemLeapGoal(BTLandGolem golemIn, float minimumLeap, float maximumLeap) {
        this.golem = golemIn;
        this.minleap = minimumLeap;
        this.maxleap = maximumLeap;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }


    public boolean canUse() {
        if (this.golem.isVehicle()) {
            return false;
        }
        if (this.golem.getTarget() != null && !this.golem.isDormant() && this.golem.onGround()) {
            if (this.target == null || !this.golem.onGround()) {
                return false;
            }

            this.target = this.golem.getTarget();
            double d0 = BTUtil.distanceTo2D(this.golem, this.target);
            boolean horizontal = this.minleap < d0 && (d0 <= (this.maxleap * 2));
            if (horizontal) {
                this.warmup--;
                return this.warmup <= 0;
            } else {
                return false;
            }

        }

        return false;
    }

    public boolean canContinueToUse() {return !this.golem.onGround() && this.golem.isAwake();}

    public void start() {
        Vec3 vec3 = this.golem.getDeltaMovement();

        double d1 = abs(this.target.getY() - this.golem.getY());
        if (d1 >= 4) {
            this.golem.bigLeap();
        }

        Vec3 vec31 = vec3.add(this.target.getX() - this.golem.getX(), 0, this.target.getZ() - this.golem.getZ());
        vec31 = vec31.normalize().scale(0.8D);

        this.golem.setDeltaMovement(vec31.x, vec31.y, vec31.z);
    }

    @Override
    public void stop() {
        this.warmup = WARMUP_TICKS;
    }
}