package com.BrassAmber.ba_bt.item;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrassAmberBattleTowers.MOD_ID);

	public static final Item MONOLITH_KEY = registerItem("land_monolith_key", new MonolithKeyItem((new Item.Properties()).stacksTo(16).rarity(Rarity.RARE).tab(ItemGroup.TAB_MISC)));

	/**
	 * Helper method for registering all Items
	 */
	public static Item registerItem(String registryName, Item item) {
		ITEMS.register(registryName, () -> item);
		return item;
	}
}