package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.entity.DestroyTower;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class BTObelisk extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<BlockPos> TOWER_CENTER = SynchedEntityData.defineId(BTObelisk.class, EntityDataSerializers.BLOCK_POS);

    //Other Parameters
    private boolean initialized = false;

    // Data Strings
    private final String towercenterName = "TowerCenter";


    private final CommandSourceStack source = createCommandSourceStack().withPermission(4);

    public BTObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BTObelisk(GolemType golemType, BlockPos towerCenter, Level level) {
        super(GolemType.getObeliskFor(golemType), level);
        this.setTowerCenter(towerCenter);

    }

    public void findChestsAndSpawners() {
        BlockPos center = this.getTowerCenter();
        BlockPos corner = center.offset(-10,0,-10);

    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide()) {
            return;
        }
        if (!this.initialized) {
            findChestsAndSpawners();
            this.initialized = true;
        }

        if (this.tickCount == 0 || this.tickCount % 7200 == 0) {
            this.level.playSound(null, this.getTowerCenter().above(10), BTSoundEvents.TOWER_MUSIC, SoundSource.MUSIC, 5F, 1F);
        }


    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TOWER_CENTER, BlockPos.ZERO);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        ListTag centerPos = tag.getList(this.towercenterName, 6);
        double x = centerPos.getDouble(0);
        double y = centerPos.getDouble(1);
        double z = centerPos.getDouble(2);
        this.setTowerCenter(new BlockPos(x, y, z));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
       tag.put(this.towercenterName, this.newDoubleList(this.getTowerCenter().getX(), this.getTowerCenter().getY(), this.getTowerCenter().getZ()));
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return super.getPickedResult(target);
    }

    @Override
    public MobCategory getClassification(boolean forSpawnCount) {
        return MobCategory.AMBIENT;
    }

    /************************************************** DATA SET/GET **************************************************/

    /**
     * Define the tower center.
     */
    public void setTowerCenter(BlockPos center) {
        this.entityData.set(TOWER_CENTER, center);
    }


    public BlockPos getTowerCenter() {return this.entityData.get(TOWER_CENTER);}
}
