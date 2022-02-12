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
 * Sky golem legs do not move and she uses vex animations [weapon hand raises when she charges]
 *
 */
@OnlyIn(Dist.CLIENT)
public class SkyGolemModel extends EntityModel<BTGolemEntityAbstract> {
	// TODO
	private final ModelRenderer bone;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	
	public SkyGolemModel() {
		this(0.0f);
	}

	public SkyGolemModel(float scale) {
		this.texWidth = 128;
		this.texHeight = 64;

		this.bone = new ModelRenderer(this);
		this.bone.setPos(0.0F, 6.0F, -8.0F);
		this.bone.texOffs(0, 0).addBox(-8.0F, -46.0F, 0.0F, 16.0F, 16.0F, 16.0F, 0.0F + scale, false);
		this.bone.texOffs(32, 32).addBox(-8.0F, -30.0F, 4.0F, 16.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.bone.texOffs(80, 32).addBox(-16.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.bone.texOffs(80, 32).addBox(8.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.bone.texOffs(0, 32).addBox(-8.0F, -6.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);
		this.bone.texOffs(0, 32).addBox(0.0F, -6.0F, 4.0F, 8.0F, 24.0F, 8.0F, 0.0F + scale, false);

		this.cube_r1 = new ModelRenderer(this);
		this.cube_r1.setPos(3.0F, -23.0F, 12.0F);
		this.bone.addChild(this.cube_r1);
		setRotationAngle(this.cube_r1, 2.5261F, 0.5236F, 2.1863F);
		this.cube_r1.texOffs(78, 2).addBox(-20.0F, -3.0F, 0.0F, 22.0F, 28.0F, 0.0F, 0.0F + scale, false);

		this.cube_r2 = new ModelRenderer(this);
		this.cube_r2.setPos(-3.0F, -23.0F, 12.0F);
		this.bone.addChild(this.cube_r2);
		setRotationAngle(this.cube_r2, 0.6155F, 0.5236F, 0.9553F);
		this.cube_r2.texOffs(78, 2).addBox(-20.0F, -3.0F, 0.0F, 22.0F, 28.0F, 0.0F, 0.0F + scale, false);
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