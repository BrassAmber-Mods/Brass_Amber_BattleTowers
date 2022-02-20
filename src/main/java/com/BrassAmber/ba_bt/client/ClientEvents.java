package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.client.model.block.MonolithModel;
import com.BrassAmber.ba_bt.client.renderer.*;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.client.renderer.block.CoreMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.block.EndMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.block.LandMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.block.NetherMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.block.OceanMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.block.SkyMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.CoreGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.EndGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.LandGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.NetherGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.OceanGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.SkyGolemRenderer;


import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

	private ClientEvents() {}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		// ENTITIES
		event.registerEntityRenderer(BTEntityTypes.LAND_GOLEM, LandGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_GOLEM, OceanGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.NETHER_GOLEM, NetherGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.CORE_GOLEM, CoreGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.END_GOLEM, EndGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.SKY_GOLEM, SkyGolemRenderer::new);

		event.registerEntityRenderer(BTEntityTypes.SKY_MINION, SkyMinionRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.SILVER_SKELETON, SkeletonRenderer::new);

		event.registerEntityRenderer(BTEntityTypes.LAND_MONOLITH, LandMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.CORE_MONOLITH, CoreMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.NETHER_MONOLITH, NetherMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.END_MONOLITH, EndMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.SKY_MONOLITH, SkyMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_MONOLITH, OceanMonolithRenderer::new);

		event.registerEntityRenderer(BTEntityTypes.DESTROY_TOWER, NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.PHYSICS_EXPLOSION, NoRenderEntity::new);

		// BLOCK ENTITIES
		event.registerBlockEntityRenderer(BTBlockEntityTypes.LAND_GOLEM_CHEST, LandGolemChestTileEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.LAND_CHEST, LandChestTileEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_MOB_SPAWNER, BTSpawnerBlockEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LandMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(OceanMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(CoreMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(NetherMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(EndMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(SkyMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);

		event.registerLayerDefinition(BTEntityTypes.SKY_MINION, SkyMinionRenderer::new);
		event.registerLayerDefinition(BTEntityTypes.SILVER_SKELETON, SkeletonRenderer::new);

		event.registerLayerDefinition(BTEntityTypes.LAND_MONOLITH, LandMonolithRenderer::new);
		event.registerLayerDefinition(BTEntityTypes.CORE_MONOLITH, CoreMonolithRenderer::new);
		event.registerLayerDefinition(BTEntityTypes.NETHER_MONOLITH, NetherMonolithRenderer::new);
		event.registerLayerDefinition(BTEntityTypes.END_MONOLITH, EndMonolithRenderer::new);
		event.registerLayerDefinition(BTEntityTypes.SKY_MONOLITH, SkyMonolithRenderer::new);
		event.registerLayerDefinition(BTEntityTypes.OCEAN_MONOLITH, OceanMonolithRenderer::new);

	}

}
