package com.BrassAmber.ba_bt.util;

import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.BrassAmber.ba_bt.BattleTowersConfig.*;
import static com.BrassAmber.ba_bt.util.BTUtil.itemByString;

public class BTLoot {

    public static final List<Item> badLoot;
    public static final List<Item> fillerLoot;
    public static final List<Item> decentLoot;
    public static final List<Item> goodLoot;
    public static final List<List<Item>> towerBadLoot;
    public static final List<List<Item>> towerFillerLoot;
    public static final List<List<Item>> towerDecentLoot;
    public static final List<List<Item>> towerGoodLoot;
    public static final List<List<Integer>> landFloorChances;
    public static final List<List<Integer>> oceanFloorChances;
    public static final List<List<List<Integer>>> towerLootRolls;
    public static final List<LootType> types;


    static {
        // loot is all loaded from the config
        badLoot = new ArrayList<>();
        fillerLoot = new ArrayList<>();
        decentLoot = new ArrayList<>();
        goodLoot = new ArrayList<>();

        for (String itemId: generalBadLoot.get()) {
            badLoot.add(itemByString(itemId));
        }
        for (String itemId: generalFillerLoot.get()) {
            fillerLoot.add(itemByString(itemId));
        }
        for (String itemId: generalDecentLoot.get()) {
            decentLoot.add(itemByString(itemId));
        }
        for (String itemId: generalGoodLoot.get()) {
            goodLoot.add(itemByString(itemId));
        }

        // Bad loot exclusive to each tower
        towerBadLoot = List.of(new ArrayList<>(), new ArrayList<>());

        towerFillerLoot = List.of(new ArrayList<>(), new ArrayList<>());

        towerDecentLoot = List.of(new ArrayList<>(), new ArrayList<>());

        towerGoodLoot = List.of(new ArrayList<>(), new ArrayList<>());

        for (String itemId: landTowerBadLoot.get()) {
            towerBadLoot.get(0).add(itemByString(itemId));
        }
        for (String itemId: oceanTowerBadLoot.get()) {
            towerBadLoot.get(1).add(itemByString(itemId));
        }
        for (String itemId: landTowerFillerLoot.get()) {
            towerFillerLoot.get(0).add(itemByString(itemId));
        }
        for (String itemId: oceanTowerFillerLoot.get()) {
            towerFillerLoot.get(1).add(itemByString(itemId));
        }
        for (String itemId: landTowerDecentLoot.get()) {
            towerDecentLoot.get(0).add(itemByString(itemId));
        }
        for (String itemId: oceanTowerDecentLoot.get()) {
            towerDecentLoot.get(1).add(itemByString(itemId));
        }
        for (String itemId: landTowerGoodLoot.get()) {
            towerGoodLoot.get(0).add(itemByString(itemId));
        }
        for (String itemId: oceanTowerGoodLoot.get()) {
            towerGoodLoot.get(1).add(itemByString(itemId));
        }

        // List of loot rolls per floor of each tower.
        landFloorChances = List.of(
                List.of(4, 3, 2, 0),
                List.of(3, 4, 2, 0),
                List.of(5, 4, 3, 0),
                List.of(4, 5, 2, 1),
                List.of(4, 4, 3, 1),
                List.of(3, 4, 4, 1),
                List.of(2, 4, 4, 2),
                List.of(4, 5, 2, 3)
        );

        oceanFloorChances = List.of(
                List.of(4, 3, 2, 0),
                List.of(3, 4, 2, 0),
                List.of(5, 4, 3, 0),
                List.of(4, 5, 2, 1),
                List.of(4, 4, 3, 1),
                List.of(3, 4, 4, 1),
                List.of(2, 4, 4, 2),
                List.of(4, 5, 2, 3)
        );

        towerLootRolls = List.of(
                landFloorChances,
                oceanFloorChances
        );

        types = List.of(LootType.BadLoot, LootType.FillerLoot, LootType.DecentLoot, LootType.GoodLoot);

    }

    

    public enum LootType implements StringRepresentable {
        Empty("empty", 0, Collections.emptyList(), Collections.emptyList(), 0),
        BadLoot("bad", .01f, badLoot, towerBadLoot, 4),
        FillerLoot("filler", 0.05f, fillerLoot, towerFillerLoot, 2),
        DecentLoot("decent", 0.09f, decentLoot, towerDecentLoot, 1),
        GoodLoot("good",0.15f, goodLoot, towerGoodLoot,1);

        private final String name;
        private final float loseChance;
        private final List<Item> loot;
        private final List<List<Item>> towerLoot;
        private final int itemAmounts;

        LootType(String name, float loseChance, List<Item> loot, List<List<Item>> towerLoot, int itemAmounts) {
            this.name = name;
            this.loseChance = loseChance;
            this.loot = loot;
            this.towerLoot = towerLoot;
            this.itemAmounts = itemAmounts;
        }

        public float getLoseChance() {
            return this.loseChance;
        }

        public List<Item> getBaseLoot() {
            return this.loot;
        }

        public List<Item> getTowerLoot(int towerType) {
            return this.towerLoot.get(towerType);
        }

        public int getItemAmounts() {
            return itemAmounts;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public static LootTable getLootTable(int towerType, int floor) {
        List<Integer> lootRolls = towerLootRolls.get(towerType).get(floor);
        LootTable.Builder chestLoot = LootTable.lootTable();
        List<LootPool.Builder> pools = new ArrayList<>(Collections.emptyList());

        for (int i = 0; i < lootRolls.size(); i++)  {
            int rolls = lootRolls.get(i);
            LootType type = types.get(i);
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(rolls));

            List<Item> baseItems = type.getBaseLoot();
            List<Item> towerItems = type.getTowerLoot(towerType);

            for (Item item: baseItems) {
                pool.add(LootItem.lootTableItem(item).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(type.getItemAmounts()))));
            }
            for (Item item: towerItems) {
                pool.add(LootItem.lootTableItem(item).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(type.getItemAmounts()))));
            }
            int emptyChance = Mth.floor((baseItems.size() + towerItems.size()) * 7.5 * type.getLoseChance());

            pool.add(EmptyLootItem.emptyItem().setWeight(emptyChance));
            pools.add(pool);
        }

        return chestLoot.withPool(pools.get(0)).withPool(pools.get(1)).withPool(pools.get(2)).withPool(pools.get(3)).build();
    }

}
