package com.BrassAmber.ba_bt.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.BTChestBlock;
import com.BrassAmber.ba_bt.item.BTItems;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassAmberBattleTowers.MOD_ID);

	public static final Block TOWER_CHEST = registerBlock("tower_chest", new BTChestBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE)));

	/**
	 * Helper method for registering all Blocks and Items
	 */
	private static Block registerBlock(String registryName, Block block) {
		BLOCKS.register(registryName, () -> block);
		// Blocks are registered before Items
		BTItems.registerItem(registryName, new BlockItem(block, new Item.Properties().tab(ItemGroup.TAB_MISC)));
		return block;
	}
}
