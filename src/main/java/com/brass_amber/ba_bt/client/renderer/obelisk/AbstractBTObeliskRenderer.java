package com.brass_amber.ba_bt.client.renderer.obelisk;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.client.model.block.ObeliskModel;
import com.brass_amber.ba_bt.entity.block.BTAbstractObelisk;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AbstractBTObeliskRenderer extends EntityRenderer<BTAbstractObelisk> {
    private final ObeliskModel<BTAbstractObelisk> obelisk;
    private ResourceLocation obeliskTexture;
    private String obeliskType;
    private int rotationAmount = 0;

    public AbstractBTObeliskRenderer(EntityRendererProvider.Context context, String obeliskType, ModelLayerLocation location) {
        super(context);
        this.obeliskType = obeliskType;
        // Set the correct textures for each Monolith type.
        this.obeliskTexture = this.setObeliskTextureLocation();
        this.obelisk = new ObeliskModel<>(
                context.bakeLayer(location),
                location);
    }

    @Override
    public void render(BTAbstractObelisk entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {

        this.rotationAmount += 2;
        if (this.rotationAmount > 360) {
            this.rotationAmount = 0;
        }
        this.obelisk.setupAnim(entityIn, this.rotationAmount, 0, 0, 0, 0);
        // Model is upside down for some reason. (No idea why!)
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrixStackIn.translate(0.0D, -2.0D, 0.0D);

        // Move model to the middle of the hit-box.
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        // Render the textures.
        VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entitySmoothCutout(this.getTextureLocation(entityIn)));
        this.obelisk.renderToBuffer(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Override
    public ResourceLocation getTextureLocation(BTAbstractObelisk entityIn) {
        return this.getObeliskTexture();
    }

    private ResourceLocation getObeliskTexture() {
        return this.obeliskTexture;
    }

    private ResourceLocation setObeliskTextureLocation() {
        return BrassAmberBattleTowers.locate("textures/entity/obelisk/" + this.obeliskType + ".png");
    }
}
