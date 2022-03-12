package com.BrassAmber.ba_bt.entity.water;

import java.util.EnumSet;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityPaladin extends Guardian {

	public EntityPaladin(EntityType<? extends EntityPaladin> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.ATTACK_DAMAGE, 12.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.125D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.MAX_HEALTH, 15.0D);
	}
	
	@Override
	public void push(Entity pusher) {
		super.push(pusher);
		
		if(pusher instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) pusher;
			if(living.isBlocking()) {
				
			}
		}
	}

	@Override
	protected void registerGoals() {
		MoveTowardsRestrictionGoal movetowardsrestrictiongoal = new MoveTowardsRestrictionGoal(this, 1.0D);
		this.randomStrollGoal = new RandomStrollGoal(this, 1.0D, 80);
		this.goalSelector.addGoal(5, movetowardsrestrictiongoal);
		this.goalSelector.addGoal(7, this.randomStrollGoal);
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0F, 0.01F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		this.randomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		movetowardsrestrictiongoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new Guardian.GuardianAttackSelector(this)));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
