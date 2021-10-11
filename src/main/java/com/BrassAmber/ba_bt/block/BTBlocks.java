package com.BrassAmber.ba_bt.block;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.GolemChestBlock;
import com.BrassAmber.ba_bt.block.block.GolemChestBlock.BTChestType;
import com.BrassAmber.ba_bt.block.block.StoneChestBlock;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.StoneChestTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.client.renderer.inventory.BTChestItemRenderer;
import com.BrassAmber.ba_bt.item.BTItems;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassAmberBattleTowers.MOD_ID);

	public static final Block GOLEM_CHEST = registerBlock("golem_chest", new GolemChestBlock(BTChestType.GOLEM, AbstractBlock.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE)), () -> chestItemRenderer(GolemChestTileEntity::new));
	public static final Block STONE_CHEST = registerBlock("stone_chest", new StoneChestBlock(BTChestType.STONE, AbstractBlock.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE)), () -> chestItemRenderer(StoneChestTileEntity::new));

	/**
	 * Helper method for registering all Blocks and Items
	 */
	private static Block registerBlock(String registryName, Block block, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
		BLOCKS.register(registryName, () -> block);
		// Blocks are registered before Items
		BTItems.registerItem(registryName, new BlockItem(block, new Item.Properties().tab(ItemGroup.TAB_MISC).setISTER(renderMethod)));
		return block;
	}

	@OnlyIn(Dist.CLIENT)
	private static <T extends TileEntity> Callable<ItemStackTileEntityRenderer> chestItemRenderer(Supplier<T> tileEntitySupplier) {
		return () -> new BTChestItemRenderer<>(tileEntitySupplier);
	}
}
