package com.brass_amber.ba_bt.client.model.hostile;

import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;

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
 * Ocean golem tail doesn't need an animation but a light waving would be fine
 *
 */
@OnlyIn(Dist.CLIENT)
public class OceanGolemModel extends EntityModel<BTAbstractGolem> {
	// TODO
	private final ModelPart bone;
	public static ModelLayerLocation LAYER;

	public OceanGolemModel(ModelPart root, ModelLayerLocation layer) {
		this.bone = root.getChild("bone");
		LAYER = layer;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-8.0F, -64.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(32, 32).addBox(-8.0F, -48.0F, -4.0F, 16.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(80, 32).addBox(8.0F, -48.0F, -4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(80, 32).addBox(-16.0F, -48.0F, -4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition tail = bone.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(89, 14).addBox(-6.0F, -24.0F, -3.0F, 12.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition tail_fin_r1 = tail.addOrReplaceChild("tail_fin_r1", CubeListBuilder.create().texOffs(55, 0).addBox(-7.0F, 0.0F, 3.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.0F, 0.0F));
		PartDefinition tail_lower_r1 = tail.addOrReplaceChild("tail_lower_r1", CubeListBuilder.create().texOffs(98, 20).addBox(-4.0F, -6.0F, 2.0F, 8.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition tail_mid_r1 = tail.addOrReplaceChild("tail_mid_r1", CubeListBuilder.create().texOffs(98, 20).addBox(-5.0F, -13.0F, 0.0F, 10.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(BTAbstractGolem entityAbstract, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, buffer, packedLight, packedOverlay);
	}
	
	


}