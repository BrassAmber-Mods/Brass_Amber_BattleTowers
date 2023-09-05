package com.brass_amber.ba_bt.block.block;

import com.brass_amber.ba_bt.block.blockentity.spawner.*;
import com.brass_amber.ba_bt.init.BTBlockEntityTypes;
import com.brass_amber.ba_bt.init.BTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;


import javax.annotation.Nullable;
import java.util.Random;

public class BTSpawnerBlock extends SpawnerBlock implements EntityBlock {

    public BTSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

        if (this == BTBlocks.BT_LAND_SPAWNER.get()) {
            return new BTLandSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.BT_OCEAN_SPAWNER.get()) {
            return new BTOceanSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.BT_CORE_SPAWNER.get()) {
            return new BTCoreSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.BT_NETHER_SPAWNER.get()) {
            return new BTNetherSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.BT_END_SPAWNER.get()) {
            return new BTEndSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.BT_SKY_SPAWNER.get()) {
            return new BTSkySpawnerEntity(blockPos, blockState);
        } else {
            return new BTLandSpawnerEntity(blockPos, blockState);

        }

    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (this == BTBlocks.BT_LAND_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_LAND_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.BT_OCEAN_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_OCEAN_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.BT_CORE_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_CORE_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.BT_NETHER_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_NETHER_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.BT_END_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_END_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.BT_SKY_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_SKY_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        } else {
            return createTickerHelper(blockEntityType, BTBlockEntityTypes.BT_LAND_MOB_SPAWNER.get(), level.isClientSide ? BTAbstractSpawnerBlockEntity::clientTick : BTAbstractSpawnerBlockEntity::serverTick);

        }

    }

    @Override
    public int getExpDrop(BlockState state, LevelReader world, RandomSource randomSource, BlockPos pos, int fortune, int silktouch) {
        Random random = new Random();
        return 15 + random.nextInt(15) + random.nextInt(15);
    }

}
