package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.registries.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

import static com.brass_amber.ba_bt.BABattleTowers.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BTRegistries {
    static final DeferredRegister<Codec<? extends LocalModification>> DEFERRED_LOCAL_MODIFICATION_TYPE_SERIALIZERS = DeferredRegister.create(Keys.LOCAL_MODIFICATION_TYPE, MOD_ID);
    public static final Supplier<IForgeRegistry<Codec<? extends LocalModification>>> LOCAL_MODIFICATION_TYPE_SERIALIZERS = DEFERRED_LOCAL_MODIFICATION_TYPE_SERIALIZERS.makeRegistry(() -> getLocalModificationSerializersRegistryBuilder());

    public static final class Keys {
        public static final ResourceKey<Registry<Codec<? extends LocalModification>>> LOCAL_MODIFICATION_TYPE = key("local_modification_type");

        public static final ResourceKey<Registry<ItemPool>> ITEM_POOLS = key("item_pools");
        public static final ResourceKey<Registry<TowerData>> TOWER_DATA = key("tower_data");
        public static final ResourceKey<Registry<EntityData>> ENTITY_DATA = key("entity_data");
        public static final ResourceKey<Registry<GenerationData>> GENERATION_DATA = key("generation_data");
        public static final ResourceKey<Registry<LocalModification>> LOCAL_MODIFICATIONS = key("local_modifications");
        public static final ResourceKey<Registry<EntityData>> DESTRUCTION_TYPE = key("destruction_type");
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
        event.dataPackRegistry(
                Keys.TOWER_DATA,
                TowerData.CODEC
        );
        event.dataPackRegistry(
                Keys.ENTITY_DATA,
                EntityData.CODEC
        );
        event.dataPackRegistry(
                Keys.GENERATION_DATA,
                GenerationData.CODEC
        );
        event.dataPackRegistry(
                Keys.LOCAL_MODIFICATIONS,
                LocalModification.DIRECT_CODEC
        );
        /*event.dataPackRegistry(
                Keys.DESTRUCTION_TYPE,
                TowerData.CODEC
        );*/
    }

    static RegistryBuilder<Codec<? extends LocalModification>> getLocalModificationSerializersRegistryBuilder() {
        return new RegistryBuilder<Codec<? extends LocalModification>>().disableSaving().disableSync();
    }
}
