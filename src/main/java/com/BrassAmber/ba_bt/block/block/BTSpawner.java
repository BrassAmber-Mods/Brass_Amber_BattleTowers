package com.BrassAmber.ba_bt.block.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.BTBlocks;
import com.BrassAmber.ba_bt.block.tileentity.BTMobSpawnerTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.StoneChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.Random;

public class BTSpawner extends SpawnerBlock {
    private BTMobSpawnerTileEntity mobSpawnerEntity;


    public BTSpawner(AbstractBlock.Properties p_i48364_1_) {
        super(p_i48364_1_);


    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        this.mobSpawnerEntity = new BTMobSpawnerTileEntity();
        return this.mobSpawnerEntity;
    }

    public void destroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        try {
            StoneChestTileEntity entity = this.mobSpawnerEntity.chestTileEntity;
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG,"Chest " + entity);
            entity.spawnerDestroyed();


            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockState.is(BTBlocks.BT_SPAWNER));

        } catch (Exception e) {

        }

    }

}
