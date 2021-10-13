package com.BrassAmber.ba_bt.entity.client.renderer.golem;

import com.BrassAmber.ba_bt.entity.golem.BTGolemEntityAbstract;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BTAbstractGolemRenderer <E extends BTGolemEntityAbstract, M extends EntityModel<E>> extends MobRenderer<E, M> {
	private static final float SCALE = BTGolemEntityAbstract.SCALE;

	public BTAbstractGolemRenderer(EntityRendererManager renderManagerIn, M model) {
		super(renderManagerIn, model, 0.5F * SCALE);
	}

	@Override
	protected void scale(E entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(SCALE, SCALE, SCALE);
	}
}
