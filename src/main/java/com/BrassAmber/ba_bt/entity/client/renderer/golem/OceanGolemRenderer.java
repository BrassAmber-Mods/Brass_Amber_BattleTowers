package com.BrassAmber.ba_bt.entity.client.renderer.golem;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.client.model.golem.OceanGolemModel;
import com.BrassAmber.ba_bt.entity.golem.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, OceanGolemModel> {
	private static final ResourceLocation GOLEM_TEXTURES_DORMANT = BrassAmberBattleTowers.locate("textures/entity/hostile/ocean_golem/ocean_golem_dormant.png");
	private static final ResourceLocation GOLEM_TEXTURES_AWAKEN = BrassAmberBattleTowers.locate("textures/entity/hostile/ocean_golem/ocean_golem.png");
	private static final ResourceLocation GOLEM_TEXTURES_ENRAGED = BrassAmberBattleTowers.locate("textures/entity/hostile/ocean_golem/ocean_golem.png");

	public OceanGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new OceanGolemModel());
	}

	@Override
	public ResourceLocation getTextureLocation(BTGolemEntityAbstract entity) {
		return entity.isAwake() ? GOLEM_TEXTURES_AWAKEN : entity.isEnraged() ? GOLEM_TEXTURES_ENRAGED : GOLEM_TEXTURES_DORMANT;
	}
}
