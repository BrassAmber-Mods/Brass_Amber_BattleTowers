package com.brass_amber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SkyBTObeliskRenderer extends AbstractBTObeliskRenderer {
    public static final String SKY_OBELISK = "sky_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/sky_obelisk.png"), "main");

    public SkyBTObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, SKY_OBELISK, TEXTURE);
    }
}
