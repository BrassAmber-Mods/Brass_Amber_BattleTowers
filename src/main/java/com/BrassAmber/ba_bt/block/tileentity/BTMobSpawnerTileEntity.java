package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.BTBlocks;
import com.BrassAmber.ba_bt.block.BTTileEntityTypes;
import com.BrassAmber.ba_bt.block.block.StoneChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

public class BTMobSpawnerTileEntity extends TileEntity implements ITickableTileEntity {


    private final AbstractSpawner spawner = new AbstractSpawner() {
        public void broadcastEvent(int p_98267_1_) {
            BTMobSpawnerTileEntity.this.level.blockEvent(BTMobSpawnerTileEntity.this.worldPosition, BTBlocks.BT_SPAWNER, p_98267_1_, 0);
        }

        public World getLevel() {
            return BTMobSpawnerTileEntity.this.level;
        }

        public BlockPos getPos() {
            return BTMobSpawnerTileEntity.this.worldPosition;
        }

        public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
            super.setNextSpawnData(p_184993_1_);
            if (this.getLevel() != null) {
                BlockState blockstate = this.getLevel().getBlockState(this.getPos());
                this.getLevel().sendBlockUpdated(BTMobSpawnerTileEntity.this.worldPosition, blockstate, blockstate, 4);
            }

        }
    };

    public BTMobSpawnerTileEntity() {
        super(BTTileEntityTypes.BT_MOB_SPAWNER);
    }

    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        this.spawner.load(p_230337_2_);
    }

    public CompoundNBT save(CompoundNBT p_189515_1_) {
        super.save(p_189515_1_);
        this.spawner.save(p_189515_1_);
        return p_189515_1_;
    }

    public void tick() {
        this.spawner.tick();
    }

        @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundnbt = this.save(new CompoundNBT());
        compoundnbt.remove("SpawnPotentials");
        return compoundnbt;
    }

    public boolean triggerEvent(int p_145842_1_, int p_145842_2_) {
        return this.spawner.onEventTriggered(p_145842_1_) ? true : super.triggerEvent(p_145842_1_, p_145842_2_);
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public AbstractSpawner getSpawner() {
        return this.spawner;
    }
}
