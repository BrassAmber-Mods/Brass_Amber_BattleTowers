package com.BrassAmber.ba_bt.client.renderer.golem;

import com.BrassAmber.ba_bt.client.inventory.model.hostile.SkyGolemModel;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyGolemRenderer extends BTAbstractGolemRenderer<BTAbstractGolem, SkyGolemModel> {
	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/golem/sky_golem/sky_golem_dormant");
	public static ModelLayerLocation LAYER = new ModelLayerLocation(TEXTURE, "main");

	public SkyGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new SkyGolemModel(context.bakeLayer(LAYER), LAYER), "sky_golem");
		this.setGolemTextures("sky_golem_dormant", "sky_golem", "sky_golem");
	}
}
