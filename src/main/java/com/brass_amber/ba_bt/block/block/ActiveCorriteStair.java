package com.brass_amber.ba_bt.block.block;

import com.brass_amber.ba_bt.BABattleTowers;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.brass_amber.ba_bt.block.block.ActiveCorriteBlock.*;

public class ActiveCorriteStair extends StairBlock {

    public ActiveCorriteStair(BlockState state, Properties properties) {
        super(state, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CORRITE, 0));
    }


    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.hurt(level.damageSources().hotFloor(), FOOT_DAMAGE);
        }
        super.stepOn(level, blockPos, blockState, entity);
    }

    public void attack(BlockState p_55467_, Level p_55468_, BlockPos p_55469_, Player p_55470_) {
        interact(p_55467_, p_55468_, p_55469_);
        super.attack(p_55467_, p_55468_, p_55469_, p_55470_);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        entity.makeStuckInBlock(blockState, new Vec3(0.25D, FALL_SPEED, 0.25D));
        entity.hurt(level.damageSources().lava(), LAVA_DAMAGE);
    }

    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        BubbleColumnBlock.updateColumn(serverLevel, blockPos.above(), blockState);
        if (blockState.getValue(CORRITE) > 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(CORRITE, blockState.getValue(CORRITE) - 1), 3);
        }
    }

    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext p_56071_) {
        return blockState.getValue(CORRITE) != 0 ? Shapes.empty() : blockState.getShape(blockGetter, blockPos);
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
        BABattleTowers.LOGGER.debug("Corrite Interaction {}", blockState.getValue(CORRITE));
        if (blockState.getValue(CORRITE) == 0) {
            level.setBlock(blockPos, blockState.setValue(CORRITE, 10), 3);
        }
    }


    private static void spawnParticles(Level level, BlockPos blockPos) {
        double d0 = 0.5625D;
        RandomSource randomsource = level.random;

        for (Direction direction : Direction.values()) {
            BlockPos blockpos = blockPos.relative(direction);
            if (!level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : (double) randomsource.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : (double) randomsource.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : (double) randomsource.nextFloat();
                level.addParticle(ParticleTypes.LAVA, (double) blockPos.getX() + d1, (double) blockPos.getY() + d2, (double) blockPos.getZ() + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(CORRITE);
    }
}
