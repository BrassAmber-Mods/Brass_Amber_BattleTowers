package com.BrassAmber.ba_bt.client.model.hostile;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * Sky golem legs do not move and she uses vex animations [weapon hand raises when she charges]
 *
 */
@OnlyIn(Dist.CLIENT)
public class SkyGolemModel extends EntityModel<BTAbstractGolem> {
	private final ModelPart bone;
	public static ModelLayerLocation LAYER;

	public SkyGolemModel(ModelPart root, ModelLayerLocation layer) {
		this.bone = root.getChild("bone");
		this.LAYER = layer;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, -46.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
						.texOffs(32, 32).addBox(-8.0F, -30.0F, 4.0F, 16.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(80, 32).addBox(-16.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(80, 32).addBox(8.0F, -30.0F, 4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 32).addBox(-8.0F, -6.0F, 4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 32).addBox(0.0F, -6.0F, 4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 6.0F, -8.0F));

		PartDefinition cube_r1 = partdefinition.addOrReplaceChild("cube_r1", CubeListBuilder.create()
				.texOffs(78, 2).addBox(-20.0F, -3.0F, 0.0F, 22.0F, 28.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(3.0F, -23.0F, 12.0F));


		PartDefinition cube_r2 = partdefinition.addOrReplaceChild("cube_r2", CubeListBuilder.create()

				.texOffs(78, 2).addBox(-20.0F, -3.0F, 0.0F, 22.0F, 28.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-3.0F, -23.0F, 12.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(BTAbstractGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, buffer, packedLight, packedOverlay);
	}
}