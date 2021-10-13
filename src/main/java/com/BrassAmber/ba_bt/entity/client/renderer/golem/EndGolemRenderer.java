package com.BrassAmber.ba_bt.entity.client.renderer.golem;

import com.BrassAmber.ba_bt.entity.client.model.golem.LandGolemModel;
import com.BrassAmber.ba_bt.entity.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, LandGolemModel> {

	public EndGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new LandGolemModel(), "end_golem");
		this.setGolemTextures("end_golem_dormant", "end_golem", "end_golem_apparition");
	}
}
