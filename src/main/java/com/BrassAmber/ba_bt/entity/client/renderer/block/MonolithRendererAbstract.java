package com.BrassAmber.ba_bt.entity.client.renderer.block;

import java.util.List;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.google.common.collect.Lists;
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
	private List<ResourceLocation> monolithTextures = Lists.newArrayList();
	private String monolithType;

	public MonolithRendererAbstract(EntityRendererManager renderManagerIn, String monolithType) {
		super(renderManagerIn);
		this.monolithType = monolithType;
		// Set the correct textures for each Monolith type.
		this.setMonolithTextures();

		// Monolith Model
		this.crystal.setPos(0.0F, 13.0F, 0.0F);
		this.crystal.texOffs(16, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 4.0F, 8.0F);
		this.crystal.texOffs(8, 14).addBox(-6.0F, -17.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		this.crystal.texOffs(0, 32).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 16.0F, 16.0F);
		this.crystal.texOffs(60, 14).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		this.crystal.texOffs(52, 0).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 4.0F, 8.0F);
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
		matrixStackIn.popPose();
	}

	/**
	 * Returns the correct texture according to the amount of keys or eyes.
	 */
	@Override
	public ResourceLocation getTextureLocation(MonolithEntity entityIn) {
		int eyeSlotIncrement = entityIn.isEyeSlotDisplayed() ? 1 : 0;
		int textureLocation = entityIn.getKeyCountInEntity() + eyeSlotIncrement;
		return this.getMonolithTexture(textureLocation);
	}

	/**
	 * Get the correct texture from the List.
	 */
	private ResourceLocation getMonolithTexture(int textureLocation) {
		int maxAmountOfTextures = this.isLandMonolith() ? 3 : 4;
		return this.monolithTextures.get(MathHelper.clamp(textureLocation, 0, maxAmountOfTextures));
	}
	
	/**
	 * Initialize all the Monolith textures automatically.
	 */
	private void setMonolithTextures() {
		int maxTextures = this.isLandMonolith() ? 3 : 4;
		for (int x = 0; x <= maxTextures; x++) {
			this.monolithTextures.add(this.setMonolithTextureLocation(this.monolithType + "_" + x));
		}
	}

	private ResourceLocation setMonolithTextureLocation(String textureName) {
		return BrassAmberBattleTowers.locate("textures/entity/monolith/" + this.monolithType + "/" + textureName + ".png");
	}
	
	/**
	 * Check for the Land Monolith, because that one doesn't have an Eye texture.
	 */
	private boolean isLandMonolith() {
		return this.monolithType.equals(LandMonolithRenderer.LAND_MONOLITH);
	}
}
