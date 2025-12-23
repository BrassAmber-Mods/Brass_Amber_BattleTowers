package com.brass_amber.ba_bt.enchantment;

import com.brass_amber.ba_bt.init.BTExtras;
import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.Map;

import static com.brass_amber.ba_bt.util.BTUtil.getEquipment;
import static com.brass_amber.ba_bt.util.BTUtil.getItemsWith;

public class CoreTemperatureEnchantment extends Enchantment {

    protected CoreTemperatureEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
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
        return itemStack.is(BTTags.Items.CORE_TEMPERATURE_CAN_ENCHANT);
    }

    @Override
    public void doPostAttack(LivingEntity livingEntity, Entity entity, int i) {
        if (entity instanceof LivingEntity living) {
            getEquipment(living).forEach(
                    (equipmentSlot, stack) ->
                            stack.hurtAndBreak(1, livingEntity,
                                    (living2) -> living2.broadcastBreakEvent(equipmentSlot))
            );
        }
    }

    @Override
    public void doPostHurt(LivingEntity livingEntity, Entity entity, int i) {
        List<Map.Entry<EquipmentSlot, ItemStack>> entries = getItemsWith(BTExtras.CORE_TEMPERATURE.get(), livingEntity);

        if (entity != null && entries != null) {
            entity.hurt(livingEntity.damageSources().thorns(livingEntity), 1f * entries.size());
            for (Map.Entry<EquipmentSlot, ItemStack> entry : entries) {
                entry.getValue().hurtAndBreak(
                        1, livingEntity, (living) -> living.broadcastBreakEvent(entry.getKey())
                );
            }

        }
    }
}
