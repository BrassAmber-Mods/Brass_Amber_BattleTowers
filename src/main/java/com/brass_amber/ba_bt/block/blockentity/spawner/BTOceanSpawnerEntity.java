package com.brass_amber.ba_bt.block.blockentity.spawner;

import com.brass_amber.ba_bt.init.BTBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BTOceanSpawnerEntity extends BTAbstractSpawnerBlockEntity {

    public BTOceanSpawnerEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState, BTBlockEntityType.BT_OCEAN_MOB_SPAWNER.get());
    }
}
