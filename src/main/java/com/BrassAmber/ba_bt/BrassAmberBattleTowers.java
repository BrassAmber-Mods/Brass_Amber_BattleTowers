package com.BrassAmber.ba_bt;

import com.BrassAmber.ba_bt.init.*;
import com.BrassAmber.ba_bt.util.BTCreativeTab;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(BrassAmberBattleTowers.MOD_ID)
public class BrassAmberBattleTowers {

	public static final String MOD_ID = "ba_bt";
	public static final CreativeModeTab BATLETOWERSTAB = new BTCreativeTab();
	// Directly reference a log4j logger
	public static final Logger LOGGER = LogManager.getLogger();

	public static final Component HOLD_SHIFT_TOOLTIP = (new TranslatableComponent("tooltip.battletowers.hold_shift").withStyle(ChatFormatting.DARK_GRAY));

	public BrassAmberBattleTowers() {
		// Register the setup method for modloading
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register Blocks
		BTBlocks.BLOCKS.register(eventBus);
		// Register EntityTypes
		BTEntityTypes.ENTITY_TYPES.register(eventBus);
		// Register Items
		BTItems.ITEMS.register(eventBus);
		// Register TileEntityTypes
		BTBlockEntityTypes.BLOCK_ENTITY_TYPES.register(eventBus);
		// Register SoundEvents
		BTSoundEvents.SOUND_EVENTS.register(eventBus);

		// Register Structures
		BTStructures.DEFERRED_REGISTRY_STRUCTURE.register(eventBus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BattleTowersConfig.SPEC, "ba-battletowers-config.toml");

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		// For events that happen after initialization. This is probably going to be use a lot.
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

	}


	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
