package com.BrassAmber.ba_bt.client.model.hostile;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.PillagerRenderer;

public class BTCultistModel extends IllagerModel {

    public static final ModelLayerLocation BT_CULTIST_TEXTURE = new ModelLayerLocation(BrassAmberBattleTowers.locate("bt_cultist.png"), "main");

    public BTCultistModel(ModelPart modelPart) {
        super(modelPart);
    }
}
