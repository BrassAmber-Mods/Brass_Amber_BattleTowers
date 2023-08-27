package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.blockentity.spawner.*;
import com.BrassAmber.ba_bt.block.blockentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.block.blockentity.TowerChestBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BrassAmberBattleTowers.MODID);

	public static final RegistryObject<BlockEntityType<GolemChestBlockEntity>> LAND_GOLEM_CHEST = BLOCK_ENTITY_TYPES.register("land_golem_chest", () -> BlockEntityType.Builder.of(GolemChestBlockEntity::new, BTBlocks.LAND_GOLEM_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<TowerChestBlockEntity>> LAND_CHEST = BLOCK_ENTITY_TYPES.register("land_chest", () -> BlockEntityType.Builder.of(TowerChestBlockEntity::new, BTBlocks.LAND_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<GolemChestBlockEntity>> OCEAN_GOLEM_CHEST = BLOCK_ENTITY_TYPES.register("ocean_golem_chest", () -> BlockEntityType.Builder.of(GolemChestBlockEntity::new, BTBlocks.OCEAN_GOLEM_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<TowerChestBlockEntity>> OCEAN_CHEST = BLOCK_ENTITY_TYPES.register("ocean_chest", () -> BlockEntityType.Builder.of(TowerChestBlockEntity::new, BTBlocks.OCEAN_CHEST.get()).build(null));

	public static final RegistryObject<BlockEntityType<BTLandSpawnerEntity>> BT_LAND_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_l_spawner", () -> BlockEntityType.Builder.of(BTLandSpawnerEntity::new, BTBlocks.BT_LAND_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BTOceanSpawnerEntity>> BT_OCEAN_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_o_spawner", () -> BlockEntityType.Builder.of(BTOceanSpawnerEntity::new, BTBlocks.BT_OCEAN_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BTCoreSpawnerEntity>> BT_CORE_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_c_spawner", () -> BlockEntityType.Builder.of(BTCoreSpawnerEntity::new, BTBlocks.BT_CORE_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BTNetherSpawnerEntity>> BT_NETHER_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_n_spawner", () -> BlockEntityType.Builder.of(BTNetherSpawnerEntity::new, BTBlocks.BT_NETHER_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BTEndSpawnerEntity>> BT_END_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_e_spawner", () -> BlockEntityType.Builder.of(BTEndSpawnerEntity::new, BTBlocks.BT_END_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BTSkySpawnerEntity>> BT_SKY_MOB_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_s_spawner", () -> BlockEntityType.Builder.of(BTSkySpawnerEntity::new, BTBlocks.BT_SKY_SPAWNER.get()).build(null));
}
