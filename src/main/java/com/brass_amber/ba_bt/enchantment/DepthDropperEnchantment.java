package com.brass_amber.ba_bt.enchantment;

import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DepthDropperEnchantment extends Enchantment {
    public DepthDropperEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, EnchantmentCategory.BREAKABLE, equipmentSlots);
    }

    public int getMinCost(int i) {
        return i * 10;
    }

    public int getMaxCost(int i) {
        return this.getMinCost(i) + 15;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.is(BTTags.Items.DEPTH_DROPPER_CAN_ENCHANT) ;
    }

}
