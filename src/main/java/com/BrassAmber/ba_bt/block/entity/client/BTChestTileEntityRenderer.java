package com.BrassAmber.ba_bt.block.entity.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.entity.GolemChestTileEntity;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BTChestTileEntityRenderer extends ChestTileEntityRenderer<GolemChestTileEntity> {
	private static final RenderMaterial GOLEM_LOCATION = chestMaterial("golem");
	private static final RenderMaterial GOLEM_LOCATION_LEFT = chestMaterial("golem_left");
	private static final RenderMaterial GOLEM_LOCATION_RIGHT = chestMaterial("golem_right");

	public BTChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
	}

	@Override
	protected RenderMaterial getMaterial(GolemChestTileEntity tileEntity, ChestType chestType) {
		return chooseMaterial(chestType, GOLEM_LOCATION, GOLEM_LOCATION_LEFT, GOLEM_LOCATION_RIGHT);
	}

	private static RenderMaterial chestMaterial(String chestTypeName) {
		return new RenderMaterial(Atlases.CHEST_SHEET, BrassAmberBattleTowers.locate("entity/chest/" + chestTypeName));
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
