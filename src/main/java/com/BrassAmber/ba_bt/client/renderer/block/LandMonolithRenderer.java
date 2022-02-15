package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandMonolithRenderer extends MonolithRendererAbstract {
	public static final String LAND_MONOLITH = "land_monolith";

	public LandMonolithRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, LAND_MONOLITH);
	}
}
