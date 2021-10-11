package com.BrassAmber.ba_bt.block.entity.client.inventory;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BTChestItemRenderer<T extends TileEntity> extends ItemStackTileEntityRenderer {
	private final Supplier<T> tileEntity;

	public BTChestItemRenderer(Supplier<T> tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		TileEntityRendererDispatcher.instance.renderItem(this.tileEntity.get(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
}
