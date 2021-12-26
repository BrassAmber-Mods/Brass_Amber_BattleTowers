package com.BrassAmber.ba_bt.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionPhysicsEntity extends TNTEntity {

	public ExplosionPhysicsEntity(EntityType<? extends TNTEntity> p_i50216_1_, World p_i50216_2_) {
		super(p_i50216_1_, p_i50216_2_);
	}
	
	@Override
	public void tick() {
		if(this.firstTick) {
			this.firstTick = false;
			return;
		}
		final Explosion expl = this.explosion();
		if(expl != null) {
			//calculates the blocks to explode
			expl.explode();
			final Vector3d vo = this.position();
			List<FallingBlockEntity> physicObjects = new ArrayList<>();
			for(BlockPos pos : expl.getToBlow()) {
				BlockState state = this.level.getBlockState(pos);
				if(!state.getBlock().isAir(state, this.level, pos) && this.level.getBlockEntity(pos) == null && !state.getFluidState().isSource()) {
					double vx = pos.getX() - vo.x;
					double vz = pos.getZ() - vo.z;
					final Vector3d velocity = new Vector3d(vx / Math.abs(vx) * 0.5D, 0.5D, vz / Math.abs(vz) * 0.5D);
					
					this.level.removeBlock(pos, true);
					
					FallingBlockEntity fallingBlock = new FallingBlockEntity(
							this.level, 
							pos.getX() + 0.5F, 
							pos.getY(), 
							pos.getZ() + 0.5F,
							state
					) {
						@Override
						public void remove() {
							//Dirty workaround to avoid the sudden disappearance of the entity
							if(this.tickCount <= 5 && !this.level.isClientSide && this.tickCount < 60) {
								return;
							}
							super.remove();
						}
					};
					fallingBlock.setInvulnerable(true);
					fallingBlock.dropItem = false;
					fallingBlock.setDeltaMovement(velocity);
					
					physicObjects.add(fallingBlock);
				}
			}
			expl.clearToBlow();
			
			physicObjects.forEach((fb) -> 
				this.level.addFreshEntity(fb)
			);
			
			expl.finalizeExplosion(true);
			
			this.remove();
		}
	}
	
	@Override
	protected void explode() {
		//NOPE
	}
	
	protected Explosion explosion() {
		Explosion explosion = new Explosion(this.level, null, null, null, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, false, Explosion.Mode.BREAK);
		return explosion;
	}
	
}
