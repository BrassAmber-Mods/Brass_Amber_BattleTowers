package com.brass_amber.ba_bt.block.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.brass_amber.ba_bt.block.block.ActiveCorriteBlock.FALL_SPEED;
import static com.brass_amber.ba_bt.block.block.ActiveCorriteBlock.LAVA_DAMAGE;

public class CoreMatterBlock extends Block {
    public CoreMatterBlock(Properties properties) {
        super(properties);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        entity.makeStuckInBlock(blockState, new Vec3(0.25D, FALL_SPEED * 2, 0.25D));
        entity.hurt(level.damageSources().lava(), LAVA_DAMAGE);
    }

}
