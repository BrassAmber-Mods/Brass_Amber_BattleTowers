package com.BrassAmber.ba_bt.entity.client;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.client.renderer.block.MonolithEntityRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.CoreGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.EndGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.LandGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.NetherGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.OceanGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.SkyGolemRenderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class BTEntityRender {

	public static void init() {
		registerEntityRenderer(BTEntityTypes.LAND, LandGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.CORE, CoreGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.NETHER, NetherGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.END, EndGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.SKY, SkyGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.OCEAN, OceanGolemRenderer::new);

		registerEntityRenderer(BTEntityTypes.MONOLITH, MonolithEntityRenderer::new);
	}

	private static <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, IRenderFactory<? super T> renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityType, renderFactory);
	}
}
