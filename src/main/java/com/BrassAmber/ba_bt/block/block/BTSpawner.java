package com.BrassAmber.ba_bt.block.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.BTBlocks;
import com.BrassAmber.ba_bt.block.BTTileEntityTypes;
import com.BrassAmber.ba_bt.block.tileentity.BTMobSpawnerTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.StoneChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.Random;

public class BTSpawner extends ContainerBlock {
    private BTMobSpawnerTileEntity mobSpawnerEntity;
    private Boolean foundChest = false;
    public BlockPos chestTileEntityPos;

    public BTSpawner(AbstractBlock.Properties p_i48364_1_) {
        super(p_i48364_1_);
    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        this.mobSpawnerEntity = new BTMobSpawnerTileEntity();
        return this.mobSpawnerEntity;
    }
    public void spawnAfterBreak(BlockState p_220062_1_, ServerWorld p_220062_2_, BlockPos p_220062_3_, ItemStack p_220062_4_) {
        super.spawnAfterBreak(p_220062_1_, p_220062_2_, p_220062_3_, p_220062_4_);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }

    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    public ItemStack getCloneItemStack(IBlockReader p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return ItemStack.EMPTY;
    }

    public void checkPos(World world, BlockPos pos) {
        TileEntity posEntity = world.getBlockEntity(pos);

        if (posEntity != null && posEntity.getType() == BTTileEntityTypes.STONE_CHEST) {
            this.chestTileEntityPos = pos;

        }
    }

    public void destroy(IWorld iWorld, BlockPos spawnerPos, BlockState blockState) {
        this.foundChest = false;
        this.chestTileEntityPos = null;
        World world = Minecraft.getInstance().getSingleplayerServer().overworld();
        for (int x = -30; x<31; x++) {
            if (this.foundChest) {
                break;
            }
            for (int z = -30; z<31; z++) {
                if (this.foundChest) {
                    break;
                }
                BlockPos newBlockPos = new BlockPos(spawnerPos.getX() + x, spawnerPos.getY(), spawnerPos.getZ() + z);
                checkPos(world, newBlockPos);
                checkPos(world, newBlockPos.below());
                checkPos(world, newBlockPos.above());
                if (this.chestTileEntityPos != null) {
                    this.foundChest = true;
                }
            }
        }
        if (this.chestTileEntityPos == null) {
            for (int x = -5; x<6; x++) {
                if (this.foundChest) {
                    break;
                }
                for (int z = -5; z < 6; z++) {
                    if (this.foundChest) {
                        break;
                    }
                    BlockPos newBlockPos = new BlockPos(spawnerPos.getX() + x, spawnerPos.getY(), spawnerPos.getZ() + z);
                    checkPos(world, newBlockPos);
                    checkPos(world, newBlockPos.below(1));
                    checkPos(world, newBlockPos.below(2));
                    checkPos(world, newBlockPos.below(3));
                    checkPos(world, newBlockPos.below(4));
                    checkPos(world, newBlockPos.below(5));
                    checkPos(world, newBlockPos.below(6));
                    checkPos(world, newBlockPos.below(7));
                    if (this.chestTileEntityPos != null) {
                        this.foundChest = true;
                    }
                }
            }
        }
        try {
            StoneChestTileEntity entity = (StoneChestTileEntity) world.getBlockEntity(this.chestTileEntityPos);
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG,"Chest " + entity);
            entity.spawnerDestroyed();

        } catch (Exception e) {

        }

    }

}
