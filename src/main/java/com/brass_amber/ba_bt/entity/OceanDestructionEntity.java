package com.brass_amber.ba_bt.entity;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static com.brass_amber.ba_bt.util.BTUtil.distanceTo2D;

public class OceanDestructionEntity extends AbstractDestructionEntity {
    private final ArrayList<FallingBlockEntity> fallingBlocks;
    private final ArrayList<BlockState> belowTowerBlocks;
    private boolean waterbelowTower;

    public String waterBelowTowerName = "waterBelowTower";


    public OceanDestructionEntity(EntityType<OceanDestructionEntity> type, Level level) {
        super(type, level);
        this.blockSearchDistance = 17;
        this.destructionRadius = 17.5;
        this.crumbleDirection = 1;
        this.destroySpeed = 0;
        this.fallingBlocks = new ArrayList<>();
        this.belowTowerBlocks = new ArrayList<>();
        this.waterbelowTower = false;
    }

    public OceanDestructionEntity(Level level, BlockPos obeliskPos) {
        this(BTEntityType.OCEAN_DESTRUCTION.get(), level);
        this.setPos(obeliskPos, -110);
        LOGGER.debug("Destruction {} spawned at: {}", this.golemType.getSerializedName(), this.blockPosition());
        LOGGER.debug("Start Y: {} | Stop Y: {}", this.crumbleStartY, this.crumbleStopY);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void collectBlocks() {
        BABattleTowers.LOGGER.debug("In Collect Sequence");
        BlockPos checkPos;

        if (!BattleTowersConfig.oceanTowerVoidHole) {
            for (int y = this.crumbleStartY; y != this.level().getMinBuildHeight() - 1; y--) {
                BABattleTowers.LOGGER.debug("Below Tower Y {}", y);
                for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                    for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                        checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                        BlockState state = this.level().getBlockState(checkPos);
                        // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                        if (distanceTo2D(this, checkPos) < this.destructionRadius) {
                            this.belowTowerBlocks.add(state);
                        }
                    }
                }
            }
        }

        for (int y = this.crumbleStartY; y != this.crumbleStopY; y += this.crumbleDirection) {
            BABattleTowers.LOGGER.debug("Collect Y {}, CD: {}", y, this.crumbleDirection);
            for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                    checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                    BlockState state = this.level().getBlockState(checkPos);
                    // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                    if (distanceTo2D(this, checkPos) < this.destructionRadius) {
                        Optional<Boolean> prop = state.getOptionalValue(BlockStateProperties.WATERLOGGED);
                        if (!this.level().isWaterAt(checkPos) || prop.isPresent()){
                            this.blocksToRemove.add(checkPos);
                        }
                    }
                }
            }
        }
        this.destructionState = this.destructionState.getNext();
    }

    @Override
    public void destroyTower() {
        if (this.currentTicks % 240 == 0) {
            this.level().playSound(null, this.blocksToRemove.get(this.random.nextInt(Math.min(this.blocksToRemove.size(), 64))),
                    BTSoundEvents.TOWER_BREAK_CRUMBLE.get(), SoundSource.AMBIENT, 4F, 1F);
            this.destroySpeed += 4;
        }
        if (!this.waterbelowTower) {
            BlockPos checkPos;
            for (int y = this.crumbleStartY; y != this.level().getMinBuildHeight() - 1; y--) {
                for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                    for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                        checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                        // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                        if (distanceTo2D(this, checkPos) < this.destructionRadius) {
                            this.level().setBlockAndUpdate(checkPos, Blocks.WATER.defaultBlockState());
                        }
                    }
                }
            }
            this.waterbelowTower = true;
        }
        if (this.blocksToRemove.isEmpty()) {
            this.destructionState = this.destructionState.getNext();
        } else {
            int countBad = 0;
            for (int i = 0; i < Math.min(this.blocksToRemove.size(), 24); i++) {
                int removeInt = this.random.nextInt(Math.min(this.blocksToRemove.size(), 64));
                BlockPos removeBlockPos = this.blocksToRemove.get(removeInt);
                if (this.level().isWaterAt(removeBlockPos.below())) {
                    // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                    removeBlockPos = this.blocksToRemove.remove(removeInt);
                    final Vec3 velocity = new Vec3(0D, 0.5D, 0D);

                    FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
                            this.level(),
                            removeBlockPos,
                            this.level().getBlockState(removeBlockPos)
                    );

                    fallingBlock.setInvulnerable(true);
                    fallingBlock.dropItem = false;
                    fallingBlock.setDeltaMovement(velocity);

                    this.fallingBlocks.add(fallingBlock);
                    this.level().setBlockAndUpdate(removeBlockPos, Blocks.WATER.defaultBlockState());
                } else {
                    countBad++;
                }
            }
            if (countBad >= Math.max(Math.min(this.blocksToRemove.size(), 24) - 2, 0)) {
                for (int i = 0; i < Math.min(this.blocksToRemove.size(), 8); i++) {
                    int removeInt = this.random.nextInt(Math.min(this.blocksToRemove.size(), 64));
                    BlockPos removeBlockPos = this.blocksToRemove.remove(removeInt);
                    this.level().setBlockAndUpdate(removeBlockPos, Blocks.WATER.defaultBlockState());
                }

            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        // New list to save the id pf the entities that should be removed
        ArrayList<FallingBlockEntity> removeFallEntity = new ArrayList<>();
        for (int i = 0; i < this.fallingBlocks.size(); i++) {
            // Check each block in the Falling entities list
            FallingBlockEntity blockEntity = this.fallingBlocks.get(i);
            if (blockEntity.getY() < this.getBlockY() - 4 + this.destroySpeed) {
                // remove if too far down
                removeFallEntity.add(this.fallingBlocks.remove(i));
            }
        }
        // Reverse list of Ids so that we remove from the end first, thereby avoiding a call to an item already removed
        Collections.reverse(removeFallEntity);
        for (int i = 0; i < removeFallEntity.size(); i++) {
            FallingBlockEntity blockEntity = removeFallEntity.remove(i);
            blockEntity.kill();
        }
    }

    @Override
    public void cleanupTowerZone() {
        BlockPos checkPos;

        if (!BattleTowersConfig.oceanTowerVoidHole) {
            for (int y = this.crumbleStartY; y != this.level().getMinBuildHeight() - 1; y--) {
                for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                    for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                        checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                        // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                        if (distanceTo2D(this, checkPos) < this.destructionRadius && !this.belowTowerBlocks.isEmpty()) {
                            BlockState state = this.belowTowerBlocks.remove(0);
                            this.level().setBlock(checkPos, state, 2);
                        }
                    }
                }
            }
        }

        for (int y = this.crumbleStartY; y != this.crumbleStopY; y += this.crumbleDirection) {
            BABattleTowers.LOGGER.debug("Fix Y {}", y);
            for (int x = -this.blockSearchDistance; x < this.blockSearchDistance; x++) {
                for (int z = -this.blockSearchDistance; z < this.blockSearchDistance; z++) {
                    checkPos = this.blockPosition().offset(x, 0, z).atY(y);
                    BlockState state = this.level().getBlockState(checkPos);
                    // BABattleTowers.LOGGER.debug("CheckPos {}", checkPos);
                    if (distanceTo2D(this, checkPos) < this.destructionRadius) {
                        Optional<Boolean> prop = state.getOptionalValue(BlockStateProperties.WATERLOGGED);
                        if (!this.level().isWaterAt(checkPos) || prop.isPresent()) {
                            this.level().setBlock(checkPos, Blocks.WATER.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
        this.destructionState = this.destructionState.getNext();
    }

    /**************************************************** DATA ****************************************************/

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        CompoundTag blocksTag = new CompoundTag();
        blocksTag.putInt("size", this.belowTowerBlocks.size());
        for (int i=0; i < this.belowTowerBlocks.size(); i++) {
            BlockState state = this.belowTowerBlocks.get(i);
            blocksTag.put(String.valueOf(i), NbtUtils.writeBlockState(state));
        }
        compoundTag.put("blocksTag", blocksTag);
        compoundTag.putBoolean(this.waterBelowTowerName, this.waterbelowTower);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        HolderLookup<Block> holderLookup = this.level().holderLookup(Registries.BLOCK);
        CompoundTag blocksTag = compoundTag.getCompound("blocksTag");
        int size = blocksTag.getInt("size");
        for (int i = 0; i < size; i++) {
            BlockState state = NbtUtils.readBlockState(holderLookup , blocksTag.getCompound(String.valueOf(i)));
            this.belowTowerBlocks.add(state);
        }
        this.waterbelowTower = compoundTag.getBoolean(this.waterBelowTowerName);
    }

}
