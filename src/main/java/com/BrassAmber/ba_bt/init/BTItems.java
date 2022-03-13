package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.BTMonolith;
import com.BrassAmber.ba_bt.item.BTArmorMaterial;
import com.BrassAmber.ba_bt.item.BTItemTier;
import com.BrassAmber.ba_bt.item.item.GuardianEyeItem;
import com.BrassAmber.ba_bt.item.item.MonolithItem;
import com.BrassAmber.ba_bt.item.item.MonolithKeyItem;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTItems {
	private static final CreativeModeTab BATLETOWERSTAB = BrassAmberBattleTowers.BATLETOWERSTAB;
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrassAmberBattleTowers.MOD_ID);

	public static final Item PLATINUM_SKELETON_SPAWN_EGG = registerItem("platinum_skeleton_spawn_egg", new ForgeSpawnEggItem(() ->BTEntityTypes.PLATINUM_SKELETON, 0xb4bebf, 0x4a4e4f, new Item.Properties().tab(BATLETOWERSTAB)));

	public static final Item LAND_MONOLOITH_KEY = registerMonolithKey("monolith_key_land");
	public static final Item CORE_MONOLOITH_KEY = registerMonolithKey("monolith_key_core");
	public static final Item NETHER_MONOLOITH_KEY = registerMonolithKey("monolith_key_nether");
	public static final Item END_MONOLOITH_KEY = registerMonolithKey("monolith_key_end");
	public static final Item SKY_MONOLOITH_KEY = registerMonolithKey("monolith_key_sky");
	public static final Item OCEAN_MONOLOITH_KEY = registerMonolithKey("monolith_key_ocean");

	public static final Item LAND_GUARDIAN_EYE = registerGuardianEye("guardian_eye_land", GolemType.LAND);
	public static final Item CORE_GUARDIAN_EYE = registerGuardianEye("guardian_eye_core", GolemType.CORE);
	public static final Item NETHER_GUARDIAN_EYE = registerGuardianEye("guardian_eye_nether", GolemType.NETHER);
	public static final Item END_GUARDIAN_EYE = registerGuardianEye("guardian_eye_end", GolemType.END);
	public static final Item SKY_GUARDIAN_EYE = registerGuardianEye("guardian_eye_sky", GolemType.SKY);
	public static final Item OCEAN_GUARDIAN_EYE = registerGuardianEye("guardian_eye_ocean", GolemType.OCEAN);

	public static final Item LAND_MONOLITH = registerMonolith("monolith_land", BTEntityTypes.LAND_MONOLITH);
	public static final Item CORE_MONOLITH = registerMonolith("monolith_core", BTEntityTypes.CORE_MONOLITH);
	public static final Item NETHER_MONOLITH = registerMonolith("monolith_nether", BTEntityTypes.NETHER_MONOLITH);
	public static final Item END_MONOLITH = registerMonolith("monolith_end", BTEntityTypes.END_MONOLITH);
	public static final Item SKY_MONOLITH = registerMonolith("monolith_sky", BTEntityTypes.SKY_MONOLITH);
	public static final Item OCEAN_MONOLITH = registerMonolith("monolith_ocean", BTEntityTypes.OCEAN_MONOLITH);

	// Note: Materials ItemGroup.
	public static final Item PLATINUM_INGOT = registerItem("platinum_ingot", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final Item PLATINUM_SHARD = registerItem("platinum_shard", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	// Note: Combat ItemGroup.
	public static final Item PLATINUM_SWORD = registerItem("platinum_sword", new SwordItem(BTItemTier.PLATINUM, 3, -2.4F, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final Item PLATINUM_HELMET = registerItem("platinum_helmet", new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.HEAD, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final Item PLATINUM_CHESTPLATE = registerItem("platinum_chestplate", new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.CHEST, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final Item PLATINUM_LEGGINGS = registerItem("platinum_leggings", new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.LEGS, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
	public static final Item PLATINUM_BOOTS = registerItem("platinum_boots", new ArmorItem(BTArmorMaterial.PLATINUM, EquipmentSlot.FEET, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));

	public static final Item LAND_CHEST_SHARD = registerItem("land_chest_shard", new Item(new Item.Properties().stacksTo(16).tab(BATLETOWERSTAB)));
	/**
	 * Helper method for registering all Items
	 */
	public static Item registerItem(String registryName, Item item) {
		ITEMS.register(registryName, () -> item);
		return item;
	}

	/**
	 * Helper method for creating Monolith Key items.
	 */
	private static Item registerMonolithKey(String registryName) {
		Item newMonolithKeyItem = new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(BATLETOWERSTAB));
		return registerItem(registryName, newMonolithKeyItem);
	}

	/**
	 * Helper method for creating Guardian Eye items.
	 */
	private static Item registerGuardianEye(String registryName, GolemType golemType) {
		Item newGuardianEyeItem = new GuardianEyeItem(golemType, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(BATLETOWERSTAB));
		return registerItem(registryName, newGuardianEyeItem);
	}

	/**
	 * Helper method for creating Monolith items.
	 */
	private static Item registerMonolith(String registryName, EntityType<BTMonolith> monolithEntityType) {
		Item newMonolithItem = new MonolithItem(monolithEntityType, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(BATLETOWERSTAB));
		return registerItem(registryName, newMonolithItem);
	}
}