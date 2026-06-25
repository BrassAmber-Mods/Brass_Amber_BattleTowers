package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.registries.ItemPool;
import com.brass_amber.ba_bt.registries.EntityData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import static com.brass_amber.ba_bt.BABattleTowers.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BTRegistries {

    public static final class Keys {
        public static final ResourceKey<Registry<ItemPool>> ITEM_POOLS = key("item_pools");

        public static final ResourceKey<Registry<EntityData>> TOWER_DATA = key("tower_data");
        public static final ResourceKey<Registry<EntityData>> ENTITY_DATA = key("entity_data");
        public static final ResourceKey<Registry<EntityData>> GENERATION_DATA = key("generation_data");
        public static final ResourceKey<Registry<EntityData>> DESTRUCTION_TYPE = key("destruction_type");
        public static final ResourceKey<Registry<EntityData>> LOCAL_MODIFICATION_TYPE = key("local_modifications_type");
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
