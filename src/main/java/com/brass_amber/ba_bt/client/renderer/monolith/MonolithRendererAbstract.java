package com.brass_amber.ba_bt.client.renderer.monolith;

import java.util.List;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.client.model.block.MonolithModel;
import com.google.common.collect.Lists;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MonolithRendererAbstract extends EntityRenderer<BTMonolith> {
	private final MonolithModel monolith;
	private List<ResourceLocation> monolithTextures = Lists.newArrayList();
	private List<ModelLayerLocation> monolithLayers = Lists.newArrayList();
	private String monolithType;

	public MonolithRendererAbstract(EntityRendererProvider.Context context, String monolithType, ModelLayerLocation location) {
		super(context);
		this.monolithType = monolithType;
		// Set the correct textures for each Monolith type.
		this.setMonolithTextures();
		this.monolith = new MonolithModel(
				context.bakeLayer(location),
				location);
	}

	/**
	 * Referenced from {@link net.minecraft.client.renderer.entity.LivingEntityRenderer}
	 */
	@Override
	public void render(BTMonolith entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		/*********************************************************** Animations ********************************************************/
		float speedModifier = 32.0f;
		float amplitude = 0.25F;
		float defaultHeight = 0.5f;

		float floatingInput = entityIn.getFloatingRotation() / speedModifier;
		float floatingWave = amplitude * Mth.sin(floatingInput) + defaultHeight;

		//	Bobbing up and down animation.
		matrixStackIn.translate(0.0D, (double) (floatingWave), 0.0D);

		//	Rotate the Monolith [N,E,S,W] during placement.
		float yaw = entityIn.getYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));

		// TODO make the smaller parts of the monolith rotate 
		//	    float f1 = ((float)entityIn.rotation) * 10;
		//	    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));

		/*********************************************************** Rendering ********************************************************/

		matrixStackIn.pushPose();
		// Model is upside down for some reason. (No idea why!)
		matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
		matrixStackIn.translate(0.0D, -2.0D, 0.0D);

		// Move model to the middle of the hit-box.
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		// Render the textures.
		VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entitySolid(this.getTextureLocation(entityIn)));
		this.monolith.renderToBuffer(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		// Finish.
		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	 * Returns the correct texture according to the amount of keys or eyes.
	 */
	@Override
	public ResourceLocation getTextureLocation(BTMonolith entityIn) {
		int eyeSlotIncrement = entityIn.isEyeSlotDisplayed() ? 1 : 0;
		int textureLocation = entityIn.getKeyCountInEntity() + eyeSlotIncrement;
		return this.getMonolithTexture(textureLocation);
	}

	/**
	 * Get the correct texture from the List.
	 */
	private ResourceLocation getMonolithTexture(int textureLocation) {
		int maxAmountOfTextures = this.isLandMonolith() ? 3 : 4;
		return this.monolithTextures.get(Mth.clamp(textureLocation, 0, maxAmountOfTextures));
	}

	/**
	 * Initialize all the Monolith textures automatically.
	 */
	private void setMonolithTextures() {
		int maxTextures = this.isLandMonolith() ? 3 : 4;
		for (int x = 0; x <= maxTextures; x++) {
			ResourceLocation textureLocation = this.setMonolithTextureLocation(this.monolithType + "_" + x);
			this.monolithTextures.add(textureLocation);
			this.monolithLayers.add(new ModelLayerLocation(textureLocation, "main"));
		}
	}

	private ResourceLocation setMonolithTextureLocation(String textureName) {
		return BrassAmberBattleTowers.locate("entity/monolith/" + this.monolithType + "/" + textureName + ".png");
	}

	/**
	 * Check for the Land Monolith, because that one doesn't have an Eye texture.
	 */
	private boolean isLandMonolith() {
		return this.monolithType.equals(LandMonolithRenderer.LAND_MONOLITH);
	}
}
