package com.brass_amber.ba_bt.block.block;

import com.brass_amber.ba_bt.block.blockentity.DataMarkerBlockEntity;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        BlockEntity entity = level.getBlockEntity(hitResult.getBlockPos());

        if (entity instanceof DataMarkerBlockEntity dataMarkerBlockEntity) {
            if (itemstack.getItem() == BTItems.LAND_GUARDIAN_EYE.get()) {
                dataMarkerBlockEntity.setRarity(-1);
            } else if (itemstack.getItem() == BTItems.OCEAN_GUARDIAN_EYE.get()) {
                dataMarkerBlockEntity.setRarity(0);
            } else if (itemstack.getItem() == BTItems.CORE_GUARDIAN_EYE.get()) {
                dataMarkerBlockEntity.setRarity(1);
            } else if (itemstack.getItem() == BTItems.NETHER_GUARDIAN_EYE.get()) {
                dataMarkerBlockEntity.setRarity(2);
            } else if (itemstack.getItem() == BTItems.END_GUARDIAN_EYE.get()) {
                dataMarkerBlockEntity.setRarity(3);
            } else if (itemstack.getItem() == BTItems.SKY_GUARDIAN_EYE.get()) {
                dataMarkerBlockEntity.setRarity(4);
            }

            if (itemstack.getItem() instanceof BlockItem blockItem) {
                dataMarkerBlockEntity.setPlaceBlockState(blockItem.getBlock().defaultBlockState());
            }
            return InteractionResult.PASS;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
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
