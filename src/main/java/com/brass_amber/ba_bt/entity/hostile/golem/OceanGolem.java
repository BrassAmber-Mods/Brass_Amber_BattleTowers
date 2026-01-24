package com.brass_amber.ba_bt.entity.hostile.golem;

import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.brass_amber.ba_bt.sound.BTMusic.OCEAN_GOLEM_FIGHT_MUSIC;

public class OceanGolem extends AbstractGolem {

	private int drowned;

	public OceanGolem(EntityType<? extends OceanGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.YELLOW);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.setGolemName(TowerType.OCEAN.getDisplayName());
		this.setBossBarName();
		this.BOSS_MUSIC = OCEAN_GOLEM_FIGHT_MUSIC;
		this.moveControl = new SmoothSwimmingMoveControl(this, 90, 90, .08f, .5f, false);
		this.lookControl = new SmoothSwimmingLookControl(this, 90);
		this.xpReward = 910;
		this.drowned = 0;
		this.towerType = TowerType.OCEAN;
		this.allowedTowerRange = 64;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return AbstractGolem.createBattleGolemAttributes().add(Attributes.MAX_HEALTH, 250).add(Attributes.MOVEMENT_SPEED, 1D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 12.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
		return new WaterBoundPathNavigation(this, level);
	}

	@Override
	protected void addBehaviorGoals() {
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.2D, true) {
			@Override
			public boolean canUse() {
				return !OceanGolem.this.isDormant() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return !OceanGolem.this.isDormant() && super.canContinueToUse();
			}
		});
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
	public void onAboveBubbleCol(boolean pDownwards) {
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.x, vec3.y, vec3.z);
	}

	@Override
	public void onInsideBubbleColumn(boolean pDownwards) {
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.x, vec3.y, vec3.z);
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
				// BrassAmberBattleTowers.LOGGER.debug("Success");
				if (x < this.getBlockX()) {
					x += 2;
				} else if (z < this.getBlockZ()) {
					z += 2;
				}
			}
		}
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	protected void playRoarSound() {
		this.playSoundEvent(SoundEvents.ENDER_DRAGON_GROWL, 1.6F);
	}

	public boolean isPushedByFluid() {
		return false;
	}

}