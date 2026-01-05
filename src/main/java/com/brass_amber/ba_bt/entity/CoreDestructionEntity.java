package com.brass_amber.ba_bt.entity;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.util.BTUtil.distanceTo2D;

public class CoreDestructionEntity extends AbstractDestructionEntity {
    public List<BlockPos> coreMatterBlocks = new ArrayList<>();
    public boolean coreMatterConversion = false;

    public CoreDestructionEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blockSearchDistance = 16;
        this.destructionRadius = 15.5;
        this.crumbleDirection = -1;
        this.destroySpeed = 0;
    }

    public CoreDestructionEntity(Level level, BlockPos obeliskPos) {
        this(BTEntityType.CORE_DESTRUCTION.get(), level);
        this.setPos(obeliskPos, 98);
        LOGGER.debug("Destruction {} spawned at: {}", this.golemType.getSerializedName(), this.blockPosition());
        LOGGER.debug("Start Y: {} | Stop Y: {}", this.crumbleStartY, this.crumbleStopY);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void collectBlocks() {
        BABattleTowers.LOGGER.debug("In Collect Sequence");
        BlockPos checkPos;
        for (int y = this.crumbleStartY; y != this.crumbleStopY; y += this.crumbleDirection) {
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


        if (this.currentTicks % 120 == 0) {
            this.destroySpeed += 4;
        }

        if (this.blocksToRemove.isEmpty()) {
            for (int i = 0; i < Math.min(this.coreMatterBlocks.size(), 8 + this.destroySpeed); i++) {
                BlockPos removeBlockPos = this.coreMatterBlocks.remove(this.random.nextInt(Math.min(this.coreMatterBlocks.size(), 32)));
                this.level().destroyBlock(removeBlockPos, false);
            }
            if (this.coreMatterBlocks.isEmpty()) {
                this.destructionState = this.destructionState.getNext();
            }
            if (this.currentTicks % 240 == 0) {
                this.level().playSound(null, this.coreMatterBlocks.get(this.random.nextInt(Math.min(this.coreMatterBlocks.size(), 64))),
                        BTSoundEvents.TOWER_BREAK_CRUMBLE.get(), SoundSource.AMBIENT, 4F, 1F);
            }
        } else {
            if (this.currentTicks % 240 == 0) {
                this.level().playSound(null, this.blocksToRemove.get(this.random.nextInt(Math.min(this.blocksToRemove.size(), 64))),
                        BTSoundEvents.TOWER_BREAK_CRUMBLE.get(), SoundSource.AMBIENT, 4F, 1F);
            }
            for (int i = 0; i < Math.min(this.blocksToRemove.size(), 12 + this.destroySpeed); i++) {
                BlockPos removeBlockPos = this.blocksToRemove.remove(this.random.nextInt(Math.min(this.blocksToRemove.size(), 48)));
                // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                setCoreMatterBlock(removeBlockPos);
                this.coreMatterBlocks.add(removeBlockPos);
            }
        }

    }

    public void setCoreMatterBlock(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        BlockState setState;

        if (state.is(BlockTags.SLABS)) {
            setState = BTBlocks.CORE_MATTER_SLAB.get().defaultBlockState();
            setState = setState.trySetValue(BlockStateProperties.SLAB_TYPE, state.getValue(BlockStateProperties.SLAB_TYPE));
            setState = setState.trySetValue(BlockStateProperties.WATERLOGGED, false);
        } else if (state.is(BlockTags.STAIRS)) {
            setState = BTBlocks.CORE_MATTER_STAIR.get().defaultBlockState();
            setState = setState.trySetValue(BlockStateProperties.STAIRS_SHAPE, state.getValue(BlockStateProperties.STAIRS_SHAPE));
            setState = setState.trySetValue(BlockStateProperties.WATERLOGGED, false);
        } else if (state.is(BlockTags.WALLS)) {
            setState = BTBlocks.CORE_MATTER_WALL.get().defaultBlockState();
            setState = setState.trySetValue(BlockStateProperties.UP, state.getValue(BlockStateProperties.UP));
            setState = setState.trySetValue(BlockStateProperties.EAST_WALL, state.getValue(BlockStateProperties.EAST_WALL));
            setState = setState.trySetValue(BlockStateProperties.NORTH_WALL, state.getValue(BlockStateProperties.NORTH_WALL));
            setState = setState.trySetValue(BlockStateProperties.SOUTH_WALL, state.getValue(BlockStateProperties.SOUTH_WALL));
            setState = setState.trySetValue(BlockStateProperties.WEST_WALL, state.getValue(BlockStateProperties.WEST_WALL));
            setState = setState.trySetValue(BlockStateProperties.WATERLOGGED, false);
        } else {
            setState = BTBlocks.CORE_MATTER.get().defaultBlockState();
        }

        this.level().setBlockAndUpdate(pos, setState);
    }

    @Override
    public void cleanupTowerZone() {
        BABattleTowers.LOGGER.debug("In Cleanup Sequence");
        BlockPos checkPos;
        for (int y = this.crumbleStartY + 4; y != this.crumbleStopY + 5; y += this.crumbleDirection) {
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

        for (int y = this.crumbleStopY; y != this.crumbleStopY - 15; y += this.crumbleDirection) {
            for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                    checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                    BlockState state = this.level().getBlockState(checkPos);
                    // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                    double distanceto = distanceTo2D(this, checkPos);

                    if (y == this.crumbleStopY - 1) {
                        if (distanceto < this.destructionRadius && distanceto > 4) {
                            if (!state.getFluidState().isEmpty() || !state.isAir()) {
                                this.level().setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                            }
                        }
                    } else if (distanceto < this.destructionRadius) {
                        if (!state.getFluidState().isEmpty() || !state.isAir()) {
                            this.level().setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
        this.destructionState = this.destructionState.getNext();
    }


    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

}
