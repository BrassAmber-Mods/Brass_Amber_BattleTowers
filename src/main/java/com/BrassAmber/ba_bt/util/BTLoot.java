package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.block.BTSpawnerBlock;
import com.BrassAmber.ba_bt.init.BTItems;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.abego.treelayout.internal.util.java.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.BrassAmber.ba_bt.BattleTowersConfig.*;
import static com.BrassAmber.ba_bt.util.BTUtil.itemByString;

public class BTLoot {

    public static final List<Item> badLoot;
    public static final List<Item> fillerLoot;
    public static final List<Item> decentLoot;
    public static final List<Item> goodLoot;
    public static final List<Integer> badLootCounts;
    public static final List<Integer> fillerLootCounts;
    public static final List<Integer> decentLootCounts;
    public static final List<Integer> goodLootCounts;

    public static final List<List<Item>> towerBadLoot;
    public static final List<List<Item>> towerFillerLoot;
    public static final List<List<Item>> towerDecentLoot;
    public static final List<List<Item>> towerGoodLoot;
    public static final List<List<Integer>> towerBadLootCounts;
    public static final List<List<Integer>> towerFillerLootCounts;
    public static final List<List<Integer>> towerDecentLootCounts;
    public static final List<List<Integer>> towerGoodLootCounts;

    public static final List<List<Item>> golemLoot;
    public static final List<List<Integer>> golemLootCounts;

    public static final List<List<Integer>> landFloorChances;
    public static final List<List<Integer>> oceanFloorChances;
    public static final List<List<Integer>> golemLootChances;
    public static final List<List<List<Integer>>> towerLootRolls;
    public static final List<LootType> types;


    static {
        // loot is all loaded from the config
        badLoot = getItemList(generalBadLoot.get());
        fillerLoot = getItemList(generalFillerLoot.get());
        decentLoot = getItemList(generalDecentLoot.get());
        goodLoot = getItemList(generalGoodLoot.get());
        badLootCounts = new ArrayList<>();
        fillerLootCounts = new ArrayList<>();
        decentLootCounts = new ArrayList<>();
        goodLootCounts = new ArrayList<>();

        badLootCounts.addAll(generalBadLootCounts.get());
        fillerLootCounts.addAll(generalFillerLootCounts.get());
        decentLootCounts.addAll(generalDecentLootCounts.get());
        goodLootCounts.addAll(generalGoodLootCounts.get());

        // ----------------------------------------------------------------- \\

        // Loot exclusive to each tower
        towerBadLoot = List.of(getItemList(landTowerBadLoot.get()), getItemList(oceanTowerBadLoot.get()));
        towerFillerLoot = List.of(getItemList(landTowerFillerLoot.get()), getItemList(oceanTowerFillerLoot.get()));
        towerDecentLoot = List.of(getItemList(landTowerDecentLoot.get()), getItemList(oceanTowerDecentLoot.get()));
        towerGoodLoot = List.of(getItemList(landTowerGoodLoot.get()), getItemList(oceanTowerGoodLoot.get()));
        towerBadLootCounts = List.of(new ArrayList<>(), new ArrayList<>());
        towerFillerLootCounts = List.of(new ArrayList<>(), new ArrayList<>());
        towerDecentLootCounts = List.of(new ArrayList<>(), new ArrayList<>());
        towerGoodLootCounts = List.of(new ArrayList<>(), new ArrayList<>());

        towerBadLootCounts.get(0).addAll(landBadLootCounts.get());
        towerBadLootCounts.get(1).addAll(oceanBadLootCounts.get());
        towerFillerLootCounts.get(0).addAll(landFillerLootCounts.get());
        towerFillerLootCounts.get(1).addAll(oceanFillerLootCounts.get());
        towerDecentLootCounts.get(0).addAll(landDecentLootCounts.get());
        towerDecentLootCounts.get(1).addAll(oceanDecentLootCounts.get());
        towerGoodLootCounts.get(0).addAll(landGoodLootCounts.get());
        towerGoodLootCounts.get(1).addAll(oceanGoodLootCounts.get());

        // ----------------------------------------------------------------- \\

        // Golem loot
        golemLoot = List.of(getItemList(landTowerGolemLoot.get()), getItemList(oceanTowerGolemLoot.get()));
        golemLootCounts = List.of(new ArrayList<>(), new ArrayList<>());

        golemLootCounts.get(0).addAll(landGolemLootCounts.get());
        golemLootCounts.get(1).addAll(oceanGolemLootCounts.get());

        // ----------------------------------------------------------------- \\

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

        golemLootChances = List.of(
                List.of(2, 4, 4, 4),
                List.of(1, 4, 5, 4)
        );

        towerLootRolls = List.of(
                landFloorChances,
                oceanFloorChances
        );

        types = List.of(LootType.BadLoot, LootType.FillerLoot, LootType.DecentLoot, LootType.GoodLoot);

    }
    

