package com.BrassAmber.ba_bt.client.renderer.golem;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BTAbstractGolemRenderer<E extends BTGolemEntityAbstract, M extends EntityModel<E>> extends MobRenderer<E, M> {
	private static final float SCALE = BTGolemEntityAbstract.SCALE;
	protected ResourceLocation golemTexturesDormant;
	protected ResourceLocation golemTexturesAwaken;
	protected ResourceLocation golemTexturesEnraged;
	private String golemType;

	public BTAbstractGolemRenderer(EntityRendererManager renderManagerIn, M model, String golemType) {
		super(renderManagerIn, model, 0.5F * SCALE);
		this.golemType = golemType;
	}

	@Override
	protected void scale(E entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(SCALE, SCALE, SCALE);
	}

	@Override
	public ResourceLocation getTextureLocation(BTGolemEntityAbstract entity) {
		return entity.isEnraged() ? golemTexturesEnraged : entity.isAwake() ? golemTexturesAwaken : golemTexturesDormant;
	}

	protected void setGolemTextures(String dormant, String awake, String special) {
		this.golemTexturesDormant = this.setGolemTexture(dormant);
		this.golemTexturesAwaken = this.setGolemTexture(awake);
		this.golemTexturesEnraged = this.setGolemTexture(special);
	}
	
	protected ResourceLocation setGolemTexture(String textureName) {
		return BrassAmberBattleTowers.locate("textures/entity/golem/" + this.golemType + "/" + textureName + ".png");
	}
}
