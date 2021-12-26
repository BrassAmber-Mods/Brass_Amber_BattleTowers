package com.BrassAmber.ba_bt.block.tileentity.client.renderer;

import com.BrassAmber.ba_bt.block.tileentity.BTMobSpawnerTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.MobSpawnerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.spawner.AbstractSpawner;

public class BTMobSpawnerTileEntityRenderer extends TileEntityRenderer<BTMobSpawnerTileEntity> {
    public BTMobSpawnerTileEntityRenderer(TileEntityRendererDispatcher p_i226016_1_) {
        super(p_i226016_1_);
    }

    public void render(BTMobSpawnerTileEntity btMobSpawnerTileEntity, float x, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int lightCoords, int p_225616_6_) {
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.0D, 0.5D);
        AbstractSpawner abstractspawner = btMobSpawnerTileEntity.getSpawner();
        Entity entity = abstractspawner.getOrCreateDisplayEntity();
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }

            matrixStack.translate(0.0D, (double)0.4F, 0.0D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees((float)MathHelper.lerp((double)x, abstractspawner.getoSpin(), abstractspawner.getSpin()) * 10.0F));
            matrixStack.translate(0.0D, (double)-0.2F, 0.0D);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-30.0F));
            matrixStack.scale(f, f, f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, x, matrixStack, iRenderTypeBuffer, lightCoords);
        }

        matrixStack.popPose();
    }
}