    public enum LootType implements StringRepresentable {
        Empty("empty", 0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
        BadLoot("bad", .01f, badLoot, towerBadLoot, badLootCounts, towerBadLootCounts),
        FillerLoot("filler", 0.05f, fillerLoot, towerFillerLoot,  fillerLootCounts, towerFillerLootCounts),
        DecentLoot("decent", 0.09f, decentLoot, towerDecentLoot,  decentLootCounts, towerDecentLootCounts),
        GoodLoot("good",0.15f, goodLoot, towerGoodLoot, goodLootCounts, towerGoodLootCounts);

        private final String name;
        private final float loseChance;
        private final List<Item> loot;
        private final List<List<Item>> towerLoot;
        private final List<Integer> counts;
        private final List<List<Integer>> towerCounts;

        LootType(String name, float loseChance, List<Item> loot, List<List<Item>> towerLoot, List<Integer> counts, List<List<Integer>> towerCounts) {
            this.name = name;
            this.loseChance = loseChance;
            this.loot = loot;
            this.towerLoot = towerLoot;
            this.counts = counts;
            this.towerCounts = towerCounts;
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

        public List<Integer> getBaseCounts() {
            return this.counts;
        }

        public List<Integer> getTowerCounts(int towerType) {
            return this.towerCounts.get(towerType);
        }


        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public static List<Item> getItemList(List<? extends String> itemIds) {
        List<Item> itemList= new ArrayList<>();
        for (String itemId: itemIds) {
            if (itemId.equals("minecraft:air")) {
                itemList.add(BTItems.BT_LAND_SPAWNER.get());
            } else {
                itemList.add(itemByString(itemId));
            }

        }
        return itemList;
    }

    public static LootTable getLootTable(int towerType, int floor) {
        List<Integer> lootRolls = towerLootRolls.get(towerType).get(floor);
        LootTable.Builder chestLoot = LootTable.lootTable();
        List<LootPool.Builder> pools = new ArrayList<>(Collections.emptyList());
        int w;
        int itemCount;
        int maxCount;
        int minCount;
        for (int i = 0; i < lootRolls.size(); i++)  {
            int rolls = lootRolls.get(i);
            LootType type = types.get(i);
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(rolls));

            List<Item> baseItems = type.getBaseLoot();
            List<Item> towerItems = type.getTowerLoot(towerType);
            List<Item> allItems = new ArrayList<>();
            allItems.addAll(baseItems);
            allItems.addAll(towerItems);

            List<Integer> baseCounts = type.getBaseCounts();
            List<Integer> towerCounts = type.getTowerCounts(towerType);
            List<Integer> allCounts = new ArrayList<>();
            allCounts.addAll(baseCounts);
            allCounts.addAll(towerCounts);

            w = 0;
            for (Item item: allItems) {
                // BrassAmberBattleTowers.LOGGER.info("current item, count" + w);
                itemCount = allCounts.get(w);
                maxCount = (itemCount - (itemCount % 10)) / 10;
                minCount = itemCount - (maxCount * 10);
                if (item == BTItems.BT_LAND_SPAWNER.get()) {
                    pool.add(EmptyLootItem.emptyItem().setWeight(0));
                }
                if (item instanceof EnchantedBookItem bookItem) {
                    pool.add(
                            LootItem.lootTableItem(bookItem).setWeight(8)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)))
                                    .apply(EnchantRandomlyFunction.randomEnchantment())
                    );
                }
                else {
                    int weight = 10;
                    if (item instanceof RecordItem) {
                        weight = 2;
                    }
                    pool.add(LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount))));
                }
                w ++;
            }
            int emptyChance = Mth.floor(allItems.size() * 7.5 * type.getLoseChance());

            pool.add(EmptyLootItem.emptyItem().setWeight(emptyChance));
            pools.add(pool);
        }

        return chestLoot.withPool(pools.get(0)).withPool(pools.get(1)).withPool(pools.get(2)).withPool(pools.get(3)).build();
    }

    public static LootTable getGolemLootTable(int towerType) {
        List<Integer> lootRolls = golemLootChances.get(towerType);
        LootTable.Builder chestLoot = LootTable.lootTable();
        List<LootPool.Builder> pools = new ArrayList<>(Collections.emptyList());
        int w;
        int itemCount;
        int maxCount;
        int minCount;
        for (int i = 0; i < lootRolls.size(); i++)  {
            int rolls = lootRolls.get(i);
            LootType type = types.get(i);
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(rolls));

            List<Item> baseItems = type.getBaseLoot();
            List<Item> golemItems = golemLoot.get(towerType);
            List<Item> allItems = new ArrayList<>();
            allItems.addAll(baseItems);
            allItems.addAll(golemItems);

            List<Integer> baseCounts = type.getBaseCounts();
            List<Integer> golemCounts = golemLootCounts.get(towerType);
            List<Integer> allCounts = new ArrayList<>();
            allCounts.addAll(baseCounts);
            allCounts.addAll(golemCounts);

            w = 0;
            for (Item item: allItems) {
                itemCount = allCounts.get(w);
                maxCount = (itemCount - (itemCount % 10)) / 10;
                minCount = itemCount - (maxCount * 10);
                if (item == BTItems.BT_LAND_SPAWNER.get()) {
                    pool.add(EmptyLootItem.emptyItem().setWeight(0));
                }
                if (item instanceof EnchantedBookItem bookItem) {
                    pool.add(
                            LootItem.lootTableItem(bookItem).setWeight(8)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)))
                                    .apply(EnchantRandomlyFunction.randomEnchantment())
                    );
                }
                else {
                    int weight = 10;
                    if (item instanceof RecordItem) {
                        weight = 2;
                    }
                    pool.add(LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount))));
                }
            }
            int emptyChance = Mth.floor(allItems.size() * 7.5 * type.getLoseChance());

            pool.add(EmptyLootItem.emptyItem().setWeight(emptyChance));
            pools.add(pool);
        }

        return chestLoot.withPool(pools.get(0)).withPool(pools.get(1)).withPool(pools.get(2)).withPool(pools.get(3)).build();
    }

}
