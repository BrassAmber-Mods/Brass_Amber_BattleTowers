package com.BrassAmber.ba_bt.client.renderer;

import com.BrassAmber.ba_bt.block.blockentity.spawner.BTAbstractSpawnerBlockEntity;
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

    public void render(BTAbstractSpawnerBlockEntity p_112563_, float p_112564_, PoseStack p_112565_, MultiBufferSource p_112566_, int p_112567_, int p_112568_) {
        p_112565_.pushPose();
        p_112565_.translate(0.5D, 0.0D, 0.5D);
        BaseSpawner basespawner = p_112563_.getSpawner();
        Entity entity = basespawner.getOrCreateDisplayEntity(p_112563_.getLevel(), p_112563_.getLevel().getRandom(), p_112563_.getBlockPos());
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }

            p_112565_.translate(0.0D, (double)0.4F, 0.0D);
            p_112565_.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp((double)p_112564_, basespawner.getoSpin(), basespawner.getSpin()) * 10.0F));
            p_112565_.translate(0.0D, (double)-0.2F, 0.0D);
            p_112565_.mulPose(Axis.XP.rotationDegrees(-30.0F));
            p_112565_.scale(f, f, f);
            this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, p_112564_, p_112565_, p_112566_, p_112567_);
        }

        p_112565_.popPose();
    }
}