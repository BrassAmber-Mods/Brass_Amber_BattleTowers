package com.brass_amber.ba_bt.client.renderer.obelisk;

import com.brass_amber.ba_bt.client.renderer.monolith.MonolithRendererAbstract;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LandObeliskRenderer extends ObeliskRendererAbstract {
    public static final String LAND_OBELISK = "land_obelisk";
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(new ResourceLocation("textures/entity/obelisk/land_obelisk.png"), "main");

    public LandObeliskRenderer(EntityRendererProvider.Context context) {
        super(context, LAND_OBELISK, TEXTURE);
    }
}
