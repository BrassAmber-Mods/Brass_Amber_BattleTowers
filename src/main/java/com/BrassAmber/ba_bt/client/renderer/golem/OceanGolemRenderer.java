package com.BrassAmber.ba_bt.client.renderer.golem;

import com.BrassAmber.ba_bt.client.inventory.model.hostile.OceanGolemModel;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanGolemRenderer extends BTAbstractGolemRenderer<BTAbstractGolem, OceanGolemModel> {
	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/golem/ocean_golem/ocean_golem_dormant");
	public static ModelLayerLocation LAYER = new ModelLayerLocation(TEXTURE, "main");

	public OceanGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new OceanGolemModel(context.bakeLayer(LAYER), LAYER),"ocean_golem");
		this.setGolemTextures("ocean_golem_dormant", "ocean_golem", "ocean_golem");
	}
}
