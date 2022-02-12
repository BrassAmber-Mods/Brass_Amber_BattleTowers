package com.BrassAmber.ba_bt.client.model.block;

import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MonolithModel extends EntityModel<MonolithEntity> {
	private final ModelRenderer monolith;

	public MonolithModel() {
		this(0.0f);
	}

	public MonolithModel(float scale) {
		this.texWidth = 128;
		this.texHeight = 64;

		monolith = new ModelRenderer(this);
		monolith.setPos(0.0F, 8.0F, 0.0F);
		monolith.texOffs(16, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);
		monolith.texOffs(8, 14).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 4.0F, 12.0F, 0.0F, false);
		monolith.texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
		monolith.texOffs(60, 14).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 4.0F, 12.0F, 0.0F, false);
		monolith.texOffs(52, 0).addBox(-4.0F, 12.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);
	}

	/*********************************************************** Animations ********************************************************/

	@Override
	public void setupAnim(MonolithEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Previously the render function, render code was moved to a method below.	
	}

	/*********************************************************** Render ********************************************************/

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.monolith.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@SuppressWarnings("unused")
	private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}