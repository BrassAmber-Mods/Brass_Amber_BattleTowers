package com.BrassAmber.ba_bt.entity.client.model.golem;

import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Referenced from {@link BipedModel}
 * ====>>
 * End Golem needs transparency?
 *
 */
@OnlyIn(Dist.CLIENT)
public class LandGolemModel extends EntityModel<BTGolemEntityAbstract> implements IHasArm, IHasHead {
	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;
	public BipedModel.ArmPose leftArmPose = BipedModel.ArmPose.EMPTY;
	public BipedModel.ArmPose rightArmPose = BipedModel.ArmPose.EMPTY;
	public float swimAmount;

	public LandGolemModel() {
		this(0.0f);
	}

	public LandGolemModel(float scale) {
		this.texWidth = 128;
		this.texHeight = 64;

		head = new ModelRenderer(this);
		head.setPos(0.0F, -32.0F, 0.0F);
		head.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setPos(0.0F, -24.0F, 0.0F);
		body.texOffs(32, 32).addBox(-8.0F, 0.0F, -4.0F, 16.0F, 24.0F, 8.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-10.0F, -20.0F, 0.0F);
		rightArm.texOffs(0, 32).addBox(-6.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(10.0F, -20.0F, 0.0F);
		leftArm.texOffs(80, 32).addBox(-2.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-3.8F, 0.0F, 0.0F);
		rightLeg.texOffs(80, 32).addBox(-4.2F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(3.8F, 0.0F, 0.0F);
		leftLeg.texOffs(0, 32).addBox(-3.8F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
	}

	/*********************************************************** Animations ********************************************************/

	@Override
	public void setupAnim(BTGolemEntityAbstract entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
		this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
		this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;

		// Riding animation

		// Arm animations
		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean isRightHanded = entity.getMainArm() == HandSide.RIGHT;
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
		ModelHelper.bobArms(this.rightArm, this.leftArm, ageInTicks);

		// Swim animation
	}

	protected void setupAttackAnimation(BTGolemEntityAbstract entity, float ageInTicks) {
		if (!(this.attackTime <= 0.0F)) {
			HandSide handside = this.getAttackArm(entity);
			ModelRenderer modelrenderer = this.getArm(handside);
			float f = this.attackTime;
			this.body.yRot = MathHelper.sin(MathHelper.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
			if (handside == HandSide.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = MathHelper.sin(this.body.yRot) * 10.0F;
			this.rightArm.x = -MathHelper.cos(this.body.yRot) * 10.0F;
			this.leftArm.z = -MathHelper.sin(this.body.yRot) * 10.0F;
			this.leftArm.x = MathHelper.cos(this.body.yRot) * 10.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			f = 1.0F - this.attackTime;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = MathHelper.sin(f * (float) Math.PI);
			float f2 = MathHelper.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			modelrenderer.xRot = (float) ((double) modelrenderer.xRot - ((double) f1 * 1.2D + (double) f2));
			modelrenderer.yRot += this.body.yRot * 2.0F;
			modelrenderer.zRot += MathHelper.sin(this.attackTime * (float) Math.PI) * -0.4F;
		}
	}

	/*********************************************************** Render ********************************************************/

	@Override
	public void prepareMobModel(BTGolemEntityAbstract entity, float limbSwing, float limbSwingAmount, float partialTick) {
		this.swimAmount = entity.getSwimAmount(partialTick);
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.headParts().forEach((headPart) -> {
			headPart.render(matrixStack, buffer, packedLight, packedOverlay);
		});
		this.bodyParts().forEach((bodyPart) -> {
			bodyPart.render(matrixStack, buffer, packedLight, packedOverlay);
		});
	}

	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	/*********************************************************** Implementations ********************************************************/

	@Override
	public ModelRenderer getHead() {
		return this.head;
	}

	@Override
	public void translateToHand(HandSide handSide, MatrixStack matrixStack) {
		this.getArm(handSide).translateAndRotate(matrixStack);
	}

	protected ModelRenderer getArm(HandSide handSide) {
		return handSide == HandSide.LEFT ? this.leftArm : this.rightArm;
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

	protected HandSide getAttackArm(BTGolemEntityAbstract entity) {
		HandSide handside = entity.getMainArm();
		return entity.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
	}

	private void poseRightArm(BTGolemEntityAbstract entity) {
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
			ModelHelper.animateCrossbowCharge(this.leftArm, this.rightArm, entity, true);
			break;
		case CROSSBOW_HOLD:
			ModelHelper.animateCrossbowHold(this.leftArm, this.rightArm, this.head, true);
		}

	}

	private void poseLeftArm(BTGolemEntityAbstract entity) {
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
			ModelHelper.animateCrossbowCharge(this.leftArm, this.rightArm, entity, false);
			break;
		case CROSSBOW_HOLD:
			ModelHelper.animateCrossbowHold(this.leftArm, this.rightArm, this.head, false);
		}
	}
}