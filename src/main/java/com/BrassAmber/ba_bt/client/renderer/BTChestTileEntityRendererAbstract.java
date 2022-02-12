package com.BrassAmber.ba_bt.client.renderer;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BTChestTileEntityRendererAbstract extends ChestTileEntityRenderer<ChestTileEntity> {
	private final RenderMaterial singleChest;
	private final RenderMaterial doubleChestLeft;
	private final RenderMaterial doubleChestRight;

	public BTChestTileEntityRendererAbstract(TileEntityRendererDispatcher tileEntityRendererDispatcher, ResourceLocation[] chestTextureLocations) {
		super(tileEntityRendererDispatcher);
		this.singleChest = chestMaterial(chestTextureLocations[0]);
		this.doubleChestLeft = chestMaterial(chestTextureLocations[1]);
		this.doubleChestRight = chestMaterial(chestTextureLocations[2]);
	}

	@Override
	protected RenderMaterial getMaterial(ChestTileEntity tileEntity, ChestType chestType) {
		return chooseMaterial(chestType, this.singleChest, this.doubleChestLeft, this.doubleChestRight);
	}

	private static RenderMaterial chestMaterial(ResourceLocation chestTextureLocation) {
		return new RenderMaterial(Atlases.CHEST_SHEET, chestTextureLocation);
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
