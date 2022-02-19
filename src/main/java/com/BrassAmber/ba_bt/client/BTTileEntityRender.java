package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.init.BTTileEntityTypes;
import com.BrassAmber.ba_bt.client.renderer.BTMobSpawnerTileEntityRenderer;
import com.BrassAmber.ba_bt.client.renderer.LandGolemChestTileEntityRenderer;
import com.BrassAmber.ba_bt.client.renderer.LandChestTileEntityRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber
public class BTTileEntityRender {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void bindTileEntityRenderers(final FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntityRenderer(BTBlockEntityTypes.LAND_GOLEM_CHEST, LandGolemChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BTBlockEntityTypes.LAND_CHEST, LandChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BTBlockEntityTypes.BT_MOB_SPAWNER, BTMobSpawnerTileEntityRenderer::new);
	}
}
