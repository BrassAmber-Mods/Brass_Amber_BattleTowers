package com.BrassAmber.ba_bt.block.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BTSpawner extends ContainerBlock {
    private StoneChestBlock chest;
    private boolean foundChest = false;

    public BTSpawner(AbstractBlock.Properties p_i48364_1_) {
        super(p_i48364_1_);
    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new MobSpawnerTileEntity();
    }

    public void spawnAfterBreak(BlockState p_220062_1_, ServerWorld p_220062_2_, BlockPos p_220062_3_, ItemStack p_220062_4_) {
        super.spawnAfterBreak(p_220062_1_, p_220062_2_, p_220062_3_, p_220062_4_);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld world, BlockPos position, Random p_225534_4_) {
        if (!foundChest) {
            findChest(position);
        }
    }

    private void findChest(BlockPos spawnerPos) {

    }

    public void playerDestroy(World p_180657_1_, PlayerEntity p_180657_2_, BlockPos p_180657_3_, BlockState p_180657_4_, @Nullable TileEntity p_180657_5_, ItemStack p_180657_6_) {
        p_180657_2_.awardStat(Stats.BLOCK_MINED.get(this));
        p_180657_2_.causeFoodExhaustion(0.005F);
        dropResources(p_180657_4_, p_180657_1_, p_180657_3_, p_180657_5_, p_180657_2_, p_180657_6_);
    }

    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    public ItemStack getCloneItemStack(IBlockReader p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return ItemStack.EMPTY;
    }
}
