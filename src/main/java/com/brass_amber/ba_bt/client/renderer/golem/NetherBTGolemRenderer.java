package com.brass_amber.ba_bt.client.renderer.golem;

import com.brass_amber.ba_bt.client.model.hostile.LandGolemModel;
import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherBTGolemRenderer extends AbstractBTGolemRenderer<BTAbstractGolem, LandGolemModel> {
	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/golem/nether_golem/nether_golem_dormant");
	public static ModelLayerLocation LAYER = new ModelLayerLocation(TEXTURE, "main");

	public NetherBTGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new LandGolemModel(context.bakeLayer(LAYER), LAYER), "nether_golem");
		this.setGolemTextures("nether_golem_dormant", "nether_golem", "nether_golem");
	}
}
