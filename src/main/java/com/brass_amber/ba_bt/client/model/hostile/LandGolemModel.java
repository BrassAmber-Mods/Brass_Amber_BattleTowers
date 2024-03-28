package com.brass_amber.ba_bt.client.model.hostile;

import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.google.common.collect.ImmutableList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.blockentity.ChestRenderer;

/**
 * Referenced from {@link HumanoidModel}
 * ====>>
 * End Golem needs transparency?
 *
 */
@OnlyIn(Dist.CLIENT)
public class LandGolemModel extends HumanoidModel<BTAbstractGolem> {
	/**
	 * Look at {@link ChestRenderer} for example of doing multiple types of textures for an entity
	 * Essentially create three separate sets of model parts (Dormant, Awake, Enraged) and register all of them with
	 * 		separate createLayer methods in Client Events, then switch between them based off of Golem state.
	 * 	Then make all anim/render methods check the Golem state and call the different model parts respectively.
	 */

	public static ModelLayerLocation LAYER_LOCATION;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart hat;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
	public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
	public float swimAmount;

	public LandGolemModel(ModelPart root, ModelLayerLocation location) {
		super(root);
		LAYER_LOCATION = location;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.hat = root.getChild("hat");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F,
								new CubeDeformation(0.0F)), PartPose.offset(0.0F, -32.0F, 0.0F));

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(128, 64).addBox(0F, 0F, 0F, 0F, 0F, 0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(32, 32)
						.addBox(-8.0F, 0.0F, -4.0F, 16.0F, 24.0F, 8.0F,
								new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(16, 71)
						.addBox(-6.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -20.0F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(80, 32)
						.addBox(-2.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -20.0F, 0.0F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 32)
						.addBox(-4.2F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.8F, 0.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(37, 32)
						.addBox(-3.8F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.8F, 0.0F, 0.0F));


		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	/*********************************************************** Animations ********************************************************/

	@Override
	public void setupAnim(BTAbstractGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean isFalling = entity.getFallFlyingTicks() > 4;
		boolean isSwimming = entity.isVisuallySwimming();

		// Head rotations
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		if (isFalling) {
			this.head.xRot = (-(float) Math.PI / 4F);
		} else if (this.swimAmount > 0.0F) {
			if (isSwimming) {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float) Math.PI / 4F));
			} else {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, headPitch * ((float) Math.PI / 180F));
			}
		} else {
			this.head.xRot = headPitch * ((float) Math.PI / 180F);
		}

		// Initial Values
		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -10.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 10.0F;

		float f = 1.0F;
		if (isFalling) {
			f = (float) entity.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		// Swing arms and legs when taking damage (and walking?). 
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;

		// Riding animation

		// Arm animations
		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean isRightHanded = entity.getMainArm() == HumanoidArm.RIGHT;
		boolean isTwoHanded = isRightHanded ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
		if (isRightHanded != isTwoHanded) {
			this.poseLeftArm(entity);
			this.poseRightArm(entity);
		} else {
			this.poseRightArm(entity);
			this.poseLeftArm(entity);
		}

		this.setupAttackAnimation(entity, ageInTicks);

		// Don't know what this is??
		this.body.xRot = 0.0F;
		this.rightLeg.z = 0.1F;
		this.leftLeg.z = 0.1F;
		this.rightLeg.y = 0.0F;
		this.leftLeg.y = 0.0F;
		this.head.y = -32.0F;
		this.body.y = -24.0F;
		this.leftArm.y = -20.0F;
		this.rightArm.y = -20.0F;

		// Swinging arms
		AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);

		// Swim animation
	}

	protected void setupAttackAnimation(BTAbstractGolem entity, float ageInTicks) {
		if (!(this.attackTime <= 0.0F)) {
			HumanoidArm humanoidarm = this.getAttackArm(entity);
			ModelPart arm = this.getArm(humanoidarm);
			float f = this.attackTime;
			this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
			if (humanoidarm == HumanoidArm.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = Mth.sin(this.body.yRot) * 10.0F;
			this.rightArm.x = -Mth.cos(this.body.yRot) * 10.0F;
			this.leftArm.z = -Mth.sin(this.body.yRot) * 10.0F;
			this.leftArm.x = Mth.cos(this.body.yRot) * 10.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.xRot;
			f = 1.0F - this.attackTime;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = Mth.sin(f * (float) Math.PI);
			float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			arm.xRot = (float) ((double) arm.xRot - ((double) f1 * 1.2D + (double) f2));
			arm.yRot += this.body.yRot * 2.0F;
			arm.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
		}
	}

	/*********************************************************** Render ********************************************************/

	@Override
	public void prepareMobModel(BTAbstractGolem entity, float limbSwing, float limbSwingAmount, float partialTick) {
		this.swimAmount = entity.getSwimAmount(partialTick);
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.headParts().forEach((headPart) -> {
			headPart.render(poseStack, consumer, packedLight, packedOverlay);
		});
		this.bodyParts().forEach((bodyPart) -> {
			bodyPart.render(poseStack, consumer, packedLight, packedOverlay);
		});
	}
	

	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}

	public void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.xRot = x;
		modelPart.yRot = y;
		modelPart.zRot = z;
	}

	/*********************************************************** Implementations ********************************************************/

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public void translateToHand(HumanoidArm handSide, PoseStack poseStack) {
		this.getArm(handSide).translateAndRotate(poseStack);
	}

	protected ModelPart getArm(HumanoidArm handSide) {
		return handSide == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
	}

	/*********************************************************** Animation Helpers ********************************************************/

	protected float rotlerpRad(float rotateAmount, float currentRotation, float startingRotation) {
		float f = (startingRotation - currentRotation) % ((float) Math.PI * 2F);
		if (f < -(float) Math.PI) {
			f += ((float) Math.PI * 2F);
		}

		if (f >= (float) Math.PI) {
			f -= ((float) Math.PI * 2F);
		}

		return currentRotation + rotateAmount * f;
	}

	protected HumanoidArm getAttackArm(BTAbstractGolem entity) {
		HumanoidArm handside = entity.getMainArm();
		return entity.swingingArm == InteractionHand.MAIN_HAND ? handside : handside.getOpposite();
	}

	

	public void poseRightArm(BTAbstractGolem entity) {
		switch (this.rightArmPose) {
		case EMPTY:
			this.leftArm.yRot = 0.0F;
			break;
		case BLOCK:
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
			this.leftArm.yRot = (-(float) Math.PI / 6F);
			break;
		case ITEM:
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
			this.leftArm.yRot = 0.0F;
			break;
		case THROW_SPEAR:
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
			this.leftArm.yRot = 0.0F;
			break;
		case BOW_AND_ARROW:
			this.leftArm.yRot = -0.1F + this.head.yRot;
			this.rightArm.yRot = 0.1F + this.head.yRot + 0.4F;
			this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			break;
		case CROSSBOW_CHARGE:
			AnimationUtils.animateCrossbowCharge(this.leftArm, this.rightArm, entity, true);
			break;
		case CROSSBOW_HOLD:
			AnimationUtils.animateCrossbowHold(this.leftArm, this.rightArm, this.head, true);
		}

	}

	private void poseLeftArm(BTAbstractGolem entity) {
		switch (this.leftArmPose) {
		case EMPTY:
			this.rightArm.yRot = 0.0F;
			break;
		case BLOCK:
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
			this.rightArm.yRot = ((float) Math.PI / 6F);
			break;
		case ITEM:
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
			this.rightArm.yRot = 0.0F;
			break;
		case THROW_SPEAR:
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
			this.rightArm.yRot = 0.0F;
			break;
		case BOW_AND_ARROW:
			this.leftArm.yRot = -0.1F + this.head.yRot - 0.4F;
			this.rightArm.yRot = 0.1F + this.head.yRot;
			this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			break;
		case CROSSBOW_CHARGE:
			AnimationUtils.animateCrossbowCharge(this.leftArm, this.rightArm, entity, false);
			break;
		case CROSSBOW_HOLD:
			AnimationUtils.animateCrossbowHold(this.leftArm, this.rightArm, this.head, false);
		}
	}
}