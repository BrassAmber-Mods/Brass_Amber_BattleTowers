package com.brass_amber.ba_bt.block.block;

import com.brass_amber.ba_bt.block.blockentity.DataMarkerBlockEntity;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataMarkerBlock extends BaseEntityBlock {
    public BlockPlaceContext PLACE_CONTEXT = null;
    public DataMarkerBlock(Properties properties) {
        super(properties);
    }


    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DataMarkerBlockEntity(blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof DataMarkerBlockEntity) {
                ((DataMarkerBlockEntity)blockEntity).drops();
            }
        }

        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof DataMarkerBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer) player), (DataMarkerBlockEntity) blockEntity, blockPos);
            } else {
                throw new IllegalStateException("Our Container Provider is missing!");
            }
        }

        return  InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, BTBlockEntityType.DATA_MARKER.get(), (pLevel, pPos, pState, pBlockEntity) -> pBlockEntity.tick(pLevel, pPos, pState));
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        this.PLACE_CONTEXT = blockPlaceContext;
        return this.defaultBlockState();
    }
}
