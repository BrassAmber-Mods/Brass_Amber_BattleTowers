package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BrassAmberBattleTowers.MOD_ID);

	public static final RegistryObject<BlockEntityType<GolemChestBlockEntity>> LAND_GOLEM_CHEST = BLOCK_ENTITY_TYPES.register("land_golem_chest", () -> BlockEntityType.Builder.of(GolemChestBlockEntity::new, BTBlocks.LAND_GOLEM_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<TowerChestBlockEntity>> LAND_CHEST = BLOCK_ENTITY_TYPES.register("land_chest", () -> BlockEntityType.Builder.of(TowerChestBlockEntity::new, BTBlocks.LAND_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<GolemChestBlockEntity>> OCEAN_GOLEM_CHEST = BLOCK_ENTITY_TYPES.register("ocean_golem_chest", () -> BlockEntityType.Builder.of(GolemChestBlockEntity::new, BTBlocks.OCEAN_GOLEM_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<TowerChestBlockEntity>> OCEAN_CHEST = BLOCK_ENTITY_TYPES.register("ocean_chest", () -> BlockEntityType.Builder.of(TowerChestBlockEntity::new, BTBlocks.OCEAN_CHEST.get()).build(null));

	public static final RegistryObject<BlockEntityType<BTSpawnerBlockEntity>> BT_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_spawner", () -> BlockEntityType.Builder.of(BTSpawnerBlockEntity::new, BTBlocks.BT_LAND_SPAWNER.get()).build(null));

}
