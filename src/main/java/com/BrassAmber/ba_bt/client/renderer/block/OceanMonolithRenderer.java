package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanMonolithRenderer extends MonolithRendererAbstract {

	public OceanMonolithRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, "ocean_monolith");
	}
}
