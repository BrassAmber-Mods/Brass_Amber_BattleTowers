package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BrassAmberBattleTowers.MOD_ID);

	public static final BlockEntityType<GolemChestBlockEntity> LAND_GOLEM_CHEST = registerBlockEntity("land_golem_chest", BlockEntityType.Builder.of(GolemChestBlockEntity::new, BTBlocks.LAND_GOLEM_CHEST));
	public static final BlockEntityType<TowerChestBlockEntity> LAND_CHEST = registerBlockEntity("land_chest", BlockEntityType.Builder.of(TowerChestBlockEntity::new, BTBlocks.LAND_CHEST));
	public static final BlockEntityType<BTSpawnerBlockEntity> BT_MOB_SPAWNER = registerBlockEntity("bt_spawner", BlockEntityType.Builder.of(BTSpawnerBlockEntity::new, BTBlocks.BT_LAND_SPAWNER));

	/**
	 * Helper method for registering Tile Entities
	 */
	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String registryName, BlockEntityType.Builder<T> builder) {
		BlockEntityType<T> BlockEntityType = builder.build(null);
		TILE_ENTITY_TYPES.register(registryName, () -> BlockEntityType);
		return BlockEntityType;
	}
}
