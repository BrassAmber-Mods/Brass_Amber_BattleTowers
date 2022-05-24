package com.BrassAmber.ba_bt.client.renderer.chest;

import com.BrassAmber.ba_bt.client.BTChestTextures;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanGolemChestBlockEntityRenderer extends BTChestBlockEntityRendererAbstract {

	public OceanGolemChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.OCEAN_GOLEM_CHEST_TEXTURES);
	}
}
