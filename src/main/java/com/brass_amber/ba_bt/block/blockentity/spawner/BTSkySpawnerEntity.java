package com.brass_amber.ba_bt.block.blockentity.spawner;

import com.brass_amber.ba_bt.init.BTBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BTSkySpawnerEntity extends BTAbstractSpawnerBlockEntity {

    public BTSkySpawnerEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState, BTBlockEntityType.BT_SKY_MOB_SPAWNER.get());
    }
}
