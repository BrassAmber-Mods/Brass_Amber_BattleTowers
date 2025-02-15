package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.inventory.BTChestMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTContainerTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BABTMain.MODID);

    public static final RegistryObject<MenuType<BTChestMenu>> GENERIC_9x8 = CONTAINERS.register("generic_9x8", () -> new MenuType<>(BTChestMenu::eightRows, FeatureFlags.REGISTRY.allFlags()));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
