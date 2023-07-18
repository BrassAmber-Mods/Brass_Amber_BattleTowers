package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemLeapGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTOceanGolem extends BTAbstractGolem {

	private int drowned;

	public BTOceanGolem(EntityType<? extends BTOceanGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.YELLOW);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.setGolemName(GolemType.OCEAN.getDisplayName());
		this.setBossBarName();
		this.BOSS_MUSIC = BTSoundEvents.OCEAN_GOLEM_FIGHT_MUSIC;
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.moveControl = new SmoothSwimmingMoveControl(this, 90, 90, .08f, .5f, false);
		this.lookControl = new SmoothSwimmingLookControl(this, 90);
		this.xpReward = 910;
		this.drowned = 0;
		this.golemType = GolemType.OCEAN;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return BTAbstractGolem.createBattleGolemAttributes().add(Attributes.MAX_HEALTH, oceanGolemHP.get()).add(Attributes.MOVEMENT_SPEED, 1D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
		return new WaterBoundPathNavigation(this, level);
	}

	@Override
	protected void goDownInWater() {
	}

	@Override
	protected void addBehaviorGoals() {
		super.addBehaviorGoals();
	}

	protected float getWaterSlowDown() {
		return 0.0F;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getTarget() != null && !this.level().isClientSide()) {
			LivingEntity target = this.getTarget();
			if (distanceTo(target) > 16 && this.random.nextInt(500) == 1) {
				this.playRoarSound();
				target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,60, 1), target);
				this.playRoarSound();
			}
		}
	}

	public int getAllowedTowerRange() {
		return 56;
	}


	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("drowned", this.drowned);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.drowned = compound.getInt("drowned");
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (!this.level().isClientSide() && this.drowned < 4) {
			if (this.getHealth() < this.getMaxHealth() * .7 && this.drowned == 0) {
				this.spawnDrowned((ServerLevel) this.level());
				this.drowned = 1;
			} else if (this.getHealth() < this.getMaxHealth() * .5 && this.drowned == 1) {
				this.spawnDrowned((ServerLevel) this.level());
				this.drowned = 2;
			} else if (this.getHealth() < this.getMaxHealth() * .3 && this.drowned == 2) {
				this.spawnDrowned((ServerLevel) this.level());
				this.drowned = 3;
			}
		}
		return super.hurt(source, damage);
	}

	@Override
	public void onAboveBubbleCol(boolean p_20313_) {}

	@Override
	public void onInsideBubbleColumn(boolean p_20322_) {
		this.resetFallDistance();
	}
	

	public void spawnDrowned(ServerLevel level) {
		@SuppressWarnings("ConstantConditions") List<Entity> drownedList = List.of(EntityType.DROWNED.create(level),EntityType.DROWNED.create(level),EntityType.DROWNED.create(level),EntityType.DROWNED.create(level));
		int x = this.getBlockX() - 1;
		int z = this.getBlockZ() - 1;
		for (Entity drowned : drownedList) {
			if (drowned instanceof Mob mob) {
				mob.setPos(x, this.getY(), z);
				mob.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.TRIGGERED, null, null);
				level.addFreshEntity(drowned);
				// BrassAmberBattleTowers.LOGGER.info("Success");
				if (x < this.getBlockX()) {
					x += 2;
				} else if (z < this.getBlockZ()) {
					z += 2;
				}
			}
		}
	}


	protected void playRoarSound() {
		this.playSoundEvent(SoundEvents.ENDER_DRAGON_GROWL, 1.6F);
	}

}