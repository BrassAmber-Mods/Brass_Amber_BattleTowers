package com.BrassAmber.ba_bt.entity.hostile.golem;

import java.util.EnumSet;

import com.BrassAmber.ba_bt.entity.ai.goal.GolemFireballAttackGoal;
import com.BrassAmber.ba_bt.entity.ai.goal.skygolem.SkyGolemFireballAttackGoal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class SkyGolemEntity extends BTGolemEntityAbstract {

	public SkyGolemEntity(EntityType<? extends SkyGolemEntity> type, World worldIn) {
		super(type, worldIn, BossInfo.Color.WHITE);
		this.moveControl = new SkyGolemEntity.MoveHelperController(this);
	}
	
	@Override
	protected GolemFireballAttackGoal createFireballAttackGoal() {
		return new SkyGolemFireballAttackGoal(this);
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
		if (source.getEntity() instanceof SkyGolemEntity) {
			// Can't hurt herself.
			return false;
		}
		return super.hurt(source, damage);
	}

	/*********************************************************** AI Goals ********************************************************/

	/**
	 * Register all goals that a Golem should have. Called in the {@link MobEntity} constructor.
	 */
	@Override
	protected void registerGoals() {
		this.addGolemGoal(0, new SwimGoal(this));
		// TODO Doesn't charge yet and stuff.
		this.addGolemGoal(1, new SkyGolemEntity.ChargeAttackGoal());
		this.addGolemGoal(2, new SkyGolemEntity.MoveRandomGoal());
		this.addGolemGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.addGolemTargetGoal(1, new HurtByTargetGoal(this));
		this.addGolemTargetGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false /*mustSee*/, false /*mustReach*/));
	}

	class ChargeAttackGoal extends Goal {
		public ChargeAttackGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (SkyGolemEntity.this.getTarget() != null && !SkyGolemEntity.this.getMoveControl().hasWanted() && SkyGolemEntity.this.random.nextInt(7) == 0) {
				return SkyGolemEntity.this.distanceToSqr(SkyGolemEntity.this.getTarget()) > 4.0D;
			} else {
				return false;
			}
		}

		public boolean canContinueToUse() {
			return SkyGolemEntity.this.getMoveControl().hasWanted() && /*SkyGolemEntity.this.isCharging() &&*/ SkyGolemEntity.this.getTarget() != null && SkyGolemEntity.this.getTarget().isAlive();
		}

		public void start() {
			LivingEntity livingentity = SkyGolemEntity.this.getTarget();
			Vector3d vector3d = livingentity.getEyePosition(1.0F);
			SkyGolemEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
			// SkyGolemEntity.this.setIsCharging(true);
			SkyGolemEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
		}

		public void stop() {
			// SkyGolemEntity.this.setIsCharging(false);
		}

		public void tick() {
			LivingEntity livingentity = SkyGolemEntity.this.getTarget();
			if (SkyGolemEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
				SkyGolemEntity.this.doHurtTarget(livingentity);
				// SkyGolemEntity.this.setIsCharging(false);
			} else {
				double d0 = SkyGolemEntity.this.distanceToSqr(livingentity);
				if (d0 < 9.0D) {
					Vector3d vector3d = livingentity.getEyePosition(1.0F);
					SkyGolemEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
				}
			}

		}
	}

	class MoveRandomGoal extends Goal {
		public MoveRandomGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !SkyGolemEntity.this.getMoveControl().hasWanted() && SkyGolemEntity.this.random.nextInt(7) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockpos = SkyGolemEntity.this.getSpawnPos();
			if (blockpos == null) {
				blockpos = SkyGolemEntity.this.blockPosition();
			}

			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(SkyGolemEntity.this.random.nextInt(15) - 7, SkyGolemEntity.this.random.nextInt(11) - 5, SkyGolemEntity.this.random.nextInt(15) - 7);
				if (SkyGolemEntity.this.level.isEmptyBlock(blockpos1)) {
					SkyGolemEntity.this.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
					if (SkyGolemEntity.this.getTarget() == null) {
						SkyGolemEntity.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}

	/*********************************************************** MoveHelperController ********************************************************/

	class MoveHelperController extends MovementController {
		public MoveHelperController(SkyGolemEntity skyGolemEntity) {
			super(skyGolemEntity);
		}

		public void tick() {
			if (this.operation == MovementController.Action.MOVE_TO) {
				Vector3d vector3d = new Vector3d(this.wantedX - SkyGolemEntity.this.getX(), this.wantedY - SkyGolemEntity.this.getY(), this.wantedZ - SkyGolemEntity.this.getZ());
				double d0 = vector3d.length();
				if (d0 < SkyGolemEntity.this.getBoundingBox().getSize()) {
					this.operation = MovementController.Action.WAIT;
					SkyGolemEntity.this.setDeltaMovement(SkyGolemEntity.this.getDeltaMovement().scale(0.5D));
				} else {
					SkyGolemEntity.this.setDeltaMovement(SkyGolemEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
					if (SkyGolemEntity.this.getTarget() == null) {
						Vector3d vector3d1 = SkyGolemEntity.this.getDeltaMovement();
						SkyGolemEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
						SkyGolemEntity.this.yBodyRot = SkyGolemEntity.this.yRot;
					} else {
						double d2 = SkyGolemEntity.this.getTarget().getX() - SkyGolemEntity.this.getX();
						double d1 = SkyGolemEntity.this.getTarget().getZ() - SkyGolemEntity.this.getZ();
						SkyGolemEntity.this.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
						SkyGolemEntity.this.yBodyRot = SkyGolemEntity.this.yRot;
					}
				}

			}
		}
	}
}