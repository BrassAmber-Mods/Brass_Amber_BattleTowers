package com.BrassAmber.ba_bt.client.renderer;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.client.model.hostile.SkyMinionModel;
import com.BrassAmber.ba_bt.entity.hostile.SkyMinionEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyMinionRenderer extends MobRenderer<SkyMinionEntity, SkyMinionModel> {
	private static final ResourceLocation TEXTURE_LOCATION = BrassAmberBattleTowers.locate("textures/entity/sky_minion.png");

	public SkyMinionRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SkyMinionModel(), 0.6f);
	}

	@Override
	public ResourceLocation getTextureLocation(SkyMinionEntity p_110775_1_) {
		return TEXTURE_LOCATION;
	}
}
