package com.BrassAmber.ba_bt.entity.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndMonolithRenderer extends MonolithRendererAbstract {

	public EndMonolithRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, "end_monolith");
	}
}
