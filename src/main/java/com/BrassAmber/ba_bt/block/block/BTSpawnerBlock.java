package com.BrassAmber.ba_bt.block.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;


import javax.annotation.Nullable;

public class BTSpawnerBlock extends SpawnerBlock {
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
        return createTickerHelper(p_154685_, BTBlockEntityTypes.BT_MOB_SPAWNER, level.isClientSide ? BTSpawnerBlockEntity::clientTick : BTSpawnerBlockEntity::serverTick);
    }


    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, int fortune, int silktouch) {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }

    public void checkPos(Level world, BlockPos pos) {
        BlockEntity posEntity = world.getBlockEntity(pos);
        if (posEntity != null && posEntity.getType() == BTBlockEntityTypes.LAND_CHEST) {
            this.chestTileEntityPos = pos;
        }
    }


    public void playerDestroy(@NotNull Level level, Player player, @NotNull BlockPos spawnerPos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack itemStack) {
        super.playerDestroy(level, player, spawnerPos, state,blockEntity, itemStack);
        this.foundChest = false;
        this.chestTileEntityPos = null;
        if (!level.isClientSide()) {
            for (int x = -30; x<31; x++) {
                if (this.foundChest) {
                    break;
                }
                for (int z = -30; z<31; z++) {
                    if (this.foundChest) {
                        break;
                    }
                    BlockPos newBlockPos = new BlockPos(spawnerPos.getX() + x, spawnerPos.getY(), spawnerPos.getZ() + z);
                    checkPos(level, newBlockPos);
                    checkPos(level, newBlockPos.below());
                    checkPos(level, newBlockPos.above());
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
                        checkPos(level, newBlockPos);
                        checkPos(level, newBlockPos.below(1));
                        checkPos(level, newBlockPos.below(2));
                        checkPos(level, newBlockPos.below(3));
                        checkPos(level, newBlockPos.below(4));
                        checkPos(level, newBlockPos.below(5));
                        checkPos(level, newBlockPos.below(6));
                        checkPos(level, newBlockPos.below(7));
                        if (this.chestTileEntityPos != null) {
                            this.foundChest = true;
                        }
                    }
                }
            }
            try {
                TowerChestBlockEntity entity = (TowerChestBlockEntity) level.getBlockEntity(this.chestTileEntityPos);
                BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG,"Chest " + entity);
                entity.spawnerDestroyed();

            } catch (Exception e) {

            }

        }
    }
}
