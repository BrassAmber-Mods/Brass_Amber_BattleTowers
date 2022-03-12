package com.BrassAmber.ba_bt.client.model.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ModelPaladin<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "modelPaladin"), "main");
	private final ModelPart root;
	
	private final ModelPart[] spikes;

	public ModelPaladin(ModelPart root) {
		this.root = root.getChild("root");
		this.spikes = new ModelPart[] {
				root.getChild("top"),
				root.getChild("bottom"),
				root.getChild("frontLeft"),
				root.getChild("frontRight"),
				root.getChild("frontTop"),
				root.getChild("frontBottom"),
				root.getChild("backLeft"),
				root.getChild("backRight"),
				root.getChild("backTop"),
				root.getChild("backBottom"),
				root.getChild("leftTop"),
				root.getChild("leftBottom"),
				root.getChild("rightTop"),
				root.getChild("rightBottom")
		};
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 32.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 14).addBox(-6.0F, -6.0F, -8.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).mirror().addBox(-6.0F, -6.0F, 6.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 28).mirror().addBox(6.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 28).addBox(-8.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 40).addBox(-6.0F, 6.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 40).mirror().addBox(-6.0F, -8.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition spikeParts = head.addOrReplaceChild("spikeParts", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition topRotator = spikeParts.addOrReplaceChild("topRotator", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		topRotator.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottomRotator = spikeParts.addOrReplaceChild("bottomRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		bottomRotator.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition frontLeftRotator = spikeParts.addOrReplaceChild("frontLeftRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 8.0F, -6.0F, 1.5708F, -0.7854F, 0.0F));

		frontLeftRotator.addOrReplaceChild("frontLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frontRightRotator = spikeParts.addOrReplaceChild("frontRightRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 8.0F, -6.0F, 1.5708F, -0.7854F, 3.1416F));

		frontRightRotator.addOrReplaceChild("frontRight", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frontTopRotator = spikeParts.addOrReplaceChild("frontTopRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, -6.0F, 0.7854F, 0.0F, 0.0F));

		frontTopRotator.addOrReplaceChild("frontTop", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frontBottomRotator = spikeParts.addOrReplaceChild("frontBottomRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 14.0F, -6.0F, 2.3562F, 0.0F, 0.0F));

		frontBottomRotator.addOrReplaceChild("frontBottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition backLeftRotator = spikeParts.addOrReplaceChild("backLeftRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 8.0F, 6.0F, -1.5708F, -0.7854F, 3.1416F));

		backLeftRotator.addOrReplaceChild("backLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition backRightRotator = spikeParts.addOrReplaceChild("backRightRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 8.0F, 6.0F, -1.5708F, -0.7854F, 0.0F));

		backRightRotator.addOrReplaceChild("backRight", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition backTopRotator = spikeParts.addOrReplaceChild("backTopRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 6.0F, -0.7854F, 0.0F, 0.0F));

		backTopRotator.addOrReplaceChild("backTop", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition backBottomRotator = spikeParts.addOrReplaceChild("backBottomRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 14.0F, 6.0F, -2.3562F, 0.0F, 0.0F));

		backBottomRotator.addOrReplaceChild("backBottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftTopRotator = spikeParts.addOrReplaceChild("leftTopRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		leftTopRotator.addOrReplaceChild("leftTop", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftBottomRotator = spikeParts.addOrReplaceChild("leftBottomRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 14.0F, 0.0F, 3.1416F, 0.0F, -0.7854F));

		leftBottomRotator.addOrReplaceChild("leftBottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightTopRotator = spikeParts.addOrReplaceChild("rightTopRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		rightTopRotator.addOrReplaceChild("rightTop", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightBottomRotator = spikeParts.addOrReplaceChild("rightBottomRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 14.0F, 0.0F, 3.1416F, 0.0F, 0.7854F));

		rightBottomRotator.addOrReplaceChild("rightBottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, buffer, packedLight, packedOverlay);
	}
}