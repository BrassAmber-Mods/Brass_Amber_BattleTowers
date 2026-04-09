package com.brass_amber.ba_bt.block.blockentity.inventory;

import com.brass_amber.ba_bt.block.block.GolemChestBlock;
import com.brass_amber.ba_bt.block.blockentity.chest.AbstractChestBlockEntity;
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

    private AbstractChestBlockEntity[] tiles = new AbstractChestBlockEntity[TowerType.VALUES.length];
    private AbstractChestBlockEntity[] tilesGolem = new AbstractChestBlockEntity[TowerType.VALUES.length];

    {
        for (TowerType type : TowerType.VALUES) {
            tiles[type.ordinal()] = new AbstractChestBlockEntity(TowerType.getChestForType(type, false), BlockPos.ZERO, TowerType.getChestBlockForType(type, false).defaultBlockState());
            tilesGolem[type.ordinal()] = new AbstractChestBlockEntity(TowerType.getChestForType(type, false),BlockPos.ZERO, TowerType.getChestBlockForType(type, true).defaultBlockState());
        }
    }

    public BTChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLightIn, int combinedOverlayIn) {
        Block block = Block.byItem(itemStack.getItem());
        if (block instanceof GolemChestBlock) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.tiles[((GolemChestBlock)block).getType().ordinal()], poseStack, multiBufferSource, combinedLightIn, combinedOverlayIn);
        } else {
            super.renderByItem(itemStack, displayContext, poseStack, multiBufferSource, combinedLightIn, combinedOverlayIn);
        }
    }
}
