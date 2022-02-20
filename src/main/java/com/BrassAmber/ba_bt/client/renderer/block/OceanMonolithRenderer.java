package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanMonolithRenderer extends MonolithRendererAbstract {
	public static final String OCEAN_MONOLITH = "ocean_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/ocean_monolith/ocean_monolith_0.png"), "main");

	public OceanMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, OCEAN_MONOLITH);
	}
}
