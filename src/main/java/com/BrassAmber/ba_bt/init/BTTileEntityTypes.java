package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.BTMobSpawnerTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.StoneChestTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTTileEntityTypes {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BrassAmberBattleTowers.MOD_ID);

	public static final TileEntityType<GolemChestTileEntity> LAND_GOLEM_CHEST = registerTileEntity("land_golem_chest", TileEntityType.Builder.of(GolemChestTileEntity::new, BTBlocks.LAND_GOLEM_CHEST));
	public static final TileEntityType<StoneChestTileEntity> LAND_CHEST = registerTileEntity("land_chest", TileEntityType.Builder.of(StoneChestTileEntity::new, BTBlocks.LAND_CHEST));
	public static final TileEntityType<BTMobSpawnerTileEntity> BT_MOB_SPAWNER = registerTileEntity("bt_spawner", TileEntityType.Builder.of(BTMobSpawnerTileEntity::new, BTBlocks.BT_LAND_SPAWNER));

	/**
	 * Helper method for registering Tile Entities
	 */
	private static <T extends TileEntity> TileEntityType<T> registerTileEntity(String registryName, TileEntityType.Builder<T> builder) {
		TileEntityType<T> tileEntityType = builder.build(null);
		TILE_ENTITY_TYPES.register(registryName, () -> tileEntityType);
		return tileEntityType;
	}
}