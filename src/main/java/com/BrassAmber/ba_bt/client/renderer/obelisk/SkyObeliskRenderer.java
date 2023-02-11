package com.BrassAmber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SkyObeliskRenderer extends ObeliskRendererAbstract {
    public static final String SKY_OBELISK = "sky_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/sky_obelisk.png"), "main");

    public SkyObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, SKY_OBELISK, TEXTURE);
    }
}
