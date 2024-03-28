package com.brass_amber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NetherBTObeliskRenderer extends AbstractBTObeliskRenderer {
    public static final String NETHER_OBELISK = "nether_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/nether_obelisk.png"), "main");

    public NetherBTObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, NETHER_OBELISK, TEXTURE);
    }
}
