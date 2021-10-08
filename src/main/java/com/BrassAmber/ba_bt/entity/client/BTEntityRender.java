package com.BrassAmber.ba_bt.entity.client;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.client.renderer.hostile.BTGolemRenderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class BTEntityRender {

	public static void init() {
		register(BTEntityTypes.GOLEM, BTGolemRenderer::new);
	}

	private static <T extends Entity> void register(EntityType<T> entityType, IRenderFactory<? super T> renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityType, renderFactory);
	}
}
