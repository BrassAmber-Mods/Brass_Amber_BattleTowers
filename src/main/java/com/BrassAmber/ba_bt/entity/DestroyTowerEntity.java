package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;
import net.minecraft.block.Blocks;
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
import java.util.Random;

public class DestroyTowerEntity extends Entity {
    // Parameters that must be saved
    private static final DataParameter<BlockPos> CRUMBLE_START_CORNER = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> CRUMBLE_BOTTOM = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CRUMBLE_SPEED = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CURRENT_FLOOR = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> INITIALIZED = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.BOOLEAN);

    //Other Parameters
    private TowerSpecs specs;
    private GolemType golemType;
    private  ArrayList<BlockPos> blocksToRemove = new ArrayList<>();
    private int rows;
    private int currentRow = 0;
    private int startTicks = 400;
    private int currentTicks = 0;
    private Random random = new Random();

    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentFloorName = "CurrentFloor";
    private final String initializedName = "Initialized";

    public DestroyTowerEntity(EntityType<DestroyTowerEntity> type, World world) {
        super(type, world);
    }

    public DestroyTowerEntity(GolemType golemType, BlockPos golemSpawn, World level) {
        super(BTEntityTypes.DESTROY_TOWER, level);

        this.golemType = golemType;

        // Set the start for the tower crumbling to 6 blocks above the Monolith and in the corner of the tower area.
        this.setCrumbleStart(golemSpawn.above(6).offset(-14, 0, -14));

        // Set the crumble speed to the speed in the specs * 20 to account for ticks
        this.setInitialized(false);
    }


    public void getNextRow() {
        BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "In getNextRow");
        BlockPos rowCorner = this.getCrumbleStart().below(this.currentRow * 3);
        for (int y = rowCorner.getY(); y > rowCorner.getY() - 3; y--) {
            for (int x = rowCorner.getX(); x < 29; x++) {
                for(int z = rowCorner.getY(); z<29; z++) {
                    this.blocksToRemove.add(new BlockPos(x,y,z));

                }
            }
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Added blocks at Y: " + y);
        }
        this.currentRow += 1;
    }

    @Override
    public void tick() {
        super.tick();
        this.currentTicks++;
        // Check to see if data parameters have been initialized.
        if (!this.getInitialized()) {
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Initializing");
            this.specs = TowerSpecs.getTowerFromGolem(this.golemType); // Get tower specifics (height, crumble speed)
            this.setCrumbleSpeed(this.specs.getCrumbleSpeed() * 20);
            this.setCrumbleBottom(this.getCrumbleStart().getY() - this.specs.getHeight());
            this.rows = this.specs.getHeight() / 3;
            this.setInitialized(true);
        }
        if (this.currentTicks > this.startTicks && this.currentTicks % this.getCrumbleSpeed() == 0 && this.currentRow != this.rows) {
            if (this.blocksToRemove.size() == 0) {
                this.getNextRow();
            } else if (this.currentRow > 0) {
                while (this.blocksToRemove.size() > 0) {
                    this.level.setBlock(this.blocksToRemove.get(this.random.nextInt(this.blocksToRemove.size() - 1)),
                            Blocks.AIR.defaultBlockState(), 2);
                }
            }
        } else if (this.currentTicks % 10 == 0 ){
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.currentTicks + " Ticks|CrumbleSpeed " +
                    this.getCrumbleSpeed() + "  row: " + this.currentRow);
        }
    }

    /**************************************************** DATA ****************************************************/

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CRUMBLE_START_CORNER, BlockPos.ZERO);
        this.entityData.define(CRUMBLE_BOTTOM, 0);
        this.entityData.define(CRUMBLE_SPEED,  0);
        this.entityData.define(CURRENT_FLOOR, 8);
        this.entityData.define(INITIALIZED, false);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.put(this.crumbleStartName, this.newDoubleList(this.getCrumbleStart().getX(), this.getCrumbleStart().getY(), this.getCrumbleStart().getZ()));
        compound.putInt(this.crumbleBottomName, this.getCrumbleBottom());
        compound.putInt(this.crumbleSpeedName, this.getCrumbleSpeed());
        compound.putInt(this.currentFloorName, this.getCurrentFloor());
        compound.putBoolean(this.initializedName, this.getInitialized());
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
        this.setInitialized(false);
    }



    /************************************************** DATA SET/GET **************************************************/

    /**
     * Define the start position.
     */
    public void setCrumbleStart(BlockPos startPos) {
        this.entityData.set(CRUMBLE_START_CORNER, startPos);
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
     * Define whether the entities extra data has been initialized.
     */
    public void setInitialized(boolean bool) {
        this.entityData.set(INITIALIZED, bool);
    }

    /**
     * Define the start position.
     * @return
     */
    public BlockPos getCrumbleStart() {
        return this.entityData.get(CRUMBLE_START_CORNER);
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

    /**
     * Define hether the entity's extra data has been set.
     * @return
     */
    public Boolean getInitialized() {
        return this.entityData.get(INITIALIZED);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
