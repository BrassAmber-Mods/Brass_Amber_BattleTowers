package com.brass_amber.ba_bt.client.model.hostile;
// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.brass_amber.ba_bt.entity.hostile.golem.CoreGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class CoreGolemModel extends HierarchicalModel<CoreGolem> {

	public static ModelLayerLocation LAYER_LOCATION;
	private final ModelPart core;
	private final ModelPart body;
	private final ModelPart chest;
	private final ModelPart head;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	private final ModelPart leftarm;
	private final ModelPart leftarmtop;
	private final ModelPart leftarmmid;
	private final ModelPart leftarmbot;
	private final ModelPart rightarm;
	private final ModelPart rightarmtop;
	private final ModelPart rightarmmid;
	private final ModelPart rightarmbot;

	public CoreGolemModel(ModelPart root, ModelLayerLocation location) {
		LAYER_LOCATION = location;
		this.core = root.getChild("coregod");
		this.body = this.core.getChild("body");
		this.chest = this.body.getChild("chest");
		this.head = this.body.getChild("head");
		this.leftleg = this.body.getChild("leftleg");
		this.rightleg = this.body.getChild("rightleg");
		this.leftarm = this.body.getChild("leftarm");
		this.leftarmtop = this.leftarm.getChild("leftarmtop");
		this.leftarmmid = this.leftarm.getChild("leftarmmid");
		this.leftarmbot = this.leftarm.getChild("leftarmbot");
		this.rightarm = this.body.getChild("rightarm");
		this.rightarmtop = this.rightarm.getChild("rightarmtop");
		this.rightarmmid = this.rightarm.getChild("rightarmmid");
		this.rightarmbot = this.rightarm.getChild("rightarmbot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition coregod = partdefinition.addOrReplaceChild("coregod", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = coregod.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition leftleg = body.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition rightleg = body.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition cube_r1 = rightleg.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-2.0F, -12.0F, 0.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition leftarmtop = leftarm.addOrReplaceChild("leftarmtop", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition leftarmmid = leftarm.addOrReplaceChild("leftarmmid", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftarmbot = leftarm.addOrReplaceChild("leftarmbot", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offset(-4.0F, -24.0F, 0.0F));

		PartDefinition rightarmtop = rightarm.addOrReplaceChild("rightarmtop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition rightarmmid = rightarm.addOrReplaceChild("rightarmmid", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightarmbot = rightarm.addOrReplaceChild("rightarmbot", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(CoreGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(entity, netHeadYaw, headPitch, ageInTicks);

		this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

		this.animate(entity.meleeAnimationState, entity.isLeftHanded() ? CoreGolemModelAnimations.melee_left : CoreGolemModelAnimations.melee_right, ageInTicks, 1f);
		this.animate(entity.fireballAnimationState, CoreGolemModelAnimations.fireball, ageInTicks, 1f);
		this.animate(entity.unleashedAnimationAnimationState, CoreGolemModelAnimations.unleashed, ageInTicks, 1f);


	}

	private void applyHeadRotation(CoreGolem entity, float netHeadYaw, float headPitch, float agInTicks) {
		netHeadYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
		headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		core.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return core;
	}


}