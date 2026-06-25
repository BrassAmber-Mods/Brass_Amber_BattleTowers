package com.brass_amber.ba_bt.client.model;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.brass_amber.ba_bt.entity.CorreyeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CorreyeModel extends EntityModel<CorreyeEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static ModelLayerLocation LAYER_LOCATION;
	private final ModelPart correye;
	private final ModelPart body;
	private final ModelPart tendril_right;
	private final ModelPart tendril_left;
	private final ModelPart tendril_middle;

	public CorreyeModel(ModelPart root, ModelLayerLocation location) {
		this.correye = root.getChild("correye");
		this.body = this.correye.getChild("body");
		this.tendril_right = this.body.getChild("tendril_right");
		this.tendril_left = this.body.getChild("tendril_left");
		this.tendril_middle = this.body.getChild("tendril_middle");
		LAYER_LOCATION = location;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition correye = partdefinition.addOrReplaceChild("correye", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body = correye.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -35.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition tendril_right = body.addOrReplaceChild("tendril_right", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, -27.0F, 7.0F, 0.7418F, 0.0F, 0.829F));

		PartDefinition tendril_top_r1 = tendril_right.addOrReplaceChild("tendril_top_r1", CubeListBuilder.create().texOffs(30, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 7.0F, 10.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition tendril_bottom_r1 = tendril_right.addOrReplaceChild("tendril_bottom_r1", CubeListBuilder.create().texOffs(28, 16).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 2.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition tendril_left = body.addOrReplaceChild("tendril_left", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, -27.0F, 7.0F, 0.0F, 0.0F, -0.9599F));

		PartDefinition tendril_top_r2 = tendril_left.addOrReplaceChild("tendril_top_r2", CubeListBuilder.create().texOffs(30, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 5.0F, 10.0F, -0.9599F, 0.0F, 0.0F));

		PartDefinition tendril_bottom_r2 = tendril_left.addOrReplaceChild("tendril_bottom_r2", CubeListBuilder.create().texOffs(28, 16).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition tendril_middle = body.addOrReplaceChild("tendril_middle", CubeListBuilder.create(), PartPose.offset(1.0F, -26.0F, 7.0F));

		PartDefinition tendril_top_r3 = tendril_middle.addOrReplaceChild("tendril_top_r3", CubeListBuilder.create().texOffs(30, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 5.0F, 10.0F, -0.9599F, 0.0F, 0.0F));

		PartDefinition tendril_bottom_r3 = tendril_middle.addOrReplaceChild("tendril_bottom_r3", CubeListBuilder.create().texOffs(28, 16).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(CorreyeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		correye.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}