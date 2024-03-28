package com.brass_amber.ba_bt.client;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.client.model.block.MonolithModel;
import com.brass_amber.ba_bt.client.model.block.ObeliskModel;
import com.brass_amber.ba_bt.client.renderer.BTCultistRenderer;
import com.brass_amber.ba_bt.client.renderer.BTSpawnerBlockEntityRenderer;
import com.brass_amber.ba_bt.client.renderer.NoRenderEntity;
import com.brass_amber.ba_bt.client.renderer.SkyMinionRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.LandBTChestRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.LandBTGolemChestRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.OceanBTChestRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.OceanBTGolemChestRenderer;
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
import com.brass_amber.ba_bt.client.renderer.golem.CoreBTGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.EndBTGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.LandBTGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.NetherBTGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.OceanBTGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.SkyBTGolemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

	private ClientEvents() {}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		// ENTITIES
		event.registerEntityRenderer(BTEntityType.LAND_GOLEM.get(), LandBTGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.OCEAN_GOLEM.get(), OceanBTGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.NETHER_GOLEM.get(), NetherBTGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.CORE_GOLEM.get(), CoreBTGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.END_GOLEM.get(), EndBTGolemRenderer::new);
		event.registerEntityRenderer(BTEntityType.SKY_GOLEM.get(), SkyBTGolemRenderer::new);

		event.registerEntityRenderer(BTEntityType.SKY_MINION.get(), SkyMinionRenderer::new);
		event.registerEntityRenderer(BTEntityType.BT_CULTIST.get(), BTCultistRenderer::new);

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
		event.registerEntityRenderer(BTEntityType.PHYSICS_EXPLOSION.get(), NoRenderEntity::new);

		// BLOCK ENTITIES
		event.registerBlockEntityRenderer(BTBlockEntityType.LAND_CHEST.get(), LandBTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.LAND_GOLEM_CHEST.get(), LandBTGolemChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.OCEAN_CHEST.get(), OceanBTChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.OCEAN_GOLEM_CHEST.get(), OceanBTGolemChestRenderer::new);
		
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_LAND_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_OCEAN_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_CORE_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_NETHER_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_END_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityType.BT_SKY_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
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

		event.registerLayerDefinition(LandBTGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(CoreBTGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(NetherBTGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(EndBTGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(SkyBTGolemRenderer.LAYER, SkyGolemModel::createBodyLayer);
		event.registerLayerDefinition(OceanBTGolemRenderer.LAYER, OceanGolemModel::createBodyLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 0),
				LandBTChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 1),
				LandBTChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 2),
				LandBTChestRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 0),
				LandBTGolemChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 1),
				LandBTGolemChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 2),
				LandBTGolemChestRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 0),
				OceanBTChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 1),
				OceanBTChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 2),
				OceanBTChestRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 0),
				OceanBTGolemChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 1),
				OceanBTGolemChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 2),
				OceanBTGolemChestRenderer::createDoubleBodyRightLayer);
		}


}
