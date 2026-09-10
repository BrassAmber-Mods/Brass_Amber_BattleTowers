package com.brass_amber.ba_bt.datagen.datapack;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.registries.EntityData;
import com.brass_amber.ba_bt.registries.GenerationData;
import com.brass_amber.ba_bt.registries.TowerData;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

import static com.brass_amber.ba_bt.datagen.datapack.EntityDataBootstrap.*;
import static com.brass_amber.ba_bt.datagen.datapack.GenerationDataBootstrap.*;

public class TowerDataBootstrap {
    public static final ResourceKey<TowerData> LAND_TOWER_DATA = registerTowerDataKey("land");
    public static final ResourceKey<TowerData> OCEAN_TOWER_DATA = registerTowerDataKey("ocean");
    public static final ResourceKey<TowerData> CORE_TOWER_DATA = registerTowerDataKey("core");
    public static final ResourceKey<TowerData> NETHER_TOWER_DATA = registerTowerDataKey("nether");
    public static final ResourceKey<TowerData> END_TOWER_DATA = registerTowerDataKey("end");
    public static final ResourceKey<TowerData> SKY_TOWER_DATA = registerTowerDataKey("sky");

    public static void towerBootstrap(BootstapContext<TowerData> towerDataContext) {
        HolderGetter<EntityData> entityDataHolderGetter = towerDataContext.lookup(BTRegistries.Keys.ENTITY_DATA);

        HolderGetter<GenerationData> generationDataHolderGetter = towerDataContext.lookup(BTRegistries.Keys.GENERATION_DATA);

        towerDataContext.register(
                LAND_TOWER_DATA,
                new TowerData(
                        "land",
                        entityDataHolderGetter.getOrThrow(LAND_ENTITY_DATA).get(),
                        generationDataHolderGetter.getOrThrow(LAND_GENERATION_DATA).get(),
                        new Vec3i(0, 1, 0),
                        new Vec3i(0, 103, 0)
                )
        );

        towerDataContext.register(
                OCEAN_TOWER_DATA,
                new TowerData(
                        "ocean",
                        entityDataHolderGetter.getOrThrow(OCEAN_ENTITY_DATA).get(),
                        generationDataHolderGetter.getOrThrow(OCEAN_GENERATION_DATA).get(),
                        new Vec3i(0, 3, 0),
                        new Vec3i(0, -100, 0)
                )
        );

        towerDataContext.register(
                CORE_TOWER_DATA,
                new TowerData(
                        "core",
                        entityDataHolderGetter.getOrThrow(CORE_ENTITY_DATA).get(),
                        generationDataHolderGetter.getOrThrow(CORE_GENERATION_DATA).get(),
                        new Vec3i(0, 1, 0),
                        new Vec3i(0, 103, 0)
                )
        );

        towerDataContext.register(
                NETHER_TOWER_DATA,
                new TowerData(
                        "nether",
                        entityDataHolderGetter.getOrThrow(NETHER_ENTITY_DATA).get(),
                        generationDataHolderGetter.getOrThrow(NETHER_GENERATION_DATA).get(),
                        new Vec3i(0, 1, 0),
                        new Vec3i(0, 103, 0)
                )
        );

        towerDataContext.register(
                END_TOWER_DATA,
                new TowerData(
                        "end",
                        entityDataHolderGetter.getOrThrow(END_ENTITY_DATA).get(),
                        generationDataHolderGetter.getOrThrow(END_GENERATION_DATA).get(),
                        new Vec3i(0, 1, 0),
                        new Vec3i(0, 103, 0)
                )
        );

        towerDataContext.register(
                SKY_TOWER_DATA,
                new TowerData(
                        "core",
                        entityDataHolderGetter.getOrThrow(SKY_ENTITY_DATA).get(),
                        generationDataHolderGetter.getOrThrow(SKY_GENERATION_DATA).get(),
                        new Vec3i(0, 1, 0),
                        new Vec3i(0, 103, 0)
                )
        );
    }

    private static ResourceKey<TowerData> registerTowerDataKey(String name) {
        return ResourceKey.create(BTRegistries.Keys.TOWER_DATA, BABattleTowers.locate(name));
    }
}
