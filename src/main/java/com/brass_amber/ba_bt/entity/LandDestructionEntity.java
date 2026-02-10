package com.brass_amber.ba_bt.entity;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static com.brass_amber.ba_bt.util.BTUtil.*;


public class LandDestructionEntity extends AbstractDestructionEntity {

    public LandDestructionEntity(EntityType<?> entityType, Level level) {
        super(BTEntityType.LAND_DESTRUCTION.get(), level);
        this.blockSearchDistance = 16;
        this.destructionRadius = 15.5;
        this.crumbleDirection = -1;
        this.destroySpeed = 0;
    }

    public LandDestructionEntity(Level level) {
        this(BTEntityType.LAND_DESTRUCTION.get(), level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void collectBlocks() {
        BABattleTowers.LOGGER.debug("In Collect Sequence");
        BlockPos checkPos;
        for (int y = this.crumbleStartY; y != this.crumbleStopY; y+=this.crumbleDirection) {
            // BABattleTowers.LOGGER.debug("Crumble Y {}", y);
            for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                    checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                    BlockState state = this.level().getBlockState(checkPos);
                    // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                    if (!state.getFluidState().isEmpty()) {
                        this.level().setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                    } else if (distanceTo2D(this, checkPos) < this.destructionRadius && !state.isAir()) {
                        this.blocksToRemove.add(checkPos);
                    }
                }
            }
        }
        this.destructionState = this.destructionState.getNext();
    }

    @Override
    public void destroyTower() {
        // BABattleTowers.LOGGER.debug("In Destroy Sequence: {}", this.blocksToRemove.size());
        if (this.currentTicks % 240 == 0) {
            this.level().playSound(null, this.blocksToRemove.get(this.random.nextInt(Math.min(this.blocksToRemove.size(), 64))),
                    BTSoundEvents.TOWER_BREAK_CRUMBLE.get(), SoundSource.AMBIENT, 4F, 1F);
            this.destroySpeed+= 4;
        }
        if (this.blocksToRemove.isEmpty()) {
            this.destructionState = this.destructionState.getNext();
        } else {
            if (this.random.nextDouble() <= 0.24) {
                // Fancy physics stuff
                BlockPos removeBlockPos = this.blocksToRemove.get(0);
                ExplosionPhysics explosion = new ExplosionPhysics(BTEntityType.PHYSICS_EXPLOSION.get(), this.level());
                explosion.setPos(removeBlockPos.getX(), removeBlockPos.getY(), removeBlockPos.getZ());
                this.level().addFreshEntity(explosion);
            }
            for (int i = 0; i < Math.min(this.blocksToRemove.size(), 18 + this.destroySpeed); i++) {
                BlockPos removeBlockPos = this.blocksToRemove.remove(this.random.nextInt(Math.min(this.blocksToRemove.size(), 256)));
                // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");

                this.level().destroyBlock(removeBlockPos, false);
            }
        }
    }

    @Override
    public void cleanupTowerZone() {
        BABattleTowers.LOGGER.debug("In Cleanup Sequence");
        BlockPos checkPos;
        for (int y = this.crumbleStartY + 16; y != this.crumbleStopY + 5; y += this.crumbleDirection) {
            for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                    checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                    BlockState state = this.level().getBlockState(checkPos);
                    // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);

                    if (distanceTo2D(this, checkPos) < this.destructionRadius) {
                        if (!state.getFluidState().isEmpty() || !state.isAir()) {
                            this.level().setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
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
