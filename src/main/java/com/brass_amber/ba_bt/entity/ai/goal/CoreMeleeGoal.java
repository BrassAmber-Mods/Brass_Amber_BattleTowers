package com.brass_amber.ba_bt.entity.ai.goal;

import com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import static com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem.MELEE_DURATION_TICKS;

public class CoreMeleeGoal extends MeleeAttackGoal {
    protected final CoreGolem golem;
    private int attackDelay = MELEE_DURATION_TICKS / 2;
    private int ticksUntilNextAttack = MELEE_DURATION_TICKS / 2;
    private boolean shouldCountTillNextAttack = false;

    public CoreMeleeGoal(CoreGolem golem) {
        super(golem, 1D, true);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return !this.golem.isDormant() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.golem.isDormant() && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.attackDelay =  MELEE_DURATION_TICKS / 2;
        this.ticksUntilNextAttack = MELEE_DURATION_TICKS / 2;
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distanceToSqr) {
        double reachSqr = this.getAttackReachSqr(enemy);
        if (distanceToSqr <= reachSqr && !this.golem.inAnimation(1)) {
            shouldCountTillNextAttack = true;

            if (isTimeToStartMeleeAnimation()) {
                this.golem.setMelee(true);
                this.golem.meleeAnimationTimeout = MELEE_DURATION_TICKS;
            }

            if (isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(enemy.getX(), enemy.getEyeY(), enemy.getZ());
                performAttack(enemy);
                this.golem.setMelee(false);
            }

        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            this.golem.setMelee(false);
        }

    }

    protected void performAttack(LivingEntity enemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(enemy);
    }

    private boolean isTimeToStartMeleeAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    @Override
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    @Override
    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    @Override
    public void stop() {
        this.golem.setMelee(false);
        super.stop();
    }
}
