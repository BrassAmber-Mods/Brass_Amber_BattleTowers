package com.brass_amber.ba_bt.entity.hostile.golem;

import java.util.EnumSet;

import com.brass_amber.ba_bt.entity.ai.goal.SkyGolemFireballAttackGoal;

import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.brass_amber.ba_bt.sound.BTMusic.SKY_GOLEM_FIGHT_MUSIC;

public class SkyGolem extends AbstractGolem {
	protected static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(SkyGolem.class, EntityDataSerializers.BOOLEAN);

	public SkyGolem(EntityType<? extends SkyGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.WHITE);
		this.moveControl = new SkyGolem.MoveHelperController(this);
		this.setGolemName(TowerType.SKY.getDisplayName());
		this.setBossBarName();
		this.BOSS_MUSIC = SKY_GOLEM_FIGHT_MUSIC;
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 15345;
		this.towerType = TowerType.SKY;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return AbstractGolem.createBattleGolemAttributes().add(Attributes.MAX_HEALTH, 650D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 21.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	@Override
	protected void addBehaviorGoals() {
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true) {
			@Override
			public boolean canUse() {
				return !SkyGolem.this.isDormant() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
//				BrassAmberBattleTowers.LOGGER.debug("Melee canContinueToUse():" +getTarget());
				return !SkyGolem.this.isDormant() && super.canContinueToUse();
			}
		});

		this.goalSelector.addGoal(6, new SkyGolemFireballAttackGoal(this));
	}

	/*********************************************************** Ticks ********************************************************/

	@Override
	public void tick() {
		this.noPhysics = true;
		super.tick();
		this.noPhysics = false;
		this.setNoGravity(true);
	}


	/*********************************************************** Hurt / Die ********************************************************/

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (source.getEntity() instanceof SkyGolem) {
			// Can't hurt herself.
			return false;
		}
		return super.hurt(source, damage);
	}

	/*********************************************************** AI Goals ********************************************************/

	/**
	 * Register all goals that a Golem should have. Called in the {@link net.minecraft.world.entity.Mob} constructor.
	 */
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SkyGolem.ChargeAttackGoal());
		this.goalSelector.addGoal(2, new SkyGolem.MoveRandomGoal());
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false /*mustSee*/, false /*mustReach*/));
	}

	class ChargeAttackGoal extends Goal {
		public ChargeAttackGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (SkyGolem.this.getTarget() != null && !SkyGolem.this.getMoveControl().hasWanted() && SkyGolem.this.random.nextInt(7) == 0) {
				return SkyGolem.this.distanceToSqr(SkyGolem.this.getTarget()) > 4.0D;
			} else {
				return false;
			}
		}

		public boolean canContinueToUse() {
			return SkyGolem.this.getMoveControl().hasWanted() && /*SkyGolemEntity.this.isCharging() &&*/ SkyGolem.this.getTarget() != null && SkyGolem.this.getTarget().isAlive();
		}

		public void start() {
			LivingEntity livingentity = SkyGolem.this.getTarget();
			Vec3 Vec3 = livingentity.getEyePosition(1.0F);
			SkyGolem.this.moveControl.setWantedPosition(Vec3.x, Vec3.y, Vec3.z, 1.0D);
			// SkyGolemEntity.this.setIsCharging(true);
			SkyGolem.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
		}

		public void stop() {
			// SkyGolemEntity.this.setIsCharging(false);
		}

		public void tick() {
			LivingEntity livingentity = SkyGolem.this.getTarget();
			if (SkyGolem.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
				SkyGolem.this.doHurtTarget(livingentity);
				// SkyGolemEntity.this.setIsCharging(false);
			} else {
				double d0 = SkyGolem.this.distanceToSqr(livingentity);
				if (d0 < 9.0D) {
					Vec3 Vec3 = livingentity.getEyePosition(1.0F);
					SkyGolem.this.moveControl.setWantedPosition(Vec3.x, Vec3.y, Vec3.z, 1.0D);
				}
			}

		}
	}

	class MoveRandomGoal extends Goal {
		public MoveRandomGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !SkyGolem.this.getMoveControl().hasWanted() && SkyGolem.this.random.nextInt(7) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockpos = SkyGolem.this.getSpawnPos();
			if (blockpos == null) {
				blockpos = SkyGolem.this.blockPosition();
			}

			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(SkyGolem.this.random.nextInt(15) - 7, SkyGolem.this.random.nextInt(11) - 5, SkyGolem.this.random.nextInt(15) - 7);
				if (SkyGolem.this.level().isEmptyBlock(blockpos1)) {
					SkyGolem.this.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
					if (SkyGolem.this.getTarget() == null) {
						SkyGolem.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}

	/*********************************************************** MoveHelperController ********************************************************/

	class MoveHelperController extends MoveControl {
		public MoveHelperController(SkyGolem skyGolem) {
			super(skyGolem);
		}

		public void tick() {
			if (this.operation == Operation.MOVE_TO) {
				Vec3 Vec3 = new Vec3(this.wantedX - SkyGolem.this.getX(), this.wantedY - SkyGolem.this.getY(), this.wantedZ - SkyGolem.this.getZ());
				double d0 = Vec3.length();
				if (d0 < SkyGolem.this.getBoundingBox().getSize()) {
					this.operation = Operation.WAIT;
					SkyGolem.this.setDeltaMovement(SkyGolem.this.getDeltaMovement().scale(0.5D));
				} else {
					SkyGolem.this.setDeltaMovement(SkyGolem.this.getDeltaMovement().add(Vec3.scale(this.speedModifier * 0.05D / d0)));
					if (SkyGolem.this.getTarget() == null) {
						Vec3 Vec31 = SkyGolem.this.getDeltaMovement();
						SkyGolem.this.setYRot(-((float) Mth.atan2(Vec31.x, Vec31.z)) * (180F / (float) Math.PI));
						SkyGolem.this.yBodyRot = SkyGolem.this.getYRot();
					} else {
						double d2 = SkyGolem.this.getTarget().getX() - SkyGolem.this.getX();
						double d1 = SkyGolem.this.getTarget().getZ() - SkyGolem.this.getZ();
						SkyGolem.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
						SkyGolem.this.yBodyRot = SkyGolem.this.getYRot();
					}
				}

			}
		}
	}

	public void setCharging(boolean setCharging) {
		this.entityData.set(DATA_IS_CHARGING, setCharging);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_IS_CHARGING, false);
	}
}