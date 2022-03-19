package com.BrassAmber.ba_bt.block.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;


import javax.annotation.Nullable;

public class BTSpawnerBlock extends SpawnerBlock implements EntityBlock {
    private Boolean foundChest = false;
    public BlockPos chestTileEntityPos;

    public BTSpawnerBlock(BlockBehaviour.Properties p_56781_) {
        super(p_56781_);
    }

    public BlockEntity newBlockEntity(BlockPos p_154687_, BlockState p_154688_) {
        return new BTSpawnerBlockEntity(p_154687_, p_154688_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState p_154684_, BlockEntityType<T> p_154685_) {
        return createTickerHelper(p_154685_, BTBlockEntityTypes.BT_MOB_SPAWNER.get(), level.isClientSide ? BTSpawnerBlockEntity::clientTick : BTSpawnerBlockEntity::serverTick);
    }


    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, int fortune, int silktouch) {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }
}
