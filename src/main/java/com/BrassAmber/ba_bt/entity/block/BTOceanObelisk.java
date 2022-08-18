package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.init.BTExtras;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.BTUtil;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
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
    private int westWall;
    private int northWall;
    private int eastWall;
    private int southWall;
    private int top;
    private int bottom;
    private int currentCarveLayer;
    private double wallDistance;
    private int nextStep;
    private int distanceChange;


    public BTOceanObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.BOSS_MUSIC = null;
        this.TOWER_MUSIC = BTSoundEvents.OCEAN_TOWER_MUSIC;
        this.musicDistance = 58;
        this.towerRange = 62;
    }

    public BTOceanObelisk(Level level) {
        super(GolemType.OCEAN, level);
        this.towerEffect = BTExtras.DEPTH_DROPPER_EFFECT.get();
    }


    @Override
    public void initialize() {
        super.initialize();
        if (!this.level.isClientSide()) {
            this.carveOcean();
            doNoOutputCommand(this, "/kill @e[type=item]");
        }
    }

    public void serverInitialize() {
        this.floorDistance = -11;

        this.chestBlock = BTBlocks.OCEAN_CHEST.get();
        this.spawnerBlock = BTBlocks.BT_OCEAN_SPAWNER.get();
        this.woolBlock = Blocks.BLUE_WOOL;
        this.spawnerFillBlock = Blocks.PRISMARINE_BRICKS;

        this.noise = 60 + ((random.nextInt(2) + 1) * 4);
        this.top = this.getBlockY() - 2;
        this.bottom = this.getBlockY() - 92;

        this.currentFloorY = this.top;
        this.currentCarveLayer = this.top;
        this.wallDistance = this.noise -.5;
        this.nextStep = random.nextInt(4) + 8;
        this.distanceChange = random.nextInt(3);

        this.westWall = this.getBlockX() - this.noise;
        this.northWall = this.getBlockZ() - this.noise;
        this.eastWall = this.getBlockX() + this.noise;
        this.southWall = this.getBlockZ() + this.noise;

        /* BrassAmberBattleTowers.LOGGER.info("Walls W,N,E,S " + this.westWall + " " + this.northWall + " " + this.eastWall + " " + this.southWall);
        BrassAmberBattleTowers.LOGGER.info("Noise " + this.noise);**/
    }

    public void carveOcean() {
        // BrassAmberBattleTowers.LOGGER.info("Round of carving: " + this.currentCarveLayer);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Block block;
        if (this.currentCarveLayer >= this.bottom) {
            int bottomRange = this.currentCarveLayer + this.floorDistance;
            if (this.currentCarveLayer - this.bottom < 20) {
                bottomRange = this.bottom;
            }
            // BrassAmberBattleTowers.LOGGER.info("Bottom Range: " + bottomRange);
            for (int y = this.currentCarveLayer; y >= bottomRange; y--) {
                if (y == this.bottom + 33) {
                    this.wallDistance -= 10;
                } else if ((top - y) % this.nextStep == 0) {
                    this.wallDistance -= this.distanceChange;
                    this.nextStep = random.nextInt(4)+8;
                    if (y > bottom + 33) {
                        this.distanceChange = random.nextInt(3);
                    } else {
                        this.distanceChange = random.nextInt(2)+1;
                    }
                }
                if (y > this.bottom) {
                    for (int x = this.westWall; x <= this.eastWall; x++) {
                        for (int z = this.northWall; z <= this.southWall; z++) {
                            blockpos$mutableblockpos.set(x, y, z);
                            block = this.level.getBlockState(blockpos$mutableblockpos).getBlock();
                            double distance2d = BTUtil.distanceTo2D(this, blockpos$mutableblockpos);
                            if (y > this.bottom) {
                                if (distance2d > 13) {
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
                                        } else if (distance2d < this.wallDistance) {
                                            this.level.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                        }
                                    }
                                } else if (distance2d < 13 && !this.avoidBlocks.contains(block)) {
                                    this.level.setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                } else if (y == this.bottom){
                    this.addCoral();
                }
            }
            this.currentCarveLayer = bottomRange;
            // BrassAmberBattleTowers.LOGGER.info("This Round of carving: " + this.currentCarveLayer);
        }
    }

    public void addCoral() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos blockAbove;
        for (int y = this.top; y > this.bottom - 1; y--) {
            for (int x = this.westWall; x <= this.eastWall; x++) {
                for (int z = this.northWall; z <= this.southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    blockAbove = blockpos$mutableblockpos.above();
                    double distance2d = BTUtil.distanceTo2D(this, blockpos$mutableblockpos);
                    if (distance2d > 13 && !this.level.isWaterAt(blockpos$mutableblockpos) && this.level.isWaterAt(blockAbove)) {
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
        }
    }
}
