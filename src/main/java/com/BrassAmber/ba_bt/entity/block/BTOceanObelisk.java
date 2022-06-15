package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import com.BrassAmber.ba_bt.sound.BTMusics;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.Music;
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

import static com.BrassAmber.ba_bt.util.BTStatics.towerBlocks;
import static com.BrassAmber.ba_bt.util.BTUtil.*;
import static net.minecraft.world.level.block.SeaPickleBlock.PICKLES;

public class BTOceanObelisk extends BTAbstractObelisk {
    public BTOceanObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.BOSS_MUSIC = BTMusics.OCEAN_GOLEM_FIGHT;
        this.TOWER_MUSIC = BTMusics.OCEAN_TOWER;
    }

    public BTOceanObelisk(Level level) {
        super(GolemType.OCEAN, level);
    }

    @Override
    public void initialize() {
        this.carveOcean();
        this.floorDistance = -11;
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

        int noise = 64 + ((random.nextInt(2) + 1) * 4);

        int westWall = this.getBlockX() - noise;
        int northWall = this.getBlockZ() - noise;
        int eastWall = this.getBlockX() + noise;
        int southWall = this.getBlockZ() + noise;
        int top = this.getBlockY() - 3;
        int bottom = this.getBlockY() - 91;
        double wallDistance = noise -.5;
        int nextStep = random.nextInt(4)+8;
        int distanceChange = random.nextInt(3)+2;
        boolean doVegetation= false;
        BlockPos blockAbove;

        for (int y = top; y >= bottom - 1; y--) {
            if (y == bottom + 33) {
                wallDistance -= 15;
            } else if ((top - y) % nextStep == 0) {
                wallDistance -= distanceChange;
                nextStep = random.nextInt(4)+8;
                distanceChange = random.nextInt(3)+2;
            }

            for (int x = westWall; x <= eastWall; x++) {
                for (int z = northWall; z < southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    blockAbove = blockpos$mutableblockpos.above();
                    double distance2d = horizontalDistanceTo(this, blockpos$mutableblockpos);
                    if (y != bottom - 1) {
                        if  (this.level.getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.KELP_PLANT) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);

                        } else if (distance2d > towerRange && !this.level.isWaterAt(blockpos$mutableblockpos)
                                && !(avoidBlocks.contains(this.level.getBlockState(blockpos$mutableblockpos).getBlock()))){
                            if (distance2d < wallDistance - 2) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                            } else if (distance2d < wallDistance - 1) {
                                if (random.nextInt(50) > 30) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                } else {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                }
                                doVegetation = true;
                            } else if (distance2d < wallDistance) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            }

                            if (doVegetation && !this.level.isWaterAt(blockpos$mutableblockpos)
                                    && this.level.isWaterAt(blockAbove) && y < bottom + 60 ) {
                                int vegetation = random.nextInt(75);
                                if (vegetation > 55) {
                                    this.level.setBlock(blockAbove, Blocks.SEAGRASS.defaultBlockState(), 2);
                                } else if (vegetation > 40) {
                                    this.level.setBlock(blockAbove, corals.get(random.nextInt(5)), 2);
                                }
                            }
                        }
                    }
                    else {
                        if (doVegetation && !this.level.isWaterAt(blockpos$mutableblockpos)
                                && this.level.isWaterAt(blockAbove) && y < bottom + 60 ) {
                            int vegetation = random.nextInt(75);
                            if (vegetation > 55) {
                                this.level.setBlock(blockAbove, Blocks.SEAGRASS.defaultBlockState(), 2);
                            } else if (vegetation > 40) {
                                this.level.setBlock(blockAbove, corals.get(random.nextInt(5)), 2);
                            }
                        }
                    }

                }
            }
            doNoOutputCommand(this, "/kill @e[type=item]");
        }
    }
}
