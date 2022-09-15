package com.BrassAmber.ba_bt.entity.ai.goal.oceangolem;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTOceanGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.BrassAmber.ba_bt.util.BTUtil.distanceTo3D;
import static java.lang.Math.abs;

public class DashAttackGoal extends Goal {
    private final BTOceanGolem mob;
    @Nullable
    private LivingEntity target;
    private BlockPos wantedPos;
    private final double speedModifier;
    private final int range;

    public DashAttackGoal(BTOceanGolem golem, int range) {
        this.mob = golem;
        this.speedModifier = 3.0D;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return this.mob.dashFlag;
    }

    public boolean canContinueToUse() {
        this.target = this.mob.getTarget();

        if (this.target == null) {
            return false;
        } else if (!this.target.isAlive()) {
            return false;
        }

        double currentX = this.mob.getX();
        double currentZ = this.mob.getZ();

        return currentX == wantedPos.getX() && currentZ == wantedPos.getZ();
    }

    public void stop() {
        this.target = null;
    }

    public void start() {
        this.target = this.mob.getTarget();
        int wantedDistance = this.range;
        this.wantedPos = this.getNewPos(wantedDistance);

        while (!this.mob.level.isWaterAt(this.wantedPos)) {
            wantedDistance --;
            this.wantedPos = this.getNewPos(wantedDistance);
        }

        this.mob.getNavigation().moveTo(this.wantedPos.getX(), this.wantedPos.getY(), this.wantedPos.getZ(), this.speedModifier);
    }
    
    public BlockPos getNewPos(int distance) {
        BlockPos targetPos = this.target.blockPosition();

        double ratio = distanceTo3D(this.mob, this.target) / distance;

        double xVal = (targetPos.getX() - this.mob.getBlockX()) * ratio;
        double yVal = (targetPos.getY() - this.mob.getBlockY()) * ratio;
        double zVal = (targetPos.getZ() - this.mob.getBlockZ()) * ratio;

        this.wantedPos = this.mob.blockPosition().offset(xVal, yVal, zVal);

        return this.wantedPos;
    }
}
