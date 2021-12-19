package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DestroyTower {

    private TowerSpecs specs;
    private BlockPos crumbleStart;
    private int crumbleBottom;
    private int crumbleSpeed;
    private World level;

    public DestroyTower(MonolithEntity monolith, BlockPos golemSpawn, World level) {
        this.specs = TowerSpecs.getTowerFromMonolith(monolith);
        this.crumbleStart = golemSpawn.above(6);
        this.crumbleSpeed = this.specs.getCrumbleSpeed();
        this.crumbleBottom = this.crumbleStart.getY() - this.specs.getHeight();
        this.level = level;
    }

    public void start() {
    }
}
