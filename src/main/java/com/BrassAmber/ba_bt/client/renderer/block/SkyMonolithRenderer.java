package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyMonolithRenderer extends MonolithRendererAbstract {

	public SkyMonolithRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, "sky_monolith");
	}
}
