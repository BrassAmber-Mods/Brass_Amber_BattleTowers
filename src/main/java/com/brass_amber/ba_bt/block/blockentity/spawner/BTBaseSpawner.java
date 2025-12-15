package com.brass_amber.ba_bt.block.blockentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class BTBaseSpawner extends BaseSpawner {
    private BlockPos towerCenter = BlockPos.ZERO;
    private int towerRadius = 6;
    public void setBtSpawnData(int minDelay, int maxDelay, int spawnCount, int maxNearby, int playerRange, int spawnRange, BlockPos towerCenter, int towerRadius) {
        this.minSpawnDelay = minDelay;
        this.maxSpawnDelay = maxDelay;
        this.spawnCount = spawnCount;
        this.maxNearbyEntities = maxNearby;
        this.requiredPlayerRange = playerRange;
        this.spawnRange = spawnRange;
        this.spawnDelay = 0;
        this.towerCenter = towerCenter;
        this.towerRadius = towerRadius;
    }

    public void serverTick(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos) {
        if (this.isNearPlayer(serverLevel, blockPos)) {
            if (this.spawnDelay == -1) {
                this.delay(serverLevel, blockPos);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {

                boolean flag = false;
                RandomSource randomsource = serverLevel.getRandom();
                SpawnData spawndata = this.getOrCreateNextSpawnData(serverLevel, randomsource, blockPos);
                // LOGGER.info("Attempting to Spawn Mob from spawner " + spawndata.getEntityToSpawn());
                for(int i = 0; i < this.spawnCount; ++i) {
                    CompoundTag compoundtag = spawndata.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                    if (optional.isEmpty()) {
                        this.delay(serverLevel, blockPos);
                        return;
                    }

                    ListTag listtag = compoundtag.getList("Pos", 6);
                    int j = listtag.size();
                    double d0 = j >= 1 ? listtag.getDouble(0) : (double)blockPos.getX() + (serverLevel.random.nextDouble() - serverLevel.random.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d1 = j >= 2 ? listtag.getDouble(1) : (double)(blockPos.getY() + serverLevel.random.nextInt(3) - 1);
                    double d2 = j >= 3 ? listtag.getDouble(2) : (double)blockPos.getZ() + (serverLevel.random.nextDouble() - serverLevel.random.nextDouble()) * (double)this.spawnRange + 0.5D;
                    if (serverLevel.noCollision(optional.get().getAABB(d0, d1, d2))) {
                        BlockPos blockpos2 = BlockPos.containing(d0, d1, d2);

                        Entity entity = EntityType.loadEntityRecursive(compoundtag, serverLevel, (p_151310_) -> {
                            p_151310_.moveTo(d0, d1, d2, p_151310_.getYRot(), p_151310_.getXRot());
                            return p_151310_;
                        });
                        if (entity == null) {
                            this.delay(serverLevel, blockPos);
                            return;
                        }

                        int k = serverLevel.getEntitiesOfClass(entity.getClass(), (new AABB(towerCenter.getX(), blockPos.getY(), towerCenter.getZ(), towerCenter.getX() + 1, blockPos.getY() + 1, towerCenter.getZ() + 1)).inflate(this.towerRadius)).size();
                        if (k >= this.maxNearbyEntities) {
                            this.delay(serverLevel, blockPos);
                            return;
                        }

                        entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), serverLevel.random.nextFloat() * 360.0F, 0.0F);
                        if (entity instanceof Mob mob) {
                            var event = net.minecraftforge.event.ForgeEventFactory.onFinalizeSpawnSpawner(mob, serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), null, compoundtag, this);
                            if (event != null && spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().contains("id", 8)) {
                                net.minecraftforge.event.ForgeEventFactory.onFinalizeSpawn(mob, serverLevel, event.getDifficulty(), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag());
                            }
                            serverLevel.addFreshEntityWithPassengers(entity);
                            serverLevel.levelEvent(2004, blockPos, 0);
                            serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos2);
                            mob.spawnAnim();
                        }

                        if (!entity.isAddedToWorld()) {
                            this.delay(serverLevel, blockPos);
                            return;
                        }

                        flag = true;
                    }
                }

                if (flag) {
                    this.delay(serverLevel, blockPos);
                }

            }
        }
    }

    @Override
    public void load(@Nullable Level level, BlockPos blockPos, CompoundTag compoundTag) {
        this.towerCenter = NbtUtils.readBlockPos(compoundTag.getCompound("TowerCenter"));
        super.load(level, blockPos, compoundTag);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.put("TowerCenter", NbtUtils.writeBlockPos(this.towerCenter));
        compoundTag.putInt("TowerRadius", this.towerRadius);
        return super.save(compoundTag);
    }


}
