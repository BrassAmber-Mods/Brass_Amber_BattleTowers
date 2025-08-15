package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.util.ItemPool;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

import static com.brass_amber.ba_bt.init.BTRegistries.Keys.ITEM_POOLS;

@Mod.EventBusSubscriber(modid = BABattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BTRegistries {


    public static final class Keys{
        public static final ResourceKey<Registry<ItemPool>> ITEM_POOLS = ResourceKey.createRegistryKey(BABattleTowers.locate("loot_pools"));
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                ITEM_POOLS,
                ItemPool.CODEC,
                ItemPool.CODEC
        );
    }
}
