package com.brass_amber.ba_bt.client;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.client.model.block.MonolithModel;
import com.brass_amber.ba_bt.client.model.block.ObeliskModel;
import com.brass_amber.ba_bt.client.renderer.BTCultistRenderer;
import com.brass_amber.ba_bt.client.renderer.BTSpawnerBlockEntityRenderer;
import com.brass_amber.ba_bt.client.renderer.NoRenderEntity;
import com.brass_amber.ba_bt.client.renderer.SkyMinionRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.LandChestRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.LandGolemChestRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.OceanChestRenderer;
import com.brass_amber.ba_bt.client.renderer.chest.OceanGolemChestRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.CoreMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.LandMonolithRenderer;
import com.brass_amber.ba_bt.client.model.hostile.*;
import com.brass_amber.ba_bt.client.renderer.obelisk.*;
import com.brass_amber.ba_bt.init.BTBlockEntityTypes;
import com.brass_amber.ba_bt.init.BTEntityTypes;
import com.brass_amber.ba_bt.client.renderer.monolith.EndMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.NetherMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.OceanMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.monolith.SkyMonolithRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.CoreGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.EndGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.LandGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.NetherGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.OceanGolemRenderer;
import com.brass_amber.ba_bt.client.renderer.golem.SkyGolemRenderer;


import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

	private ClientEvents() {}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		// ENTITIES
		event.registerEntityRenderer(BTEntityTypes.LAND_GOLEM.get(), LandGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_GOLEM.get(), OceanGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.NETHER_GOLEM.get(), NetherGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.CORE_GOLEM.get(), CoreGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.END_GOLEM.get(), EndGolemRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.SKY_GOLEM.get(), SkyGolemRenderer::new);

		event.registerEntityRenderer(BTEntityTypes.SKY_MINION.get(), SkyMinionRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.PLATINUM_SKELETON.get(), SkeletonRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.BT_CULTIST.get(), BTCultistRenderer::new);

		event.registerEntityRenderer(BTEntityTypes.LAND_MONOLITH.get(), LandMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_MONOLITH.get(), OceanMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.CORE_MONOLITH.get(), CoreMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.NETHER_MONOLITH.get(), NetherMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.END_MONOLITH.get(), EndMonolithRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.SKY_MONOLITH.get(), SkyMonolithRenderer::new);


		event.registerEntityRenderer(BTEntityTypes.LAND_OBELISK.get(), LandObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_OBELISK.get(), OceanObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.CORE_OBELISK.get(), CoreObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.NETHER_OBELISK.get(), NetherObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.END_OBELISK.get(), EndObeliskRenderer::new);
		event.registerEntityRenderer(BTEntityTypes.SKY_OBELISK.get(), SkyObeliskRenderer::new);

		event.registerEntityRenderer(BTEntityTypes.LAND_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_DESTRUCTION.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.PHYSICS_EXPLOSION.get(), NoRenderEntity::new);

		// BLOCK ENTITIES
		event.registerBlockEntityRenderer(BTBlockEntityTypes.LAND_CHEST.get(), LandChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.LAND_GOLEM_CHEST.get(), LandGolemChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.OCEAN_CHEST.get(), OceanChestRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.OCEAN_GOLEM_CHEST.get(), OceanGolemChestRenderer::new);
		
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_LAND_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_OCEAN_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_CORE_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_NETHER_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_END_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_SKY_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LandMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(OceanMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(CoreMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(NetherMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(EndMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);
		event.registerLayerDefinition(SkyMonolithRenderer.TEXTURE, MonolithModel::createBodyLayer);

		event.registerLayerDefinition(LandObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(OceanObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(CoreObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(NetherObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(EndObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);
		event.registerLayerDefinition(SkyObeliskRenderer.TEXTURE, ObeliskModel::createBodyLayer);

		event.registerLayerDefinition(SkyMinionRenderer.TEXTURE, SkyMinionModel::createBodyLayer);
		event.registerLayerDefinition(BTCultistRenderer.TEXTURE, BTCultistModel::createBodyLayer);

		event.registerLayerDefinition(LandGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(CoreGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(NetherGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(EndGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(SkyGolemRenderer.LAYER, SkyGolemModel::createBodyLayer);
		event.registerLayerDefinition(OceanGolemRenderer.LAYER, OceanGolemModel::createBodyLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 0),
				LandChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 1),
				LandChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 2),
				LandChestRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 0),
				LandGolemChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 1),
				LandGolemChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 2),
				LandGolemChestRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 0),
				OceanChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 1),
				OceanChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 2),
				OceanChestRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 0),
				OceanGolemChestRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 1),
				OceanGolemChestRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 2),
				OceanGolemChestRenderer::createDoubleBodyRightLayer);
		}


}
