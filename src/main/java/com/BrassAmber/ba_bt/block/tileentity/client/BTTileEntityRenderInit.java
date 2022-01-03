package com.BrassAmber.ba_bt.block.tileentity.client;

import com.BrassAmber.ba_bt.block.BTTileEntityTypes;
import com.BrassAmber.ba_bt.block.tileentity.client.renderer.BTMobSpawnerTileEntityRenderer;
import com.BrassAmber.ba_bt.block.tileentity.client.renderer.GolemChestTileEntityRenderer;
import com.BrassAmber.ba_bt.block.tileentity.client.renderer.StoneChestTileEntityRenderer;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber
public class BTTileEntityRenderInit {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void bindTileEntityRenderers(final FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntityRenderer(BTTileEntityTypes.GOLEM_CHEST, GolemChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BTTileEntityTypes.STONE_CHEST, StoneChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BTTileEntityTypes.BT_MOB_SPAWNER, BTMobSpawnerTileEntityRenderer::new);
	}
}
