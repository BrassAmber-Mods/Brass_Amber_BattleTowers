package com.brass_amber.ba_bt.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.*;

import static com.brass_amber.ba_bt.util.BTStatics.*;

public class BTUtil {
    static final Logger LOGGER = LogUtils.getLogger();


    public static ListTag newIntList(int... ints) {
        ListTag listtag = new ListTag();

        for (int d0 : ints) {
            listtag.add(IntTag.valueOf(d0));
        }

        return listtag;
    }


    public static CompoundTag newStringList(ArrayList<String> strings) {
        CompoundTag listtag = new CompoundTag();
        for (int i = 0; i < strings.size(); i++) {
            listtag.putString(String.valueOf(i), strings.get(i) != null ? strings.get(i) : "");
        }

        return listtag;
    }

    public static ArrayList<String> listFromTag(CompoundTag tag, List<String> checkFrom) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < tag.getAllKeys().size(); i++) {
            String value = tag.getString(String.valueOf(i));
            // BABTMain.LOGGER.info("get compound value: " + value + " | is in list? " + checkFrom.get().contains(value));
            list.add(checkFrom.contains(value) ? value : "Invalid");
        }

        return list;
    }


    public static int median(ArrayList<Integer> nums) {
        Collections.sort(nums);
        if (nums.size() % 2 == 1)
            return nums.get((nums.size() + 1) / 2 - 1);
        else {
            double lower = nums.get(nums.size() / 2 - 1);
            double upper = nums.get(nums.size() / 2);

            return (int) ((lower + upper) / 2.0);
        }
    }

    /**
     * Returns the squared horizontal distance as a positive double.
     */
    public static double sqrDistanceTo2D(Entity self, double targetX, double targetZ) {
        double dX = self.getX() - targetX;
        double dZ = self.getZ() - targetZ;
        return Math.abs(dX * dX + dZ * dZ);
    }

    public static double sqrDistanceTo2D(Entity self, Entity target) {
        double dX = self.getX() - target.getX();
        double dZ = self.getZ() - target.getZ();
        return Math.abs(dX * dX + dZ * dZ);
    }

    public static int chunkDistanceTo(ChunkPos start, ChunkPos end) {
        return Mth.floor(distanceTo2D(start.x - end.x, start.z - end.z));
    }

    /**
     * Returns the horizontal distance as a positive double.
     */
    public static double distanceTo2D(Entity self, double targetX, double targetZ) {
        double dX = self.getX() - targetX;
        double dZ = self.getZ() - targetZ;
        return Math.sqrt(Math.abs(dX * dX + dZ * dZ));
    }

    public static double distanceTo2D(Entity self, Entity entity) {
        double dX = Math.abs(self.getX() - entity.getX());
        double dZ = Math.abs(self.getZ() - entity.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(Entity self, BlockPos end) {
        double dX = Math.abs(self.getX() - end.getX());
        double dZ = Math.abs(self.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(BlockPos origin, BlockPos end) {
        double dX = Math.abs(origin.getX() - end.getX());
        double dZ = Math.abs(origin.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(double side, double side2) {
        return  Math.sqrt(side * side + side2 * side2);
    }


    public static double distanceTo3D(Entity self, double targetX, double targetY, double targetZ) {
        double dXZ = distanceTo2D(self, targetX, targetZ);
        double dY = self.getY() - targetY;
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(Entity self, Entity entity) {
        double dXZ = distanceTo2D(self, entity);
        double dY = self.getY() - entity.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(Entity self, BlockPos end) {
        double dXZ = distanceTo2D(self, end);
        double dY = self.getY() - end.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(BlockPos origin, BlockPos end) {
        double dXZ = distanceTo2D(origin, end);
        double dY = origin.getY() - end.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static void removeBodyOfWater(BlockPos start, Level level) {
        Set<BlockPos> waterPositions = new HashSet<>();
        int recursion = 0;
        removeBodyOWater(waterPositions, start, recursion, level);

        waterPositions.forEach((pos) -> level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0));
    }

    public static void removeBodyOWater(Set<BlockPos> storage, BlockPos position, int recursion, Level level) {
        if(!level.isWaterAt(position) || recursion == 250) {
            return;
        }
        if(!storage.contains(position)) {
            storage.add(position);
        } else {
            return;
        }
        removeBodyOWater(storage, position.north(), recursion + 1, level);
        removeBodyOWater(storage, position.east(), recursion + 1, level);
        removeBodyOWater(storage, position.south(), recursion + 1, level);
        removeBodyOWater(storage, position.west(), recursion + 1, level);
        removeBodyOWater(storage, position.below(), recursion + 1, level);
    }


    public static Pair<List<Item>, List<Integer>> createItems(int rarity, ArrayList<String> pools, RandomSource randomSource, boolean isExtra) {
        List<Item> items = new ArrayList<>();
        List<Item> poolItems = new ArrayList<>();

        List<Integer> amounts = new ArrayList<>();
        List<Integer> poolMins = new ArrayList<>();
        List<Integer> poolMaxes = new ArrayList<>();

        // BABTMain.LOGGER.debug("Pools {}", pools);

        int timesAdded;

        rarity = isExtra ? rarity - 1: rarity;

        for (String pool: pools) {
            Pair<List<List<Item>>, List<List<Double>>> itemPoolAndAmounts = lootMap.getOrDefault(pool, lootMap.get("Building Block"));
            for (int i = Math.max(rarity-4, 0); i < Math.max(Math.min(rarity + 1, 4), 1); i++) {

                // Add items of wanted rarity thrice, items of the rarity below twice, and items of a higher rarity once.
                if (i == rarity) {
                    timesAdded = 3;
                } else if (i == rarity - 1) {
                    timesAdded = 2;
                } else {
                    timesAdded = 1;
                }

                for (int j = 0; j < timesAdded ; j++) {
                    poolItems.addAll(itemPoolAndAmounts.getFirst().get(i));
                    List<Double> floats = itemPoolAndAmounts.getSecond().get(i);
                    for (double amount: floats) {
                        // BABTMain.LOGGER.debug("Min amount = " + (int) amount + "  Max amount = " + ((amount - Mth.floor(amount)) * 10));
                        poolMins.add((int) amount);
                        poolMaxes.add((int) (((amount - (int) amount) * 10)));
                    }
                }
            }
        }

        // BABTMain.LOGGER.debug("Pools {}", poolItems);

        int itemAmount = isExtra ? 4 + randomSource.nextInt(4) : 10 + randomSource.nextInt(5);
        for (int i = 0; i < itemAmount; i++) {
            int index = randomSource.nextInt(Math.max(0, poolItems.size()-1));
            items.add(poolItems.get(index));
            int min = poolMins.get(index);
            int max = poolMaxes.get(index);
            if (min < max) {
                amounts.add(randomSource.nextIntBetweenInclusive(min, max));
            } else {
                amounts.add(min);
            }
        }

        return Pair.of(items, amounts);
    }

    public static void btListFill(List<Item> loot, List<Integer> amounts, Container container, LootContext lootContext) {
        Random random = new Random();

        // Get possible slots to put items in (empty slots) should be all for tower chests.
        List<Integer> possibleSlots = btGetAvailableSlots(container, random);

        List<ItemStack> chestLoot = new ArrayList<>();
        for (int i = 0; i < loot.size(); i++) {
            Item item = loot.get(i);
            ItemStack itemStack;
            if (item instanceof PotionItem) {
                itemStack = getRandomPotion(lootContext.getRandom());
            } else if (item instanceof DyeItem) {
                itemStack = getRandomDye(lootContext.getRandom());
            } else {
                itemStack = new ItemStack(item);
            }

            // Allow item count to work for non-stackable items
            if (item.getMaxStackSize(itemStack) == 1) {
                for (int j = 0; j < amounts.get(i); j++) {
                    chestLoot.add(itemStack);
                }
            } else {
                itemStack.setCount(amounts.get(i));
                chestLoot.add(itemStack);
            }

        }

        btSplitItems(chestLoot, possibleSlots.size(), lootContext.getRandom());

        // BABTMain.LOGGER.debug("Container gets Items {}", chestLoot);

        for (ItemStack itemStack : chestLoot) {
            container.setItem(possibleSlots.remove(random.nextInt(possibleSlots.size())), itemStack);
            if (possibleSlots.isEmpty()) {
                break;
            }
        }

    }

    /** All below are recreations of @LootTable methods.
     * Needed Due to bukkit servers overwriting the fill method and breaking it.
     */
    public static void btFill(LootTable loot, Container container, LootContext lootContext, LootParams lootparams) {
        List<ItemStack> list = loot.getRandomItems(lootparams);
        RandomSource random = lootContext.getRandom();
        List<Integer> list1 = btGetAvailableSlots(container,  new Random());
        btShuffleAndSplitItems(list, list1.size(), random);

        for(ItemStack itemstack : list) {
            if (list1.isEmpty()) {
                LOGGER.warn("Tried to over-fill a container");
                return;
            }

            if (itemstack.isEmpty()) {
                container.setItem(list1.remove(list1.size() - 1), ItemStack.EMPTY);
            } else {
                container.setItem(list1.remove(list1.size() - 1), itemstack);
            }
        }

    }

    private static List<Integer> btGetAvailableSlots(Container container, Random random) {
        List<Integer> list = Lists.newArrayList();

        for(int i = 0; i < container.getContainerSize(); ++i) {
            if (container.getItem(i).isEmpty()) {
                list.add(i);
            }
        }

        Collections.shuffle(list, random);
        return list;
    }


    private static void btShuffleAndSplitItems(List<ItemStack> itemStackList, int listSize, RandomSource random) {
        List<ItemStack> list = Lists.newArrayList();
        Iterator<ItemStack> iterator = itemStackList.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = iterator.next();
            if (itemstack.isEmpty()) {
                iterator.remove();
            } else if (itemstack.getCount() > 1) {
                list.add(itemstack);
                iterator.remove();
            }
        }

        while (listSize - itemStackList.size() - list.size() > 0 && !list.isEmpty()) {
            ItemStack itemstack2 = list.remove(Mth.nextInt(random, 0, list.size() - 1));
            int i = Mth.nextInt(random, 1, itemstack2.getCount() / 2);
            ItemStack itemstack1 = itemstack2.split(i);
            if (itemstack2.getCount() > 1 && random.nextBoolean()) {
                list.add(itemstack2);
            } else {
                itemStackList.add(itemstack2);
            }

            if (itemstack1.getCount() > 1 && random.nextBoolean()) {
                list.add(itemstack1);
            } else {
                itemStackList.add(itemstack1);
            }
        }

        itemStackList.addAll(list);
        Collections.shuffle(itemStackList, new Random());
    }

    private static void btSplitItems(List<ItemStack> itemStackList, int listSize, RandomSource random) {
        List<ItemStack> list = Lists.newArrayList();
        Iterator<ItemStack> iterator = itemStackList.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = iterator.next();
            if (itemstack.isEmpty()) {
                iterator.remove();
            } else if (itemstack.getCount() > 2) {
                list.add(itemstack);
                iterator.remove();
            }
        }

        while (!list.isEmpty() && list.size() + itemStackList.size() < listSize - 1) {
            ItemStack itemstack2 = list.remove(Mth.nextInt(random, 0, list.size() - 1));
            int i = Mth.nextInt(random, 1, itemstack2.getCount() / 2);
            ItemStack itemstack1 = itemstack2.split(i);
            if (itemstack2.getCount() > 3) {
                list.add(itemstack2);
            } else {
                itemStackList.add(itemstack2);
            }

            if (itemstack1.getCount() > 3) {
                list.add(itemstack1);
            } else {
                itemStackList.add(itemstack1);
            }
        }

        itemStackList.addAll(list);
    }

    public static ItemStack getRandomPotion(RandomSource randomSource) {
        return PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potions.get(randomSource.nextInt(potions.size())));
    }

    public static ItemStack getRandomDye(RandomSource randomSource) {
        return new ItemStack(dyes.get(randomSource.nextInt(dyes.size())));
    }

    public static void doCommand(Entity self, String command) {
        Commands commands = self.level().getServer().getCommands();
        commands.performPrefixedCommand(self.createCommandSourceStack().withPermission(4).withSuppressedOutput(), command);
    }

    public static void doNoOutputCommand(Entity self, String command) {
        Commands commands = self.level().getServer().getCommands();
        commands.performPrefixedCommand(self.createCommandSourceStack().withPermission(4).withSuppressedOutput(), command);
    }

    public static void doNoOutputPostionedCommand(Entity self, String command, Vec3 vec) {
        Commands commands = self.level().getServer().getCommands();
        commands.performPrefixedCommand(self.createCommandSourceStack().withPermission(4).withSuppressedOutput().withPosition(vec), command);
    }

}
