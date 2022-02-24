package com.BrassAmber.ba_bt.item;

import java.util.function.Supplier;

import com.BrassAmber.ba_bt.init.BTItems;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

import javax.swing.*;

/**
 * Reference from {@link Tiers}
 */
public enum BTItemTier implements Tier {
	SILVER(1, 200, 12.0F, 1.0F, 14, () -> {
		return Ingredient.of(BTItems.SILVER_INGOT);
	});

	private final int level;
	private final int uses;
	private final float speed;
	private final float damage;
	private final int enchantmentValue;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	private BTItemTier(int miningLevel, int durability, float miningSpeed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
		this.level = miningLevel;
		this.uses = durability;
		this.speed = miningSpeed;
		this.damage = attackDamageBonus;
		this.enchantmentValue = enchantmentValue;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}

	/**
	 * Get the base durability.
	 */
	@Override
	public int getUses() {
		return this.uses;
	}

	/**
	 * Get the mining speed.
	 */
	@Override
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * Get the extra damage caused.
	 */
	@Override
	public float getAttackDamageBonus() {
		return this.damage;
	}

	/**
	 * Get the mining level.
	 */
	@Override
	public int getLevel() {
		return this.level;
	}

	/**
	 * Get the Item's enchantability.
	 */
	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	/**
	 * Get the repair ingredient.
	 */
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
}