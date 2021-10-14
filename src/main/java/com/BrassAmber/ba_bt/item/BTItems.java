package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.BrassAmber.ba_bt.util.GolemType;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrassAmberBattleTowers.MOD_ID);

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
		Item newMonolithKeyItem = new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC));
		return registerItem(registryName, newMonolithKeyItem);
	}

	/**
	 * Helper method for creating Guardian Eye items.
	 */
	private static Item registerGuardianEye(String registryName, GolemType golemType) {
		Item newGuardianEyeItem = new GuardianEyeItem(golemType, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC));
		return registerItem(registryName, newGuardianEyeItem);
	}

	/**
	 * Helper method for creating Monolith items.
	 */
	private static Item registerMonolith(String registryName, EntityType<MonolithEntity> monolithEntityType) {
		Item newMonolithItem = new MonolithItem(monolithEntityType, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC));
		return registerItem(registryName, newMonolithItem);
	}
}