package com.brass_amber.ba_bt.block.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import static com.brass_amber.ba_bt.block.block.ActiveCorriteBlock.*;
import static com.brass_amber.ba_bt.block.block.CoreMatterBlock.MAX_PICKUP;
import static com.brass_amber.ba_bt.block.block.CoreMatterBlock.PICKUP_LEVEL;

public class CoreMatterSlab extends SlabBlock {

    public CoreMatterSlab(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(PICKUP_LEVEL, MAX_PICKUP));
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        entity.makeStuckInBlock(blockState, new Vec3(1D, FALL_SPEED * 2, 1D));
        entity.hurt(level.damageSources().lava(), LAVA_DAMAGE);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        state.trySetValue(PICKUP_LEVEL, state.getValue(PICKUP_LEVEL) - 1);
        return state.getValue(PICKUP_LEVEL) > 0 ? new ItemStack(Items.LAVA_BUCKET) : ItemStack.EMPTY;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(PICKUP_LEVEL);
    }
}
