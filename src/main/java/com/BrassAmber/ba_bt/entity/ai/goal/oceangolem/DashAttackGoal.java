package com.BrassAmber.ba_bt.entity.ai.goal.oceangolem;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTOceanGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.BrassAmber.ba_bt.util.BTUtil.distanceTo2D;
import static com.BrassAmber.ba_bt.util.BTUtil.distanceTo3D;
import static java.lang.Math.abs;

public class DashAttackGoal extends Goal {
    private final BTOceanGolem mob;
    @Nullable
    private LivingEntity target;
    private BlockPos wantedPos;
    private final double speedModifier;
    private final int range;

    protected static final int WARMUP_TICKS = 60;

    private int warmup = WARMUP_TICKS;


    public DashAttackGoal(BTOceanGolem golem, int range) {
        this.mob = golem;
        this.speedModifier = .7D;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        this.target = this.mob.getTarget();

        if (this.target != null && this.target.isAlive()) {
            if (distanceTo2D(this.mob, this.target) > 10D) {
                this.warmup--;
                return this.warmup <= 0;
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        this.target = this.mob.getTarget();

        double currentX = this.mob.getX();
        double currentZ = this.mob.getZ();


        if (this.target != null && this.target.isAlive()) {
            return currentX != wantedPos.getX() && currentZ != wantedPos.getZ();
        }
        return false;
    }

    public void stop() {
        this.target = null;
        this.warmup = WARMUP_TICKS;
    }

    public void start() {
        this.target = this.mob.getTarget();
        int wantedDistance = this.range;
        this.wantedPos = this.getNewPos(wantedDistance);

        while (!this.mob.level().isWaterAt(this.wantedPos)) {
            wantedDistance --;
            this.wantedPos = this.getNewPos(wantedDistance);
        }

        this.mob.getNavigation().moveTo(this.wantedPos.getX(), this.wantedPos.getY(), this.wantedPos.getZ(), this.speedModifier);
    }
    
    public BlockPos getNewPos(int distance) {
        BlockPos targetPos = this.target.blockPosition();

        double ratio = distanceTo3D(this.mob, this.target) / distance;

        int xVal = (int) ((targetPos.getX() - this.mob.getBlockX()) * ratio);
        int yVal = (int) ((targetPos.getY() - this.mob.getBlockY()) * ratio);
        int zVal = (int) ((targetPos.getZ() - this.mob.getBlockZ()) * ratio);

        this.wantedPos = this.mob.blockPosition().offset(xVal, yVal, zVal);

        return this.wantedPos;
    }
}
