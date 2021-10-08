package com.BrassAmber.ba_bt.block.entity.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Bus.MOD)
public class BTChestTextures {
	public static final ResourceLocation GOLEM_CHEST = BrassAmberBattleTowers.locate("entity/chest/golem");
	public static final ResourceLocation GOLEM_CHEST_LEFT = BrassAmberBattleTowers.locate("entity/chest/golem_left");
	public static final ResourceLocation GOLEM_CHEST_RIGHT = BrassAmberBattleTowers.locate("entity/chest/golem_right");

	/**
	 * We need to stitch the textures before we can use them. Otherwise they'll just appear as missing textures.
	 */
	@SubscribeEvent
	public static void textureStitch(TextureStitchEvent.Pre event) {
		if (event.getMap().location().equals(Atlases.CHEST_SHEET)) {
			event.addSprite(GOLEM_CHEST);
			event.addSprite(GOLEM_CHEST_LEFT);
			event.addSprite(GOLEM_CHEST_RIGHT);
		}
	}
}
