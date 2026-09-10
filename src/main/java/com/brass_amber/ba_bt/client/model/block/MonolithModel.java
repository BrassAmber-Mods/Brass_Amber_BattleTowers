package com.brass_amber.ba_bt.client.model.block;
// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.block.MonolithEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MonolithModel<T extends MonolithEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BABattleTowers.locate("monolith"), "main");
	private final ModelPart body;
	private final ModelPart Center;
	private final ModelPart Middle;
	private final ModelPart double_key;
	private final ModelPart double_key_filled;
	private final ModelPart double_key_filled_0;
	private final ModelPart double_key_filled_1;
	private final ModelPart triple_key;
	private final ModelPart triple_key_filled;
	private final ModelPart triple_key_filled_2;
	private final ModelPart triple_key_filled_1;
	private final ModelPart triple_key_filled_0;
	private final ModelPart eye;
	private final ModelPart eye_filled;

	public MonolithModel(ModelPart root) {
		this.body = root.getChild("body");
		this.Center = this.body.getChild("Center");
		this.Middle = this.body.getChild("Middle");
		this.double_key = this.body.getChild("double_key");
		this.double_key_filled = this.body.getChild("double_key_filled");
		this.double_key_filled_0 = this.double_key_filled.getChild("double_key_filled_0");
		this.double_key_filled_1 = this.double_key_filled.getChild("double_key_filled_1");
		this.triple_key = this.body.getChild("triple_key");
		this.triple_key_filled = this.body.getChild("triple_key_filled");
		this.triple_key_filled_2 = this.triple_key_filled.getChild("triple_key_filled_2");
		this.triple_key_filled_1 = this.triple_key_filled.getChild("triple_key_filled_1");
		this.triple_key_filled_0 = this.triple_key_filled.getChild("triple_key_filled_0");
		this.eye = this.body.getChild("eye");
		this.eye_filled = this.body.getChild("eye_filled");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition Center = body.addOrReplaceChild("Center", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, 36.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition Piece5_r1 = Center.addOrReplaceChild("Piece5_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 12.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition Middle = body.addOrReplaceChild("Middle", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 32.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition Piece4_r1 = Middle.addOrReplaceChild("Piece4_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition double_key = body.addOrReplaceChild("double_key", CubeListBuilder.create().texOffs(32, 0).addBox(-6.0F, 20.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(2.0F, 20.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition double_key_filled = body.addOrReplaceChild("double_key_filled", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition double_key_filled_0 = double_key_filled.addOrReplaceChild("double_key_filled_0", CubeListBuilder.create().texOffs(32, 8).addBox(-6.0F, 20.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition double_key_filled_1 = double_key_filled.addOrReplaceChild("double_key_filled_1", CubeListBuilder.create().texOffs(32, 8).addBox(2.0F, 20.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition triple_key = body.addOrReplaceChild("triple_key", CubeListBuilder.create().texOffs(32, 0).addBox(-6.0F, 24.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(2.0F, 24.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-2.0F, 17.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition triple_key_filled = body.addOrReplaceChild("triple_key_filled", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition triple_key_filled_2 = triple_key_filled.addOrReplaceChild("triple_key_filled_2", CubeListBuilder.create().texOffs(32, 8).addBox(-2.0F, -7.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition triple_key_filled_1 = triple_key_filled.addOrReplaceChild("triple_key_filled_1", CubeListBuilder.create().texOffs(32, 8).addBox(2.0F, 0.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition triple_key_filled_0 = triple_key_filled.addOrReplaceChild("triple_key_filled_0", CubeListBuilder.create().texOffs(32, 8).addBox(-6.0F, 0.0F, -8.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eye = body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(48, 0).addBox(-4.0F, -4.0F, -8.01F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eye_filled = body.addOrReplaceChild("eye_filled", CubeListBuilder.create().texOffs(48, 8).addBox(-4.0F, 12.0F, -8.01F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animate(
				entity.spawningGolem.isStarted() ? entity.spawningGolem : entity.floating,
				entity.spawningGolem.isStarted() ? MonolithModelAnimations.Float : MonolithModelAnimations.Spawn,
				ageInTicks
		);

		this.toggleInvisibleParts(entity);
	}

	private void toggleInvisibleParts(T pEntity) {

		int currentVisualMax = pEntity.btEntityData.getMultiStateKeys().getKeyMax(pEntity.getStateInEntity());

		switch (currentVisualMax) {
			case 2 -> {
				this.double_key.visible = true;
				this.double_key_filled_0.visible = pEntity.getKeyCountInEntity() > 0;
				this.double_key_filled_1.visible = pEntity.getKeyCountInEntity() > 1;
			}
			case 3 -> {
				this.triple_key.visible = true;
				this.triple_key_filled_0.visible = pEntity.getKeyCountInEntity() > 0;
				this.triple_key_filled_1.visible = pEntity.getKeyCountInEntity() > 0;
				this.triple_key_filled_2.visible = pEntity.getKeyCountInEntity() > 0;
			}
			default -> {
				this.eye.visible = currentVisualMax == 1;
				this.eye_filled.visible = pEntity.getKeyCountInEntity() > 0;
			}
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.body;
	}
}