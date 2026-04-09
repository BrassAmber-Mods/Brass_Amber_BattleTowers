package com.brass_amber.ba_bt.client.renderer.golem;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.hostile.golem.AbstractGolem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;



public abstract class AbstractGolemRenderer<E extends AbstractGolem, M extends EntityModel<E>> extends MobRenderer<E, M> {
	private static final float SCALE = AbstractGolem.SCALE;
	protected ResourceLocation golemTexturesDormant;
	protected ResourceLocation golemTexturesAwaken;
	protected ResourceLocation golemTexturesEnraged;
	private String golemType;

	public AbstractGolemRenderer(EntityRendererProvider.Context context, M model, String golemType) {
		super(context, model, 0.5F * SCALE);
		this.golemType = golemType;
	}

	@Override
	protected void scale(E entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
		poseStack.scale(SCALE, SCALE, SCALE);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractGolem entity) {
		return entity.isEnraged() ? golemTexturesEnraged : entity.isAwake() ? golemTexturesAwaken : golemTexturesDormant;
	}

	protected void setGolemTextures(String dormant, String awake, String special) {
		this.golemTexturesDormant = this.setGolemTexture(dormant);
		this.golemTexturesAwaken = this.setGolemTexture(awake);
		this.golemTexturesEnraged = this.setGolemTexture(special);
	}
	
	protected ResourceLocation setGolemTexture(String textureName) {
		return BABattleTowers.locate("textures/entity/golem/" + this.golemType + "/" + textureName + ".png");
	}
}
