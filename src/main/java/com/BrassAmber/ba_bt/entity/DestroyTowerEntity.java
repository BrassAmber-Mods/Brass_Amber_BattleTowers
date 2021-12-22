package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntity;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class DestroyTowerEntity extends Entity {
    private static final DataParameter<BlockPos> CRUMBLE_START = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> CRUMBLE_BOTTOM = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CRUMBLE_SPEED = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CURRENT_FLOOR = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);

    private TowerSpecs specs;

    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentFloorName = "CurrentFloor";

    public DestroyTowerEntity(EntityType<DestroyTowerEntity> type, World world) {
        super(type, world);
    }

    public DestroyTowerEntity(GolemType golemType, BlockPos golemSpawn, World level) {
        super(BTEntityTypes.DESTROY_TOWER, level);
        this.specs = TowerSpecs.getTowerFromGolem(golemType);
        this.setCrumbleStart(golemSpawn.above(6));
        this.setCrumbleSpeed(this.specs.getCrumbleSpeed() * 60);
        this.setCrumbleBottom(this.getCrumbleStart().getY() - this.specs.getHeight());
    }



    public void start() {
        long startTime = this.level.getGameTime();
        while((this.level.getGameTime() - startTime) > 200) {
            if ((this.level.getGameTime() - startTime) % 10 == 0) {
                BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.level.getGameTime() + "  ---   " + startTime);
            }
        }
        BlockPos startCorner = getCrumbleStart().offset(-14, 0, -14); // in this section, this acts as 0,0 on an imaginary graph
        ArrayList<BlockPos> blocksToRemove = new ArrayList<>();
        for (int x = 0; x < 29; x++) {
            for(int y = 0; y<29; y++) {

            }
        }
    }

    /**************************************************** DATA ****************************************************/

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CRUMBLE_START, BlockPos.ZERO);
        this.entityData.define(CRUMBLE_BOTTOM, 0);
        this.entityData.define(CRUMBLE_SPEED,  0);
        this.entityData.define(CURRENT_FLOOR, 8);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.put(this.crumbleStartName, this.newDoubleList(this.getCrumbleStart().getX(), this.getCrumbleStart().getY(), this.getCrumbleStart().getZ()));
        compound.putInt(this.crumbleBottomName, this.getCrumbleBottom());
        compound.putInt(this.crumbleSpeedName, this.getCrumbleSpeed());
        compound.putInt(this.currentFloorName, this.getCurrentFloor());
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        ListNBT startPos = compound.getList(this.crumbleStartName, 6);
        double x = startPos.getDouble(0);
        double y = startPos.getDouble(1);
        double z = startPos.getDouble(2);
        this.setCrumbleStart(new BlockPos(x, y, z));
        this.setCrumbleBottom(compound.getInt(this.crumbleBottomName));
        this.setCrumbleSpeed(compound.getInt(this.crumbleSpeedName));
        this.setCurrentFloor(compound.getInt(this.currentFloorName));
    }

    /************************************************** DATA SET/GET **************************************************/

    /**
     * Define the start position.
     */
    public void setCrumbleStart(BlockPos startPos) {
        this.entityData.set(CRUMBLE_START, startPos);
    }

    /**
     * Define the bottom position.
     */
    public void setCrumbleBottom(Integer bottomPos) {
        this.entityData.set(CRUMBLE_BOTTOM, bottomPos);
    }
    /**
     * Define the crumble speed.
     */
    public void setCrumbleSpeed(int speed) {
        this.entityData.set(CRUMBLE_SPEED, speed);
    }
    /**
     * Define the current floor.
     */
    public void setCurrentFloor(int floor) {
        this.entityData.set(CURRENT_FLOOR, floor);
    }

    /**
     * Define the start position.
     * @return
     */
    public BlockPos getCrumbleStart() {
        return this.entityData.get(CRUMBLE_START);
    }

    /**
     * Define the bottom position.
     * @return
     */
    public Integer getCrumbleBottom() {
        return this.entityData.get(CRUMBLE_BOTTOM);
    }
    /**
     * Define the crumble speed.
     * @return
     */
    public Integer getCrumbleSpeed() {
        return this.entityData.get(CRUMBLE_SPEED);
    }
    /**
     * Define the current floor.
     * @return
     */
    public Integer getCurrentFloor() {
        return this.entityData.get(CURRENT_FLOOR);
    }



    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
