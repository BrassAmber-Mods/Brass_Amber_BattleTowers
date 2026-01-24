package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.item.item.*;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.brass_amber.ba_bt.sound.BTSoundEvents.*;


public class BTItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, BABattleTowers.MOD_ID);

	public static final RegistryObject<Item> TAB_ICON = ITEMS.register("tab_icon",() -> new Item(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> LAND_MONOLITH_KEY = ITEMS.register("land_monolith_key", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> OCEAN_MONOLITH_KEY = ITEMS.register("ocean_monolith_key", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> CORE_MONOLITH_KEY = ITEMS.register("core_monolith_key", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> NETHER_MONOLITH_KEY = ITEMS.register("nether_monolith_key", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> END_MONOLITH_KEY = ITEMS.register("end_monolith_key", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> SKY_MONOLITH_KEY = ITEMS.register("sky_monolith_key", () -> new MonolithKeyItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant()));

	public static final RegistryObject<Item> LAND_GOLEM_EYE = ITEMS.register("land_golem_eye", () -> new GuardianEyeItem(TowerType.LAND, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).fireResistant()));
	public static final RegistryObject<Item> OCEAN_GOLEM_EYE = ITEMS.register("ocean_golem_eye", () -> new GuardianEyeItem(TowerType.OCEAN, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).fireResistant()));
	public static final RegistryObject<Item> CORE_GOLEM_EYE = ITEMS.register("core_golem_eye", () -> new GuardianEyeItem(TowerType.CORE, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).fireResistant()));
	public static final RegistryObject<Item> NETHER_GOLEM_EYE = ITEMS.register("nether_golem_eye", () -> new GuardianEyeItem(TowerType.NETHER, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).fireResistant()));
	public static final RegistryObject<Item> END_GOLEM_EYE = ITEMS.register("end_golem_eye", () -> new GuardianEyeItem(TowerType.END, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).fireResistant()));
	public static final RegistryObject<Item> SKY_GOLEM_EYE = ITEMS.register("sky_golem_eye", () -> new GuardianEyeItem(TowerType.SKY, (new Item.Properties()).stacksTo(8).rarity(Rarity.EPIC).fireResistant()));

	public static final RegistryObject<Item> LAND_MONOLITH = ITEMS.register("land_monolith", () -> new MonolithItem(TowerType.LAND, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> OCEAN_MONOLITH = ITEMS.register("ocean_monolith", () -> new MonolithItem(TowerType.OCEAN, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CORE_MONOLITH = ITEMS.register("core_monolith", () -> new MonolithItem(TowerType.CORE, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> NETHER_MONOLITH = ITEMS.register("nether_monolith", () -> new MonolithItem(TowerType.NETHER, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> END_MONOLITH = ITEMS.register("end_monolith", () -> new MonolithItem(TowerType.END, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> SKY_MONOLITH = ITEMS.register("sky_monolith", () -> new MonolithItem(TowerType.SKY, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC)));

	public static final RegistryObject<Item> LAND_CHEST_SHARD = ITEMS.register("land_chest_shard", () -> new Item(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> OCEAN_CHEST_SHARD = ITEMS.register("ocean_chest_shard", () -> new Item(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> CORE_CHEST_SHARD = ITEMS.register("core_chest_shard", () -> new Item(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> NETHER_CHEST_SHARD = ITEMS.register("nether_chest_shard", () -> new Item(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> END_CHEST_SHARD = ITEMS.register("end_chest_shard", () -> new Item(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> SKY_CHEST_SHARD = ITEMS.register("sky_chest_shard", () -> new Item(new Item.Properties().stacksTo(16)));

	public static final RegistryObject<Item> LAND_RESONANCE_CRYSTAL = ITEMS.register("land_resonance_stone", () -> new ResonanceStoneItem(TowerType.LAND, MUSIC_LAND_TOWER, new Item.Properties().stacksTo(1).fireResistant(),3940));
	public static final RegistryObject<Item> OCEAN_RESONANCE_CRYSTAL = ITEMS.register("ocean_resonance_stone", () -> new ResonanceStoneItem(TowerType.OCEAN, MUSIC_OCEAN_TOWER,new Item.Properties().stacksTo(1).fireResistant(), 4780));
	public static final RegistryObject<Item> CORE_RESONANCE_CRYSTAL = ITEMS.register("core_resonance_stone", () -> new ResonanceStoneItem(TowerType.CORE, MUSIC_CORE_TOWER,new Item.Properties().stacksTo(1).fireResistant(), 4440));
	public static final RegistryObject<Item> NETHER_RESONANCE_CRYSTAL = ITEMS.register("nether_resonance_stone", () -> new ResonanceStoneItem(TowerType.NETHER, MUSIC_NETHER_TOWER,new Item.Properties().stacksTo(1).fireResistant(), 3880));
	public static final RegistryObject<Item> END_RESONANCE_CRYSTAL = ITEMS.register("end_resonance_stone", () -> new ResonanceStoneItem(TowerType.END, MUSIC_END_TOWER,new Item.Properties().stacksTo(1).fireResistant(), 5060));
	public static final RegistryObject<Item> SKY_RESONANCE_CRYSTAL = ITEMS.register("sky_resonance_stone", () -> new ResonanceStoneItem(TowerType.SKY, MUSIC_SKY_TOWER,new Item.Properties().stacksTo(1).fireResistant(), 3940));
	public static final RegistryObject<Item> CITY_RESONANCE_CRYSTAL = ITEMS.register("city_resonance_stone", () -> new ResonanceStoneItem(TowerType.CITY, MUSIC_CITY,new Item.Properties().stacksTo(1).fireResistant(), 6560));

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

}