package com.brass_amber.ba_bt.block.blockentity.spawner;

import com.brass_amber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BTEndSpawnerEntity extends BTAbstractSpawnerBlockEntity {

    public BTEndSpawnerEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState, BTBlockEntityTypes.BT_END_MOB_SPAWNER.get());
    }
}
