package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.inventory.BTChestMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BABTMain.MODID);

    public static final RegistryObject<MenuType<BTChestMenu>> GENERIC_9x8 = MENUS.register("generic_9x8", () -> new MenuType<>(BTChestMenu::eightRows, FeatureFlags.REGISTRY.allFlags()));

    private static  <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
