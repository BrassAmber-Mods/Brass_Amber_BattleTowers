package com.brass_amber.ba_bt.entity;

import java.util.*;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.util.BTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static com.brass_amber.ba_bt.util.BTUtil.*;


public class LandDestructionEntity extends AbstractDestructionEntity {

    public LandDestructionEntity(EntityType<LandDestructionEntity> type, Level level) {
        super(type, level);
    }

    public LandDestructionEntity(Level level, BlockPos obeliskPos) {
        super(BTEntityType.LAND_DESTRUCTION.get(), level, obeliskPos.above(98));
        this.crumbleStopY = this.crumbleStartY - Mth.floor(101 * BattleTowersConfig.landTowerCrumblePercent);
        this.blockSearchDistance = 16;
        this.destructionRadius = 15.5;
        this.crumbleDirection = -1;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void collectBlocks() {
        BlockPos checkPos;
        for (; this.crumbleY != this.crumbleStopY; this.crumbleY+=this.crumbleDirection) {
            for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; x++) {
                    checkPos = this.blockPosition().offset(x, 0, z).atY(this.crumbleY);
                    if (this.level().isFluidAtPosition(checkPos, fluidState -> !fluidState.isEmpty())) {
                        this.level().setBlock(checkPos, Blocks.AIR.defaultBlockState(), 4);
                    } else if (distanceTo2D(this, checkPos) < this.destructionRadius) {
                        this.blocksToRemove.add(checkPos);
                    }
                }
            }
        }
        this.destructionState = this.destructionState.getNext();
    }

    @Override
    public void destroyTower() {

        if (this.blocksToRemove.isEmpty()) {
            this.destructionState = this.destructionState.getNext();
        } else {
            for (int i = 0; i < 4; i++) {
                BlockPos removeBlockPos = this.blocksToRemove.remove(this.random.nextInt(Math.min(this.blocksToRemove.size(), 256)));
                BlockState removeState = this.level().getBlockState(removeBlockPos);
                // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                if (this.random.nextDouble() <= 0.1 && !removeState.isAir()) {
                    // Fancy physics stuff
                    ExplosionPhysics explosion = new ExplosionPhysics(BTEntityType.PHYSICS_EXPLOSION.get(), this.level());
                    explosion.setPos(removeBlockPos.getX(), removeBlockPos.getY(), removeBlockPos.getZ());
                    this.level().addFreshEntity(explosion);

                } else {
                    this.level().destroyBlock(removeBlockPos, false);
                }
            }
        }
    }

    @Override
    public void cleanupTowerZone() {
            BABattleTowers.LOGGER.debug("In Ending Sequence");
            List<BlockPos> shouldBeEmptySpace = new ArrayList<>();
            int yForClear = this.crumbleStartY + 16;
            for (int y = yForClear; y > this.crumbleStopY + 3; y--) {
                for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                    for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; x++) {
                        BlockPos checkPos = new BlockPos(x, y, z);
                        if ((
                                (!this.level().getBlockState(checkPos).isAir() || this.level().isFluidAtPosition(checkPos, fluidState -> !fluidState.isEmpty()))
                                && BTUtil.distanceTo2D(this, checkPos) < this.destructionRadius)
                        ) {
                            this.level().setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                            // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockToAdd);
                        }
                    }
                }
                //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.blocksToRemove.size());
            }
            this.destructionState = this.destructionState.getNext();
    }

    /**************************************************** DATA ****************************************************/

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }
}
