package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndMonolithRenderer extends MonolithRendererAbstract {
	public static final String END_MONOLITH = "end_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/end_monolith/end_monolith_0.png"), "main");

	public EndMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, END_MONOLITH, TEXTURE);
	}
}
