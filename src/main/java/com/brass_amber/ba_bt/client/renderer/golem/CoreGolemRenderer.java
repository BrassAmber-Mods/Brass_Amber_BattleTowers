package com.brass_amber.ba_bt.client.renderer.golem;

import com.brass_amber.ba_bt.client.model.hostile.CoreGolemModel;

import com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;



public class CoreGolemRenderer extends AbstractGolemRenderer<CoreGolem, CoreGolemModel> {
	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/golem/core_golem/core_golem_dormant");
	public static ModelLayerLocation LAYER = new ModelLayerLocation(TEXTURE, "main");
	protected ResourceLocation golemTexturesUnleashed;

	public CoreGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new CoreGolemModel(context.bakeLayer(LAYER), LAYER), "core_golem");
		this.setGolemTextures("core_golem_dormant", "core_golem", "core_golem_cracked");
		this.golemTexturesUnleashed = this.setGolemTexture("core_golem_unleashed");

	}

	@Override
	public ResourceLocation getTextureLocation(CoreGolem entity) {
		return entity.isUnleashed() ? golemTexturesUnleashed : entity.isEnraged() ? golemTexturesEnraged : entity.isAwake() ? golemTexturesAwaken : golemTexturesDormant;
	}

	@Override
	protected void scale(CoreGolem entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
		poseStack.scale(1.8f, 1.8f, 1.8f);
	}
}
