package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.BTMonolith;
import com.BrassAmber.ba_bt.item.BTArmorMaterial;
import com.BrassAmber.ba_bt.item.BTItemTier;
import com.BrassAmber.ba_bt.item.ChestBlockItem;
import com.BrassAmber.ba_bt.item.item.GuardianEyeItem;
import com.BrassAmber.ba_bt.item.item.MonolithItem;
import com.BrassAmber.ba_bt.item.item.MonolithKeyItem;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTItems {
	private static final CreativeModeTab BATLETOWERSTAB = BrassAmberBattleTowers.BATLETOWERSTAB;
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrassAmberBattleTowers.MOD_ID);

	public static final RegistryObject<Item> PLATINUM_SKELETON_SPAWN_EGG = ITEMS.register("platinum_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(BTEntityTypes.PLATINUM_SKELETON, 0xb4bebf, 0x4a4e4f, new Item.Properties().tab(BATLETOWERSTAB)));

	public static final RegistryObject<Item> LAND_MONOLOITH_KEY = ITEMS.register("monolith_key_land", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> OCEAN_MONOLOITH_KEY = ITEMS.register("monolith_key_ocean", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> CORE_MONOLOITH_KEY = ITEMS.register("monolith_key_core", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> NETHER_MONOLOITH_KEY = ITEMS.register("monolith_key_nether", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> END_MONOLOITH_KEY = ITEMS.register("monolith_key_end", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> SKY_MONOLOITH_KEY = ITEMS.register("monolith_key_sky", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB)));

	public static final RegistryObject<Item> LAND_GUARDIAN_EYE = ITEMS.register("guardian_eye_land", () -> new GuardianEyeItem(GolemType.LAND, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> OCEAN_GUARDIAN_EYE = ITEMS.register("guardian_eye_ocean", () -> new GuardianEyeItem(GolemType.OCEAN, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> CORE_GUARDIAN_EYE = ITEMS.register("guardian_eye_core", () -> new GuardianEyeItem(GolemType.CORE, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> NETHER_GUARDIAN_EYE = ITEMS.register("guardian_eye_nether", () -> new GuardianEyeItem(GolemType.NETHER, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> END_GUARDIAN_EYE = ITEMS.register("guardian_eye_end", () -> new GuardianEyeItem(GolemType.END, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> SKY_GUARDIAN_EYE = ITEMS.register("guardian_eye_sky", () -> new GuardianEyeItem(GolemType.SKY, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));

	public static final RegistryObject<Item> LAND_MONOLITH = ITEMS.register("monolith_land", () -> new MonolithItem(GolemType.LAND, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> OCEAN_MONOLITH = ITEMS.register("monolith_ocean", () -> new MonolithItem(GolemType.OCEAN, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> CORE_MONOLITH = ITEMS.register("monolith_core", () -> new MonolithItem(GolemType.CORE, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> NETHER_MONOLITH = ITEMS.register("monolith_nether", () -> new MonolithItem(GolemType.NETHER, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> END_MONOLITH = ITEMS.register("monolith_end", () -> new MonolithItem(GolemType.END, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> SKY_MONOLITH = ITEMS.register("monolith_sky", () -> new MonolithItem(GolemType.SKY, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB)));

	// Note: Materials ItemGroup.
	public static final RegistryObject<Item> PLATINUM_INGOT = ITEMS.register("platinum_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> PLATINUM_SHARD = ITEMS.register("platinum_shard", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	// Note: Combat ItemGroup.
	public static final RegistryObject<Item> PLATINUM_SWORD = ITEMS.register("platinum_sword", () -> new SwordItem(BTItemTier.PLATINUM, 3, -2.4F, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> PLATINUM_HELMET = ITEMS.register("platinum_helmet", () -> new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.HEAD, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> PLATINUM_CHESTPLATE = ITEMS.register("platinum_chestplate", () -> new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.CHEST, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> PLATINUM_LEGGINGS = ITEMS.register("platinum_leggings", () -> new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.LEGS, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> PLATINUM_BOOTS = ITEMS.register("platinum_boots", () -> new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.FEET, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<Item> LAND_CHEST_SHARD = ITEMS.register("land_chest_shard", () -> new Item(new Item.Properties().stacksTo(16).tab(BATLETOWERSTAB)));

	public static final RegistryObject<Item> LAND_GOLEM_CHEST = ITEMS.register("land_golem_chest", () -> new ChestBlockItem(BTBlocks.LAND_GOLEM_CHEST.get(), new Item.Properties().tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> LAND_CHEST = ITEMS.register("land_chest", () -> new ChestBlockItem(BTBlocks.LAND_CHEST.get(), new Item.Properties().tab(BATLETOWERSTAB)));
	public static final RegistryObject<Item> PLATINUM_BLOCK = ITEMS.register("platinum_block", () -> new BlockItem(BTBlocks.PLATINUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> PLATINUM_TILES = ITEMS.register("platinum_tiles", () -> new BlockItem(BTBlocks.PLATINUM_TILES.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> TAB_ICON = ITEMS.register("tab_icon", () -> new BlockItem(BTBlocks.TAB_ICON.get(), new Item.Properties().tab(null)));
	public static final RegistryObject<Item> BT_LAND_SPAWNER = ITEMS.register("bt_land_spawner", () -> new BlockItem(BTBlocks.BT_LAND_SPAWNER.get(), new Item.Properties().tab(null)));

	/**
	 * Helper methods do not work anymore.
	 */
	public static RegistryObject<Item> registerItem(String registryName, Item item) {
		return ITEMS.register(registryName, () -> item);

	}
}