package com.BrassAmber.ba_bt.block.blockentity.spawner;

import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BTOceanSpawnerEntity extends BTAbstractSpawnerBlockEntity {

    public BTOceanSpawnerEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState, BTBlockEntityTypes.BT_OCEAN_MOB_SPAWNER.get());
    }
}
