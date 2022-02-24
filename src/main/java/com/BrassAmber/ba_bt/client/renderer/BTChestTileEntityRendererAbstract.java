package com.BrassAmber.ba_bt.client.renderer;


import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public abstract class BTChestTileEntityRendererAbstract extends ChestRenderer<ChestBlockEntity> {
	private final Material singleChest;
	private final Material doubleChestLeft;
	private final Material doubleChestRight;

	public BTChestTileEntityRendererAbstract(BlockEntityRendererProvider.Context context, ResourceLocation[] chestTextureLocations) {
		super(context);
		this.singleChest = chestMaterial(chestTextureLocations[0]);
		this.doubleChestLeft = chestMaterial(chestTextureLocations[1]);
		this.doubleChestRight = chestMaterial(chestTextureLocations[2]);
	}

	@Override
	protected @NotNull Material getMaterial(@NotNull ChestBlockEntity tileEntity, @NotNull ChestType chestType) {
		return chooseMaterial(chestType, this.singleChest, this.doubleChestLeft, this.doubleChestRight);
	}

	private static Material chestMaterial(ResourceLocation chestTextureLocation) {
		return new Material(Sheets.CHEST_SHEET, chestTextureLocation);
	}

	private static Material chooseMaterial(ChestType chestType, Material renderMaterialSingle, Material renderMaterialLeft, Material renderMaterialRight) {
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
