package com.BrassAmber.ba_bt.client.renderer.chest;

import com.BrassAmber.ba_bt.client.BTChestTextures;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanChestBlockEntityRenderer extends BTChestBlockEntityRendererAbstract {

	public OceanChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.OCEAN_CHEST_TEXTURES);
	}
}
