package com.BrassAmber.ba_bt.entity.ai.goal;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GolemLeapGoal extends Goal {
    private final BTAbstractGolem golem;
    private LivingEntity target;
    private final float minleap;
    private final float maxleap;
    private final float maxJump;

    private int useChecked = 0;

    public GolemLeapGoal(BTAbstractGolem golemIn, float maxJumpHeight, float minimumLeap, float maximumLeap) {
        this.golem = golemIn;
        this.maxJump = maxJumpHeight;
        this.minleap = minimumLeap;
        this.maxleap = maximumLeap;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }


    public boolean canUse() {
        if (this.golem.isVehicle() || this.golem.isDormant() && !this.golem.isOnGround()) {
            return false;
        } else {
            if (this.useChecked == 50) {
                this.useChecked = 0;
                this.target = this.golem.getTarget();
                if (this.target == null) {
                    return false;
                } else {
                    double d0 = this.horizontalDistanceTo(this.target);
                    double d1 = this.target.getY() - this.golem.getY();
                    boolean horizontal = !(d0 < this.minleap) && !(d0 > this.maxleap) && (0 < d1) && (d1 < this.maxJump);
                    boolean vertical = this.minleap < d1 && d1 < this.maxJump;
                    if (horizontal || vertical) {
                        if (!this.golem.isOnGround()) {
                            return false;
                        } else {
                            return this.golem.getRandom().nextInt(reducedTickDelay(5)) == 0;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                this.useChecked += 1;
                return false;
            }
        }
    }

    public double horizontalDistanceTo (LivingEntity entity) {
        double xDistance = Math.abs(this.golem.getX() - entity.getX());
        double zDistance = Math.abs(this.golem.getZ() -  entity.getZ());

        return Math.sqrt((xDistance * xDistance) + (zDistance * zDistance));
    }

    public boolean canContinueToUse() {
        return !this.golem.isOnGround() && this.golem.isAwake();
    }

    public void start() {
        Vec3 vec3 = this.golem.getDeltaMovement();
        Vec3 vec31 = new Vec3(this.target.getX() - this.golem.getX(), (this.target.getY() + 6) - this.golem.getY(), this.target.getZ() - this.golem.getZ());
        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().add(vec3.scale(0.4D));
        }

        this.golem.setDeltaMovement(vec31.x, vec31.y, vec31.z);
    }
}