package com.BrassAmber.ba_bt.client.model.block;// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ObeliskModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static ModelLayerLocation LAYER_LOCATION;
	private final ModelPart bone;
	private final ModelPart bb_main;

	public ObeliskModel(ModelPart root, ModelLayerLocation location) {
		this.bone = root.getChild("bone");
		this.bb_main = root.getChild("bb_main");
		LAYER_LOCATION = location;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(4, 68).addBox(-2.0F, -27.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -11.0F, -6.0F, 12.0F, 32.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(32, 28).addBox(-8.0F, 21.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(50, 54).addBox(-7.0F, 25.0F, -7.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-8.0F, 33.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition middle_filler = bone.addOrReplaceChild("middle_filler", CubeListBuilder.create(), PartPose.offset(0.0F, 37.0F, 0.0F));

		PartDefinition top = middle_filler.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition corner1 = top.addOrReplaceChild("corner1", CubeListBuilder.create().texOffs(26, 88).addBox(-6.0F, -40.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 89).addBox(-6.0F, -39.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 90).addBox(-6.0F, -38.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 91).addBox(-6.0F, -37.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 92).addBox(-6.0F, -36.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 93).addBox(-6.0F, -35.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition corner2 = top.addOrReplaceChild("corner2", CubeListBuilder.create().texOffs(26, 88).addBox(-6.0F, -40.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 89).addBox(-6.0F, -39.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 90).addBox(-6.0F, -38.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 91).addBox(-6.0F, -37.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 92).addBox(-6.0F, -36.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 93).addBox(-6.0F, -35.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition corner3 = top.addOrReplaceChild("corner3", CubeListBuilder.create().texOffs(26, 88).addBox(-6.0F, -40.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 89).addBox(-6.0F, -39.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 90).addBox(-6.0F, -38.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 91).addBox(-6.0F, -37.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 92).addBox(-6.0F, -36.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 93).addBox(-6.0F, -35.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition corner4 = top.addOrReplaceChild("corner4", CubeListBuilder.create().texOffs(26, 88).addBox(-6.0F, -40.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 89).addBox(-6.0F, -39.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 90).addBox(-6.0F, -38.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 91).addBox(-6.0F, -37.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 92).addBox(-6.0F, -36.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 93).addBox(-6.0F, -35.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bottom = middle_filler.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(0.0F, -64.0F, 0.0F));

		PartDefinition corner5 = bottom.addOrReplaceChild("corner5", CubeListBuilder.create().texOffs(26, 73).addBox(-6.0F, 39.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 74).addBox(-6.0F, 38.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 75).addBox(-6.0F, 37.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 76).addBox(-6.0F, 36.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 77).addBox(-6.0F, 35.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 78).addBox(-6.0F, 34.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition corner6 = bottom.addOrReplaceChild("corner6", CubeListBuilder.create().texOffs(26, 73).addBox(-6.0F, 39.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 74).addBox(-6.0F, 38.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 75).addBox(-6.0F, 37.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 76).addBox(-6.0F, 36.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 77).addBox(-6.0F, 35.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 78).addBox(-6.0F, 34.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition corner7 = bottom.addOrReplaceChild("corner7", CubeListBuilder.create().texOffs(26, 73).addBox(-6.0F, 39.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 74).addBox(-6.0F, 38.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 75).addBox(-6.0F, 37.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 76).addBox(-6.0F, 36.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 77).addBox(-6.0F, 35.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 78).addBox(-6.0F, 34.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition corner8 = bottom.addOrReplaceChild("corner8", CubeListBuilder.create().texOffs(26, 73).addBox(-6.0F, 39.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 74).addBox(-6.0F, 38.0F, -6.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 75).addBox(-6.0F, 37.0F, -6.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 76).addBox(-6.0F, 36.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 77).addBox(-6.0F, 35.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 78).addBox(-6.0F, 34.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(80, 24).addBox(-8.0F, -40.0F, -2.0F, 16.0F, 16.0F, 4.0F, new CubeDeformation(-2.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		bb_main.setRotation(bb_main.xRot, limbSwing * (float)Math.PI / 360, bb_main.zRot);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}