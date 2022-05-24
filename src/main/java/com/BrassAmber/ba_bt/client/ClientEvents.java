package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.client.model.block.MonolithModel;
import com.BrassAmber.ba_bt.client.model.hostile.*;
import com.BrassAmber.ba_bt.client.renderer.*;
import com.BrassAmber.ba_bt.client.renderer.chest.LandChestBlockEntityRenderer;
import com.BrassAmber.ba_bt.client.renderer.chest.LandGolemChestBlockEntityRenderer;
import com.BrassAmber.ba_bt.client.renderer.chest.OceanChestBlockEntityRenderer;
import com.BrassAmber.ba_bt.client.renderer.chest.OceanGolemChestBlockEntityRenderer;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.client.renderer.monolith.CoreMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.monolith.EndMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.monolith.LandMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.monolith.NetherMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.monolith.OceanMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.monolith.SkyMonolithRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.CoreGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.EndGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.LandGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.NetherGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.OceanGolemRenderer;
import com.BrassAmber.ba_bt.client.renderer.golem.SkyGolemRenderer;


import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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


		event.registerEntityRenderer(BTEntityTypes.LAND_OBELISK.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.OCEAN_OBELISK.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.CORE_OBELISK.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.NETHER_OBELISK.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.END_OBELISK.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.SKY_OBELISK.get(), NoRenderEntity::new);

		event.registerEntityRenderer(BTEntityTypes.DESTROY_TOWER.get(), NoRenderEntity::new);
		event.registerEntityRenderer(BTEntityTypes.PHYSICS_EXPLOSION.get(), NoRenderEntity::new);

		// BLOCK ENTITIES
		event.registerBlockEntityRenderer(BTBlockEntityTypes.LAND_CHEST.get(), LandChestBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.LAND_GOLEM_CHEST.get(), LandGolemChestBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.OCEAN_CHEST.get(), OceanChestBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(BTBlockEntityTypes.OCEAN_GOLEM_CHEST.get(), OceanGolemChestBlockEntityRenderer::new);
		
		event.registerBlockEntityRenderer(BTBlockEntityTypes.BT_MOB_SPAWNER.get(), BTSpawnerBlockEntityRenderer::new);
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

		event.registerLayerDefinition(SkyMinionRenderer.TEXTURE, SkyMinionModel::createBodyLayer);
		event.registerLayerDefinition(BTCultistRenderer.TEXTURE, BTCultistModel::createBodyLayer);

		event.registerLayerDefinition(LandGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(CoreGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(NetherGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(EndGolemRenderer.LAYER, LandGolemModel::createBodyLayer);
		event.registerLayerDefinition(SkyGolemRenderer.LAYER, SkyGolemModel::createBodyLayer);
		event.registerLayerDefinition(OceanGolemRenderer.LAYER, OceanGolemModel::createBodyLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 0),
				LandChestBlockEntityRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 1),
				LandChestBlockEntityRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "land", 2),
				LandChestBlockEntityRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 0),
				LandGolemChestBlockEntityRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 1),
				LandGolemChestBlockEntityRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "land_golem", 2),
				LandGolemChestBlockEntityRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 0),
				OceanChestBlockEntityRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 1),
				OceanChestBlockEntityRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("tower", "ocean", 2),
				OceanChestBlockEntityRenderer::createDoubleBodyRightLayer);

		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 0),
				OceanGolemChestBlockEntityRenderer::createSingleBodyLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 1),
				OceanGolemChestBlockEntityRenderer::createDoubleBodyLeftLayer);
		event.registerLayerDefinition(BTChestTextures.locateChestLayer("golem", "ocean_golem", 2),
				OceanGolemChestBlockEntityRenderer::createDoubleBodyRightLayer);

		ItemBlockRenderTypes.setRenderLayer(BTBlocks.BT_LAND_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BTBlocks.BT_OCEAN_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BTBlocks.BT_CORE_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BTBlocks.BT_NETHER_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BTBlocks.BT_END_SPAWNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BTBlocks.BT_SKY_SPAWNER.get(), RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(BTBlocks.TAB_ICON.get(), RenderType.cutout());
	}


}
