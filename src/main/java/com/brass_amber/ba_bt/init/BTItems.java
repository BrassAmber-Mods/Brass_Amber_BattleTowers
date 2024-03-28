package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.item.item.*;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BTItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BrassAmberBattleTowers.MODID);

	public static final DeferredItem<Item> TAB_ICON = ITEMS.registerSimpleItem("tab_icon", new Item.Properties());
	public static final DeferredItem<Item> LAND_MONOLOITH_KEY = ITEMS.register("monolith_key_land", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
	public static final DeferredItem<Item> OCEAN_MONOLOITH_KEY = ITEMS.register("monolith_key_ocean", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
	public static final DeferredItem<Item> CORE_MONOLOITH_KEY = ITEMS.register("monolith_key_core", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
	public static final DeferredItem<Item> NETHER_MONOLOITH_KEY = ITEMS.register("monolith_key_nether", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
	public static final DeferredItem<Item> END_MONOLOITH_KEY = ITEMS.register("monolith_key_end", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
	public static final DeferredItem<Item> SKY_MONOLOITH_KEY = ITEMS.register("monolith_key_sky", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

	public static final DeferredItem<Item> LAND_GUARDIAN_EYE = ITEMS.register("guardian_eye_land", () -> new GuardianEyeItem(GolemType.LAND, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> OCEAN_GUARDIAN_EYE = ITEMS.register("guardian_eye_ocean", () -> new GuardianEyeItem(GolemType.OCEAN, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> CORE_GUARDIAN_EYE = ITEMS.register("guardian_eye_core", () -> new GuardianEyeItem(GolemType.CORE, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> NETHER_GUARDIAN_EYE = ITEMS.register("guardian_eye_nether", () -> new GuardianEyeItem(GolemType.NETHER, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> END_GUARDIAN_EYE = ITEMS.register("guardian_eye_end", () -> new GuardianEyeItem(GolemType.END, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> SKY_GUARDIAN_EYE = ITEMS.register("guardian_eye_sky", () -> new GuardianEyeItem(GolemType.SKY, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC)));

	public static final DeferredItem<Item> LAND_MONOLITH = ITEMS.register("monolith_land", () -> new MonolithItem(GolemType.LAND, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> OCEAN_MONOLITH = ITEMS.register("monolith_ocean", () -> new MonolithItem(GolemType.OCEAN, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> CORE_MONOLITH = ITEMS.register("monolith_core", () -> new MonolithItem(GolemType.CORE, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> NETHER_MONOLITH = ITEMS.register("monolith_nether", () -> new MonolithItem(GolemType.NETHER, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> END_MONOLITH = ITEMS.register("monolith_end", () -> new MonolithItem(GolemType.END, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> SKY_MONOLITH = ITEMS.register("monolith_sky", () -> new MonolithItem(GolemType.SKY, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));

	public static final DeferredItem<Item> LAND_CHEST_SHARD = ITEMS.registerSimpleItem("land_chest_shard", new Item.Properties().stacksTo(16));

	public static final DeferredItem<Item> BT_EMPTY_LOOT_ITEM = ITEMS.register("bt_empty_item", () -> new BTEmptyLootItem(new Item.Properties()));
	
	public static final DeferredItem<Item> BT_LAND_RESONANCE_CRYSTAL = ITEMS.register("bt_land_resonance_stone", () -> new ResonanceStoneItem(GolemType.LAND.getSerializedName(), new Item.Properties().stacksTo(1),7200));
	public static final DeferredItem<Item> BT_OCEAN_RESONANCE_CRYSTAL = ITEMS.register("bt_ocean_resonance_stone", () -> new ResonanceStoneItem(GolemType.OCEAN.getSerializedName(), new Item.Properties().stacksTo(1), 8400));
	public static final DeferredItem<Item> BT_CORE_RESONANCE_CRYSTAL = ITEMS.register("bt_core_resonance_stone", () -> new ResonanceStoneItem(GolemType.CORE.getSerializedName(), new Item.Properties().stacksTo(1), 5700));
	public static final DeferredItem<Item> BT_NETHER_RESONANCE_CRYSTAL = ITEMS.register("bt_nether_resonance_stone", () -> new ResonanceStoneItem(GolemType.NETHER.getSerializedName(), new Item.Properties().stacksTo(1), 3300));
	public static final DeferredItem<Item> BT_END_RESONANCE_CRYSTAL = ITEMS.register("bt_end_resonance_stone", () -> new ResonanceStoneItem(GolemType.END.getSerializedName(), new Item.Properties().stacksTo(1), 7800));
	public static final DeferredItem<Item> BT_SKY_RESONANCE_CRYSTAL = ITEMS.register("bt_sky_resonance_stone", () -> new ResonanceStoneItem(GolemType.SKY.getSerializedName(), new Item.Properties().stacksTo(1), 4200));
	public static final DeferredItem<Item> BT_CITY_RESONANCE_CRYSTAL = ITEMS.register("bt_city_resonance_stone", () -> new ResonanceStoneItem(GolemType.CITY.getSerializedName(), new Item.Properties().stacksTo(1), 2100));

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

}