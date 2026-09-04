package com.brass_amber.ba_bt.client.renderer.obelisk;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.client.model.block.ObeliskModel;
import com.brass_amber.ba_bt.entity.block.ObeliskEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ObeliskRenderer extends EntityRenderer<ObeliskEntity> {
    private final ObeliskModel<ObeliskEntity> model;

    public ObeliskRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ObeliskModel<>(context.bakeLayer(ObeliskModel.LAYER_LOCATION));
    }

    @Override
    public void render(ObeliskEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        float ageInTicks = (float) pEntity.tickCount + pPartialTick;
        this.model.setupAnim(pEntity, 0f, 0f, ageInTicks, 0f, 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(ObeliskEntity entityIn) {
        if (entityIn.btEntityData != null) {
            return BABattleTowers.locate("textures/entity/obelisk/" + entityIn.btEntityData.getName() + "_obelisk.png");
        }
        return BABattleTowers.locate("textures/entity/obelisk/land_obelisk.png");
    }
}
