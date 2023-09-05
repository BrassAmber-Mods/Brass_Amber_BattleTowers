package com.brass_amber.ba_bt.client.renderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NoRenderEntity extends EntityRenderer {

	public NoRenderEntity(EntityRendererProvider.Context context) {
		super(context);

	}
	@Override
	public ResourceLocation getTextureLocation(Entity entity) {
		return null;
	}
}
