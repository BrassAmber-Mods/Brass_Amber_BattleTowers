package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.client.BTChestTextures;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanBTChestRenderer extends AbstractBTChestRenderer {

	public OceanBTChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.OCEAN_CHEST_TEXTURES);
	}
}
