package com.BrassAmber.ba_bt.entity.hostile.golem;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanGolemHP;

public class BTOceanGolem extends BTAbstractGolem {

	private static final EntityDataAccessor<Boolean> DATA_ID_MOVING = SynchedEntityData.defineId(BTOceanGolem.class, EntityDataSerializers.BOOLEAN);
	public BTOceanGolem(EntityType<? extends BTOceanGolem> type, Level levelIn) {
		super(type, levelIn, BossEvent.BossBarColor.YELLOW);
		this.moveControl = new OceanGolemMoveGoal(this);
		this.setGolemName(GolemType.OCEAN.getDisplayName());
		this.setBossBarName();
	}

	public static AttributeSupplier.Builder createBattleGolemAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, oceanGolemHP.get()).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 2.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 60.0D).add(Attributes.ARMOR, 4);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_ID_MOVING, false);
	}

	static class OceanGolemMoveGoal extends MoveControl {
		private final BTOceanGolem golem;

		public OceanGolemMoveGoal(BTOceanGolem oceanGolem) {
			super(oceanGolem);
			this.golem = oceanGolem;
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO && !this.golem.getNavigation().isDone()) {
				Vec3 vec3 = new Vec3(this.wantedX - this.golem.getX(), this.wantedY - this.golem.getY(), this.wantedZ - this.golem.getZ());
				double d0 = vec3.length();
				double d1 = vec3.x / d0;
				double d2 = vec3.y / d0;
				double d3 = vec3.z / d0;
				float f = (float)(Mth.atan2(vec3.z, vec3.x) * (double)(180F / (float)Math.PI)) - 90.0F;
				this.golem.setYRot(this.rotlerp(this.golem.getYRot(), f, 90.0F));
				this.golem.yBodyRot = this.golem.getYRot();
				float f1 = (float)(this.speedModifier * this.golem.getAttributeValue(Attributes.MOVEMENT_SPEED));
				float f2 = Mth.lerp(0.125F, this.golem.getSpeed(), f1);
				this.golem.setSpeed(f2);
				double d4 = Math.sin((double)(this.golem.tickCount + this.golem.getId()) * 0.5D) * 0.05D;
				double d5 = Math.cos((double)(this.golem.getYRot() * ((float)Math.PI / 180F)));
				double d6 = Math.sin((double)(this.golem.getYRot() * ((float)Math.PI / 180F)));
				double d7 = Math.sin((double)(this.golem.tickCount + this.golem.getId()) * 0.75D) * 0.05D;
				this.golem.setDeltaMovement(this.golem.getDeltaMovement().add(d4 * d5, d7 * (d6 + d5) * 0.25D + (double)f2 * d2 * 0.1D, d4 * d6));
				LookControl lookcontrol = this.golem.getLookControl();
				double d8 = this.golem.getX() + d1 * 2.0D;
				double d9 = this.golem.getEyeY() + d2 / d0;
				double d10 = this.golem.getZ() + d3 * 2.0D;
				double d11 = lookcontrol.getWantedX();
				double d12 = lookcontrol.getWantedY();
				double d13 = lookcontrol.getWantedZ();
				if (!lookcontrol.isLookingAtTarget()) {
					d11 = d8;
					d12 = d9;
					d13 = d10;
				}

				this.golem.getLookControl().setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);
				this.golem.setMoving(true);
			} else {
				this.golem.setSpeed(0.0F);
				this.golem.setMoving(false);
			}
		}
	}

	public boolean isMoving() {
		return this.entityData.get(DATA_ID_MOVING);
	}

	void setMoving(boolean p_32862_) {
		this.entityData.set(DATA_ID_MOVING, p_32862_);
	}

}