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

import javax.annotation.Nullable;

public class BTSpawnerBlockEntity extends BlockEntity {

    private final BTBaseSpawner spawner = new BTBaseSpawner() {
        public void broadcastEvent(Level p_155767_, BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, Blocks.SPAWNER, p_155769_, 0);
        }

        public void setNextSpawnData(@Nullable Level p_155771_, BlockPos p_155772_, SpawnData p_155773_) {
            super.setNextSpawnData(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState blockstate = p_155771_.getBlockState(p_155772_);
                p_155771_.sendBlockUpdated(p_155772_, blockstate, blockstate, 4);
            }

        }
        @javax.annotation.Nullable
        public net.minecraft.world.level.block.entity.BlockEntity getSpawnerBlockEntity(){ return BTSpawnerBlockEntity.this; }
    };

    public BTSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BTBlockEntityTypes.BT_MOB_SPAWNER, blockPos, blockState);
    }

    public void load(CompoundTag p_155760_) {
        super.load(p_155760_);
        this.spawner.load(this.level, this.worldPosition, p_155760_);
    }

    protected void saveAdditional(CompoundTag p_187521_) {
        super.saveAdditional(p_187521_);
        this.spawner.save(p_187521_);
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

    public boolean triggerEvent(int p_59797_, int p_59798_) {
        return this.spawner.onEventTriggered(this.level, p_59797_) ? true : super.triggerEvent(p_59797_, p_59798_);
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}
