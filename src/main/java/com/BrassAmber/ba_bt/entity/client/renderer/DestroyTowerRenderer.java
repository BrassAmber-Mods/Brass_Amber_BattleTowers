package com.BrassAmber.ba_bt.entity.client.renderer;

import com.BrassAmber.ba_bt.entity.client.model.golem.SkyGolemModel;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.BTAbstractGolemRenderer;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DestroyTowerRenderer extends EntityRenderer {

	public DestroyTowerRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);

	}

	@Override
	public ResourceLocation getTextureLocation(Entity p_110775_1_) {
		return null;
	}
}
