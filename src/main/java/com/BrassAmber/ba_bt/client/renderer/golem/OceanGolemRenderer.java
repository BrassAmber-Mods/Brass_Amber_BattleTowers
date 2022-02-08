package com.BrassAmber.ba_bt.client.renderer.golem;

import com.BrassAmber.ba_bt.client.model.hostile.OceanGolemModel;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, OceanGolemModel> {

	public OceanGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new OceanGolemModel(), "ocean_golem");
		this.setGolemTextures("ocean_golem_dormant", "ocean_golem", "ocean_golem");
	}
}
