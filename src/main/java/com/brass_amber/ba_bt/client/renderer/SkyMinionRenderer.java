package com.brass_amber.ba_bt.client.renderer;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.client.model.hostile.SkyMinionModel;
import com.brass_amber.ba_bt.entity.hostile.SkyMinion;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkyMinionRenderer extends MobRenderer<SkyMinion, SkyMinionModel> {
	public static final ResourceLocation LOCATION = BrassAmberBattleTowers.locate("textures/entity/sky_minion.png");
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(LOCATION,"main");

	public SkyMinionRenderer(EntityRendererProvider.Context context) {
		super(context, new SkyMinionModel(context.bakeLayer(TEXTURE),
				TEXTURE), 0.6f);
	}

	@Override
	public ResourceLocation getTextureLocation(SkyMinion p_110775_1_) {
		return LOCATION;
	}
}
