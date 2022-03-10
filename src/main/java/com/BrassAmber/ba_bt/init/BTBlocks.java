package com.BrassAmber.ba_bt.init;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.*;
import com.BrassAmber.ba_bt.block.block.GolemChestBlock.BTChestType;
import com.BrassAmber.ba_bt.block.block.TowerChestBlock;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class BTBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassAmberBattleTowers.MOD_ID);

	public static final Block LAND_GOLEM_CHEST = registerBlock("land_golem_chest",
			new GolemChestBlock(BTChestType.GOLEM, Block.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE).noOcclusion().explosionResistance(1200.0F)));
	public static final Block LAND_CHEST = registerBlock("land_chest",
			new TowerChestBlock(BTChestType.TOWER, Block.Properties.of(Material.STONE).strength(2.5F, 1200.0F).sound(SoundType.STONE).noOcclusion().explosionResistance(6.0F)));



	public static final Block PLATINUM_BLOCK = registerBlock("platinum_block",
			new Block(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops()
					.strength(4.0F, 6.0F).sound(SoundType.METAL)), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final Block PLATINUM_TILES = registerBlock("platinum_tiles",
			new Block(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops()
					.strength(4.0F, 6.0F).sound(SoundType.METAL)), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final Block TAB_ICON = registerBlockNoGroup("tab_icon",
			new TabIconBlock(Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final Block BT_LAND_SPAWNER = registerSpawnerBlock("bt_land_spawner",
			new BTSpawnerBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()));


	/**
	 * Items without a group will not show up in the creative inventory and JEI.
	 */
	private static Block registerBlockNoGroup(String registryName, Block block) {
		return registerBlock(registryName, block, (CreativeModeTab) null);
	}

	/**
	 * Helper method for registering all Blocks and Items to the BattleTowers ItemGroup.
	 */
	private static Block registerBlock(String registryName, Block block) {
		return registerBlock(registryName, block, BrassAmberBattleTowers.BATLETOWERSTAB);
	}

	/**
	 * Helper method for registering all Blocks and Items with a custom ItemGroup.
	 */
	private static Block registerBlock(String registryName, Block block, CreativeModeTab creativeModeTab) {
		return registerBlock(registryName, block, new BlockItem(block, new Item.Properties().tab(creativeModeTab)));
	}
	
	/**
	 * Helper method for registering all Blocks and Items
	 */
	private static Block registerBlock(String registryName, Block block, Item item) {
		BLOCKS.register(registryName, () -> block);
		// Blocks are registered before Items
		BTItems.registerItem(registryName, item);
		return block;
	}

	/**
	 * Helper method for registering Spawner
	 */
	private static Block registerSpawnerBlock(String registryName, Block block) {
		BLOCKS.register(registryName, () -> block);
		// Blocks are registered before Items
		BTItems.registerItem(registryName, new BlockItem(block, new Item.Properties()));
		return block;
	}
}
