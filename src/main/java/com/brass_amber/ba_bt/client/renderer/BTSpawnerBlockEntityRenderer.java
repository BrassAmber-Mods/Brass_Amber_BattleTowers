package com.brass_amber.ba_bt.client.renderer;

import com.brass_amber.ba_bt.block.blockentity.spawner.BTAbstractSpawnerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BTSpawnerBlockEntityRenderer implements BlockEntityRenderer<BTAbstractSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;
    public BTSpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context p_173673_) {
        this.entityRenderer = p_173673_.getEntityRenderer();
    }

    public void render(BTAbstractSpawnerBlockEntity spawnerBlockEntity, float rotation, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int p_112568_) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0D, 0.5F);
        BaseSpawner basespawner = spawnerBlockEntity.getSpawner();
        Entity entity = basespawner.getOrCreateDisplayEntity(spawnerBlockEntity.getLevel(), spawnerBlockEntity.getLevel().getRandom(), spawnerBlockEntity.getBlockPos());
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0D) {
                f /= f1;
            }

            poseStack.translate(0.0F, (double)0.4F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp((double) rotation, basespawner.getoSpin(), basespawner.getSpin()) * 10.0F));
            poseStack.translate(0.0F, (double)-0.2F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
            poseStack.scale(f, f, f);
            this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, rotation, poseStack, bufferSource, lightCoords);
        }

        poseStack.popPose();
    }


}