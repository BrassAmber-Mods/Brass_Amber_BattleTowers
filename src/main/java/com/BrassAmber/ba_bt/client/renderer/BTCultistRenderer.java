package com.BrassAmber.ba_bt.client.renderer;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.client.inventory.model.hostile.BTCultistModel;
import com.BrassAmber.ba_bt.entity.hostile.BTCultist;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class BTCultistRenderer extends HumanoidMobRenderer<BTCultist, BTCultistModel<BTCultist>> {

    private static final ResourceLocation CULTIST = BrassAmberBattleTowers.locate("textures/entity/bt_cultist.png");

    public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(CULTIST, "main");

    public BTCultistRenderer(EntityRendererProvider.Context context) {
        super(context, new BTCultistModel<>(context.bakeLayer(TEXTURE)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BTCultist btCultist) {
        return CULTIST;
    }
}
