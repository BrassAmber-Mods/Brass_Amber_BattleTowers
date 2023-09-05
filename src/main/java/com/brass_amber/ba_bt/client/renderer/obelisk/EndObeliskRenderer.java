package com.brass_amber.ba_bt.client.renderer.obelisk;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EndObeliskRenderer extends ObeliskRendererAbstract {
    public static final String END_OBELISK = "end_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/end_obelisk.png"), "main");

    public EndObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, END_OBELISK, TEXTURE);
    }
}
