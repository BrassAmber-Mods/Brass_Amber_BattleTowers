package com.brass_amber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CoreBTObeliskRenderer extends AbstractBTObeliskRenderer {
    public static final String CORE_OBELISK = "land_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/core_obelisk.png"), "main");

    public CoreBTObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, CORE_OBELISK, TEXTURE);
    }
}
