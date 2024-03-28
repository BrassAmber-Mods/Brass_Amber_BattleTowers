package com.brass_amber.ba_bt.client.renderer.monolith;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndBTMonolithRenderer extends AbstractBTMonolithRenderer {
	public static final String END_MONOLITH = "end_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/end_monolith/end_monolith_0.png"), "main");

	public EndBTMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, END_MONOLITH, TEXTURE);
	}
}
