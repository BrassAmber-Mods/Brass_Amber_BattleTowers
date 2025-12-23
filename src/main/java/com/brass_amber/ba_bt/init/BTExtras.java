package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.effect.CoreTemperatureEffect;
import com.brass_amber.ba_bt.effect.DepthDropperEffect;
import com.brass_amber.ba_bt.enchantment.CoreTemperatureEnchantment;
import com.brass_amber.ba_bt.enchantment.DepthDropperEnchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BTExtras {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, BABattleTowers.MOD_ID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, BABattleTowers.MOD_ID);

    public static final RegistryObject<Enchantment> DEPTH_DROPPER = ENCHANTMENTS.register("depth_dropper",
            () -> new DepthDropperEnchantment(Enchantment.Rarity.RARE,  EquipmentSlot.values())
    );

    public static final RegistryObject<MobEffect> DEPTH_DROPPER_EFFECT = EFFECTS.register("depth_dropper_effect",
            () -> new DepthDropperEffect(MobEffectCategory.NEUTRAL, 13565951)
    );

    public static final RegistryObject<Enchantment> CORE_TEMPERATURE = ENCHANTMENTS.register("core_temperature",
            () -> new CoreTemperatureEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.values())
    );
    public static final RegistryObject<MobEffect> CORE_TEMPERATURE_EFFECT = EFFECTS.register("core_temperature_effect",
            () -> new CoreTemperatureEffect(MobEffectCategory.HARMFUL, 10027008)
    );

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
        EFFECTS.register(eventBus);
    }
}
