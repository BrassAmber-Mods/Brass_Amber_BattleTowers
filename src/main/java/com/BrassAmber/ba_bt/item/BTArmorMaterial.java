package com.BrassAmber.ba_bt.item;

import java.util.function.Supplier;

import com.BrassAmber.ba_bt.init.BTItems;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * Reference from {@link ArmorMaterials}
 */
public enum BTArmorMaterial implements ArmorMaterial {
	PLATINUM("platinum", 11, new int[] { 2, 5, 6, 2 }, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> {
		return Ingredient.of(BTItems.PLATINUM_INGOT.get());
	});

	private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	private BTArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.slotProtections = slotProtections;
		this.enchantmentValue = enchantmentValue;
		this.sound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}


	public int getDurabilityForSlot(EquipmentSlot equipmentSlotType) {
		return HEALTH_PER_SLOT[equipmentSlotType.getIndex()] * this.durabilityMultiplier;
	}


	public int getDefenseForSlot(EquipmentSlot equipmentSlotType) {
		return this.slotProtections[equipmentSlotType.getIndex()];
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type p_266807_) {
		return 0;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type p_267168_) {
		return 0;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public @NotNull SoundEvent getEquipSound() {
		return this.sound;
	}

	@Override
	public @NotNull Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public @NotNull String getName() {
		return BrassAmberBattleTowers.MODID + ":" + this.name;
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