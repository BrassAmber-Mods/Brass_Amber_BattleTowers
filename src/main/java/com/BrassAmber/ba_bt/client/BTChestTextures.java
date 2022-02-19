package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Bus.MOD)
public class BTChestTextures {
	public static final ResourceLocation[] LAND_GOLEM_CHEST_TEXTURES = locateChestTextures("golem", "land_golem");
	public static final ResourceLocation[] LAND_CHEST_TEXTURES = locateChestTextures("tower", "land");


	/**
	 * We need to stitch the textures before we can use them. Otherwise they'll just appear as missing textures.
	 */
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent()
	public static void textureStitch(TextureStitchEvent.Pre event) {
		if (event.getAtlas().equals(TextureManager)) {
			stitchAll(event, LAND_GOLEM_CHEST_TEXTURES);
			stitchAll(event, LAND_CHEST_TEXTURES);
		}
	}

	/**
	 * Stitch all textures in the array
	 */
	private static void stitchAll(TextureStitchEvent.Pre event, ResourceLocation[] textureLocations) {
		for (ResourceLocation chestTexture : textureLocations) {
			event.addSprite(chestTexture);
		}
	}

	/**
	 * Helper method for new chest textures
	 */
	private static ResourceLocation[] locateChestTextures(String chestType, String chestName) {
		return new ResourceLocation[] {
			BrassAmberBattleTowers.locate("entity/chest/" + chestType + "_chest/" + chestName),
			BrassAmberBattleTowers.locate("entity/chest/" + chestType + "_chest/" + chestName + "_left"),
			BrassAmberBattleTowers.locate("entity/chest/" + chestType + "_chest/" + chestName + "_right")
		};
	}
}
