package com.BrassAmber.ba_bt.entity.client.model.hostile;

import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandGolemModel extends EntityModel<BTGolemEntity> {
	// TODO
	private final ModelRenderer allBodyParts;
	
	public LandGolemModel() {
		this(0.0f);
	}

	public LandGolemModel(float scale) {
		this.texWidth = 128;
		this.texHeight = 64;

		this.allBodyParts = new ModelRenderer(this);
		this.allBodyParts.setPos(1.0F, 6.0F, -8.0F);
		this.allBodyParts.texOffs(0, 0).addBox(-9.0F, -46.0F, 0.0F, 16.0F, 16.0F, 16.0F, 0.0F + scale, false);
		this.allBodyParts.texOffs(32, 32).addBox(-9.0F, -30.0F, 4.0F, 16.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.allBodyParts.texOffs(0, 32).addBox(-17.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.allBodyParts.texOffs(80, 32).addBox(7.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.allBodyParts.texOffs(80, 32).addBox(-9.0F, -6.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.allBodyParts.texOffs(0, 32).addBox(-1.0F, -6.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
	}

	@Override
	public void setupAnim(BTGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.allBodyParts.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}