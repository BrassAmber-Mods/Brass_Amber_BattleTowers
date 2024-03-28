package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.client.BTChestTextures;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandBTChestRenderer extends AbstractBTChestRenderer {

	public LandBTChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.LAND_CHEST_TEXTURES);
	}
}
