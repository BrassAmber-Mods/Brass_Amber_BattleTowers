package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.util.BTUtil;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.sound.BTMusic.*;
import static com.brass_amber.ba_bt.util.BTStatics.towerBlocks;
import static java.lang.Math.abs;

public class BTCoreObelisk extends BTAbstractObelisk {

    private final List<Block> avoidBlocks = towerBlocks.get(GolemType.CORE.ordinal());

    private int noise;
    private int westWall;
    private int northWall;
    private int eastWall;
    private int southWall;
    private int top;
    private int towerTop;
    private int bottom;
    private int currentCarveLayer;
    private double wallDistance;


    public BTCoreObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BTCoreObelisk(Level level) {
        super(GolemType.CORE, level);
    }


    @Override
    public void initialize() {
        this.musicDistance = 75;
        this.towerRange = 80;
        super.initialize();
        this.enemySpawnRange = 15;
    }

    @Override
    public void clientInitialize() {
        this.BOSS_MUSIC = CORE_GOLEM_FIGHT_MUSIC;
        this.TOWER_MUSIC = CORE_TOWER_MUSIC;
        super.clientInitialize();
    }

    public void serverInitialize() {
        this.floorDistance = 11;

        this.chestBlock = BTBlocks.CORE_CHEST.get();
        this.golemChestBlock = BTBlocks.CORE_GOLEM_CHEST.get();
        this.spawnerBlock = BTBlocks.CORE_SPAWNER.get();
        this.spawnerFillBlock = BTBlocks.CORRITE_BLOCK.get();
        this.golemChestLootTypes = new ArrayList<>(List.of("armor", "weapon", "gem"));
        this.towerChestLootTypes = new ArrayList<>(List.of("armor", "weapon", "ore", "consumable"));
        this.golemLoot = new ItemStack[]{BTBlocks.CORRITE_BLOCK.get().asItem().getDefaultInstance(), Items.MAGMA_CREAM.getDefaultInstance(), Items.NETHERITE_INGOT.getDefaultInstance()};

        this.noise = 60;

        this.bottom = this.getBlockY() - 1;
        this.top = this.getBlockY() - 12 + (noise * 2);
        this.towerTop = this.getBlockY() + 97;

        this.currentFloorY = this.bottom;
        this.currentCarveLayer = this.bottom;
        this.wallDistance = this.noise -.5;


        this.westWall = this.getBlockX() - this.noise;
        this.northWall = this.getBlockZ() - this.noise;
        this.eastWall = this.getBlockX() + this.noise;
        this.southWall = this.getBlockZ() + this.noise;

        super.serverInitialize();

        /* BrassAmberBattleTowers.LOGGER.debug("Walls W,N,E,S " + this.westWall + " " + this.northWall + " " + this.eastWall + " " + this.southWall);
        BrassAmberBattleTowers.LOGGER.debug("Noise " + this.noise);**/
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // BABTMain.LOGGER.debug("CORE Carved in read data " + this.CORECarved);
    }

    @Override
    public void tick() {
        super.tick();
    }


