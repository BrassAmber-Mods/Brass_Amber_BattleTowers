package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.client.BTChestTextures;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandChestRenderer extends BTChestRendererAbstract {

	public LandChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.LAND_CHEST_TEXTURES);
	}
}
