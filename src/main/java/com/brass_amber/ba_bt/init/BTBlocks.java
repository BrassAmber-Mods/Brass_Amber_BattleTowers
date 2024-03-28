package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.block.block.*;
import com.brass_amber.ba_bt.block.block.GolemChestBlock.BTChestType;
import com.brass_amber.ba_bt.item.ChestBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.function.Supplier;


public class BTBlocks {

	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BrassAmberBattleTowers.MODID);

	public static final DeferredBlock<Block> LAND_GOLEM_CHEST = registerChestBlock("land_golem_chest",
			() -> new GolemChestBlock(
					BTChestType.GOLEM, BTBlockEntityType.LAND_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F).sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F)), 1
	);
	public static final DeferredBlock<Block> LAND_CHEST = registerChestBlock("land_chest",
			() -> new TowerChestBlock(
					BTChestType.TOWER, BTBlockEntityType.LAND_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F).sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F)), 1
	);

	public static final DeferredBlock<Block> OCEAN_GOLEM_CHEST = registerChestBlock("ocean_golem_chest",
			() -> new GolemChestBlock(
					BTChestType.GOLEM, BTBlockEntityType.OCEAN_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F).sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F)), 1
	);
	public static final DeferredBlock<Block> OCEAN_CHEST = registerChestBlock("ocean_chest",
			() -> new TowerChestBlock(
					BTChestType.TOWER, BTBlockEntityType.OCEAN_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F).sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F)), 1
	);

    public static final DeferredBlock<Block> BT_LAND_SPAWNER = registerBlock("bt_land_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()), 1);

	public static final DeferredBlock<Block> BT_OCEAN_SPAWNER = registerBlock("bt_ocean_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()), 1);

	public static final DeferredBlock<Block> BT_CORE_SPAWNER = registerBlock("bt_core_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()), 1);

	public static final DeferredBlock<Block> BT_NETHER_SPAWNER = registerBlock("bt_nether_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()), 1);

	public static final DeferredBlock<Block> BT_END_SPAWNER = registerBlock("bt_end_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()), 1);

	public static final DeferredBlock<Block> BT_SKY_SPAWNER = registerBlock("bt_sky_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()), 1);

	public static final  DeferredBlock<Block> BT_SPAWNER_MARKER = registerBlock("spawner_marker",
			() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 1200.0F).noOcclusion().explosionResistance(6.0F).isValidSpawn(BTBlocks::never)), 1);

	public static final DeferredBlock<Block> BT_AIR_FILL = registerBlock("bt_air_fill",
			() -> new BTBlockingAirBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().noLootTable().air()), 1);



	private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
		return false;
	}

	private static Boolean always(BlockState p_50810_, BlockGetter p_50811_, BlockPos p_50812_, EntityType<?> p_50813_) {
		return true;
	}

	private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, int stackSize) {
		DeferredBlock<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, stackSize);
		return toReturn;
	}
	
	private static  <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block, int stackSize) {
		BTItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(stackSize)));
	}


	private static <T extends Block> DeferredBlock<T> registerChestBlock(String name, Supplier<T> block, int stackSize) {
		DeferredBlock<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, stackSize);
		return toReturn;
	}

	private static  <T extends Block> DeferredItem<Item> registerChestBlockItem(String name, DeferredBlock<T> block, int stackSize) {
		return BTItems.ITEMS.register(name, () -> new ChestBlockItem(block.get(), new Item.Properties().stacksTo(stackSize)));
	}

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

}
