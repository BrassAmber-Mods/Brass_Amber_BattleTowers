package com.BrassAmber.ba_bt.client.renderer.chest;

import com.BrassAmber.ba_bt.client.BTChestTextures;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandGolemChestBlockEntityRenderer extends BTChestBlockEntityRendererAbstract {

	public LandGolemChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.LAND_GOLEM_CHEST_TEXTURES);
	}
}
