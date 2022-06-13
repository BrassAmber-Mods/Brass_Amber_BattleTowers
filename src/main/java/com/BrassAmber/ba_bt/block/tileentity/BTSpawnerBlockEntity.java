package com.BrassAmber.ba_bt.block.tileentity;


import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BTSpawnerBlockEntity extends BlockEntity {

    private final BTBaseSpawner spawner = new BTBaseSpawner() {
        public void broadcastEvent(Level level, BlockPos blockPos, int p_155769_) {
            level.blockEvent(blockPos, Blocks.SPAWNER, p_155769_, 0);
        }

        public void setNextSpawnData(@Nullable Level level, BlockPos blockPos, SpawnData spawnData) {
            super.setNextSpawnData(level, blockPos, spawnData);
            if (level != null) {
                BlockState blockstate = level.getBlockState(blockPos);
                level.sendBlockUpdated(blockPos, blockstate, blockstate, 4);
            }

        }
        public net.minecraft.world.level.block.entity.@NotNull BlockEntity getSpawnerBlockEntity(){ return BTSpawnerBlockEntity.this; }
    };

    public BTSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BTBlockEntityTypes.BT_MOB_SPAWNER.get(), blockPos, blockState);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.spawner.load(this.level, this.worldPosition, tag);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.spawner.save(tag);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, BTSpawnerBlockEntity btSpawnerBlockEntity) {
        btSpawnerBlockEntity.spawner.clientTick(level, blockPos);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BTSpawnerBlockEntity btSpawnerBlockEntity) {
        btSpawnerBlockEntity.spawner.serverTick((ServerLevel) level, blockPos);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = this.saveWithoutMetadata();
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    public boolean triggerEvent(int i, int i1) {
        return this.spawner.onEventTriggered(this.level, i) || super.triggerEvent(i, i1);
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BTBaseSpawner getSpawner() {
        return this.spawner;
    }
}
