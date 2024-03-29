package com.BrassAmber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NetherObeliskRenderer extends ObeliskRendererAbstract {
    public static final String NETHER_OBELISK = "nether_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/nether_obelisk.png"), "main");

    public NetherObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, NETHER_OBELISK, TEXTURE);
    }
}
