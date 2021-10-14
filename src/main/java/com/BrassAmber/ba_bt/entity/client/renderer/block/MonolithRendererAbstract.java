package com.BrassAmber.ba_bt.entity.client.renderer.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MonolithRendererAbstract extends EntityRenderer<MonolithEntity> {
	private final ModelRenderer crystal = new ModelRenderer(128, 64, 0, 0);
	private final ModelRenderer crystalOverlay = new ModelRenderer(128, 64, 0, 0);
	// TODO Make arrays?
	protected ResourceLocation monolith0;
	protected ResourceLocation monolith1;
	protected ResourceLocation monolith2;
	protected ResourceLocation monolith3;
	protected ResourceLocation monolith0E;
	protected ResourceLocation monolith1E;
	protected ResourceLocation monolith2E;
	protected ResourceLocation monolith3E;
	private String monolithType;

	public MonolithRendererAbstract(EntityRendererManager renderManagerIn, String monolithType) {
		super(renderManagerIn);
		this.monolithType = monolithType;
		this.setMonolithTextures();

		// Monolith Model
		this.crystal.setPos(0.0F, 13.0F, 0.0F);
		this.crystal.texOffs(16, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 4.0F, 8.0F);
		this.crystal.texOffs(8, 14).addBox(-6.0F, -17.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		this.crystal.texOffs(0, 32).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 16.0F, 16.0F);
		this.crystal.texOffs(60, 14).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		this.crystal.texOffs(52, 0).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 4.0F, 8.0F);

		this.crystalOverlay.setPos(0.0F, 13.0F, 0.0F);
		this.crystalOverlay.texOffs(16, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 4.0F, 8.0F);
		this.crystalOverlay.texOffs(8, 14).addBox(-6.0F, -17.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		this.crystalOverlay.texOffs(0, 32).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 16.0F, 16.0F);
		this.crystalOverlay.texOffs(60, 14).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		this.crystalOverlay.texOffs(52, 0).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 4.0F, 8.0F);
	}

	@Override
	public void render(MonolithEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

		float speedModifier = 32.0f;
		float amplitude = 0.25F;
		float defaultHeight = 0.5f;

		float floatingInput = entityIn.getFloatingRotation() / speedModifier;
		float floatingWave = amplitude * MathHelper.sin(floatingInput) + defaultHeight;

		//	Bobbing up and down animation
		matrixStackIn.translate(0.0D, (double) (floatingWave), 0.0D);

		//	Rotate the Monolith [N,E,S,W] during placement 
		float yaw = entityIn.yRot;
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(yaw));

		// TODO make the smaller parts of the monolith rotate 
		//	    float f1 = ((float)entityIn.rotation) * 10;
		//	    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));

		matrixStackIn.pushPose();
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		// Render the base textures
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(this.getTextureLocation(entityIn)));
		this.crystal.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);

		// Render the blue glowing textures
		matrixStackIn.scale(1.01F, 1.01F, 1.01F);
		ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutout(this.getGlowingEntityTexture(entityIn)));
		int light = /*config.disableGlow.get() ? packedLightIn : */200;
		this.crystalOverlay.render(matrixStackIn, ivertexbuilder, light, OverlayTexture.NO_OVERLAY);
		matrixStackIn.popPose();
	}

	/*
	 * Returns the correct texture according to the amount of keys
	 */
	@Override
	public ResourceLocation getTextureLocation(MonolithEntity entityIn) {
		switch (entityIn.getKeyCountInEntity()) {
		case 0:
		default:
			return monolith0;
		case 1:
			return monolith1;
		case 2:
			return monolith2;
		case 3:
			return monolith3;
		}
	}

	protected ResourceLocation getGlowingEntityTexture(MonolithEntity entityIn) {
		switch (entityIn.getKeyCountInEntity()) {
		case 0:
		default:
			return monolith0E;
		case 1:
			return monolith1E;
		case 2:
			return monolith2E;
		case 3:
			return monolith3E;
		}
	}

	protected void setMonolithTextures() {
		this.monolith0 = this.setMonolithTexture(this.monolithType + "_0");
		this.monolith1 = this.setMonolithTexture(this.monolithType + "_1");
		this.monolith2 = this.setMonolithTexture(this.monolithType + "_2");
		this.monolith3 = this.setMonolithTexture(this.monolithType + "_3");
		this.monolith0E = this.setMonolithTexture(this.monolithType + "_0_e");
		this.monolith1E = this.setMonolithTexture(this.monolithType + "_1_e");
		this.monolith2E = this.setMonolithTexture(this.monolithType + "_2_e");
		this.monolith3E = this.setMonolithTexture(this.monolithType + "_3_e");
	}

	protected ResourceLocation setMonolithTexture(String textureName) {
		return BrassAmberBattleTowers.locate("textures/entity/monolith/" + this.monolithType + "/" + textureName + ".png");
	}
}
