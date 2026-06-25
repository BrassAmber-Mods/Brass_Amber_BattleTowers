package com.brass_amber.ba_bt.client;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.client.model.CorreyeModel;
import com.brass_amber.ba_bt.client.model.block.MonolithModel;
import com.brass_amber.ba_bt.client.model.block.ObeliskModel;
import com.brass_amber.ba_bt.client.renderer.*;
import com.brass_amber.ba_bt.client.renderer.chest.*;
import com.brass_amber.ba_bt.client.renderer.monolith.CoreBTMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.LandBTMonolithRenderer;
import com.brass_amber.ba_bt.client.model.hostile.*;
import com.brass_amber.ba_bt.client.renderer.obelisk.*;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.client.renderer.monolith.EndBTMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.NetherBTMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.OceanBTMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.SkyBTMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.CoreGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.EndGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.LandGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.NetherGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.OceanGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.SkyGolemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = BABattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

	private ClientEvents() {}
	
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		// ENTITIES
		event.registerEntityRenderer(BTEntityType.LAND_GOLEM.get(), LandGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.OCEAN_GOLEM.get(), OceanGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.NETHER_GOLEM.get(), NetherGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.CORE_GOLEM.get(), CoreGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.END_GOLEM.get(), EndGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.SKY_GOLEM.get(), SkyGolemRenderer::new);

		event.registerEntityRenderer(BTEntityType.SKY_MINION.get(), SkyMinionRenderer::new);
		event.registerEntityRenderer(BTEntityType.BT_CULTIST.get(), BTCultistRenderer::new);
		event.registerEntityRenderer(BTEntityType.FRAGMENT_OF_OBTHUURYN.get(), FragmentOfObthuurynRenderer::new);
		event.registerEntityRenderer(BTEntityType.CORREYE_ENTITY.get(), CorreyeRenderer::new);

		event.registerEntityRenderer(BTEntityType.LAND_MONOLITH.get(), LandBTMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityType.OCEAN_MONOLITH.get(), OceanBTMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityType.CORE_MONOLITH.get(), CoreBTMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityType.NETHER_MONOLITH.get(), NetherBTMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityType.END_MONOLITH.get(), EndBTMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityType.SKY_MONOLITH.get(), SkyBTMonolithRenderer::new);

		event.registerEntityRenderer(BTEntityType.LAND_OBELISK.get(), LandBTObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityType.OCEAN_OBELISK.get(), OceanBTObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityType.CORE_OBELISK.get(), CoreBTObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityType.NETHER_OBELISK.get(), NetherBTObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityType.END_OBELISK.get(), EndBTObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityType.SKY_OBELISK.get(), SkyBTObeliskRenderer::new);

		event.registerEntityRenderer(BTEntityType.LAND_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.OCEAN_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.CORE_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.NETHER_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.END_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.SKY_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.PHYSICS_EXPLOSION.get(), NoRenderEntity::new);

		// BLOCK ENTITIES
		event.registerBlockEntityRenderer(BTBlockEntityType.LAND_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.LAND_GOLEM_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.OCEAN_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.OCEAN_GOLEM_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.CORE_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.CORE_GOLEM_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.NETHER_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.NETHER_GOLEM_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.END_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.END_GOLEM_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.SKY_CHEST.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.SKY_GOLEM_CHEST.get(), BTChestRenderer::new);
		
		event.registerBlockEntityRenderer(BTBlockEntityType.LAND_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.OCEAN_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.CORE_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.NETHER_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.END_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.SKY_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);

		event.registerBlockEntityRenderer(BTBlockEntityType.DATA_MARKER.get(), DataMarkerBlockRenderer::new);
	}


	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LandBTMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(OceanBTMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(CoreBTMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(NetherBTMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(EndBTMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(SkyBTMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);

		event.registerLayerDefinition(LandBTObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(OceanBTObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(CoreBTObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(NetherBTObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(EndBTObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(SkyBTObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);

		event.registerLayerDefinition(SkyMinionRenderer.TEXTURE, SkyMinionModel::createBodyLayer);
		event.registerLayerDefinition(BTCultistRenderer.TEXTURE, BTCultistModel::createBodyLayer);
		event.registerLayerDefinition(FragmentOfObthuurynRenderer.TEXTURE, FragmentOfObthuurynModel::createBodyLayer);
		event.registerLayerDefinition(CorreyeRenderer.TEXTURE, CorreyeModel::createBodyLayer);

		event.registerLayerDefinition(LandGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(OceanGolemRenderer.LAYER, OceanGolemModel::createBodyLayer);
		event.registerLayerDefinition(CoreGolemRenderer.LAYER, CoreGolemModel::createBodyLayer);
		event.registerLayerDefinition(NetherGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(EndGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(SkyGolemRenderer.LAYER, SkyGolemModel::createBodyLayer);
	}

}
