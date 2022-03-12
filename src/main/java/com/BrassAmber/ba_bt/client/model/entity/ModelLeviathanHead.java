package com.BrassAmber.ba_bt.client.model.entity;
// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports

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

public class ModelLeviathanHead<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "modelLeviathanHead"), "main");
	
	private final ModelPart root;
	private final ModelPart head;

	public ModelLeviathanHead(ModelPart root) {
		this.root = root.getChild("root");
		this.head = root.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(6.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-6.0F, -6.0F, -8.0F, 12.0F, 12.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-8.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 40).addBox(-6.0F, 6.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 40).mirror().addBox(-6.0F, -8.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition spikeParts = root.addOrReplaceChild("spikeParts", CubeListBuilder.create(), PartPose.offset(0.0F, -8.8346F, 0.4616F));

		spikeParts.addOrReplaceChild("13_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 8.5F, -9.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, 1.5708F, 0.6894F, 0.0F));

		spikeParts.addOrReplaceChild("12_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, 8.5F, -9.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, 1.5708F, -0.6894F, 0.0F));

		spikeParts.addOrReplaceChild("11_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 3.5F, 7.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, -1.5708F, 0.6894F, 0.0F));

		spikeParts.addOrReplaceChild("10_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 3.5F, 7.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, -1.5708F, -0.6894F, 0.0F));

		spikeParts.addOrReplaceChild("9_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 9.5F, 3.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-1.0F, -11.0F, 5.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, -0.6894F, 0.0F, 0.0F));

		spikeParts.addOrReplaceChild("7_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 14.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, -11.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, 0.0F, 0.0F, -0.6894F));

		spikeParts.addOrReplaceChild("6_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(3.0F, 14.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, -11.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, 0.0F, 0.0F, 0.6894F));

		spikeParts.addOrReplaceChild("2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -7.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.1654F, -0.4616F, 0.6894F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, buffer, packedLight, packedOverlay);
	}
}