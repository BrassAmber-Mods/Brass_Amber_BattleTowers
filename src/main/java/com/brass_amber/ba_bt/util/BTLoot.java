package com.brass_amber.ba_bt.util;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.init.BTItems;
import com.brass_amber.ba_bt.item.item.BTEmptyLootItem;
import com.brass_amber.ba_bt.item.item.BTEnchantedBookHolder;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.*;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;
import static com.brass_amber.ba_bt.util.BTUtil.itemByString;

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

    public static final List<List<Integer>> landFloorRolls;
    public static final List<List<Integer>> oceanFloorRolls;
    public static final List<List<Integer>> golemLootRolls;
    public static final List<List<List<Integer>>> towerLootRolls;
    public static final List<LootType> lootTypes;


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
        landFloorRolls = List.of(
                List.of(4, 3, 2, 1),
                List.of(3, 4, 2, 1),
                List.of(3, 4, 1, 2),
                List.of(3, 4, 2, 2),
                List.of(2, 4, 3, 2),
                List.of(2, 2, 4, 3),
                List.of(1, 4, 5, 3),
                List.of(0, 4, 5, 4)
        );

        oceanFloorRolls = List.of(
                List.of(5, 3, 2, 1),
                List.of(3, 5, 2, 1),
                List.of(3, 5, 1, 2),
                List.of(3, 5, 2, 2),
                List.of(2, 5, 3, 2),
                List.of(2, 2, 5, 3),
                List.of(1, 5, 6, 3),
                List.of(0, 4, 6, 5)
        );

        golemLootRolls = List.of(
                List.of(0, 3, 6, 5, 4),
                List.of(0, 3, 6, 5, 4)
        );

        towerLootRolls = List.of(
                landFloorRolls,
                oceanFloorRolls
        );

        lootTypes = List.of(LootType.BadLoot, LootType.FillerLoot, LootType.DecentLoot, LootType.GoodLoot);

    }
    

    public enum LootType implements StringRepresentable {
        Empty("empty" , Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
        BadLoot("bad" , badLoot, towerBadLoot, badLootCounts, towerBadLootCounts),
        FillerLoot("filler" , fillerLoot, towerFillerLoot,  fillerLootCounts, towerFillerLootCounts),
        DecentLoot("decent" , decentLoot, towerDecentLoot,  decentLootCounts, towerDecentLootCounts),
        GoodLoot("good", goodLoot, towerGoodLoot, goodLootCounts, towerGoodLootCounts);

        private final String name;
        private final List<Item> loot;
        private final List<List<Item>> towerLoot;
        private final List<Integer> counts;
        private final List<List<Integer>> towerCounts;

        LootType(String name, List<Item> loot, List<List<Item>> towerLoot, List<Integer> counts, List<List<Integer>> towerCounts) {
            this.name = name;
            this.loot = loot;
            this.towerLoot = towerLoot;
            this.counts = counts;
            this.towerCounts = towerCounts;
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
                itemList.add(BTItems.BT_EMPTY_LOOT_ITEM.get());
            } else if (itemId.equals("minecraft:enchanted_book")) {
                itemList.add(BTItems.EBOOK_HOLDER.get());
            } else {
                itemList.add(itemByString(itemId));
            }
        }
        return itemList;
    }

    public static LootPool.Builder fillPool(int rolls, List<Item> items, List<Integer> counts, int enchantLevel) {
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly((float) rolls));
        int w = 0;
        float maxCount;
        float minCount;
        int itemCount;
        int weight;
        boolean isTool;
        boolean isArmor;
        boolean enchantItem;
        for (Item item: items) {
            itemCount = counts.get(w);
            maxCount = itemCount % 10;
            minCount = (itemCount - maxCount) / 10;
            // BrassAmberBattleTowers.LOGGER.info("itemCount for: " + item + " min/max " + minCount + "/" + maxCount);

            weight = 10;

            isTool = (item instanceof TieredItem || item instanceof TridentItem
                    || item instanceof ElytraItem || item instanceof ProjectileWeaponItem) && enchantTools.get() ;
            isArmor = item instanceof ArmorItem && enchantArmor.get();

            enchantItem = isTool || isArmor || item instanceof BTEnchantedBookHolder;

            LootPoolSingletonContainer.Builder<?> lootItem;
            if (item instanceof BTEmptyLootItem) {
                lootItem = EmptyLootItem.emptyItem();
                weight = 0;
            }
            else if (item instanceof BTEnchantedBookHolder) {
                lootItem = LootItem.lootTableItem(Items.BOOK);
                weight = 8;
            } else {
                lootItem = LootItem.lootTableItem(item);
                if (item instanceof RecordItem) {
                    weight = 2;
                }
            }
            Random random = new Random();
            Random r2 = new Random(random.nextLong());

            if (isTool || isArmor) {
                weight = 6;
            }

            if (enchantItem && r2.nextFloat(10) > 4) {
                lootItem.apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly((float) enchantLevel)).allowTreasure());
            }

            lootItem.setWeight(weight);
            lootItem.apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
            pool.add(lootItem);
            w++;
        }

        return pool;
    }

    public static LootTable getLootTable(int towerType, int floor) {
        return getLootTableBuilder(towerType, floor, towerLootRolls.get(towerType).get(floor)).build();
    }

    public static LootTable.Builder getLootTableBuilder(int towerType,  int floor, List<Integer> lootRolls) {
        LootTable.Builder chestLoot = LootTable.lootTable();
        List<LootPool.Builder> pools = new ArrayList<>(Collections.emptyList());

        for (int i = 0; i < lootRolls.size(); i++)  {
            int rolls = lootRolls.get(i);
            LootType type = lootTypes.get(i);

            List<Item> baseItems = type.getBaseLoot();
            List<Item> towerItems = type.getTowerLoot(towerType);
            List<Item> allItems = new ArrayList<>();
            allItems.addAll(baseItems);
            allItems.addAll(towerItems);
            // BrassAmberBattleTowers.LOGGER.info("Items: " + allItems);

            List<Integer> baseCounts = type.getBaseCounts();
            List<Integer> towerCounts = type.getTowerCounts(towerType);
            List<Integer> allCounts = new ArrayList<>();
            allCounts.addAll(baseCounts);
            allCounts.addAll(towerCounts);

            LootPool.Builder pool = fillPool(rolls, allItems, allCounts, (3*floor) + bookLevelEnchant.get());

            pools.add(pool);
        }

        return chestLoot.withPool(pools.get(0)).withPool(pools.get(1)).withPool(pools.get(2)).withPool(pools.get(3));
    }

    public static LootTable getGolemLootTable(int towerType) {
        List<Integer> lootRolls = golemLootRolls.get(towerType);
        int golemRolls = lootRolls.get(4);
        LootTable.Builder chestLoot = getLootTableBuilder(towerType, 8, lootRolls.subList(0, 4));

        LootPool.Builder pool = fillPool(golemRolls, golemLoot.get(towerType), golemLootCounts.get(towerType), 20 + bookLevelEnchant.get());

        int emptyChance = Mth.floor(golemLoot.get(towerType).size() * 7.5 * .1F);

        pool.add(EmptyLootItem.emptyItem().setWeight(emptyChance));

        return chestLoot.withPool(pool).build();
    }

}
