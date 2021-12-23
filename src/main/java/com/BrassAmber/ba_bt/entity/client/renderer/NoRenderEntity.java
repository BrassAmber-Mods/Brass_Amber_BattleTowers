package com.BrassAmber.ba_bt.entity.client.renderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NoRenderEntity extends EntityRenderer {

	public NoRenderEntity(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);

	}

	@Override
	public ResourceLocation getTextureLocation(Entity p_110775_1_) {
		return null;
	}
}
