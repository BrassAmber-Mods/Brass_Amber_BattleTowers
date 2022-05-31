package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.WaterFluid;

import java.util.List;

import static com.BrassAmber.ba_bt.util.BTUtil.*;

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
        double towerRange = 15.5D;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        List<Block> avoidBlocks = towerBlocks.get(GolemType.getNumForType(GolemType.OCEAN));

        for (int x = this.getBlockX() - 80; x <= this.getBlockX() + 80; x++) {
            for (int z = this.getBlockZ() - 80; z < this.getBlockZ() + 80; z++) {
                for (int y = this.getBlockY() - 55; y < this.getBlockY() - 22; y++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    if (horizontalDistanceTo(this, blockpos$mutableblockpos) > towerRange
                            && !this.level.isWaterAt(blockpos$mutableblockpos)
                            && !(avoidBlocks.contains(this.level.getBlockState(blockpos$mutableblockpos).getBlock()))) {
                        double distance3d = distanceTo3D(this, blockpos$mutableblockpos);
                        if ( distance3d < 60.5) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);
                        } else if (distance3d < 61.5) {
                            if (random.nextInt(50) > 40) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.SEAGRASS.defaultBlockState(), 3);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);
                            }
                        } else if (distance3d < 62.5){
                            if (random.nextInt(50) > 30) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 3);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 3);
                            }
                        }else if (distance3d < 63.5){

                            this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        int startX;
        int endX;
        // BrassAmberBattleTowers.LOGGER.info("X start: " + startX + " end: " + endX);

        int startZ;
        int endZ;
        Direction trenchDirection = random.nextInt(2) == 0 ? Direction.WEST : Direction.NORTH;
        int trenchLength = 32 + random.nextInt(24);
        BlockPos bbCenter = this.blockPosition().below(88);

        if (trenchDirection == Direction.WEST) {
            startX = bbCenter.getX() - trenchLength;
            endX = bbCenter.getX() + trenchLength;
            startZ = bbCenter.getZ() - 20;
            endZ = bbCenter.getZ() + 20;
        }
        else {
            startX = bbCenter.getX() - 20;
            endX = bbCenter.getX() + 20;
            startZ = bbCenter.getZ()  - trenchLength;
            endZ = bbCenter.getZ() + trenchLength;
        }

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                for (int y = bbCenter.getY(); y < bbCenter.getY() + 66; y++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    double horizontalDistance = horizontalDistanceTo(bbCenter, blockpos$mutableblockpos);
                    if (horizontalDistance > towerRange
                            && !this.level.isWaterAt(blockpos$mutableblockpos)
                            && !(avoidBlocks.contains(this.level.getBlockState(blockpos$mutableblockpos).getBlock()))) {

                        int isWall = this.checkWall(startX, endX, startZ, endZ, blockpos$mutableblockpos);

                        if (isWall == 0) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);
                        } else if (isWall == 2) {
                            if (random.nextInt(50) > 30) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            } else {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 3);
                            }
                        } else if (isWall == 1) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.STONE.defaultBlockState(), 2);
                        }
                        BrassAmberBattleTowers.LOGGER.info("Trench BlockPos: "+ blockpos$mutableblockpos);
                    }
                }
            }
        }



    }

    public int checkWall(double startX, double endX, double startZ, double endZ, BlockPos toCheck) {
        if (
            toCheck.getX() == startX + 1 || toCheck.getX() == endX - 1
                || toCheck.getZ() == startZ + 1 || toCheck.getZ() == endZ - 1
        ) {
            return 2;
        } else if (
            toCheck.getX() == startX || toCheck.getX() == endX
                || toCheck.getZ() == startZ || toCheck.getZ() == endZ
        ) {
            return 1;
        } else {
            return 0;
        }
    }
}
