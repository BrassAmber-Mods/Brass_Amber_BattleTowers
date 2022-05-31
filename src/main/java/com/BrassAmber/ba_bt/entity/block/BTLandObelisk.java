package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BTLandObelisk extends BTAbstractObelisk {
    public BTLandObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BTLandObelisk(Level level) {
        super(GolemType.LAND, level);
    }

    @Override
    public void extraCheck(BlockPos toUpdate, Level level) {
        // Update Sand
        if (level.getBlockState(toUpdate) == Blocks.SAND.defaultBlockState()) {
            level.removeBlock(toUpdate, false);
            BrassAmberBattleTowers.LOGGER.info("Sand? :" + level.getBlockState(toUpdate));
            level.setBlockAndUpdate(toUpdate, Blocks.SAND.defaultBlockState());
        }
    }
}
