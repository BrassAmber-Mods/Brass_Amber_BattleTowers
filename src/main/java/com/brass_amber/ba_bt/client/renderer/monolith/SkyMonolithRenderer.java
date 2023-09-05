package com.brass_amber.ba_bt.client.renderer.monolith;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyMonolithRenderer extends MonolithRendererAbstract {
	public static final String SKY_MONOLITH = "sky_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/sky_monolith/sky_monolith_0.png"), "main");

	public SkyMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, SKY_MONOLITH, TEXTURE);
	}
}
