package com.brass_amber.ba_bt.entity.hostile.golem;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.ai.goal.CoreGolemFireballAttackGoal;
import com.brass_amber.ba_bt.entity.ai.goal.CoreMeleeGoal;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import static com.brass_amber.ba_bt.sound.BTMusic.CORE_GOLEM_FIGHT_MUSIC;

public class CoreGolem extends AbstractGolem {
	public static final EntityDataAccessor<Boolean> MELEE = SynchedEntityData.defineId(CoreGolem.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> FIREBALL = SynchedEntityData.defineId(CoreGolem.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> UNLEASHED = SynchedEntityData.defineId(CoreGolem.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> UNLEASHED_ANIMATION = SynchedEntityData.defineId(CoreGolem.class, EntityDataSerializers.BOOLEAN);

	public final AnimationState meleeAnimationState = new AnimationState();
	public final AnimationState fireballAnimationState = new AnimationState();
	public final AnimationState unleashedAnimationAnimationState = new AnimationState();
	public final AnimationState unleashedPoseAnimationState = new AnimationState();

	public static final int MELEE_DURATION_TICKS = 40;
	public static final int FIREBALL_DURATION_TICKS = 60;
	public static final int UNLEASHED_DURATION_TICKS = 60;

	public int meleeAnimationTimeout = 0;
	public int fireballAnimationTimeout = 0;
	public int unleashedAnimationTimeout = 0;
	public boolean resetAi = true;

	public CoreGolem(EntityType<? extends CoreGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.PURPLE);
		this.setGolemName(GolemType.CORE.getDisplayName());
		this.setBossBarName();
		this.BOSS_MUSIC = CORE_GOLEM_FIGHT_MUSIC;
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 2045;
		this.golemType = GolemType.CORE;

		// Reference for disregarding lava taken from ZombiefiedPiglin
		this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);

		// Reference for disregarding fire taken from Blaze
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
		this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);

		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	@Override
	protected void addBehaviorGoals() {
		this.goalSelector.addGoal(3, new CoreMeleeGoal(this));
		this.goalSelector.addGoal(5, new CoreGolemFireballAttackGoal(this));
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return AbstractGolem.createBattleGolemAttributes().add(Attributes.MAX_HEALTH, 500D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	private void setupAnimimationStates() {

		if (this.isMelee() && this.meleeAnimationTimeout <= 0) {
			this.meleeAnimationTimeout = MELEE_DURATION_TICKS; // Length in ticks of your animation
			this.meleeAnimationState.start(this.tickCount);
			this.setLeftHanded(this.getRandom().nextBoolean());
		} else if (this.meleeAnimationTimeout <= 0) {
			this.meleeAnimationState.stop();
		}else {
			--this.meleeAnimationTimeout;
		}

		if (this.isFireball() && this.fireballAnimationTimeout <= 0) {
			this.fireballAnimationTimeout = FIREBALL_DURATION_TICKS; // Length in ticks of your animation
			this.fireballAnimationState.start(this.tickCount);
			if (!this.isSilent()) {
				this.playSoundEventWithVariation(BTSoundEvents.ENTITY_GOLEM_CHARGE.get());
			}
		} else if (this.fireballAnimationTimeout <= 0) {
			this.fireballAnimationState.stop();
		} else {
			--this.fireballAnimationTimeout;
		}

		if (!this.isUnleashedAnimation()) {
			this.unleashedAnimationAnimationState.stop();
		}

		this.unleashedPoseAnimationState.animateWhen(this.isUnleashed() && !this.inAnimation(2), this.tickCount);

	}

	@Override
	protected void updateWalkAnimation(float p_268283_) {
		super.updateWalkAnimation(p_268283_);
	}

	public boolean inAnimation(int passState) {
		return (this.isMelee() && passState != 1) || (this.isFireball() && passState != 2) || (this.isUnleashedAnimation() && passState != 3);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.isUnleashedBasedOnHP() && !this.isUnleashed() && this.isEnraged()) {
			BABattleTowers.LOGGER.debug("wtf");
			this.playSoundEvent(BTSoundEvents.ENTITY_GOLEM_SPECIAL.get(), 0.3f); // LOUD AF (Still? I adjusted the volume)
			this.setUnleashed(true);
			this.setUnleashedAnimation(true);

			this.goalSelector.setNewGoalRate(0);
			this.unleashedAnimationTimeout = UNLEASHED_DURATION_TICKS;
			if (this.level().isClientSide()) {
				this.unleashedAnimationAnimationState.start(this.tickCount);
			}
		}

		// BABattleTowers.LOGGER.debug(" {} {} {}", this.isUnleashed(), !this.isUnleashedAnimation(), this.inAnimation(0));
		if (this.isUnleashed() && !this.isUnleashedAnimation() && this.resetAi) {
			BABattleTowers.LOGGER.debug("fix ai");
			this.resetAi = false;
			this.goalSelector.setNewGoalRate(3);
		}

		if (this.level().isClientSide()) {
			this.setupAnimimationStates();
		}

		if (this.unleashedAnimationTimeout == 0) {
			this.setUnleashedAnimation(false);
		} else if (this.unleashedAnimationTimeout > 0) {
			this.unleashedAnimationTimeout--;
		}
	}

	public boolean isEnragedBasedOnHP() {
		return this.getHealth() / this.getMaxHealth() < 0.75F;
	}

	public boolean isUnleashedBasedOnHP() {
		return this.getHealth() / this.getMaxHealth() < 0.5F;
	}

	public boolean isMelee() {
		return this.entityData.get(MELEE);
	}

	public boolean isFireball() {
		return this.entityData.get(FIREBALL);
	}

	public boolean isUnleashed() {
		return this.entityData.get(UNLEASHED);
	}

	public boolean isUnleashedAnimation() {
		return this.entityData.get(UNLEASHED_ANIMATION);
	}


	public void setMelee(boolean melee) {
		this.entityData.set(MELEE, melee);
	}

	public void setFireball(boolean fireball) {
		this.entityData.set(FIREBALL, fireball);
	}

	public void setUnleashed(boolean unleashed) {
		this.entityData.set(UNLEASHED, unleashed);
	}

	public void setUnleashedAnimation(boolean unleashedAnimation) {
		this.entityData.set(UNLEASHED_ANIMATION, unleashedAnimation);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(MELEE, false);
		this.entityData.define(FIREBALL, false);
		this.entityData.define(UNLEASHED, false);
		this.entityData.define(UNLEASHED_ANIMATION, false);
	}
}