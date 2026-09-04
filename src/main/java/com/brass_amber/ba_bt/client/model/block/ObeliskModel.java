package com.brass_amber.ba_bt.client.model.block;
// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.block.ObeliskEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ObeliskModel<T extends ObeliskEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BABattleTowers.locate("obelisk"), "main");
	private final ModelPart body;
	private final ModelPart middle_filler;
	private final ModelPart top;
	private final ModelPart corner1;
	private final ModelPart corner2;
	private final ModelPart corner3;
	private final ModelPart corner4;
	private final ModelPart bottom;
	private final ModelPart corner5;
	private final ModelPart corner6;
	private final ModelPart corner7;
	private final ModelPart corner8;
	private final ModelPart stone;

	public ObeliskModel(ModelPart root) {
		this.body = root.getChild("body");
		this.middle_filler = this.body.getChild("middle_filler");
		this.top = this.middle_filler.getChild("top");
		this.corner1 = this.top.getChild("corner1");
		this.corner2 = this.top.getChild("corner2");
		this.corner3 = this.top.getChild("corner3");
		this.corner4 = this.top.getChild("corner4");
		this.bottom = this.middle_filler.getChild("bottom");
		this.corner5 = this.bottom.getChild("corner5");
		this.corner6 = this.bottom.getChild("corner6");
		this.corner7 = this.bottom.getChild("corner7");
		this.corner8 = this.bottom.getChild("corner8");
		this.stone = this.body.getChild("stone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(4, 68).addBox(-2.0F, -27.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-4.0F, -21.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -11.0F, -6.0F, 12.0F, 32.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(32, 28).addBox(-8.0F, 21.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(50, 54).addBox(-7.0F, 25.0F, -7.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-8.0F, 33.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition middle_filler = body.addOrReplaceChild("middle_filler", CubeListBuilder.create(), PartPose.offset(0.0F, 37.0F, 0.0F));

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

		PartDefinition stone = body.addOrReplaceChild("stone", CubeListBuilder.create().texOffs(80, 24).addBox(-8.0F, -3.0F, -2.0F, 16.0F, 16.0F, 4.0F, new CubeDeformation(-2.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animate(entity.powered, ObeliskModelAnimations.powered, ageInTicks);
		this.stone.visible = entity.getHasCrystal();
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return body;
	}
}