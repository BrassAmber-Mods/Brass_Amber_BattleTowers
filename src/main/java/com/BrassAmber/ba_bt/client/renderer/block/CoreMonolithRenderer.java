package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoreMonolithRenderer extends MonolithRendererAbstract {

	public CoreMonolithRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, "core_monolith");
	}
}
