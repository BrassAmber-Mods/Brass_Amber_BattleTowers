package com.brass_amber.ba_bt.client.renderer.monolith;

import com.brass_amber.ba_bt.BABattleTowers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandBTMonolithRenderer extends AbstractBTMonolithRenderer {
	public static final String LAND_MONOLITH = "land_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(BABattleTowers.locate("textures/entity/monolith/land_monolith/land_monolith_0.png"), "main");

	public LandBTMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, LAND_MONOLITH, TEXTURE);
	}
}
