package com.BrassAmber.ba_bt.client.renderer;

import com.BrassAmber.ba_bt.client.BTChestTextures;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandGolemChestTileEntityRenderer extends BTChestTileEntityRendererAbstract {

	public LandGolemChestTileEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.LAND_GOLEM_CHEST_TEXTURES);
	}
}
