package com.BrassAmber.ba_bt.block.tileentity.client.renderer;

import com.BrassAmber.ba_bt.block.tileentity.client.BTChestTextures;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StoneChestTileEntityRenderer extends BTChestTileEntityRendererAbstract {

	public StoneChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher, BTChestTextures.STONE_CHEST_TEXTURES);
	}
}
