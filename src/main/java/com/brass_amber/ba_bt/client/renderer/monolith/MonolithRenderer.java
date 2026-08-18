package com.brass_amber.ba_bt.client.renderer.monolith;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.block.MonolithEntity;
import com.brass_amber.ba_bt.client.model.block.MonolithModel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class MonolithRenderer extends EntityRenderer<MonolithEntity> {
    protected final MonolithModel<MonolithEntity> model;

	public MonolithRenderer(EntityRendererProvider.Context context) {
		super(context);
        this.model = new MonolithModel<>(context.bakeLayer(MonolithModel.LAYER_LOCATION));
	}

	@Override
	public void render(MonolithEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
		float ageInTicks = (float) pEntity.tickCount + pPartialTick;
		this.model.setupAnim(pEntity, 0f, 0f, ageInTicks, 0f, 0f);
	}

	/**
	 * Returns the correct texture.
	 */
	@Override
	public ResourceLocation getTextureLocation(MonolithEntity entityIn) {
		if (entityIn.towerData != null) {
			return BABattleTowers.locate("textures/entity/monolith/" + entityIn.towerData.getName() + "_monolith.png");
		}
		return BABattleTowers.locate("textures/entity/monolith/land_monolith.png");
	}

}
