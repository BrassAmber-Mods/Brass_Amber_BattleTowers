package com.brass_amber.ba_bt.block.blockentity.inventory;

import com.brass_amber.ba_bt.block.block.BTChestBlock;
import com.brass_amber.ba_bt.block.blockentity.BTChestBlockEntity;
import com.brass_amber.ba_bt.util.GolemType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class BTChestItemRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {

    public static BTChestItemRenderer INSTANCE = new BTChestItemRenderer();

    private BTChestBlockEntity[] tiles = new BTChestBlockEntity[GolemType.VALUES.length];
    private BTChestBlockEntity[] tilesGolem = new BTChestBlockEntity[GolemType.VALUES.length];

    {
        for (GolemType type : GolemType.VALUES) {
            tiles[type.ordinal()] = new BTChestBlockEntity(BlockPos.ZERO, GolemType.getChestBlockForType(type, false).defaultBlockState(), type, false);
            tilesGolem[type.ordinal()] = new BTChestBlockEntity(BlockPos.ZERO, GolemType.getChestBlockForType(type, true).defaultBlockState(), type, true);
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
        if (block instanceof BTChestBlock) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.tiles[((BTChestBlock)block).getType().ordinal()], poseStack, multiBufferSource, combinedLightIn, combinedOverlayIn);
        } else {
            super.renderByItem(itemStack, displayContext, poseStack, multiBufferSource, combinedLightIn, combinedOverlayIn);
        }
    }
}
