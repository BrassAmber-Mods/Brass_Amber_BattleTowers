package com.BrassAmber.ba_bt.entity.client.renderer.hostile;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.client.model.hostile.LandGolemModel;
import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntityAbstract;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndGolemRenderer extends BTAbstractGolemRenderer<BTGolemEntityAbstract, LandGolemModel> {
	private static final ResourceLocation GOLEM_TEXTURES_DORMANT = BrassAmberBattleTowers.locate("textures/entity/hostile/end_golem/end_golem_dormant.png");
	private static final ResourceLocation GOLEM_TEXTURES_AWAKEN = BrassAmberBattleTowers.locate("textures/entity/hostile/end_golem/end_golem.png");
	private static final ResourceLocation GOLEM_TEXTURES_ENRAGED = BrassAmberBattleTowers.locate("textures/entity/hostile/end_golem/end_golem_apparition.png");

	public EndGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new LandGolemModel());
	}

	@Override
	public ResourceLocation getTextureLocation(BTGolemEntityAbstract entity) {
		return entity.isAwake() ? GOLEM_TEXTURES_AWAKEN : entity.isEnraged() ? GOLEM_TEXTURES_ENRAGED : GOLEM_TEXTURES_DORMANT;
	}
}
