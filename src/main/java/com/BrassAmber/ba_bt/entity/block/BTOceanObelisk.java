package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.init.BTExtras;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.BTUtil;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.BrassAmber.ba_bt.util.BTStatics.towerBlocks;
import static com.BrassAmber.ba_bt.util.BTUtil.*;

public class BTOceanObelisk extends BTAbstractObelisk {

    private final List<Block> avoidBlocks = towerBlocks.get(GolemType.getNumForType(GolemType.OCEAN));
    private final List<BlockState> corals = List.of(Blocks.BRAIN_CORAL.defaultBlockState(),
            Blocks.BUBBLE_CORAL.defaultBlockState(), Blocks.FIRE_CORAL.defaultBlockState(),
            Blocks.HORN_CORAL.defaultBlockState(), Blocks.TUBE_CORAL.defaultBlockState());

    private int noise;
    private int top;
    private int bottom;
    private int carveLayer;
    private int currentCarveLayer;
    private double wallDistance;
    private int nextStep;
    private int distanceChange;
    private int lastDistanceChange = 0;
    private boolean doVegetation = false;

    public BTOceanObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.BOSS_MUSIC = null;
        this.TOWER_MUSIC = BTSoundEvents.OCEAN_TOWER_MUSIC;
        this.musicDistance = 45;
        this.towerEffect = BTExtras.DEPTH_DROPPER_EFFECT.get();
    }

    public BTOceanObelisk(Level level) {
        super(GolemType.OCEAN, level);
    }


    @Override
    public void initialize() {
        if (this.doServerInit){
            this.floorDistance = -11;
            this.noise = 60 + ((random.nextInt(2) + 1) * 4);
            this.top = this.getBlockY() - 3;
            this.bottom = this.getBlockY() - 92;
            this.carveLayer = (this.top - (this.bottom + 1)) / 8;
            this.currentCarveLayer = this.top;
            this.wallDistance = this.noise -.5;
            this.nextStep = random.nextInt(4)+8;
            this.distanceChange = random.nextInt(3);
        }
        this.carveOcean();
        super.initialize();
        doNoOutputCommand(this, "/kill @e[type=item]");
    }

    public void carveOcean() {
        double towerRange = 11.5D;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Block block;

        int westWall = this.getBlockX() - this.noise;
        int northWall = this.getBlockZ() - this.noise;
        int eastWall = this.getBlockX() + this.noise;
        int southWall = this.getBlockZ() + this.noise;

        BlockPos blockAbove;
        for (int y = this.currentCarveLayer; y >= this.currentCarveLayer - this.carveLayer; y--) {
            if (y == this.bottom + 33) {
                this.wallDistance -= 10;
            } else if ((top - y) % this.nextStep == 0) {
                this.wallDistance -= this.distanceChange;
                this.lastDistanceChange = this.distanceChange;
                this.nextStep = random.nextInt(4)+8;
                if (y > bottom + 33) {
                    this.distanceChange = random.nextInt(3);
                } else {
                    this.distanceChange = random.nextInt(2)+1;
                }

            }

            for (int x = westWall; x <= eastWall; x++) {
                for (int z = northWall; z < southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    blockAbove = blockpos$mutableblockpos.above();
                    block = this.level.getBlockState(blockpos$mutableblockpos).getBlock();
                    double distance2d = BTUtil.distanceTo2D(this, blockpos$mutableblockpos);
                    if (y > this.bottom && !(avoidBlocks.contains(block))) {
                        if  (this.level.getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.KELP_PLANT) {
                            this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);

                        } else if (!this.level.isWaterAt(blockpos$mutableblockpos)){
                            if (distance2d < this.wallDistance - 2) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                            } else if (distance2d < this.wallDistance - 1) {
                                if (random.nextInt(50) > 30) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                } else {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                }
                                this.doVegetation = true;
                            } else if (distance2d < this.wallDistance) {
                                this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                            }
                            if (!this.level.isWaterAt(blockpos$mutableblockpos) && this.level.isWaterAt(blockAbove)
                                    && distance2d < this.wallDistance + this.lastDistanceChange) {
                                if (random.nextInt(50) > 30) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                } else {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                }
                            }

                            if (this.doVegetation && !this.level.isWaterAt(blockpos$mutableblockpos)
                                    && this.level.isWaterAt(blockAbove) && y < this.bottom + 60 ) {
                                int vegetation = random.nextInt(75);
                                if (vegetation > 55) {
                                    this.level.setBlock(blockAbove, Blocks.SEAGRASS.defaultBlockState(), 2);
                                } else if (vegetation > 40) {
                                    this.level.setBlock(blockAbove, corals.get(random.nextInt(5)), 2);
                                } else {
                                    this.level.setBlock(blockAbove, Blocks.WATER.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                    else if (!this.level.isWaterAt(blockpos$mutableblockpos) && this.level.isWaterAt(blockAbove)) {
                        int vegetation = random.nextInt(75);
                        if (vegetation > 55) {
                            this.level.setBlock(blockAbove, Blocks.SEAGRASS.defaultBlockState(), 2);
                        } else if (vegetation > 40) {
                            this.level.setBlock(blockAbove, corals.get(random.nextInt(5)), 2);
                        } else {
                            this.level.setBlock(blockAbove, Blocks.WATER.defaultBlockState(), 2);
                        }
                    }
                }
            }
            this.currentCarveLayer = this.currentCarveLayer - this.carveLayer;
        }
    }
}
