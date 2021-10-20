package com.BrassAmber.ba_bt.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum BTItemTier implements IItemTier {
   SILVER(1, 200, 12.0F, 1.0F, 14, () -> {
      return Ingredient.of(BTItems.SILVER_INGOT);
   });

   private final int level;
   private final int uses;
   private final float speed;
   private final float damage;
   private final int enchantmentValue;
   private final LazyValue<Ingredient> repairIngredient;

   private BTItemTier(int p_i48458_3_, int p_i48458_4_, float p_i48458_5_, float p_i48458_6_, int p_i48458_7_, Supplier<Ingredient> p_i48458_8_) {
      this.level = p_i48458_3_;
      this.uses = p_i48458_4_;
      this.speed = p_i48458_5_;
      this.damage = p_i48458_6_;
      this.enchantmentValue = p_i48458_7_;
      this.repairIngredient = new LazyValue<>(p_i48458_8_);
   }

   public int getUses() {
      return this.uses;
   }

   public float getSpeed() {
      return this.speed;
   }

   public float getAttackDamageBonus() {
      return this.damage;
   }

   public int getLevel() {
      return this.level;
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }
}