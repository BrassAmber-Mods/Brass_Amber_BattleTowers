package com.BrassAmber.ba_bt.item;

import java.util.function.Supplier;

import com.BrassAmber.ba_bt.init.BTItems;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Reference from {@link ArmorMaterial}
 */
public enum BTArmorMaterial implements IArmorMaterial {
	SILVER("silver", 11, new int[] { 2, 5, 6, 2 }, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> {
		return Ingredient.of(BTItems.SILVER_INGOT);
	});

	private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyValue<Ingredient> repairIngredient;

	private BTArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.slotProtections = slotProtections;
		this.enchantmentValue = enchantmentValue;
		this.sound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyValue<>(repairIngredient);
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType equipmentSlotType) {
		return HEALTH_PER_SLOT[equipmentSlotType.getIndex()] * this.durabilityMultiplier;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType equipmentSlotType) {
		return this.slotProtections[equipmentSlotType.getIndex()];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.sound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return BrassAmberBattleTowers.MOD_ID + ":" + this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}