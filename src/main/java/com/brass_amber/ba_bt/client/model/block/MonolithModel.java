package com.brass_amber.ba_bt.client.model.block;

import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MonolithModel<T extends BTMonolith> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static ModelLayerLocation LAYER_LOCATION;
	private final ModelPart monolith;

	public MonolithModel(ModelPart root, ModelLayerLocation location) {
		this.monolith = root.getChild("monolith");
		LAYER_LOCATION = location;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition monolith = partdefinition.addOrReplaceChild("monolith", CubeListBuilder.create().texOffs(16, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(8, 14).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(60, 14).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(52, 0).addBox(-4.0F, 12.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		monolith.render(poseStack, buffer, packedLight, packedOverlay);
	}
}