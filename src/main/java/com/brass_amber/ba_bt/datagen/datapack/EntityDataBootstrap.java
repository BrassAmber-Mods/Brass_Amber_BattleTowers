package com.brass_amber.ba_bt.datagen.datapack;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.init.BTItems;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.registries.EntityData;
import com.brass_amber.ba_bt.registries.LocalModification;
import com.brass_amber.ba_bt.util.BTTags;
import com.brass_amber.ba_bt.worldGen.structures.towerChanges.EmptyModification;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

import static com.brass_amber.ba_bt.sound.BTMusic.*;
import static com.brass_amber.ba_bt.sound.BTMusic.CORE_GOLEM_FIGHT_MUSIC;

public class EntityDataBootstrap {

    public static final ResourceKey<EntityData> LAND_ENTITY_DATA = registerEntityDataKey("land");
    public static final ResourceKey<EntityData> OCEAN_ENTITY_DATA = registerEntityDataKey("ocean");
    public static final ResourceKey<EntityData> CORE_ENTITY_DATA = registerEntityDataKey("core");
    public static final ResourceKey<EntityData> NETHER_ENTITY_DATA = registerEntityDataKey("nether");
    public static final ResourceKey<EntityData> END_ENTITY_DATA = registerEntityDataKey("end");
    public static final ResourceKey<EntityData> SKY_ENTITY_DATA = registerEntityDataKey("sky");
    public static final ResourceKey<EntityData> CITY_ENTITY_DATA = registerEntityDataKey("city");
    public static final ResourceKey<EntityData> AETHER_ENTITY_DATA = registerEntityDataKey("aether");
    public static final ResourceKey<EntityData> DEEPER_DARK_ENTITY_DATA = registerEntityDataKey("deeper_dark");

