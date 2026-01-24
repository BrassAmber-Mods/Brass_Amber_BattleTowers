package com.brass_amber.ba_bt.item.item;

import com.brass_amber.ba_bt.init.BTExtras;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Supplier;

public class ResonanceStoneItem extends RecordItem {

    public Enchantment enchantment;
    public MobEffect effect;
    private final TowerType towerType;
    public boolean effectOn;
    private boolean initialized;

    public ResonanceStoneItem(TowerType towerType, Supplier<SoundEvent> soundSupplier, Properties properties, int length) {
        super(4, soundSupplier, properties, length);
        this.towerType = towerType;
        this.effectOn = false;
        this.initialized = false;
    }

    public boolean isEnchantable(ItemStack itemStack) {
        return itemStack.getCount() == 1;
    }

    public void addEnchantment(ItemStack stackInUse) {
        if (this.enchantment != null) {
            Map<Enchantment, Integer> map = Map.of(this.enchantment, 1);
            EnchantmentHelper.setEnchantments(map, stackInUse);
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        super.inventoryTick(itemStack, level, entity, i, b);
        if (!this.initialized) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (this.towerType) {
                case OCEAN -> {
                    this.enchantment = BTExtras.DEPTH_DROPPER.get();
                    this.effect = BTExtras.DEPTH_DROPPER_EFFECT.get();
                }
                default -> {
                    this.enchantment = null;
                    this.effect = null;
                }
            }
            this.initialized = true;
        }

        if (this.enchantment != null && EnchantmentHelper.getEnchantments(itemStack).containsKey(this.enchantment) && !level.isClientSide()) {
            if (entity instanceof LivingEntity player && this.effectOn) {
                player.forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(),80, 3), null);
            }
        }
    }
}
