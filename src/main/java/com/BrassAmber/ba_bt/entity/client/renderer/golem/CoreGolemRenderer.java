package com.BrassAmber.ba_bt.entity.client.renderer.golem;

import com.BrassAmber.ba_bt.entity.client.model.golem.LandGolemModel;
import com.BrassAmber.ba_bt.entity.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoreGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, LandGolemModel> {

	public CoreGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new LandGolemModel(), "core_golem");
		this.setGolemTextures("core_golem_dormant", "core_golem", "core_golem_cracked");
	}
}
