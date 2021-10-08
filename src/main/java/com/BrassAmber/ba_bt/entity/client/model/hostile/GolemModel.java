package com.BrassAmber.ba_bt.entity.client.model.hostile;

import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GolemModel extends BipedModel<BTGolemEntity> {

	public GolemModel() {
		this(0.0F);
	}

	public GolemModel(float modelSize) {
		super(modelSize, 0.0F, 64, 32);
	}
}
