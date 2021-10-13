package com.BrassAmber.ba_bt.entity.client.renderer.golem;

import com.BrassAmber.ba_bt.entity.client.model.golem.SkyGolemModel;
import com.BrassAmber.ba_bt.entity.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, SkyGolemModel> {

	public SkyGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SkyGolemModel(), "sky_golem");
		this.setGolemTextures("sky_golem_dormant", "sky_golem", "sky_golem");
	}
}