    public void removeMotionActiveBlocks() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockState state;
        for (int y = this.top; y > this.bottom - 1; y--) {
            for (int x = this.westWall; x <= this.eastWall; x++) {
                for (int z = this.northWall; z <= this.southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    double distance3d = BTUtil.distanceTo3D(this.blockPosition().above(this.noise - 5), blockpos$mutableblockpos);
                    state = this.level().getBlockState(blockpos$mutableblockpos);
                    if (distance3d < this.wallDistance) {
                        if (state.is(Blocks.GRAVEL)) {
                            this.level().setBlock(blockpos$mutableblockpos, Blocks.AIR.defaultBlockState(), 2);
                        } else if (state.getFluidState().getAmount() > 0) {
                            try {
                                this.level().setBlock(blockpos$mutableblockpos, state.setValue(BlockStateProperties.WATERLOGGED, false), 2);
                            } catch (Exception e) {
                                this.level().setBlock(blockpos$mutableblockpos, Blocks.AIR.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
        this.generationState = GenerationState.GATHER_AREA_BLOCKS;
    }

    public void gatherAreaBlocks() {
        // BrassAmberBattleTowers.LOGGER.debug(this.level().isClientSide());

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        while (this.currentCarveLayer < this.top) {
            // BABattleTowers.LOGGER.debug("Round of carving: {}", this.currentCarveLayer);
            int topRange = this.currentCarveLayer + this.floorDistance;
            if (this.top - this.currentCarveLayer <= abs(this.floorDistance) + 1) {
                topRange = this.top;
            }
            // BrassAmberBattleTowers.LOGGER.debug("Bottom Range: " + topRange);
            for (int y = this.currentCarveLayer; y <= topRange; y++) {

                for (int x = this.westWall; x <= this.eastWall; x++) {
                    for (int z = this.northWall; z <= this.southWall; z++) {
                        blockpos$mutableblockpos.set(x, y, z);
                        BlockState state = this.level().getBlockState(blockpos$mutableblockpos);
                        double distance3d = BTUtil.distanceTo3D(this.blockPosition().above(this.noise - 12), blockpos$mutableblockpos);
                        double distance2d = BTUtil.distanceTo2D(this, blockpos$mutableblockpos);
                        if (distance3d < this.wallDistance ) {
                            if ((distance2d > 15.5 || y >= this.towerTop) && !state.isAir()) {
                                this.toRemove.add(blockpos$mutableblockpos.immutable());
                            } else if (distance2d > 12.5 && !avoidBlocks.contains(state.getBlock())) {
                                this.toRemove.add(blockpos$mutableblockpos.immutable());
                            }
                        } else if (distance3d < this.wallDistance + 1) {
                            this.level().setBlock(blockpos$mutableblockpos, Blocks.OBSIDIAN.defaultBlockState(), 2);
                        }
                        //     BABattleTowers.LOGGER.debug("Position Refused: {} {} {}", distance3d, distance2d, y > this.towerTop);
                        // }
                    }

                }
            }
            this.currentCarveLayer = topRange;
            // BrassAmberBattleTowers.LOGGER.debug("This Round of carving: " + this.currentCarveLayer);
        }

        this.generationState = GenerationState.REMOVE_AREA_BLOCKS;
        // BABattleTowers.LOGGER.debug("Core Carved : " + this.coreCarved);
    }

    @Override
    public void removeAreaBlocks() {
        int removeSize = this.toRemove.size();
        // BABattleTowers.LOGGER.debug("Removing blocks: {}", removeSize);
        if (removeSize > 0) {
            for (int i = 0; i < Math.min(removeSize, 2048); i++) {
                this.level().setBlock(this.toRemove.remove(0), Blocks.AIR.defaultBlockState(), 3);
            }
        } else {
            this.generationState = GenerationState.ADD_AREA_FEATURES;
        }
    }

    public void addAreaFeatures() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos blockAbove;
        for (int y = this.top; y > this.bottom - 1; y--) {
            for (int x = this.westWall; x <= this.eastWall; x++) {
                for (int z = this.northWall; z <= this.southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    blockAbove = blockpos$mutableblockpos.above();
                    if (this.level().getBlockState(blockpos$mutableblockpos).is(Blocks.RED_STAINED_GLASS)) {
                        this.level().setBlock(blockpos$mutableblockpos, Blocks.LAVA.defaultBlockState(), 2);
                    }

                    if (y <= this.bottom +14) {
                        BlockState state = this.level().getBlockState(blockpos$mutableblockpos);
                        BlockState aboveState = this.level().getBlockState(blockAbove);
                        double distance3d = BTUtil.distanceTo3D(this.blockPosition().above(this.noise - 12), blockpos$mutableblockpos);
                        if (distance3d < this.wallDistance + 1 && state.is(Blocks.OBSIDIAN) && aboveState.isAir()) {
                            float delta = random.nextFloat();
                            if (delta > 0.13f) {
                                this.level().setBlock(blockAbove, Blocks.BASALT.defaultBlockState(), 2);
                            } else {
                                this.level().setBlock(blockAbove, Blocks.LAVA.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }

        this.generationState = GenerationState.FINISHED;
    }
}
