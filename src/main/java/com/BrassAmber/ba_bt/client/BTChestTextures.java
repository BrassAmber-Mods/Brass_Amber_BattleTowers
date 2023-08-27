package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@EventBusSubscriber(modid = BrassAmberBattleTowers.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class BTChestTextures {
	public static final ResourceLocation[] LAND_GOLEM_CHEST_TEXTURES = locateChestTextures("golem", "land_golem");
	public static final ResourceLocation[] LAND_CHEST_TEXTURES = locateChestTextures("tower", "land");
	public static final ResourceLocation[] OCEAN_GOLEM_CHEST_TEXTURES = locateChestTextures("golem", "ocean_golem");
	public static final ResourceLocation[] OCEAN_CHEST_TEXTURES = locateChestTextures("tower", "ocean");


	/**
	 * We need to stitch the textures before we can use them. Otherwise they'll just appear as missing textures.
	 */
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent()
	public static void textureStitch(TextureStitchEvent event) {
		stitchAll(event, LAND_GOLEM_CHEST_TEXTURES);
		stitchAll(event, LAND_CHEST_TEXTURES);
		stitchAll(event, OCEAN_GOLEM_CHEST_TEXTURES);
		stitchAll(event, OCEAN_CHEST_TEXTURES);
	}

	/**
	 * Stitch all textures in the array
	 */
	private static void stitchAll(TextureStitchEvent event, ResourceLocation[] textureLocations) {
		for (ResourceLocation chestTexture : textureLocations) {
			event.getAtlas().getSprite(chestTexture);
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

	public static ModelLayerLocation locateChestLayer(String chestType, String chestName, int side) {
		if (side == 0) {
			return new ModelLayerLocation(BrassAmberBattleTowers.locate("entity/chest/" + chestType + "_chest/" + chestName), "main");
		} else if (side == 1) {
			return  new ModelLayerLocation(BrassAmberBattleTowers.locate("entity/chest/" + chestType + "_chest/" + chestName + "_left"), "main");
		} else if (side == 2) {
			return  new ModelLayerLocation(BrassAmberBattleTowers.locate("entity/chest/" + chestType + "_chest/" + chestName + "_right"), "main");
		}
		return null;
	}
}
