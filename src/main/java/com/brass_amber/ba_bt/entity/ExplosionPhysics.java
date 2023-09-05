package com.brass_amber.ba_bt.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ExplosionPhysics extends PrimedTnt {

	public ExplosionPhysics(EntityType<? extends PrimedTnt> tnt, Level level) {
		super(tnt, level);
	}

	//TODO REWRITE THIS TO BE CREATED ONCE PER FLOOR TO MAKE 2-4 EXPLOSIONS
	
	@Override
	public void tick() {
		if(this.level().isClientSide) {
			return;
		}
		if(this.firstTick) {
			this.firstTick = false;
			return;
		}
		final Explosion expl = this.explosion();
		if(expl != null) {
			//calculates the blocks to explode
			expl.explode();
			final Vec3 vo = this.position();
			for(BlockPos pos : expl.getToBlow()) {
				BlockState state = this.level().getBlockState(pos);
				if(!state.isAir() && this.level().getBlockEntity(pos) == null && !state.getFluidState().isSource()) {
					double vx = pos.getX() - vo.x;
					double vz = pos.getZ() - vo.z;
					final Vec3 velocity = new Vec3(vx / Math.abs(vx) * 0.5D, 0.5D, vz / Math.abs(vz) * 0.5D);
					
					this.level().removeBlock(pos, true);
					
					FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
							this.level(),
							pos,
							state
					);
					fallingBlock.setInvulnerable(true);
					fallingBlock.dropItem = false;
					fallingBlock.setDeltaMovement(velocity);

					this.level().gameEvent(this, GameEvent.EXPLODE, pos);
					this.level().playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,
							1.0F, 1.0F);
				}
			}
			expl.clearToBlow();
			
			expl.finalizeExplosion(true);
			
			this.setRemoved(RemovalReason.DISCARDED);
		}
	}
	
	protected Explosion explosion() {
		return new Explosion(this.level(), null, null, null, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, false, Explosion.BlockInteraction.DESTROY);
	}
	
}
