package com.brass_amber.ba_bt.entity.hostile;

import com.brass_amber.ba_bt.entity.ai.goal.DualMeleeAttackGoal;
import com.brass_amber.ba_bt.init.BTItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class PlatinumSkeleton extends Skeleton {
	// We don't need this if we decide the animation is enough.
	private final RangedBowAttackGoal<PlatinumSkeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
	private final DualMeleeAttackGoal doubleMeleeGoal = new DualMeleeAttackGoal(this, 1.2D, false) {
		public void stop() {
			super.stop();
			PlatinumSkeleton.this.setAggressive(false);
		}

		public void start() {
			super.start();
			PlatinumSkeleton.this.setAggressive(true);
		}
	};

	public PlatinumSkeleton(EntityType<? extends Skeleton> entityType, Level levelIn) {
		super(entityType, levelIn);
	}

	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		// Give swords.
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BTItems.PLATINUM_SWORD.get()));
		this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(BTItems.PLATINUM_SWORD.get()));
		// Give armor.
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BTItems.PLATINUM_HELMET.get()));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(BTItems.PLATINUM_CHESTPLATE.get()));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(BTItems.PLATINUM_LEGGINGS.get()));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(BTItems.PLATINUM_BOOTS.get()));
	}

	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
										MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag) {
		groupData = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, groupData, compoundTag);
		this.populateDefaultEquipmentSlots(difficultyInstance);
		return groupData;
	}

	@Override
	public void reassessWeaponGoal() {
		if (this.bowGoal != null || this.doubleMeleeGoal != null) {
			if (!this.level().isClientSide) {
				this.goalSelector.removeGoal(this.doubleMeleeGoal);
				this.goalSelector.removeGoal(this.bowGoal);
				ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
				if (itemstack.getItem() == Items.BOW) {
					int minAttackInterval = 20;
					if (this.level().getDifficulty() != Difficulty.HARD) {
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