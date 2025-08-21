package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.item.ItemPool;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

@Mod.EventBusSubscriber(modid = BABattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BTRegistries {

    public static final class Keys {
        public static final ResourceKey<Registry<ItemPool>> ITEM_POOLS = key("item_pools");
    }

    private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(BABattleTowers.locate(name));
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                Keys.ITEM_POOLS,
                ItemPool.CODEC
        );
    }
}
