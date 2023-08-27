package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.effect.DepthDropperEffect;
import com.BrassAmber.ba_bt.enchantment.DepthDropperEnchantment;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BTExtras {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, BrassAmberBattleTowers.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BrassAmberBattleTowers.MODID);

    public static final RegistryObject<Enchantment> DEPTH_DROPPER = ENCHANTMENTS.register("depth_dropper",
            () -> new DepthDropperEnchantment(Enchantment.Rarity.RARE,  EquipmentSlot.values())
    );

    public static final RegistryObject<MobEffect> DEPTH_DROPPER_EFFECT = EFFECTS.register("depth_dropper_effect",
            () -> new DepthDropperEffect(MobEffectCategory.NEUTRAL, 13565951)
    );

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
        EFFECTS.register(eventBus);
    }
}
