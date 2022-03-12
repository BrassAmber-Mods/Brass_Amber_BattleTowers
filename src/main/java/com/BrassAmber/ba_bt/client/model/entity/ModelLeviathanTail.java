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

public class ModelLeviathanTail<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "modelLeviathanTail"), "main");
	
	private final ModelPart root;
	private final ModelPart tail;
	private final ModelPart tailMiddle;
	private final ModelPart tailFarMiddle;
	private final ModelPart tailEnd;
	private final ModelPart tailTip;
	private final ModelPart tailFin;

	public ModelLeviathanTail(ModelPart root) {
		this.root = root.getChild("root");
		this.tail = root.getChild("tail");
		this.tailMiddle = root.getChild("middle");
		this.tailFarMiddle = root.getChild("farmid");
		this.tailEnd = root.getChild("end");
		this.tailTip = root.getChild("tip");
		this.tailFin = root.getChild("fin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(68, 0).addBox(-5.0F, -5.0F, 0.25F, 10.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -8.25F));

		PartDefinition middle = tail.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(81, 32).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 16.25F));

		PartDefinition farmid = middle.addOrReplaceChild("farmid", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 11.0F));

		PartDefinition end = farmid.addOrReplaceChild("end", CubeListBuilder.create().texOffs(0, 54).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		PartDefinition tip = end.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(41, 32).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		tip.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(25, 19).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 4.5F));

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