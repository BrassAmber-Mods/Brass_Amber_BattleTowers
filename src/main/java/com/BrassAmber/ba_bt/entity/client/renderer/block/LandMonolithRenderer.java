package com.BrassAmber.ba_bt.entity.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandMonolithRenderer extends MonolithRendererAbstract {

	public LandMonolithRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, "land_monolith");
	}
}