    public static void entityBootstrap(BootstapContext<EntityData> entityDataContext) {

        entityDataContext.register(
                LAND_ENTITY_DATA,
                new EntityData(
                        "land", "#9BDAE7", "ba_bt", LAND_TOWER_MUSIC, LAND_GOLEM_FIGHT_MUSIC,
                        new EntityData.TypeReferences(
                                BTBlocks.LAND_CHEST.get(), BTBlocks.LAND_GOLEM_CHEST.get(), BTBlocks.LAND_SPAWNER.get(),
                                Blocks.AIR, BTItems.LAND_MONOLITH.get(),
                                BTEntityType.LAND_GOLEM.get(), BTEntityType.MONOLITH.get(), BTEntityType.OBELISK.get(),
                                Optional.empty(), Optional.of(new EntityData.MobList(List.of("minecraft:zombie", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider")))
                                ),
                        new EntityData.MultiStateKeys(
                                List.of(List.of(BTItems.LAND_MONOLITH_KEY.get(), BTItems.LAND_MONOLITH_KEY.get(), BTItems.LAND_MONOLITH_KEY.get()))
                        ),
                        new EmptyModification(new LocalModification.Settings(0, 8,-2,false, 24,Optional.empty()))
                )
        );

        entityDataContext.register(
                OCEAN_ENTITY_DATA,
                new EntityData(
                        "ocean", "#EAE78A", "ba_bt", OCEAN_TOWER_MUSIC, OCEAN_GOLEM_FIGHT_MUSIC,
                        new EntityData.TypeReferences(
                                BTBlocks.OCEAN_CHEST.get(), BTBlocks.OCEAN_GOLEM_CHEST.get(), BTBlocks.OCEAN_SPAWNER.get(),
                                Blocks.WATER, BTItems.OCEAN_MONOLITH.get(),
                                BTEntityType.OCEAN_GOLEM.get(), BTEntityType.MONOLITH.get(), BTEntityType.OBELISK.get(),
                                Optional.empty(), Optional.of(new EntityData.MobList(List.of("minecraft:drowned", "minecraft:guardian", "minecraft:drowned", "minecraft:drowned", "minecraft:drowned", "minecraft:pufferfish")))
                        ),
                        new EntityData.MultiStateKeys(
                                List.of(List.of(BTItems.OCEAN_MONOLITH_KEY.get(), BTItems.OCEAN_MONOLITH_KEY.get()), List.of(BTItems.LAND_MONOLITH_KEY.get()))
                        ),
                        new EmptyModification(new LocalModification.Settings(0, -2,-4, false, 60, Optional.empty()))
                )
        );

        entityDataContext.register(
                CORE_ENTITY_DATA,
                new EntityData(
                        "core", "#F79B3A", "ba_bt", CORE_TOWER_MUSIC, CORE_GOLEM_FIGHT_MUSIC,
                        new EntityData.TypeReferences(
                                BTBlocks.CORE_CHEST.get(), BTBlocks.CORE_GOLEM_CHEST.get(), BTBlocks.CORE_SPAWNER.get(),
                                Blocks.AIR, BTItems.CORE_MONOLITH.get(),
                                BTEntityType.CORE_GOLEM.get(), BTEntityType.MONOLITH.get(), BTEntityType.OBELISK.get(),
                                Optional.empty(), Optional.of(new EntityData.MobList(List.of("minecraft:magma_cube", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider")))
                        ),
                        new EntityData.MultiStateKeys(
                                List.of(List.of(BTItems.CORE_MONOLITH_KEY.get(), BTItems.CORE_MONOLITH_KEY.get()), List.of(BTItems.OCEAN_GOLEM_EYE.get()))
                        ),
                        new EmptyModification(new LocalModification.Settings(0, 8, -2, true, 53,Optional.empty()))
                )
        );

        entityDataContext.register(
                NETHER_ENTITY_DATA,
                new EntityData(
                        "nether", "#88EB63", "ba_bt", NETHER_TOWER_MUSIC, NETHER_GOLEM_FIGHT_MUSIC,
                        new EntityData.TypeReferences(
                                BTBlocks.NETHER_CHEST.get(), BTBlocks.NETHER_GOLEM_CHEST.get(), BTBlocks.NETHER_SPAWNER.get(),
                                Blocks.AIR, BTItems.NETHER_MONOLITH.get(),
                                BTEntityType.NETHER_GOLEM.get(), BTEntityType.MONOLITH.get(), BTEntityType.OBELISK.get(),
                                Optional.empty(), Optional.of(new EntityData.MobList(List.of("minecraft:magma_cube", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider")))
                        ),
                        new EntityData.MultiStateKeys(
                                List.of(List.of(BTItems.NETHER_MONOLITH_KEY.get(), BTItems.NETHER_MONOLITH_KEY.get()), List.of(BTItems.OCEAN_GOLEM_EYE.get()))
                        ),
                        new EmptyModification(new LocalModification.Settings(0, 0, 0,false, 24, Optional.empty()))
                )
        );

        entityDataContext.register(
                END_ENTITY_DATA,
                new EntityData(
                        "end", "#BA49EF", "ba_bt", END_TOWER_MUSIC, END_GOLEM_FIGHT_MUSIC,
                        new EntityData.TypeReferences(
                                BTBlocks.END_CHEST.get(), BTBlocks.END_GOLEM_CHEST.get(), BTBlocks.END_SPAWNER.get(),
                                Blocks.AIR, BTItems.END_MONOLITH.get(),
                                BTEntityType.END_GOLEM.get(), BTEntityType.MONOLITH.get(), BTEntityType.OBELISK.get(),
                                Optional.empty(), Optional.of(new EntityData.MobList(List.of("minecraft:magma_cube", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider")))
                        ),
                        new EntityData.MultiStateKeys(
                                List.of(List.of(BTItems.END_MONOLITH_KEY.get(), BTItems.END_MONOLITH_KEY.get()), List.of(BTItems.OCEAN_GOLEM_EYE.get()))
                        ),
                        new EmptyModification(new LocalModification.Settings(0, 0, 0, false, 24, Optional.empty()))
                )
        );

        entityDataContext.register(
                SKY_ENTITY_DATA,
                new EntityData(
                        "sky", "#FBC1EB", "ba_bt", SKY_TOWER_MUSIC, SKY_GOLEM_FIGHT_MUSIC,
                        new EntityData.TypeReferences(
                                BTBlocks.SKY_CHEST.get(), BTBlocks.SKY_GOLEM_CHEST.get(), BTBlocks.SKY_SPAWNER.get(),
                                Blocks.AIR, BTItems.SKY_MONOLITH.get(),
                                BTEntityType.SKY_GOLEM.get(), BTEntityType.MONOLITH.get(), BTEntityType.OBELISK.get(),
                                Optional.empty(), Optional.of(new EntityData.MobList(List.of("minecraft:magma_cube", "minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider")))
                        ),
                        new EntityData.MultiStateKeys(
                                List.of(List.of(BTItems.SKY_MONOLITH_KEY.get(), BTItems.SKY_MONOLITH_KEY.get()), List.of(BTItems.OCEAN_GOLEM_EYE.get()))
                        ),
                        new EmptyModification(new LocalModification.Settings(0, 0, 0, false, 24, Optional.empty()))
                )
        );

        /*
        entityDataContext.register(
                CITY_ENTITY_DATA,
        );

        entityDataContext.register(
                AETHER_ENTITY_DATA,

        );

        entityDataContext.register(
                DEEPER_DARK_ENTITY_DATA,

        );
        */
    }

    private static ResourceKey<EntityData> registerEntityDataKey(String name) {
        return ResourceKey.create(BTRegistries.Keys.ENTITY_DATA, BABattleTowers.locate(name));
    }
}
