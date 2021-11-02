package com.BrassAmber.ba_bt.entity.client.renderer.golem;

import com.BrassAmber.ba_bt.entity.client.model.hostile.LandGolemModel;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, LandGolemModel> {

	public LandGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new LandGolemModel(), "land_golem");
		this.setGolemTextures("land_golem_dormant", "land_golem", "land_golem_enraged");
	}
}
