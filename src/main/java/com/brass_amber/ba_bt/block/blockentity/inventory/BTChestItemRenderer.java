package com.brass_amber.ba_bt.block.blockentity.inventory;

import com.brass_amber.ba_bt.block.block.BTChestBlock;
import com.brass_amber.ba_bt.block.blockentity.chest.BTChestBlockEntity;
import com.brass_amber.ba_bt.util.TowerType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;



public class BTChestItemRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {

    public static BTChestItemRenderer INSTANCE = new BTChestItemRenderer();


    public BTChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLightIn, int combinedOverlayIn) {
        Block block = Block.byItem(itemStack.getItem());

        if (block instanceof BTChestBlock chestBlock) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(
                    chestBlock.newBlockEntity(BlockPos.ZERO, chestBlock.defaultBlockState()),
                    poseStack, multiBufferSource, combinedLightIn, combinedOverlayIn
            );
        } else {
            super.renderByItem(itemStack, displayContext, poseStack, multiBufferSource, combinedLightIn, combinedOverlayIn);
        }
    }
}
