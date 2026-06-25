package com.brass_amber.ba_bt.entity;

import com.brass_amber.ba_bt.entity.ai.goal.CorreyeFollowOwnerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CorreyeEntity extends PathfinderMob {
    private Player player = null;

    public CorreyeEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    protected void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOwner() {
        return this.player;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CorreyeFollowOwnerGoal(this, 2.0f, 5.0f, 1.0f, true));
    }
}
