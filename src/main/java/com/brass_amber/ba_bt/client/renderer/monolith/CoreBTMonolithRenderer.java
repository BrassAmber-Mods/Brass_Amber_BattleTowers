package com.brass_amber.ba_bt.client.renderer.monolith;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoreBTMonolithRenderer extends AbstractBTMonolithRenderer {
	public static final String CORE_MONOLITH = "core_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/core_monolith/core_monolith_0.png"), "main");

	public CoreBTMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, CORE_MONOLITH, TEXTURE);
	}
}
