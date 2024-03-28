package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.client.BTChestTextures;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanBTGolemChestRenderer extends AbstractBTChestRenderer {

	public OceanBTGolemChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.OCEAN_GOLEM_CHEST_TEXTURES);
	}
}
