package com.brass_amber.ba_bt;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.init.BTExtras;
import com.brass_amber.ba_bt.init.BTItems;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.brass_amber.ba_bt.init.BTItems.TAB_ICON;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BrassAmberBattleTowers.MODID)
public class BrassAmberBattleTowers {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "ba_bt";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final SaveTowers SAVE_TOWERS = new SaveTowers();
    public static final Component HOLD_SHIFT_TOOLTIP = (Component.translatable("tooltip.ba_bt.hold_shift").withStyle(ChatFormatting.DARK_GRAY));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BT_TAB = CREATIVE_MODE_TABS.register("bt_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativemodetab.bt_tab")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> TAB_ICON.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(TAB_ICON.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public BrassAmberBattleTowers(IEventBus modEventBus)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BTBlocks.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        BTItems.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered

        BTEntityType.register(modEventBus);
        BTSoundEvents.register(modEventBus);
        BTExtras.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BattleTowersConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

        if (event.getTab() == BT_TAB.get()) {

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

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static ResourceLocation locate(String name) {
        return new ResourceLocation(MODID, name);
    }
}
