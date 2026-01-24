package com.brass_amber.ba_bt.datagen.loot;

import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.stream.Stream;

public class BTEntitiesLootTables extends EntityLootSubProvider {
    public BTEntitiesLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(BTEntityType.LAND_GOLEM.get(), getGolemLootTable(TowerType.LAND));
        this.add(BTEntityType.OCEAN_GOLEM.get(), getGolemLootTable(TowerType.OCEAN));
        this.add(BTEntityType.CORE_GOLEM.get(), getGolemLootTable(TowerType.CORE));
        this.add(BTEntityType.NETHER_GOLEM.get(), getGolemLootTable(TowerType.NETHER));
        this.add(BTEntityType.END_GOLEM.get(), getGolemLootTable(TowerType.END));
        this.add(BTEntityType.SKY_GOLEM.get(), getGolemLootTable(TowerType.SKY));
        this.add(BTEntityType.SKY_MINION.get(), LootTable.lootTable());
        this.add(BTEntityType.BT_CULTIST.get(), LootTable.lootTable());
        this.add(BTEntityType.FRAGMENT_OF_OBTHUURYN.get(), LootTable.lootTable());
        
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return BTEntityType.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
    }

    public static LootTable.Builder getGolemLootTable(TowerType type) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Objects.requireNonNull(TowerType.getEyeFor(type))))
        );
    }
}
