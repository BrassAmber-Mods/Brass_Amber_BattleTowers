package com.BrassAmber.ba_bt.client.renderer.golem;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BTAbstractGolemRenderer<E extends BTAbstractGolem, M extends EntityModel<E>> extends MobRenderer<E, M> {
	private static final float SCALE = BTAbstractGolem.SCALE;
	protected ResourceLocation golemTexturesDormant;
	protected ResourceLocation golemTexturesAwaken;
	protected ResourceLocation golemTexturesEnraged;
	private String golemType;

	public BTAbstractGolemRenderer(EntityRendererProvider.Context context, M model, String golemType) {
		super(context, model, 0.5F * SCALE);
		this.golemType = golemType;
	}

	@Override
	protected void scale(E entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
		poseStack.scale(SCALE, SCALE, SCALE);
	}

	@Override
	public ResourceLocation getTextureLocation(BTAbstractGolem entity) {
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
