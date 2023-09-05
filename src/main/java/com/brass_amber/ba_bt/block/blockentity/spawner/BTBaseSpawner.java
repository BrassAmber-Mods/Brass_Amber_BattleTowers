package com.brass_amber.ba_bt.block.blockentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class BTBaseSpawner extends BaseSpawner {

    public void setBtSpawnData(EntityType<?> entityToSpawn, int minDelay, int maxDelay, int spawnCount, int maxNearby, int playerRange, int spawnRange) {
        this.nextSpawnData.getEntityToSpawn().putString("id", EntityType.getKey(entityToSpawn).toString());
        this.minSpawnDelay = minDelay;
        this.maxSpawnDelay = maxDelay;
        this.spawnCount = spawnCount;
        this.maxNearbyEntities = maxNearby;
        this.requiredPlayerRange = playerRange;
        this.spawnRange = spawnRange;
        this.spawnDelay = 0;
    }

    public void serverTick(@NotNull ServerLevel p_151312_, @NotNull BlockPos p_151313_) {
        if (this.isNearPlayer(p_151312_, p_151313_)) {
            if (this.spawnDelay == -1) {
                this.delay(p_151312_, p_151313_);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {
                boolean flag = false;
                RandomSource randomsource = p_151312_.getRandom();
                SpawnData spawndata = this.getOrCreateNextSpawnData(p_151312_, randomsource, p_151313_);

                for(int i = 0; i < this.spawnCount; ++i) {
                    CompoundTag compoundtag = this.nextSpawnData.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                    if (optional.isEmpty()) {
                        this.delay(p_151312_, p_151313_);
                        return;
                    }

                    ListTag listtag = compoundtag.getList("Pos", 6);
                    int j = listtag.size();
                    double d0 = j >= 1 ? listtag.getDouble(0) : (double)p_151313_.getX() + (p_151312_.random.nextDouble() - p_151312_.random.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d1 = j >= 2 ? listtag.getDouble(1) : (double)(p_151313_.getY() + p_151312_.random.nextInt(3) - 1);
                    double d2 = j >= 3 ? listtag.getDouble(2) : (double)p_151313_.getZ() + (p_151312_.random.nextDouble() - p_151312_.random.nextDouble()) * (double)this.spawnRange + 0.5D;
                    if (p_151312_.noCollision(optional.get().getAABB(d0, d1, d2))) {
                        BlockPos blockpos = BlockPos.containing(d0, d1, d2);
                        if (spawndata.getCustomSpawnRules().isPresent()) {
                            if (!optional.get().getCategory().isFriendly() && p_151312_.getDifficulty() == Difficulty.PEACEFUL) {
                                continue;
                            }
                        } else if (!SpawnPlacements.checkSpawnRules(optional.get(), p_151312_, MobSpawnType.SPAWNER, blockpos, p_151312_.getRandom())) {
                            continue;
                        }

                        Entity entity = EntityType.loadEntityRecursive(compoundtag, p_151312_, (p_151310_) -> {
                            p_151310_.moveTo(d0, d1, d2, p_151310_.getYRot(), p_151310_.getXRot());
                            return p_151310_;
                        });
                        if (entity == null) {
                            this.delay(p_151312_, p_151313_);
                            return;
                        }

                        int k = p_151312_.getEntitiesOfClass(entity.getClass(), (new AABB((double)p_151313_.getX(), (double)p_151313_.getY(), (double)p_151313_.getZ(), (double)(p_151313_.getX() + 1), (double)(p_151313_.getY() + 1), (double)(p_151313_.getZ() + 1))).inflate((double)this.spawnRange)).size();
                        if (k >= this.maxNearbyEntities) {
                            this.delay(p_151312_, p_151313_);
                            return;
                        }

                        entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), p_151312_.random.nextFloat() * 360.0F, 0.0F);
                        if (entity instanceof Mob mob) {
                            if (spawndata.getCustomSpawnRules().isEmpty() && !mob.checkSpawnRules(p_151312_, MobSpawnType.SPAWNER) || !mob.checkSpawnObstruction(p_151312_)) {
                                continue;
                            }

                            // Forge: Patch in the spawn event for spawners so it may be fired unconditionally, instead of only when vanilla normally would trigger it.
                            var event = net.minecraftforge.event.ForgeEventFactory.onFinalizeSpawnSpawner(mob, p_151312_, p_151312_.getCurrentDifficultyAt(entity.blockPosition()), null, compoundtag, this);
                            if (event != null && spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().contains("id", 8)) {
                                ((Mob)entity).finalizeSpawn(p_151312_, event.getDifficulty(), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag());
                            }
                        }

                        if (!p_151312_.tryAddFreshEntityWithPassengers(entity)) {
                            this.delay(p_151312_, p_151313_);
                            return;
                        }

                        p_151312_.levelEvent(2004, p_151313_, 0);
                        p_151312_.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos);
                        if (entity instanceof Mob) {
                            ((Mob)entity).spawnAnim();
                        }

                        flag = true;
                    }
                }

                if (flag) {
                    this.delay(p_151312_, p_151313_);
                }

            }
        }
    }
}
