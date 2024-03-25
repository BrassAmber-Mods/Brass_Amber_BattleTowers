package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.client.BTChestTextures;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanGolemChestRenderer extends BTChestRendererAbstract {

	public OceanGolemChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context, BTChestTextures.OCEAN_GOLEM_CHEST_TEXTURES);
	}
}
