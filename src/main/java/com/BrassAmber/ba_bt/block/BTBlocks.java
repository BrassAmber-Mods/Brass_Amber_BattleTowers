package com.BrassAmber.ba_bt.block;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.BTSpawner;
import com.BrassAmber.ba_bt.block.block.GolemChestBlock;
import com.BrassAmber.ba_bt.block.block.GolemChestBlock.BTChestType;
import com.BrassAmber.ba_bt.block.block.StoneChestBlock;
import com.BrassAmber.ba_bt.block.block.TabIconBlock;
import com.BrassAmber.ba_bt.block.block.TotemBlock;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.StoneChestTileEntity;
import com.BrassAmber.ba_bt.block.tileentity.client.renderer.inventory.BTChestItemRenderer;
import com.BrassAmber.ba_bt.item.BTItems;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BTBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassAmberBattleTowers.MOD_ID);

	public static final Block GOLEM_CHEST = registerChestBlock("golem_chest", new GolemChestBlock(BTChestType.GOLEM, AbstractBlock.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE)), () -> chestItemRenderer(GolemChestTileEntity::new));
	public static final Block STONE_CHEST = registerChestBlock("stone_chest", new StoneChestBlock(BTChestType.STONE, AbstractBlock.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE)), () -> chestItemRenderer(StoneChestTileEntity::new));

	public static final Block TOTEM = registerBlock("totem", new TotemBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE)));


	public static final Block SILVER_BLOCK = registerBlock("silver_block", new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(4.0F, 6.0F).sound(SoundType.METAL)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final Block SILVER_TILES = registerBlock("silver_tiles", new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(4.0F, 6.0F).sound(SoundType.METAL)), ItemGroup.TAB_BUILDING_BLOCKS);

	public static final Block TAB_ICON = registerBlockNoGroup("tab_icon", new TabIconBlock(AbstractBlock.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final Block BT_SPAWNER = registerSpawnerBlock("bt_spawner", new BTSpawner(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion()));

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent()
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		BrassAmberBattleTowers.LOGGER.info("Set Block RenderTypes");
		RenderType cutoutRenderType = RenderType.cutout();
		RenderTypeLookup.setRenderLayer(TAB_ICON, cutoutRenderType);
	}

	/**
	 * Helper method for registering all Blocks and Items with a custom ItemGroup.
	 */
	private static Block registerBlock(String registryName, Block block, ItemGroup itemGroup) {
		return registerBlock(registryName, block, new BlockItem(block, new Item.Properties().tab(itemGroup)));
	}

	/**
	 * Helper method for registering all Blocks and Items to the BattleTowers ItemGroup.
	 */
	private static Block registerBlock(String registryName, Block block) {
		return registerBlock(registryName, block, BrassAmberBattleTowers.BATLETOWERSTAB);
	}

	/**
	 * Items without a group will not show up in the creative inventory and JEI.
	 */
	private static Block registerBlockNoGroup(String registryName, Block block) {
		return registerBlock(registryName, block, (ItemGroup) null);
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

	/*********************************************************** Chest Stuff ********************************************************/

	/**
	 * Helper method for registering Chests
	 */
	private static Block registerChestBlock(String registryName, Block block, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
		return registerBlock(registryName, block, new BlockItem(block, new Item.Properties().tab(BrassAmberBattleTowers.BATLETOWERSTAB).setISTER(renderMethod)));
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

	@OnlyIn(Dist.CLIENT)
	private static <T extends TileEntity> Callable<ItemStackTileEntityRenderer> chestItemRenderer(Supplier<T> tileEntitySupplier) {
		return () -> new BTChestItemRenderer<>(tileEntitySupplier);
	}
}
