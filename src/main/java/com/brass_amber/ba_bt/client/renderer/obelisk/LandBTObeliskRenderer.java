package com.brass_amber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LandBTObeliskRenderer extends AbstractBTObeliskRenderer {
    public static final String LAND_OBELISK = "land_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/land_obelisk.png"), "main");

    public LandBTObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, LAND_OBELISK, TEXTURE);
    }
}
