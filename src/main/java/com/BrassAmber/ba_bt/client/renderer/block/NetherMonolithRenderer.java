package com.BrassAmber.ba_bt.client.renderer.block;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherMonolithRenderer extends MonolithRendererAbstract {
	public static final String NETHER_MONOLITH = "nether_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/monolith/nether_monolith/nether_monolith_0.png"), "main");

	public NetherMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, NETHER_MONOLITH, TEXTURE);
	}
}
