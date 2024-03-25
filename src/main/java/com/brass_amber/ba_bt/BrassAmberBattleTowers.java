package com.brass_amber.ba_bt;

import com.brass_amber.ba_bt.client.BTEvents;
import com.brass_amber.ba_bt.init.*;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import com.brass_amber.ba_bt.util.BTCreativeTab;
import com.brass_amber.ba_bt.util.SaveTowers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(BrassAmberBattleTowers.MOD_ID)
public class BrassAmberBattleTowers {

	public static final String MOD_ID = "ba_bt";
	public static final SaveTowers SAVETOWERS = new SaveTowers();

	// Directly reference a log4j logger
	public static final Logger LOGGER = LogManager.getLogger();

	public static final Component HOLD_SHIFT_TOOLTIP = (Component.translatable("tooltip.ba_bt.hold_shift").withStyle(ChatFormatting.DARK_GRAY));

	public BrassAmberBattleTowers() {
		// Register the setup method for modloading
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register
		BTCreativeTab.register(eventBus);

		// Register Blocks
		BTBlocks.register(eventBus);
		// Register EntityTypes
		BTEntityTypes.register(eventBus);
		BTExtras.register(eventBus);
		// Register Items
		BTItems.register(eventBus);
		// Register TileEntityTypes
		BTBlockEntityTypes.register(eventBus);
		// Register SoundEvents
		BTSoundEvents.register(eventBus);

		// Register Structures
		BTStructures.register(eventBus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BattleTowersConfig.SPEC, "ba-battletowers-config.toml");

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		// For events that happen after initialization. This is probably going to be use a lot.
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		@SuppressWarnings("InstantiationOfUtilityClass")
		BTEvents btEvents = new BTEvents();


		forgeBus.register(btEvents);
        eventBus.addListener(this::addCreative);
	}


	public void addCreative(BuildCreativeModeTabContentsEvent event) {

		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(BTItems.PLATINUM_SWORD);
			event.accept(BTItems.PLATINUM_HELMET);
			event.accept(BTItems.PLATINUM_CHESTPLATE);
			event.accept(BTItems.PLATINUM_LEGGINGS);
			event.accept(BTItems.PLATINUM_BOOTS);
		}

		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			event.accept(BTBlocks.PLATINUM_BLOCK);
			event.accept(BTBlocks.PLATINUM_TILES);
		}

		if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			event.accept(BTItems.PLATINUM_INGOT);
			event.accept(BTItems.PLATINUM_SHARD);
		}


		if (event.getTab() == BTCreativeTab.BT_TAB.get()) {
			event.accept(BTItems.PLATINUM_SKELETON_SPAWN_EGG);
			event.accept(BTItems.CULTIST_SPAWN_EGG);

			event.accept(BTItems.LAND_MONOLOITH_KEY);
			event.accept(BTItems.OCEAN_MONOLOITH_KEY);
			event.accept(BTItems.CORE_MONOLOITH_KEY);
			event.accept(BTItems.NETHER_MONOLOITH_KEY);
			event.accept(BTItems.END_MONOLOITH_KEY);
			event.accept(BTItems.SKY_MONOLOITH_KEY);

			event.accept(BTItems.LAND_GUARDIAN_EYE);
			event.accept(BTItems.OCEAN_GUARDIAN_EYE);
			event.accept(BTItems.CORE_GUARDIAN_EYE);
			event.accept(BTItems.NETHER_GUARDIAN_EYE);
			event.accept(BTItems.END_GUARDIAN_EYE);
			event.accept(BTItems.SKY_GUARDIAN_EYE);

			event.accept(BTItems.LAND_MONOLITH);
			event.accept(BTItems.OCEAN_MONOLITH);
			event.accept(BTItems.CORE_MONOLITH);
			event.accept(BTItems.NETHER_MONOLITH);
			event.accept(BTItems.END_MONOLITH);
			event.accept(BTItems.SKY_MONOLITH);

			event.accept(BTItems.LAND_CHEST_SHARD);
			event.accept(BTBlocks.LAND_CHEST);
			event.accept(BTBlocks.LAND_GOLEM_CHEST);
			event.accept(BTBlocks.OCEAN_CHEST);
			event.accept(BTBlocks.OCEAN_GOLEM_CHEST);

			event.accept(BTItems.BT_LAND_RESONANCE_CRYSTAL);
			event.accept(BTItems.BT_OCEAN_RESONANCE_CRYSTAL);
			event.accept(BTItems.BT_CORE_RESONANCE_CRYSTAL);
			event.accept(BTItems.BT_NETHER_RESONANCE_CRYSTAL);
			event.accept(BTItems.BT_END_RESONANCE_CRYSTAL);
			event.accept(BTItems.BT_SKY_RESONANCE_CRYSTAL);
			event.accept(BTItems.BT_CITY_RESONANCE_CRYSTAL);
		}
	}


	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
