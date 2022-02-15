package com.BrassAmber.ba_bt.entity.hostile;

import com.BrassAmber.ba_bt.entity.ai.goal.DualMeleeAttackGoal;
import com.BrassAmber.ba_bt.init.BTItems;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class SilverSkeletonEntity extends SkeletonEntity {
	// We don't need this if we decide the animation is enough.
	private final RangedBowAttackGoal<SilverSkeletonEntity> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
	private final DualMeleeAttackGoal doubleMeleeGoal = new DualMeleeAttackGoal(this, 1.2D, false) {
		public void stop() {
			super.stop();
			SilverSkeletonEntity.this.setAggressive(false);
		}

		public void start() {
			super.start();
			SilverSkeletonEntity.this.setAggressive(true);
		}
	};

	public SilverSkeletonEntity(EntityType<? extends SkeletonEntity> entityType, World worldIn) {
		super(entityType, worldIn);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		// Give swords.
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(BTItems.SILVER_SWORD));
		this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(BTItems.SILVER_SWORD));
		// Give armor.
		this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(BTItems.SILVER_HELMET));
		this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(BTItems.SILVER_CHESTPLATE));
		this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(BTItems.SILVER_LEGGINGS));
		this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(BTItems.SILVER_BOOTS));
	}

	@Override
	public void reassessWeaponGoal() {
		if (this.bowGoal != null || this.doubleMeleeGoal != null) {
			if (this.level != null && !this.level.isClientSide) {
				this.goalSelector.removeGoal(this.doubleMeleeGoal);
				this.goalSelector.removeGoal(this.bowGoal);
				ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem));
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