package com.brass_amber.ba_bt.client.renderer;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.client.model.CorreyeModel;
import com.brass_amber.ba_bt.entity.CorreyeEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CorreyeRenderer extends MobRenderer<CorreyeEntity, CorreyeModel> {

    public static final ResourceLocation LOCATION = BABattleTowers.locate("textures/entity/sky_minion.png");
    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(LOCATION, "main");

    public CorreyeRenderer(EntityRendererProvider.Context context) {
        super(context, new CorreyeModel(context.bakeLayer(TEXTURE), TEXTURE), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(CorreyeEntity p_110775_1_) {
        return LOCATION;
    }
}
