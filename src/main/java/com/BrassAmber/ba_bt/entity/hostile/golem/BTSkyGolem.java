package com.BrassAmber.ba_bt.entity.hostile.golem;

import java.util.EnumSet;

import com.BrassAmber.ba_bt.entity.ai.goal.skygolem.SkyGolemFireballAttackGoal;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTSkyGolem extends BTAbstractGolem {

	public BTSkyGolem(EntityType<? extends BTSkyGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.WHITE);
		this.moveControl = new BTSkyGolem.MoveHelperController(this);
		this.setGolemName(GolemType.SKY.getDisplayName());
		this.setBossBarName();
		// Sets the experience points to drop. Reference taken from the EnderDragon.
		this.xpReward = 15345;
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 650D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 21.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	@Override
	protected void addBehaviorGoals() {
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true) {
			@Override
			public boolean canUse() {
				return !BTSkyGolem.this.isDormant() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
//				BrassAmberBattleTowers.LOGGER.info("Melee canContinueToUse():" +getTarget());
				return !BTSkyGolem.this.isDormant() && super.canContinueToUse();
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
		if (source.getEntity() instanceof BTSkyGolem) {
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
		this.goalSelector.addGoal(1, new BTSkyGolem.ChargeAttackGoal());
		this.goalSelector.addGoal(2, new BTSkyGolem.MoveRandomGoal());
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false /*mustSee*/, false /*mustReach*/));
	}

	class ChargeAttackGoal extends Goal {
		public ChargeAttackGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (BTSkyGolem.this.getTarget() != null && !BTSkyGolem.this.getMoveControl().hasWanted() && BTSkyGolem.this.random.nextInt(7) == 0) {
				return BTSkyGolem.this.distanceToSqr(BTSkyGolem.this.getTarget()) > 4.0D;
			} else {
				return false;
			}
		}

		public boolean canContinueToUse() {
			return BTSkyGolem.this.getMoveControl().hasWanted() && /*SkyGolemEntity.this.isCharging() &&*/ BTSkyGolem.this.getTarget() != null && BTSkyGolem.this.getTarget().isAlive();
		}

		public void start() {
			LivingEntity livingentity = BTSkyGolem.this.getTarget();
			Vec3 Vec3 = livingentity.getEyePosition(1.0F);
			BTSkyGolem.this.moveControl.setWantedPosition(Vec3.x, Vec3.y, Vec3.z, 1.0D);
			// SkyGolemEntity.this.setIsCharging(true);
			BTSkyGolem.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
		}

		public void stop() {
			// SkyGolemEntity.this.setIsCharging(false);
		}

		public void tick() {
			LivingEntity livingentity = BTSkyGolem.this.getTarget();
			if (BTSkyGolem.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
				BTSkyGolem.this.doHurtTarget(livingentity);
				// SkyGolemEntity.this.setIsCharging(false);
			} else {
				double d0 = BTSkyGolem.this.distanceToSqr(livingentity);
				if (d0 < 9.0D) {
					Vec3 Vec3 = livingentity.getEyePosition(1.0F);
					BTSkyGolem.this.moveControl.setWantedPosition(Vec3.x, Vec3.y, Vec3.z, 1.0D);
				}
			}

		}
	}

	class MoveRandomGoal extends Goal {
		public MoveRandomGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !BTSkyGolem.this.getMoveControl().hasWanted() && BTSkyGolem.this.random.nextInt(7) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockpos = BTSkyGolem.this.getSpawnPos();
			if (blockpos == null) {
				blockpos = BTSkyGolem.this.blockPosition();
			}

			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(BTSkyGolem.this.random.nextInt(15) - 7, BTSkyGolem.this.random.nextInt(11) - 5, BTSkyGolem.this.random.nextInt(15) - 7);
				if (BTSkyGolem.this.level.isEmptyBlock(blockpos1)) {
					BTSkyGolem.this.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
					if (BTSkyGolem.this.getTarget() == null) {
						BTSkyGolem.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}

	/*********************************************************** MoveHelperController ********************************************************/

	class MoveHelperController extends MoveControl {
		public MoveHelperController(BTSkyGolem skyGolem) {
			super(skyGolem);
		}

		public void tick() {
			if (this.operation == Operation.MOVE_TO) {
				Vec3 Vec3 = new Vec3(this.wantedX - BTSkyGolem.this.getX(), this.wantedY - BTSkyGolem.this.getY(), this.wantedZ - BTSkyGolem.this.getZ());
				double d0 = Vec3.length();
				if (d0 < BTSkyGolem.this.getBoundingBox().getSize()) {
					this.operation = Operation.WAIT;
					BTSkyGolem.this.setDeltaMovement(BTSkyGolem.this.getDeltaMovement().scale(0.5D));
				} else {
					BTSkyGolem.this.setDeltaMovement(BTSkyGolem.this.getDeltaMovement().add(Vec3.scale(this.speedModifier * 0.05D / d0)));
					if (BTSkyGolem.this.getTarget() == null) {
						Vec3 Vec31 = BTSkyGolem.this.getDeltaMovement();
						BTSkyGolem.this.setYRot(-((float) Mth.atan2(Vec31.x, Vec31.z)) * (180F / (float) Math.PI));
						BTSkyGolem.this.yBodyRot = BTSkyGolem.this.getYRot();
					} else {
						double d2 = BTSkyGolem.this.getTarget().getX() - BTSkyGolem.this.getX();
						double d1 = BTSkyGolem.this.getTarget().getZ() - BTSkyGolem.this.getZ();
						BTSkyGolem.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
						BTSkyGolem.this.yBodyRot = BTSkyGolem.this.getYRot();
					}
				}

			}
		}
	}
}