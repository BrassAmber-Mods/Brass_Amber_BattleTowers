package com.BrassAmber.ba_bt.client.renderer;

import com.BrassAmber.ba_bt.client.BTChestTextures;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandChestTileEntityRenderer extends BTChestTileEntityRendererAbstract {

	public LandChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher, BTChestTextures.LAND_CHEST_TEXTURES);
	}
}
