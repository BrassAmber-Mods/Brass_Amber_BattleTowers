package com.brass_amber.ba_bt.client.renderer.monolith;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanBTMonolithRenderer extends AbstractBTMonolithRenderer {
	public static final String OCEAN_MONOLITH = "ocean_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/ocean_monolith/ocean_monolith_0.png"), "main");

	public OceanBTMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, OCEAN_MONOLITH, TEXTURE);
	}
}
