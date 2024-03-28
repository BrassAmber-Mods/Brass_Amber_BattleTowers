package com.brass_amber.ba_bt.client.renderer.chest;


import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractBTChestRenderer extends ChestRenderer<ChestBlockEntity> {
	private final Material singleChest;
	private final Material doubleChestLeft;
	private final Material doubleChestRight;

	public AbstractBTChestRenderer(BlockEntityRendererProvider.Context context, ResourceLocation[] chestTextureLocations) {
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

	private static Material chooseMaterial(ChestType chestType, Material single, Material doubleLeft, Material doubleRight) {
        return switch (chestType) {
            case LEFT -> doubleLeft;
            case RIGHT -> doubleRight;
            default -> single;
        };
	}
}
