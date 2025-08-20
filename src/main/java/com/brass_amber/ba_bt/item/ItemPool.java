package com.brass_amber.ba_bt.item;

import com.brass_amber.ba_bt.util.BTRarity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

import static com.brass_amber.ba_bt.util.BTRarity.*;

public class ItemPool {

    public static final Codec<ItemPool> CODEC = RecordCodecBuilder.create(lootPoolInstance ->
            lootPoolInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(itemPool -> itemPool.name),
                    Codec.list(PoolItem.CODEC.codec()).fieldOf(JUNK.getSerializedName()).forGetter(itemPool -> itemPool.junkItems),
                    Codec.list(PoolItem.CODEC.codec()).fieldOf(COMMON.getSerializedName()).forGetter(itemPool -> itemPool.commonItems),
                    Codec.list(PoolItem.CODEC.codec()).fieldOf(UNCOMMON.getSerializedName()).forGetter(itemPool -> itemPool.uncommonItems),
                    Codec.list(PoolItem.CODEC.codec()).fieldOf(RARE.getSerializedName()).forGetter(itemPool -> itemPool.rareItems),
                    Codec.list(PoolItem.CODEC.codec()).fieldOf(EPIC.getSerializedName()).forGetter(itemPool -> itemPool.epicItems)
            ).apply(lootPoolInstance, ItemPool::new)
    );

    protected final String name;
    protected final List<List<PoolItem>> items;
    protected final List<PoolItem> junkItems;
    protected final List<PoolItem> commonItems;
    protected final List<PoolItem> uncommonItems;
    protected final List<PoolItem> rareItems;
    protected final List<PoolItem> epicItems;

    public ItemPool(String name, List<PoolItem> junkItems, List<PoolItem> commonItems, List<PoolItem> uncommonItems, List<PoolItem> rareItems, List<PoolItem> epicItems) {
        this.name = name;
        this.junkItems = junkItems;
        this.commonItems = commonItems;
        this.uncommonItems = uncommonItems;
        this.rareItems = rareItems;
        this.epicItems = epicItems;
        this.items = List.of(junkItems, commonItems, uncommonItems, rareItems, epicItems);
    }


    public LootPool.Builder getLootTableForRarity(LootPool.Builder pool, BTRarity requestedRarity, RandomSource randomSource) {
        int timesToAdd;

        for (int i = 0; i < this.items.size(); i++) {

            if (i <= requestedRarity.getRarity()) {
                for (PoolItem poolItem : this.items.get(i)) {
                    timesToAdd = (5 - requestedRarity.getRarity()) * 2;


                    pool.add(LootItem.lootTableItem(poolItem.item).setWeight(timesToAdd).apply(
                                SetItemCountFunction.setCount(UniformGenerator.between(poolItem.intProvider.getMinValue(), Math.min(new ItemStack(poolItem.item).getMaxStackSize(), poolItem.intProvider.getMaxValue()))
                                )
                    ));
                }
            }
        }

        return pool;
    }

    public String getName() {
        return name;
    }

    public boolean isName(String name) {
        return Objects.equals(this.name, name);
    }

    public static PoolItem singlePoolItem(ItemLike itemLike) {
            return new PoolItem(itemLike.asItem(), ConstantInt.of(1));
    }

    public static PoolItem rangePoolItem(ItemLike itemLike, int max) {
            return offsetRangePoolItem(itemLike, 1, max);
    }

    public static PoolItem offsetRangePoolItem(ItemLike itemLike, int min, int max) {
            return new PoolItem(itemLike.asItem(), UniformInt.of(min, max));
    }

    public record PoolItem(
            Item item, IntProvider intProvider
    ) {


        public static final MapCodec<PoolItem> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ForgeRegistries.ITEMS.getCodec().fieldOf("item").orElse(Items.AIR).forGetter(PoolItem::item),
                        IntProvider.codec(1, 64).orElse(ConstantInt.of(1)).fieldOf("amount").forGetter(PoolItem::intProvider)
                ).apply(instance, ItemPool.PoolItem::new)
        );
    }
}
