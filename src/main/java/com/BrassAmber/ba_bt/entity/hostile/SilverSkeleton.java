package com.BrassAmber.ba_bt.entity.hostile;

import com.BrassAmber.ba_bt.entity.ai.goal.DualMeleeAttackGoal;
import com.BrassAmber.ba_bt.init.BTItems;

import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SilverSkeleton extends Skeleton {
	// We don't need this if we decide the animation is enough.
	private final RangedBowAttackGoal<SilverSkeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
	private final DualMeleeAttackGoal doubleMeleeGoal = new DualMeleeAttackGoal(this, 1.2D, false) {
		public void stop() {
			super.stop();
			SilverSkeleton.this.setAggressive(false);
		}

		public void start() {
			super.start();
			SilverSkeleton.this.setAggressive(true);
		}
	};

	public SilverSkeleton(EntityType<? extends Skeleton> entityType, Level levelIn) {
		super(entityType, levelIn);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		// Give swords.
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BTItems.SILVER_SWORD));
		this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(BTItems.SILVER_SWORD));
		// Give armor.
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BTItems.SILVER_HELMET));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(BTItems.SILVER_CHESTPLATE));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(BTItems.SILVER_LEGGINGS));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(BTItems.SILVER_BOOTS));
	}

	@Override
	public void reassessWeaponGoal() {
		if (this.bowGoal != null || this.doubleMeleeGoal != null) {
			if (this.level != null && !this.level.isClientSide) {
				this.goalSelector.removeGoal(this.doubleMeleeGoal);
				this.goalSelector.removeGoal(this.bowGoal);
				ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
				if (itemstack.getItem() == Items.BOW) {
					int minAttackInterval = 20;
					if (this.level.getDifficulty() != Difficulty.HARD) {
						minAttackInterval = 40;
					}
					this.bowGoal.setMinAttackInterval(minAttackInterval);
					this.goalSelector.addGoal(4, this.bowGoal);
				} else {
					this.goalSelector.addGoal(4, this.doubleMeleeGoal);
				}
			}
		}
	}
}