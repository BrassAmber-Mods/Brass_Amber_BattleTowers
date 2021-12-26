package com.BrassAmber.ba_bt.entity.client;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.client.renderer.NoRenderEntity;
import com.BrassAmber.ba_bt.entity.client.renderer.SkyMinionRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.block.CoreMonolithRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.block.EndMonolithRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.block.LandMonolithRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.block.NetherMonolithRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.block.OceanMonolithRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.block.SkyMonolithRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.CoreGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.EndGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.LandGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.NetherGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.OceanGolemRenderer;
import com.BrassAmber.ba_bt.entity.client.renderer.golem.SkyGolemRenderer;

import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class BTEntityRender {

	public static void init() {
		registerEntityRenderer(BTEntityTypes.LAND_GOLEM, LandGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.OCEAN_GOLEM, OceanGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.NETHER_GOLEM, NetherGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.CORE_GOLEM, CoreGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.END_GOLEM, EndGolemRenderer::new);
		registerEntityRenderer(BTEntityTypes.SKY_GOLEM, SkyGolemRenderer::new);

		registerEntityRenderer(BTEntityTypes.SKY_MINION, SkyMinionRenderer::new);
		registerEntityRenderer(BTEntityTypes.SILVER_SKELETON, SkeletonRenderer::new);

		registerEntityRenderer(BTEntityTypes.LAND_MONOLITH, LandMonolithRenderer::new);
		registerEntityRenderer(BTEntityTypes.CORE_MONOLITH, CoreMonolithRenderer::new);
		registerEntityRenderer(BTEntityTypes.NETHER_MONOLITH, NetherMonolithRenderer::new);
		registerEntityRenderer(BTEntityTypes.END_MONOLITH, EndMonolithRenderer::new);
		registerEntityRenderer(BTEntityTypes.SKY_MONOLITH, SkyMonolithRenderer::new);
		registerEntityRenderer(BTEntityTypes.OCEAN_MONOLITH, OceanMonolithRenderer::new);

		registerEntityRenderer(BTEntityTypes.DESTROY_TOWER, NoRenderEntity::new);
		registerEntityRenderer(BTEntityTypes.PHYSICS_EXPLOSION, NoRenderEntity::new);
	}

	private static <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, IRenderFactory<? super T> renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityType, renderFactory);
	}
}
