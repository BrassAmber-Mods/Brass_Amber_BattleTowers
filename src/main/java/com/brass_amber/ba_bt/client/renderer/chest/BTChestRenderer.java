package com.brass_amber.ba_bt.client.renderer.chest;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.block.blockentity.BTChestBlockEntity;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BTChestRenderer extends ChestRenderer<BTChestBlockEntity> {
	public static Material[] single = new Material[TowerType.VALUES.length];
	public static Material[] left = new Material[TowerType.VALUES.length];
	public static Material[] right = new Material[TowerType.VALUES.length];

	public static Material[] singleGolem = new Material[TowerType.VALUES.length];
	public static Material[] leftGolem = new Material[TowerType.VALUES.length];
	public static Material[] rightGolem = new Material[TowerType.VALUES.length];

	static {
		for (TowerType type: TowerType.VALUES) {
			single[type.ordinal()] = getChestMaterial(type.name().toLowerCase(), "", false);
			left[type.ordinal()] = getChestMaterial(type.name().toLowerCase(), "_left", false);
			right[type.ordinal()] = getChestMaterial(type.name().toLowerCase(), "_right", false);
			singleGolem[type.ordinal()] = getChestMaterial(type.name().toLowerCase(), "", true);
			leftGolem[type.ordinal()] = getChestMaterial(type.name().toLowerCase(), "_left", true);
			rightGolem[type.ordinal()] = getChestMaterial(type.name().toLowerCase(), "_right", true);
		}
	}

	public BTChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected Material getMaterial(BTChestBlockEntity chestBlockEntity, ChestType chestType) {
		return getChestMaterial(chestBlockEntity, chestType);
	}

	private static Material getChestMaterial(String name, String type, boolean isGolem) {
		if (isGolem) {
			// BABTMain.LOGGER.debug(BABTMain.locate("entity/chest/" + name + "_golem_chest" + type).getPath());
			return new Material(Sheets.CHEST_SHEET, BABattleTowers.locate("entity/chest/" + name + "_golem_chest" + type));
		}
		// BABTMain.LOGGER.debug(BABTMain.locate("entity/chest/" + name + "_chest" + type).getPath());
		return new Material(Sheets.CHEST_SHEET, BABattleTowers.locate("entity/chest/" + name + "_chest" + type));

	}

	private static Material getChestMaterial(BTChestBlockEntity chestBlockEntity, ChestType type) {
		if (chestBlockEntity.isGolemChest()) {
			return switch (type) {
				case LEFT -> leftGolem[chestBlockEntity.getChestType().ordinal()];
				case RIGHT -> rightGolem[chestBlockEntity.getChestType().ordinal()];
				default -> singleGolem[chestBlockEntity.getChestType().ordinal()];
			};
		}
        return switch (type) {
            case LEFT -> left[chestBlockEntity.getChestType().ordinal()];
            case RIGHT -> right[chestBlockEntity.getChestType().ordinal()];
            default -> single[chestBlockEntity.getChestType().ordinal()];
        };
	}
}
