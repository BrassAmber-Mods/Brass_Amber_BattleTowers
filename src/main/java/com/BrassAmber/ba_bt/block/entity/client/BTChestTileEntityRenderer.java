package com.BrassAmber.ba_bt.block.entity.client;

import com.BrassAmber.ba_bt.block.entity.BTChestTileEntity;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BTChestTileEntityRenderer extends ChestTileEntityRenderer<BTChestTileEntity> {
	public static final RenderMaterial CHEST_XMAS_LOCATION = chestMaterial("christmas");
	public static final RenderMaterial CHEST_XMAS_LOCATION_LEFT = chestMaterial("christmas_left");
	public static final RenderMaterial CHEST_XMAS_LOCATION_RIGHT = chestMaterial("christmas_right");

	public BTChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
	}

	@Override
	protected RenderMaterial getMaterial(BTChestTileEntity tileEntity, ChestType chestType) {
		return chooseMaterial(chestType, CHEST_XMAS_LOCATION, CHEST_XMAS_LOCATION_LEFT, CHEST_XMAS_LOCATION_RIGHT);
	}

	/**
	 * TODO Can't get custom textures working. Seems that I need to use TextureStitchEvent.Pre looking at the IronChestsMod
	 * {@linkplain https://github.com/progwml6/ironchest/blob/1.16/src/main/java/com/progwml6/ironchest/client/model/IronChestsModels.java}
	 */
	private static RenderMaterial chestMaterial(String chestTypeName) {
		return new RenderMaterial(Atlases.CHEST_SHEET, new ResourceLocation("entity/chest/" + chestTypeName));
	}

	private static RenderMaterial chooseMaterial(ChestType chestType, RenderMaterial renderMaterialSingle, RenderMaterial renderMaterialLeft, RenderMaterial renderMaterialRight) {
		switch (chestType) {
		case LEFT:
			return renderMaterialLeft;
		case RIGHT:
			return renderMaterialRight;
		case SINGLE:
		default:
			return renderMaterialSingle;
		}
	}
}
