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
public class MonolithEntityRenderer extends EntityRenderer<MonolithEntity> {
	private static final ResourceLocation MONOLITH_0 = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_0.png");
	private static final ResourceLocation MONOLITH_1 = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_1.png");
	private static final ResourceLocation MONOLITH_2 = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_2.png");
	private static final ResourceLocation MONOLITH_3 = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_3.png");
	private static final ResourceLocation MONOLITH_0E = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_0_e.png");
	private static final ResourceLocation MONOLITH_1E = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_1_e.png");
	private static final ResourceLocation MONOLITH_2E = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_2_e.png");
	private static final ResourceLocation MONOLITH_3E = BrassAmberBattleTowers.locate("textures/entity/monolith/monolith_3_e.png");
	private final ModelRenderer crystal = new ModelRenderer(128, 64, 0, 0);
	private final ModelRenderer crystalOverlay = new ModelRenderer(128, 64, 0, 0);

	public MonolithEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);

		// Monolith Model
		crystal.setPos(0.0F, 13.0F, 0.0F);
		crystal.texOffs(16, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 4.0F, 8.0F);
		crystal.texOffs(8, 14).addBox(-6.0F, -17.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		crystal.texOffs(0, 32).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 16.0F, 16.0F);
		crystal.texOffs(60, 14).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		crystal.texOffs(52, 0).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 4.0F, 8.0F);

		crystalOverlay.setPos(0.0F, 13.0F, 0.0F);
		crystalOverlay.texOffs(16, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 4.0F, 8.0F);
		crystalOverlay.texOffs(8, 14).addBox(-6.0F, -17.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		crystalOverlay.texOffs(0, 32).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 16.0F, 16.0F);
		crystalOverlay.texOffs(60, 14).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 4.0F, 12.0F);
		crystalOverlay.texOffs(52, 0).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 4.0F, 8.0F);
	}

	@Override
	public void render(MonolithEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

		float speedModifier = 32.0f;
		float amplitude = 0.25F;
		float defaultHeight = 0.5f;

		float floatingInput = entityIn.floatingRotation / speedModifier;
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
			return MONOLITH_0;
		case 1:
			return MONOLITH_1;
		case 2:
			return MONOLITH_2;
		case 3:
		default:
			return MONOLITH_3;
		}
	}

	public ResourceLocation getGlowingEntityTexture(MonolithEntity entityIn) {
		switch (entityIn.getKeyCountInEntity()) {
		case 0:
			return MONOLITH_0E;
		case 1:
			return MONOLITH_1E;
		case 2:
			return MONOLITH_2E;
		case 3:
		default:
			return MONOLITH_3E;
		}
	}

}
