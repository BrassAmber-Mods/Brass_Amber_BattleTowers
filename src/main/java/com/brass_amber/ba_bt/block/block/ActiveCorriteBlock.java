package com.brass_amber.ba_bt.block.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ActiveCorriteBlock extends MagmaBlock {
    public static final int BUBBLE_COLUMN_CHECK_DELAY = 20;
    public static final IntegerProperty CORRITE = IntegerProperty.create("corrite", 0, 11);
    public static final float FOOT_DAMAGE = 3.0f;
    public static final float LAVA_DAMAGE = 6.0f;
    public static final double FALL_SPEED = 0.20D;

    /**
     * Class uses methods copied from net.minecraft.world.level.block.RedStoneOreBlock
     * as well as from net.minecraft.world.level.block.WebBlock
     */

    public ActiveCorriteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CORRITE, 0));
    }

    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(level.damageSources().hotFloor(), FOOT_DAMAGE);
        }
        super.stepOn(level, blockPos, blockState, entity);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
      entity.makeStuckInBlock(blockState, new Vec3(0.25D, FALL_SPEED, 0.25D));
      entity.hurt(level.damageSources().lava(), LAVA_DAMAGE);
   }

    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        BubbleColumnBlock.updateColumn(serverLevel, blockPos.above(), blockState);
        if (blockState.getValue(CORRITE) > 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(CORRITE, blockState.getValue(CORRITE) - 1), 3);
            if (blockState.getValue(CORRITE) == 20) {
                this.properties.forceSolidOn().noCollission();
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            spawnParticles(level, blockPos);
        } else {
            interact(blockState, level, blockPos);
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (direction == Direction.UP && blockState1.is(Blocks.WATER)) {
            levelAccessor.scheduleTick(blockPos, this, BUBBLE_COLUMN_CHECK_DELAY);
        }

        return super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
    }

    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        level.scheduleTick(blockPos, this, BUBBLE_COLUMN_CHECK_DELAY);
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(CORRITE) > 0) {
            spawnParticles(level, blockPos);
        }
    }

    private static void interact(BlockState blockState, Level level, BlockPos blockPos) {
        spawnParticles(level, blockPos);
        if (blockState.getValue(CORRITE) == 0) {
            level.setBlock(blockPos, blockState.setValue(CORRITE, 10), 3);
        }
    }


    private static void spawnParticles(Level level, BlockPos blockPos) {
        double d0 = 0.5625D;
        RandomSource randomsource = level.random;

        for(Direction direction : Direction.values()) {
            BlockPos blockpos = blockPos.relative(direction);
            if (!level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double)direction.getStepX() : (double)randomsource.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double)direction.getStepY() : (double)randomsource.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double)direction.getStepZ() : (double)randomsource.nextFloat();
                level.addParticle(ParticleTypes.LAVA, (double)blockPos.getX() + d1, (double)blockPos.getY() + d2, (double)blockPos.getZ() + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(CORRITE);
    }
}
