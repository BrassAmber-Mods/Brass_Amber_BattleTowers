package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.TotemBlock.TotemType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrassAmberBattleTowers.MOD_ID);

	public static final Item LAND_MONOLOITH_KEY = registerItem("monolith_key_land", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));
	public static final Item CORE_MONOLOITH_KEY = registerItem("monolith_key_core", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));
	public static final Item NETHER_MONOLOITH_KEY = registerItem("monolith_key_nether", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));
	public static final Item END_MONOLOITH_KEY = registerItem("monolith_key_end", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));
	public static final Item SKY_MONOLOITH_KEY = registerItem("monolith_key_sky", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));
	public static final Item OCEAN_MONOLOITH_KEY = registerItem("monolith_key_ocean", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));

	public static final Item LAND_GUARDIAN_EYE = registerItem("guardian_eye_land", new GuardianEyeItem(TotemType.LAND, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));
	public static final Item CORE_GUARDIAN_EYE = registerItem("guardian_eye_core", new GuardianEyeItem(TotemType.CORE, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));
	public static final Item NETHER_GUARDIAN_EYE = registerItem("guardian_eye_nether", new GuardianEyeItem(TotemType.NETHER, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));
	public static final Item END_GUARDIAN_EYE = registerItem("guardian_eye_end", new GuardianEyeItem(TotemType.END, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));
	public static final Item SKY_GUARDIAN_EYE = registerItem("guardian_eye_sky", new GuardianEyeItem(TotemType.SKY, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));
	public static final Item OCEAN_GUARDIAN_EYE = registerItem("guardian_eye_ocean", new GuardianEyeItem(TotemType.OCEAN, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));

	public static final Item MONOLITH = registerItem("monolith", new MonolithItem((new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).tab(ItemGroup.TAB_MISC)));

	/**
	 * Helper method for registering all Items
	 */
	public static Item registerItem(String registryName, Item item) {
		ITEMS.register(registryName, () -> item);
		return item;
	}
}