package com.BrassAmber.ba_bt.client.renderer;

import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BTSpawnerBlockEntityRenderer implements BlockEntityRenderer<BTSpawnerBlockEntity> {
    public BTSpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(BTSpawnerBlockEntity p_112563_, float p_112564_, PoseStack p_112565_, MultiBufferSource p_112566_, int p_112567_, int p_112568_) {
        p_112565_.pushPose();
        p_112565_.translate(0.5D, 0.0D, 0.5D);
        BaseSpawner basespawner = p_112563_.getSpawner();
        Entity entity = basespawner.getOrCreateDisplayEntity(p_112563_.getLevel());
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }

            p_112565_.translate(0.0D, (double)0.4F, 0.0D);
            p_112565_.mulPose(Vector3f.YP.rotationDegrees((float) Mth.lerp((double)p_112564_, basespawner.getoSpin(), basespawner.getSpin()) * 10.0F));
            p_112565_.translate(0.0D, (double)-0.2F, 0.0D);
            p_112565_.mulPose(Vector3f.XP.rotationDegrees(-30.0F));
            p_112565_.scale(f, f, f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, p_112564_, p_112565_, p_112566_, p_112567_);
        }

        p_112565_.popPose();
    }
}