package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.block.blockentity.*;
import com.brass_amber.ba_bt.block.blockentity.chest.*;
import com.brass_amber.ba_bt.block.blockentity.spawner.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


@SuppressWarnings("DataFlowIssue")
public class BTBlockEntityType {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BABattleTowers.MOD_ID);


	public static final RegistryObject<BlockEntityType<BTChestBlockEntity>> BT_CHEST_ENTITY = BLOCK_ENTITY_TYPES.register(
			"bt_chest_entity", () -> BlockEntityType.Builder.of(
					BTChestBlockEntity::new,
					BTBlocks.LAND_CHEST.get(), BTBlocks.OCEAN_CHEST.get(), BTBlocks.CORE_CHEST.get(),
					BTBlocks.NETHER_CHEST.get(), BTBlocks.END_CHEST.get(), BTBlocks.SKY_CHEST.get(),
					BTBlocks.LAND_GOLEM_CHEST.get(), BTBlocks.OCEAN_GOLEM_CHEST.get(), BTBlocks.CORE_GOLEM_CHEST.get(),
					BTBlocks.NETHER_GOLEM_CHEST.get(), BTBlocks.END_GOLEM_CHEST.get(), BTBlocks.SKY_GOLEM_CHEST.get()
			).build(null)
	);

	public static final RegistryObject<BlockEntityType<BTSpawnerBlockEntity>> BT_SPAWNER = BLOCK_ENTITY_TYPES.register("bt_spawner", () -> BlockEntityType.Builder.of(
			BTSpawnerBlockEntity::new,
			BTBlocks.LAND_SPAWNER.get(), BTBlocks.OCEAN_SPAWNER.get(), BTBlocks.CORE_SPAWNER.get(),
			BTBlocks.NETHER_SPAWNER.get(), BTBlocks.END_SPAWNER.get(), BTBlocks.SKY_SPAWNER.get()
	).build(null));

	public static final RegistryObject<BlockEntityType<DataMarkerBlockEntity>> DATA_MARKER = BLOCK_ENTITY_TYPES.register("data_marker", () -> BlockEntityType.Builder.of(DataMarkerBlockEntity::new, BTBlocks.DATA_MARKER.get()).build(null));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITY_TYPES.register(eventBus);
	}

}
