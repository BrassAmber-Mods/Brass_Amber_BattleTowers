package com.brass_amber.ba_bt.client;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.client.model.CorreyeModel;
import com.brass_amber.ba_bt.client.model.block.MonolithModel;
import com.brass_amber.ba_bt.client.model.block.ObeliskModel;
import com.brass_amber.ba_bt.client.renderer.*;
import com.brass_amber.ba_bt.client.renderer.chest.*;
import com.brass_amber.ba_bt.client.model.hostile.*;
import com.brass_amber.ba_bt.client.renderer.monolith.MonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.obelisk.*;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import com.brass_amber.ba_bt.init.BTEntityType;
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

		event.registerEntityRenderer(BTEntityType.MONOLITH.get(), MonolithRenderer::new);
		event.registerEntityRenderer(BTEntityType.OBELISK.get(), ObeliskRenderer::new);

		event.registerEntityRenderer(BTEntityType.LAND_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.OCEAN_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.CORE_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.NETHER_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.END_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.SKY_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityType.PHYSICS_EXPLOSION.get(), NoRenderEntity::new);

		// BLOCK ENTITIES
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_CHEST_ENTITY.get(), BTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.DATA_MARKER.get(), DataMarkerBlockRenderer::new);
	}


	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(MonolithModel.LAYER_LOCATION, MonolithModel::createBodyLayer);

		event.registerLayerDefinition(ObeliskModel.LAYER_LOCATION, ObeliskModel::createBodyLayer);

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
