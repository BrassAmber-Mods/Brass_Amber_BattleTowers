package com.BrassAmber.ba_bt.client.model.hostile;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * Ocean golem tail doesn't need an animation but a light waving would be fine
 *
 */
@OnlyIn(Dist.CLIENT)
public class OceanGolemModel extends EntityModel<BTGolemEntityAbstract> {
	// TODO
	private final ModelRenderer bone;
	private final ModelRenderer cube_r1;
	
	public OceanGolemModel() {
		this(0.0f);
	}

	public OceanGolemModel(float scale) {
		this.texWidth = 128;
		this.texHeight = 64;

		this.bone = new ModelRenderer(this);
		this.bone.setPos(1.0F, -2.0F, -13.0F);
		this.bone.texOffs(0, 0).addBox(-9.0F, -46.0F, 0.0F, 16.0F, 16.0F, 16.0F, 0.0F + scale, false);
		this.bone.texOffs(32, 32).addBox(-9.0F, -30.0F, 4.0F, 16.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.bone.texOffs(0, 32).addBox(-17.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.bone.texOffs(80, 32).addBox(7.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);

		this.cube_r1 = new ModelRenderer(this);
		this.cube_r1.setPos(0.0F, 3.0F, 12.0F);
		this.bone.addChild(this.cube_r1);
		setRotationAngle(this.cube_r1, 0.3491F, 0.0F, 0.0F);
		this.cube_r1.texOffs(55, 0).addBox(-8.0F, 9.2927F, -0.6F, 14.0F, 12.0F, 0.0F, 0.0F + scale, false);
		this.cube_r1.texOffs(112, 47).addBox(-3.0F, 6.2927F, -2.1F, 4.0F, 6.0F, 3.0F, 0.0F + scale, false);
		this.cube_r1.texOffs(89, 0).addBox(-5.0F, -0.7073F, -3.1F, 8.0F, 7.0F, 5.0F, 0.0F + scale, false);
		this.cube_r1.texOffs(88, 13).addBox(-7.0F, -11.7073F, -4.1F, 12.0F, 11.0F, 7.0F, 0.0F + scale, false);
	}

	@Override
	public void setupAnim(BTGolemEntityAbstract entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.bone.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}