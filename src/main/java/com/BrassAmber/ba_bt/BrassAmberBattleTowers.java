package com.BrassAmber.ba_bt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.BrassAmber.ba_bt.client.BTTileEntityRender;
import com.BrassAmber.ba_bt.init.*;
import com.BrassAmber.ba_bt.util.BTItemGroup;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.BrassAmber.ba_bt.client.BTEntityRender;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BrassAmberBattleTowers.MOD_ID)
public class BrassAmberBattleTowers {

	public static final String MOD_ID = "ba_bt";
	public static final ItemGroup BATLETOWERSTAB = new BTItemGroup();
	// Directly reference a log4j logger
	public static final Logger LOGGER = LogManager.getLogger();

	public static final ITextComponent HOLD_SHIFT_TOOLTIP = (new TranslationTextComponent("tooltip.battletowers.hold_shift").withStyle(TextFormatting.DARK_GRAY));

	public BrassAmberBattleTowers() {
		// Register the setup method for modloading
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		BTStructures.DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
		// Register Items
		BTItems.ITEMS.register(eventBus);
		// Register Blocks
		BTBlocks.BLOCKS.register(eventBus);
		// Register TileEntityTypes
		BTTileEntityTypes.TILE_ENTITY_TYPES.register(eventBus);
		// Register SoundEvents
		BTSoundEvents.SOUND_EVENTS.register(eventBus);
		// Register EntityTypes
		BTEntityTypes.ENTITY_TYPES.register(eventBus);

		eventBus.addListener(this::setup);
		// Register the enqueueIMC method for modloading

		// Register the doClientStuff method for modloading
		// the check for client only is isnide the method.
		eventBus.addListener(this::doClientStuff);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BattleTowersConfig.SPEC, "ba-battletowers-config.toml");


		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		// For events that happen after initialization. This is probably going to be use a lot.
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

		// The comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
		forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
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

	public void biomeModification(final BiomeLoadingEvent event) {
		/*
		 * Add our structure to all biomes including other modded biomes.
		 * You can skip or add only to certain biomes based on stuff like biome category,
		 * temperature, scale, precipitation, mod id, etc. All kinds of options!
		 *
		 * You can even use the BiomeDictionary as well! To use BiomeDictionary, do
		 * RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()) to get the biome's
		 * registrykey. Then that can be fed into the dictionary to get the biome's types.
		 */
		RegistryKey key = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());

		if (BiomeDictionary.hasType(key, BiomeDictionary.Type.NETHER) ||
				BiomeDictionary.hasType(key, BiomeDictionary.Type.END) ||
						BiomeDictionary.hasType(key, BiomeDictionary.Type.MOUNTAIN)) {

		} else if (BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD)) {
			if (BiomeDictionary.hasType(key, BiomeDictionary.Type.SWAMP)
					|| BiomeDictionary.hasType(key, BiomeDictionary.Type.FOREST)
					|| BiomeDictionary.hasType(key, BiomeDictionary.Type.SAVANNA)
					|| BiomeDictionary.hasType(key, BiomeDictionary.Type.PLAINS)
					|| BiomeDictionary.hasType(key, BiomeDictionary.Type.SANDY)
					|| BiomeDictionary.hasType(key, BiomeDictionary.Type.JUNGLE))
			{
				event.getGeneration().getStructures().add(() -> BTConfiguredStructures.CONFIGURED_LAND_BATTLE_TOWER);
			}
		}

		/**if (!BiomeDictionary.hasType(key, BiomeDictionary.Type.MOUNTAIN)) {
			event.getGeneration().getStructures().add(() -> BTConfiguredStructures.CONFIGURED_SKY_BATTLE_TOWER);
		}*/
	}

	private static Method GETCODEC_METHOD;

	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) event.getWorld();

			/*
			 * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
			 * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
			 * This here is done with reflection as this tutorial is not about setting up and using Mixins.
			 * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
			 */

			try {
				if (GETCODEC_METHOD == null)
					GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
				if (cgRL != null && cgRL.getNamespace().equals("terraforged")) {
					return;
				}
			} catch (Exception e) {
				BrassAmberBattleTowers.LOGGER.error("Was unable to check if " + serverWorld.getChunkSource().generator + " is using Terraforged's ChunkGenerator.");
			}

			/*
			 * Prevent spawning our structure in Vanilla's superflat world as
			 * people seem to want their superflat worlds free of modded structures.
			 * Also that vanilla superflat is really tricky and buggy to work with in my experience.
			 */
			if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
				return;
			}

			/*
			 * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
			 * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
			 *
			 * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
			 * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
			 * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
			 */
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			if (serverWorld.dimension().equals(World.OVERWORLD)) {
				tempMap.putIfAbsent(BTStructures.LAND_BATTLE_TOWER.get(), DimensionStructuresSettings.DEFAULTS.get(BTStructures.LAND_BATTLE_TOWER.get()));
				// tempMap.putIfAbsent(BTStructures.SKY_BATTLE_TOWER.get(), DimensionStructuresSettings.DEFAULTS.get(BTStructures.SKY_BATTLE_TOWER.get()));
			} else if (serverWorld.dimension().equals(World.NETHER)) {

			} else if (serverWorld.dimension().equals(World.END)) {

			}
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}

	// Do something that can only be done on the client
	private void doClientStuff(final FMLClientSetupEvent event) {
		try {
			boolean isClient = event.getMinecraftSupplier().get().level.isClientSide();
		} catch (Exception e) {
			e.printStackTrace();
			// Register Entity Renderers
			//Render Type Spawner
			RenderTypeLookup.setRenderLayer(BTBlocks.BT_LAND_SPAWNER, RenderType.cutout());

			BTEntityRender.init();
			// Register TileEntity Renderers
			BTTileEntityRender.bindTileEntityRenderers(event);
		}

	}


	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
