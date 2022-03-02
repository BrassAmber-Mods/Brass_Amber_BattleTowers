package com.BrassAmber.ba_bt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.BrassAmber.ba_bt.init.*;
import com.BrassAmber.ba_bt.util.BTCreativeTab;
import com.BrassAmber.ba_bt.worldGen.structures.LandBattleTower;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

		BTStructures.DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
		// Register Items
		BTItems.ITEMS.register(eventBus);
		// Register Blocks
		BTBlocks.BLOCKS.register(eventBus);
		// Register TileEntityTypes
		BTBlockEntityTypes.TILE_ENTITY_TYPES.register(eventBus);
		// Register SoundEvents
		BTSoundEvents.SOUND_EVENTS.register(eventBus);
		// Register EntityTypes
		BTEntityTypes.ENTITY_TYPES.register(eventBus);

		eventBus.addListener(this::setup);
		// Register the enqueueIMC method for modloading

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BattleTowersConfig.SPEC, "ba-battletowers-config.toml");


		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		// For events that happen after initialization. This is probably going to be use a lot.
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		forgeBus.addListener(EventPriority.NORMAL, LandBattleTower::setupStructureSpawns);

	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("HELLO FROM PREINIT");
		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

		event.enqueueWork(() -> {
			BTStructures.setupStructures();
			BTConfiguredStructures.registerConfiguredStructures();
		});
	}


	private static Method GETCODEC_METHOD;
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerLevel serverLevel) {

			ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

			// Skip superflat worlds to prevent issues with it. Plus, users don't want structures clogging up their superflat worlds. - TelepathicGrunt
			if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
				return;
			}

			//////////// BIOME BASED STRUCTURE SPAWNING ////////////
			/*
			 * NOTE: BiomeLoadingEvent from Forge API does not work with structures anymore.
			 * Instead, we will use the below to add our structure to overworld biomes.
			 * Remember, this is temporary until Forge API finds a better solution for adding structures to biomes.
			 * - TelepathicGrunt
			 */

			StructureSettings worldStructureConfig =  chunkGenerator.getSettings();

			// Create a mutable map we will use for easier adding to biomes
			HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> BTStructureToMultiMap = new HashMap<>();

			ImmutableSet<ResourceKey<Biome>> overworldBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
					.add(Biomes.FOREST)
					.add(Biomes.FLOWER_FOREST)
					.add(Biomes.BIRCH_FOREST)
					.add(Biomes.DARK_FOREST)
					.add(Biomes.OLD_GROWTH_BIRCH_FOREST)
					.add(Biomes.WINDSWEPT_FOREST)
					.add(Biomes.MEADOW)
					.add(Biomes.PLAINS)
					.add(Biomes.SAVANNA)
                    .add(Biomes.SNOWY_PLAINS)
					.add(Biomes.SUNFLOWER_PLAINS)
                    .add(Biomes.SWAMP)
					.add(Biomes.DESERT)
					.add(Biomes.JUNGLE)
                    .add(Biomes.TAIGA)
					.add(Biomes.SNOWY_TAIGA)
					.add(Biomes.OLD_GROWTH_PINE_TAIGA)
					.add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
					.add(Biomes.GROVE)
					.add(Biomes.WINDSWEPT_HILLS)
					.add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                    .build();
            overworldBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(BTStructureToMultiMap, BTConfiguredStructures.CONFIGURED_LAND_TOWER, biomeKey));

			ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
			worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !BTStructureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

			// Add our structures to the structure map/multimap and set the world to use this combined map/multimap.
			BTStructureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

			// Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
			worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();


			//////////// DIMENSION BASED STRUCTURE SPAWNING ////////////

			/*
			 * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
			 * They will handle your structure spacing for your if you add to Registry.NOISE_GENERATOR_SETTINGS_REGISTRY in your structure's registration.
			 * This here is done with reflection as this tutorial is not about setting up and using Mixins.
			 * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
			 */
			try {
				if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(chunkGenerator));
				if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
			}
			catch(Exception e){
				LOGGER.error("Was unable to check if " + serverLevel.dimension().location() + " is using Terraforged's ChunkGenerator.");
			}

			/*
			 * Makes sure this chunkgenerator and datapack dimensions can spawn our structure.
			 *
			 * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
			 * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
			 */
			Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
			tempMap.putIfAbsent(BTStructures.LAND_BATTLE_TOWER.get(), StructureSettings.DEFAULTS.get(BTStructures.LAND_BATTLE_TOWER.get()));

			if (chunkGenerator.getBiomeSource().possibleBiomes().contains(Biomes.SMALL_END_ISLANDS) || chunkGenerator.getBiomeSource().possibleBiomes().contains(Biomes.NETHER_WASTES)) {
				tempMap.remove(BTStructures.LAND_BATTLE_TOWER.get(), StructureSettings.DEFAULTS.get(BTStructures.LAND_BATTLE_TOWER.get()));
			}

			worldStructureConfig.structureConfig = tempMap;

			/*
			 * The above three lines can be changed to do dimension blacklists/whitelist for your structure.
			 * (Don't forget to attempt to remove your structure too from the map if you are blacklisting that dimension in case it already has the structure)
			 */


		}
	}

	/**
	 * Helper method that handles setting up the map to multimap relationship to help prevent issues.
	 * Thanks to TelepathicGrunts Structure tutorial @link https://github.com/TelepathicGrunt/StructureTutorialMod
	 */
	private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> BTStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
		BTStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
		HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = BTStructureToMultiMap.get(configuredStructureFeature.feature);
		if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
			LOGGER.error("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """,
					BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
					BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
					biomeRegistryKey
			);
		}
		else{
			configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
		}
	}



	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
