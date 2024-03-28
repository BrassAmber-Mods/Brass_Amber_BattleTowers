package com.brass_amber.ba_bt.client.renderer.monolith;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandBTMonolithRenderer extends AbstractBTMonolithRenderer {
	public static final String LAND_MONOLITH = "land_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(BrassAmberBattleTowers.locate("land_monolith_0.png"), "main");

	public LandBTMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, LAND_MONOLITH, TEXTURE);
	}
}
