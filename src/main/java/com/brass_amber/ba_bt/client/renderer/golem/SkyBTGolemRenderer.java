package com.brass_amber.ba_bt.client.renderer.golem;

import com.brass_amber.ba_bt.client.model.hostile.SkyGolemModel;
import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyBTGolemRenderer extends AbstractBTGolemRenderer<BTAbstractGolem, SkyGolemModel> {
	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/golem/sky_golem/sky_golem_dormant");
	public static ModelLayerLocation LAYER = new ModelLayerLocation(TEXTURE, "main");

	public SkyBTGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new SkyGolemModel(context.bakeLayer(LAYER), LAYER), "sky_golem");
		this.setGolemTextures("sky_golem_dormant", "sky_golem", "sky_golem");
	}
}
