package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
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
        super.initialize();
        this.carveOcean();
    }

    public void carveOcean() {
        double towerRange = 11.5D;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        List<Block> avoidBlocks = towerBlocks.get(GolemType.getNumForType(GolemType.OCEAN));
        List<BlockState> corals = List.of(
                Blocks.BRAIN_CORAL.defaultBlockState(), Blocks.BUBBLE_CORAL.defaultBlockState(),
                Blocks.FIRE_CORAL.defaultBlockState(), Blocks.HORN_CORAL.defaultBlockState(),
                Blocks.TUBE_CORAL.defaultBlockState());

        for (int y = this.getBlockY() - 55; y < this.getBlockY()-2; y++) {
            for (int x = this.getBlockX() - 80; x <= this.getBlockX() + 80; x++) {
                for (int z = this.getBlockZ() - 80; z < this.getBlockZ() + 80; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    if (horizontalDistanceTo(this, blockpos$mutableblockpos) > towerRange
                            && !this.level.isWaterAt(blockpos$mutableblockpos)
                            && !(avoidBlocks.contains(this.level.getBlockState(blockpos$mutableblockpos).getBlock()))) {
                        double distance3d = distanceTo3D(this, blockpos$mutableblockpos);
                        if ( distance3d < 60.5) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                        } else if (distance3d < 61.5) {
                            int vegetation = random.nextInt(75);
                            if (vegetation > 55) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.SEAGRASS.defaultBlockState(), 2);
                            } else if (vegetation > 40) {
                                this.level.setBlock(blockpos$mutableblockpos, corals.get(random.nextInt(5)), 2);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                            }
                        } else if (distance3d < 62.5){
                            if (random.nextInt(50) > 30) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                            }
                        }else if (distance3d < 63.5){

                            this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                        }
                    }
                }
            }
            doNoOutputPostionedCommand(this, "/kill @e[type=item,distance=160]", this.position());
        }
        int[] noise = new int[4];
        noise[0] = random.nextInt(8);
        noise[1] = random.nextInt(8);
        noise[2] = random.nextInt(8);
        noise[3] = random.nextInt(8);

        for (int y = this.getBlockY() - 44; y >= this.getBlockY() - 88; y--) {
            for (int x = this.getBlockX() - 32 - noise[0]; x <= this.getBlockX() + 32 + - noise[1]; x++) {
                for (int z = this.getBlockZ() - 32- noise[2]; z < this.getBlockZ() + 32 + - noise[3]; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    if (horizontalDistanceTo(this, blockpos$mutableblockpos) > towerRange
                            && !this.level.isWaterAt(blockpos$mutableblockpos)
                            && !(avoidBlocks.contains(this.level.getBlockState(blockpos$mutableblockpos).getBlock()))
                            && !this.level.getBlockState(blockpos$mutableblockpos).isAir()
                    ) {
                        double distance3d = distanceTo3D(this.blockPosition().below(65), blockpos$mutableblockpos);
                        if (distance3d < 30.5 || (y > this.getBlockY() - 65 && horizontalDistanceTo(this, blockpos$mutableblockpos) < 30.5)) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);
                        } else if (distance3d < 32.5) {
                            int vegetation = random.nextInt(75);
                            if (vegetation > 55) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.SEAGRASS.defaultBlockState(), 2);
                            } else if (vegetation > 40) {
                                this.level.setBlock(blockpos$mutableblockpos,
                                        Blocks.SEA_PICKLE.defaultBlockState().setValue(PICKLES, random.nextInt(4) + 1), 2);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);
                            }
                        } else if (distance3d < 33.5){
                            if (random.nextInt(50) > 30) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                            }
                        }   else if (distance3d < 34.5){

                            this.level.setBlock(blockpos$mutableblockpos, Blocks.STONE.defaultBlockState(), 2);
                        }
                    }
                }
                doNoOutputPostionedCommand(this, "/kill @e[type=item,distance=160]", this.position());
            }
        }

        doNoOutputPostionedCommand(this, "/kill @e[type=item,distance=160]", this.position());

    }

    public int checkPos(double trenchLength, BlockPos start, BlockPos toCheck) {
        double horizontal = distanceTo2D(toCheck.getX(), toCheck.getZ());
        double distance3d = distanceTo3D(start, toCheck);
        double useDistance;
        if (toCheck.getY() > start.getY() + 33) {
            useDistance = horizontal;
        } else {
            useDistance = distance3d;
        }

        if (useDistance < trenchLength - 1) {
            return 0;
        } else if (useDistance < trenchLength) {
            return 1;
        } else if (useDistance < trenchLength + 1) {
            return 2;
        } else {
            return 3;
        }
    }
}
