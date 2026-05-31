package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.block.blockentity.chest.BTChestBlockEntity;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class BTChestRenderer extends ChestRenderer<BTChestBlockEntity> {
	public static Map<ChestType, String> typeStringMap = Map.of(ChestType.SINGLE, "", ChestType.LEFT, "_left", ChestType.RIGHT, "_right");

	public BTChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected Material getMaterial(BTChestBlockEntity chestBlockEntity, ChestType chestType) {
		return getChestMaterial(chestBlockEntity.namePrefix, typeStringMap.get(chestType), chestBlockEntity.isGolemChest());
	}

	private static Material getChestMaterial(String name, String type, boolean isGolem) {
		if (isGolem) {
			// BABTMain.LOGGER.debug(BABTMain.locate("entity/chest/" + name + "_golem_chest" + type).getPath());
			return new Material(Sheets.CHEST_SHEET, BABattleTowers.locate("entity/chest/" + name + "_golem_chest" + type));
		}
		// BABTMain.LOGGER.debug(BABTMain.locate("entity/chest/" + name + "_chest" + type).getPath());
		return new Material(Sheets.CHEST_SHEET, BABattleTowers.locate("entity/chest/" + name + "_chest" + type));
	}
}
