package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.BrassAmber.ba_bt.util.BTUtil.*;
import static net.minecraft.world.level.block.SeaPickleBlock.PICKLES;

public class BTOceanObelisk extends BTAbstractObelisk {
    public BTOceanObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);

    }

    public BTOceanObelisk(Level level) {
        super(GolemType.OCEAN, level);
    }

    @Override
    public void initialize() {
        this.floorDistance = -11;
        this.carveOcean();
        super.initialize();
    }

    public void carveOcean() {
        double towerRange = 11.5D;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        List<Block> avoidBlocks = towerBlocks.get(GolemType.getNumForType(GolemType.OCEAN));
        List<BlockState> corals = List.of(
                Blocks.BRAIN_CORAL.defaultBlockState(), Blocks.BUBBLE_CORAL.defaultBlockState(),
                Blocks.FIRE_CORAL.defaultBlockState(), Blocks.HORN_CORAL.defaultBlockState(),
                Blocks.TUBE_CORAL.defaultBlockState());

        int noise = 72 + ((random.nextInt(2) + 1) * 4);

        int westWall = this.getBlockX() - noise;
        int northWall = this.getBlockZ() - noise;
        int eastWall = this.getBlockX() + noise;
        int southWall = this.getBlockZ() + noise;
        int top = this.getBlockY() - 3;
        int bottom = this.getBlockY() - 91;
        double wallDistance = noise -.5;

        for (int y = top; y >= bottom; y--) {
            if ((top - y) % 10 == 0) {
                wallDistance -= 4;
            }

            for (int x = westWall; x <= eastWall; x++) {
                for (int z = northWall; z < southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    double distance3d = distanceTo3D(this.blockPosition().below(38), blockpos$mutableblockpos);
                    double distance2d = horizontalDistanceTo(this, blockpos$mutableblockpos);
                    if  (this.level.getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.KELP_PLANT) {
                        this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);

                    } else if (distance2d > towerRange && !this.level.isWaterAt(blockpos$mutableblockpos)
                            && !(avoidBlocks.contains(this.level.getBlockState(blockpos$mutableblockpos).getBlock()))){
                        if (y > this.getBlockY() - 60) {
                            if (distance2d < wallDistance - 2) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                            } else if (distance2d < wallDistance - 1) {
                                if (random.nextInt(50) > 30) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                } else {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                }
                            } else if (distance2d < wallDistance) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            }
                        }
                        else {
                            if (distance3d < 52.5) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                            } else if (distance3d < 53.5) {
                                int vegetation = random.nextInt(75);
                                if (vegetation > 55) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.SEAGRASS.defaultBlockState(), 2);
                                } else if (vegetation > 40) {
                                    this.level.setBlock(blockpos$mutableblockpos, corals.get(random.nextInt(5)), 2);
                                } else {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);
                                }
                            } else if (distance3d < 54.5) {
                                if (random.nextInt(50) > 30) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                } else {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                }
                            } else if (distance3d < 55.5) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            }
                        }

                    }
                }
            }
            if (y > this.getBlockY() - 60) {
                doNoOutputCommand(this, "/kill @e[type=item,distance=120]");
            }
        }
    }
}
