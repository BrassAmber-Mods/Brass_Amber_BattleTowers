package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.block.block.*;
import com.brass_amber.ba_bt.item.ChestBlockItem;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class BTBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, BABTMain.MODID);

	public static final RegistryObject<Block> LAND_GOLEM_CHEST = registerChestBlock("land_golem_chest",
			() -> new BTChestBlock(BTBlockEntityType.LAND_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F), GolemType.LAND),
			1
	);
	public static final RegistryObject<Block> LAND_CHEST = registerChestBlock("land_chest",
			() -> new TowerChestBlock(BTBlockEntityType.LAND_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F), GolemType.LAND),
			1
	);

	public static final RegistryObject<Block> OCEAN_GOLEM_CHEST = registerChestBlock("ocean_golem_chest",
			() -> new BTChestBlock(BTBlockEntityType.OCEAN_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F), GolemType.OCEAN),
			1
	);
	public static final RegistryObject<Block> OCEAN_CHEST = registerChestBlock("ocean_chest",
			() -> new TowerChestBlock(BTBlockEntityType.OCEAN_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F), GolemType.OCEAN),
			1
	);

	public static final RegistryObject<Block> CORE_GOLEM_CHEST = registerChestBlock("core_golem_chest",
			() -> new BTChestBlock(BTBlockEntityType.CORE_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F), GolemType.CORE),
			1
	);
	public static final RegistryObject<Block> CORE_CHEST = registerChestBlock("core_chest",
			() -> new TowerChestBlock(BTBlockEntityType.CORE_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F), GolemType.CORE),
			1
	);

	public static final RegistryObject<Block> NETHER_GOLEM_CHEST = registerChestBlock("nether_golem_chest",
			() -> new BTChestBlock(BTBlockEntityType.NETHER_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F), GolemType.NETHER),
			1
	);
	public static final RegistryObject<Block> NETHER_CHEST = registerChestBlock("nether_chest",
			() -> new TowerChestBlock(BTBlockEntityType.NETHER_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F), GolemType.NETHER),
			1
	);

	public static final RegistryObject<Block> END_GOLEM_CHEST = registerChestBlock("end_golem_chest",
			() -> new BTChestBlock(BTBlockEntityType.END_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F), GolemType.END),
			1
	);
	public static final RegistryObject<Block> END_CHEST = registerChestBlock("end_chest",
			() -> new TowerChestBlock(BTBlockEntityType.END_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F), GolemType.END),
			1
	);

	public static final RegistryObject<Block> SKY_GOLEM_CHEST = registerChestBlock("sky_golem_chest",
			() -> new BTChestBlock(BTBlockEntityType.SKY_GOLEM_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F), GolemType.SKY),
			1
	);
	public static final RegistryObject<Block> SKY_CHEST = registerChestBlock("sky_chest",
			() -> new TowerChestBlock(BTBlockEntityType.SKY_CHEST::get,
					Block.Properties.of().mapColor(MapColor.STONE).strength(2.5F, 1200.0F)
							.sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F), GolemType.SKY),
			1
	);

    public static final RegistryObject<Block> LAND_SPAWNER = registerBlock("land_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().noLootTable()), 1);

	public static final RegistryObject<Block> OCEAN_SPAWNER = registerBlock("ocean_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().noLootTable()), 1);

	public static final RegistryObject<Block> CORE_SPAWNER = registerBlock("core_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().noLootTable()), 1);

	public static final RegistryObject<Block> NETHER_SPAWNER = registerBlock("nether_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().noLootTable()), 1);

	public static final RegistryObject<Block> END_SPAWNER = registerBlock("end_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().noLootTable()), 1);

	public static final RegistryObject<Block> SKY_SPAWNER = registerBlock("sky_spawner",
			() -> new BTSpawnerBlock(Block.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().noLootTable()), 1);

	public static final  RegistryObject<Block> SPAWNER_MARKER = registerBlock("spawner_marker",
			() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(-1.0F, 3600000.0F).noOcclusion().noLootTable().isValidSpawn(BTBlocks::never)), 1);

	public static final RegistryObject<Block> AIR_FILL = registerBlock("air_fill",
			() -> new BTBlockingAirBlock(BlockBehaviour.Properties.of().replaceable().noCollission().noLootTable().air().forceSolidOn()), 1);

	public static final RegistryObject<Block> DATA_MARKER = registerBlock("data_marker",
			() -> new DataMarkerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(-1.0F, 3600000.0F).noOcclusion().noLootTable().isValidSpawn(BTBlocks::never)), 1);

	private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
		return false;
	}

	private static Boolean always(BlockState p_50810_, BlockGetter p_50811_, BlockPos p_50812_, EntityType<?> p_50813_) {
		return true;
	}

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, int stackSize) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, stackSize);
		return toReturn;
	}
	
	private static  <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, int stackSize) {
		BTItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(stackSize)));
	}


	private static <T extends Block> RegistryObject<T> registerChestBlock(String name, Supplier<T> block, int stackSize) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerChestBlockItem(name, toReturn, stackSize);
		return toReturn;
	}
	private static  <T extends Block> void registerChestBlockItem(String name, RegistryObject<T> block, int stackSize) {
		BTItems.ITEMS.register(name, () -> new ChestBlockItem(block.get(), new Item.Properties().stacksTo(stackSize)));
	}


	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

}
