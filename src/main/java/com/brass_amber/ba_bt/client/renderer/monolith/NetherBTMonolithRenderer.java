package com.brass_amber.ba_bt.client.renderer.monolith;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherBTMonolithRenderer extends AbstractBTMonolithRenderer {
	public static final String NETHER_MONOLITH = "nether_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/nether_monolith/nether_monolith_0.png"), "main");

	public NetherBTMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, NETHER_MONOLITH, TEXTURE);
	}
}
