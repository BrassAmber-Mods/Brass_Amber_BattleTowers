package com.brass_amber.ba_bt.block.block;

import com.brass_amber.ba_bt.block.blockentity.spawner.*;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
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

public class TowerSpawnerBlock extends SpawnerBlock implements EntityBlock {

    public TowerSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

        if (this == BTBlocks.LAND_SPAWNER.get()) {
            return new LandSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.OCEAN_SPAWNER.get()) {
            return new OceanSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.CORE_SPAWNER.get()) {
            return new CoreSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.NETHER_SPAWNER.get()) {
            return new NetherSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.END_SPAWNER.get()) {
            return new EndSpawnerEntity(blockPos, blockState);

        } else if (this == BTBlocks.SKY_SPAWNER.get()) {
            return new SkySpawnerEntity(blockPos, blockState);
        } else {
            return new LandSpawnerEntity(blockPos, blockState);

        }

    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (this == BTBlocks.LAND_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityType.LAND_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.OCEAN_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityType.OCEAN_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.CORE_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityType.CORE_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.NETHER_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityType.NETHER_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.END_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityType.END_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        } else if (this == BTBlocks.SKY_SPAWNER.get()) {
            return createTickerHelper(blockEntityType, BTBlockEntityType.SKY_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        } else {
            return createTickerHelper(blockEntityType, BTBlockEntityType.LAND_MOB_SPAWNER.get(), level.isClientSide ? AbstractSpawnerBlockEntity::clientTick : AbstractSpawnerBlockEntity::serverTick);

        }

    }

    @Override
    public int getExpDrop(BlockState state, LevelReader world, RandomSource randomSource, BlockPos pos, int fortune, int silktouch) {
        Random random = new Random();
        return 15 + random.nextInt(15) + random.nextInt(15);
    }

}
