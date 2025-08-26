package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.util.BTUtil;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;
import static com.brass_amber.ba_bt.sound.BTMusic.*;
import static com.brass_amber.ba_bt.util.BTStatics.towerBlocks;
import static java.lang.Math.abs;

public class BTCoreObelisk extends BTAbstractObelisk {

    private final List<BlockState> avoidBlocks = towerBlocks.get(GolemType.getNumForType(GolemType.CORE));

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

        this.noise = 75;
        if (minimalCoreCarving) {
            this.noise -= 30;
        }

        this.top = this.getBlockY() + noise*2;
        this.bottom = this.getBlockY() - 2;
        this.towerTop = this.bottom + 92;

        this.currentFloorY = this.getBlockY() - 2;
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

    public void gatherAreaBlocks() {
        // BrassAmberBattleTowers.LOGGER.debug(this.level().isClientSide());
        int removeSize = this.toRemove.size();

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        while (this.currentCarveLayer < this.top) {
            BABattleTowers.LOGGER.debug("Round of carving: {}", this.currentCarveLayer);
            int topRange = this.currentCarveLayer + this.floorDistance;
            if (this.top - this.currentCarveLayer <= abs(this.floorDistance) + 1) {
                topRange = this.top;
            }
            // BrassAmberBattleTowers.LOGGER.debug("Bottom Range: " + bottomRange);
            for (int y = this.currentCarveLayer; y <= topRange; y++) {

                for (int x = this.westWall; x <= this.eastWall; x++) {
                    for (int z = this.northWall; z <= this.southWall; z++) {
                        blockpos$mutableblockpos.set(x, y, z);
                        BlockState state = this.level().getBlockState(blockpos$mutableblockpos);
                        double distance3d = BTUtil.distanceTo3D(this.blockPosition().above(this.noise), blockpos$mutableblockpos);
                        double distance2d = BTUtil.distanceTo2D(this, blockpos$mutableblockpos);
                        if (distance3d < this.wallDistance && (distance2d > 12.5 || y > this.towerTop)) {
                            if ((this.level().isWaterAt(blockpos$mutableblockpos) || !state.isAir()) && !avoidBlocks.contains(state)) {
                                this.toRemove.add(blockpos$mutableblockpos.immutable());
                            }
                        } // else {
                        //     BABattleTowers.LOGGER.debug("Position Refused: {} {} {}", distance3d, distance2d, y > this.towerTop);
                        // }
                    }
                }
            }
            this.currentCarveLayer = topRange;
            // BrassAmberBattleTowers.LOGGER.debug("This Round of carving: " + this.currentCarveLayer);
        }

        this.generationState = GenerationState.SET_BLOCKS;
        // BABattleTowers.LOGGER.debug("Core Carved : " + this.coreCarved);
    }

    @Override
    public void removeAreaBlocks() {
        int removeSize = this.toRemove.size();
        BABattleTowers.LOGGER.debug("Removing blocks: {}", removeSize);
        if (removeSize > 0) {
            for (int i = 0; i < Math.min(removeSize, 2048); i++) {
                this.level().setBlock(this.toRemove.remove(0), Blocks.AIR.defaultBlockState(), 2);
            }
        } else {
            this.generationState = GenerationState.ADD_FEATURES;
        }
    }

    public void addAreaFeatures() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos blockAbove;
        for (int y = this.top; y > this.bottom - 1; y--) {
            for (int x = this.westWall; x <= this.eastWall; x++) {
                for (int z = this.northWall; z <= this.southWall; z++) {

                }
            }
        }

        this.generationState = GenerationState.FINISHED;
    }
}
