package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.effect.DepthDropperEffect;
import com.brass_amber.ba_bt.enchantment.DepthDropperEnchantment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BTExtras {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, BrassAmberBattleTowers.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BrassAmberBattleTowers.MODID);

    public static final DeferredHolder<Enchantment, Enchantment> DEPTH_DROPPER = ENCHANTMENTS.register("depth_dropper",
            () -> new DepthDropperEnchantment(Enchantment.Rarity.RARE,  EquipmentSlot.values())
    );

    public static final DeferredHolder<MobEffect, MobEffect> DEPTH_DROPPER_EFFECT = EFFECTS.register("depth_dropper_effect",
            () -> new DepthDropperEffect(MobEffectCategory.NEUTRAL, 13565951)
    );

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
        EFFECTS.register(eventBus);
    }
}
