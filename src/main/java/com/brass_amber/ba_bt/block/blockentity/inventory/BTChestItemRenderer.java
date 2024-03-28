package com.brass_amber.ba_bt.block.blockentity.inventory;

import com.brass_amber.ba_bt.block.blockentity.GolemChestBlockEntity;
import com.brass_amber.ba_bt.block.blockentity.TowerChestBlockEntity;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BTChestItemRenderer extends BlockEntityWithoutLevelRenderer {

    public static BTChestItemRenderer INSTANCE = new BTChestItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final GolemChestBlockEntity landGolemChestEntity = new GolemChestBlockEntity(BlockPos.ZERO, BTBlocks.LAND_GOLEM_CHEST.get().defaultBlockState());
    private final TowerChestBlockEntity landTowerChestEntity = new TowerChestBlockEntity(BlockPos.ZERO, BTBlocks.LAND_CHEST.get().defaultBlockState());
    private final GolemChestBlockEntity oceanGolemChestEntity = new GolemChestBlockEntity(BlockPos.ZERO, BTBlocks.OCEAN_GOLEM_CHEST.get().defaultBlockState());
    private final TowerChestBlockEntity oceanTowerChestEntity = new TowerChestBlockEntity(BlockPos.ZERO, BTBlocks.OCEAN_CHEST.get().defaultBlockState());
    private final BlockEntityRenderDispatcher dispatcher;

    public BTChestItemRenderer(BlockEntityRenderDispatcher dispatcherIn, EntityModelSet modelSet) {
        super(dispatcherIn, modelSet);
        this.dispatcher = dispatcherIn;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLightIn, int combineOverLayIn) {
        Item item = itemStack.getItem();
        BlockEntity blockEntity = null;
        if (item instanceof BlockItem) {
            if (item == BTBlocks.LAND_CHEST.get().asItem()) {
                blockEntity = this.landTowerChestEntity;

            } else if (item == BTBlocks.LAND_GOLEM_CHEST.get().asItem()){
                blockEntity = this.landGolemChestEntity;

            } else if (item == BTBlocks.OCEAN_CHEST.get().asItem()) {
                blockEntity = this.oceanTowerChestEntity;

            } else if (item == BTBlocks.OCEAN_GOLEM_CHEST.get().asItem()){
                blockEntity = this.oceanGolemChestEntity;

            }
            if (blockEntity == null) {
                blockEntity = this.landTowerChestEntity;
            }
            this.dispatcher.renderItem(blockEntity, poseStack, multiBufferSource, combinedLightIn, combineOverLayIn);
        }
    }
}