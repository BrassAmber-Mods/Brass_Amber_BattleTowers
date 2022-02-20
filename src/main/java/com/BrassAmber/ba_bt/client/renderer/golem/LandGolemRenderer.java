package com.BrassAmber.ba_bt.client.renderer.golem;

import com.BrassAmber.ba_bt.client.model.hostile.LandGolemModel;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, LandGolemModel> {
	private final ResourceLocation

	public LandGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new LandGolemModel(), "land_golem");
		this.setGolemTextures("land_golem_dormant", "land_golem", "land_golem_enraged");
	}
}
