package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandMonolithRenderer extends MonolithRendererAbstract {
	public static final String LAND_MONOLITH = "land_monolith";

	public LandMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, LAND_MONOLITH);
	}
}
