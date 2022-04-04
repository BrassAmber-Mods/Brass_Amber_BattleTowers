package com.BrassAmber.ba_bt.client.model.hostile;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

public class BTCultistModel extends SkeletonModel {

    public static final ModelLayerLocation BT_CULTIST_TEXTURE = new ModelLayerLocation(BrassAmberBattleTowers.locate("bt_cultist.png"), "main");

    public BTCultistModel(ModelPart p_170941_) {
        super(p_170941_);
    }
}
