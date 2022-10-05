package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemLeapGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.GolemStompAttackGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.oceangolem.DashAttackGoal;
import com.BrassAmber.ba_bt.init.BTExtras;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.client.particle.WaterCurrentDownParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.List;

import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTOceanGolem extends BTAbstractGolem {

	private boolean drowned;

	public BTOceanGolem(EntityType<? extends BTOceanGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.YELLOW);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 1F, 1F, true);
		this.setGolemName(GolemType.OCEAN.getDisplayName());
		this.setBossBarName();
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 910;
		this.drowned = false;
		this.golemType = GolemType.OCEAN;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, oceanGolemHP.get()).add(Attributes.MOVEMENT_SPEED, 1.7D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	public void find_golem_chest(BlockPos spawnPos) {
		checkPos(spawnPos.north(2).below(1));
		checkPos(spawnPos.east(2).below(1));
		checkPos(spawnPos.south(2).below(1));
		checkPos(spawnPos.west(2).below(1));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getTarget() != null && !this.level.isClientSide()) {
			LivingEntity target = this.getTarget();
			if (distanceTo(target) > 16 && this.random.nextInt(50) == 1) {
				this.playRoarSound();
				target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,40, 1), target);
			}
		}
	}

	public int getAllowedTowerRange() {
		return 56;
	}

	@Override
	protected void addBehaviorGoals() {
		super.addBehaviorGoals();
		this.goalSelector.addGoal(7, new DashAttackGoal(this, 10));
	}


	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("drowned", this.drowned);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		compound.getBoolean("drowned");
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (this.getHealth() < this.getMaxHealth() / 2 && !this.level.isClientSide() && !this.drowned) {
			this.spawnDrowned((ServerLevel) this.level);
			this.drowned = true;
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
		this.playSoundEvent(SoundEvents.ENDER_DRAGON_GROWL, 1.2F);
	}

}