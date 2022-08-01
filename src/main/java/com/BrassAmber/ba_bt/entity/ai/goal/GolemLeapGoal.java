package com.BrassAmber.ba_bt.entity.ai.goal;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.util.BTUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static java.lang.Math.abs;

public class GolemLeapGoal extends Goal {
    private final BTAbstractGolem golem;
    private LivingEntity target;
    private final float minleap;
    private final float maxleap;
    private final float maxJump;

    private double target_start_y;
    private double target_start_x;
    private double target_start_z;

    protected boolean jumpingInProgress = false;

    protected static final int WARMUP_TICKS = 40;

    private int warmup = WARMUP_TICKS;

    public GolemLeapGoal(BTAbstractGolem golemIn, float maxJumpHeight, float minimumLeap, float maximumLeap) {
        this.golem = golemIn;
        this.maxJump = maxJumpHeight;
        this.minleap = minimumLeap;
        this.maxleap = maximumLeap;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }


    public boolean canUse() {
        if (this.golem.isVehicle()) {
            return false;
        }
        if (this.golem.getTarget() != null && !this.golem.isDormant() && this.golem.isOnGround()) {
            if (this.target == null || !this.golem.isOnGround()) {
                return false;
            }

            this.target = this.golem.getTarget();
            double d0 = BTUtil.distanceTo2D(this.golem, this.target);
            double d1 = abs(this.target.getY() - this.golem.getY());
            boolean horizontal = this.minleap < d0 && (d0 <= (this.maxleap * 2)) && (0 < d1) && (d1 < (this.maxJump * 2));
            boolean vertical = this.minleap < d1 && d1 < this.maxJump;

            if (horizontal || vertical) {
                this.warmup--;
                return this.warmup <= 0;
            } else {
                return false;
            }

        }

        return false;
    }

    public boolean canContinueToUse() {return !this.golem.isOnGround() && this.golem.isAwake();}

    public void start() {
        Vec3 vec3 = this.golem.getDeltaMovement();

        double y = this.target.getY() - this.golem.getY();

        y = y < this.minleap ? this.minleap : y;

        Vec3 vec31 = new Vec3(this.target.getX() - this.golem.getX(), y, this.target.getZ() - this.golem.getZ());
        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.8D).add(vec3.scale(0.2D));
        }

        this.golem.setDeltaMovement(vec31.x, vec31.y, vec31.z);
    }

    @Override
    public void stop() {
        super.stop();
        this.warmup = WARMUP_TICKS;
    }
}