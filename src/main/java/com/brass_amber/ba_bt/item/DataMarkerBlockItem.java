package com.brass_amber.ba_bt.item;
import com.brass_amber.ba_bt.block.blockentity.DataMarkerBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DataMarkerBlockItem extends BlockItem {
    public DataMarkerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext p_40611_, BlockState p_40612_) {
        return true;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        Level level = blockPlaceContext.getLevel();
        Player player = blockPlaceContext.getPlayer();
        ItemStack itemstack = blockPlaceContext.getItemInHand();
        BlockPos placePos = blockPlaceContext.getClickedPos().relative(blockPlaceContext.getClickedFace().getOpposite());
        BlockState placeState = level.getBlockState(placePos);
        BlockEntity blockEntity = level.getBlockEntity(placePos);

        CompoundTag blockData;
        if (blockEntity != null) {
            blockData = blockEntity.saveWithFullMetadata();
        } else {
            blockData = new CompoundTag();
        }

        boolean placed = level.setBlock(placePos, blockState, 11);

        if (placed) {
            BlockState blockstate1 = level.getBlockState(placePos);
            this.updateCustomBlockEntityTag(placePos, level, player, itemstack, blockstate1);
            blockstate1.getBlock().setPlacedBy(level, placePos, blockstate1, player, itemstack);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, placePos, itemstack);
            }

            DataMarkerBlockEntity dataMarkerBlockEntity = (DataMarkerBlockEntity) level.getBlockEntity(placePos);
            if (dataMarkerBlockEntity != null) {
                dataMarkerBlockEntity.setPlaceBlockState(placeState);
                dataMarkerBlockEntity.setNbt(blockData);
            }
        }

        return placed;
    }
}
