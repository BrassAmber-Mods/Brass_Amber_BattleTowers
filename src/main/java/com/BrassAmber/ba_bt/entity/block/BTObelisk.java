package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.entity.DestroyTower;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.sound.BTMusics;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTObelisk extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<BlockPos> TOWER_CENTER = SynchedEntityData.defineId(BTObelisk.class, EntityDataSerializers.BLOCK_POS);

    //Other Parameters
    private boolean initialized = false;
    private MusicManager musicManager;
    private boolean hasPlayer;


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
            this.musicManager = Minecraft.getInstance().getMusicManager();
            this.initialized = true;
        }

        List<ServerPlayer> players = this.level.getServer().overworld().players();
        for (ServerPlayer player : players
        ) {
            double xDistance = Math.abs(Math.abs(this.getX()) - Math.abs(player.getX()));
            double zDistance = Math.abs(Math.abs(this.getZ()) - Math.abs(player.getZ()));

            boolean xClose = xDistance < 125;
            boolean zClose = zDistance < 125;

            List<Boolean> playersClose = new ArrayList<>();

            if (!xClose || !zClose) {
                playersClose.add(Boolean.FALSE);
            }

            if (Collections.frequency(playersClose, Boolean.FALSE) == players.size()) {
                this.hasPlayer = false;

            }  else {
                this.hasPlayer = true;
            }
        }

        if (this.tickCount == 0 || this.tickCount % 7200 == 0 && this.hasPlayer) {
            if (!this.musicManager.isPlayingMusic(BTMusics.GOLEM_FIGHT)) {
                this.musicManager.stopPlaying();
                this.musicManager.startPlaying(BTMusics.TOWER);
            }
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
