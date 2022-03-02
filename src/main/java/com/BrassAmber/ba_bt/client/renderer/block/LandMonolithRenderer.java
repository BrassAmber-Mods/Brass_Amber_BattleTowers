package com.BrassAmber.ba_bt.client.renderer.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandMonolithRenderer extends MonolithRendererAbstract {
	public static final String LAND_MONOLITH = "land_monolith";
	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(BrassAmberBattleTowers.locate("land_monolith_0.png"), "main");

	public LandMonolithRenderer(EntityRendererProvider.Context context) {
		super(context, LAND_MONOLITH, TEXTURE);
	}
}
