package com.BrassAmber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OceanObeliskRenderer extends ObeliskRendererAbstract {
    public static final String OCEAN_OBELISK = "ocean_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/ocean_obelisk.png"), "main");

    public OceanObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, OCEAN_OBELISK, TEXTURE);
    }
}
