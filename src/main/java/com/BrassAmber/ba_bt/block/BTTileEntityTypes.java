package com.BrassAmber.ba_bt.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.entity.GolemChestTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTTileEntityTypes {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BrassAmberBattleTowers.MOD_ID);

	public static final TileEntityType<GolemChestTileEntity> GOLEM_CHEST = registerTileEntity("golem_chest", TileEntityType.Builder.of(GolemChestTileEntity::new, BTBlocks.GOLEM_CHEST));

	/**
	 * Helper method for registering Tile Entities
	 */
	private static <T extends TileEntity> TileEntityType<T> registerTileEntity(String registryName, TileEntityType.Builder<T> builder) {
		TileEntityType<T> tileEntityType = builder.build(null);
		TILE_ENTITY_TYPES.register(registryName, () -> tileEntityType);
		return tileEntityType;
	}
}
