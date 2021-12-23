package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Random;

public class DestroyTowerEntity extends Entity {
    // Parameters that must be saved
    private static final DataParameter<BlockPos> CRUMBLE_START_CORNER = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> CRUMBLE_BOTTOM = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CRUMBLE_SPEED = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CURRENT_ROW = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> INITIALIZED = EntityDataManager.defineId(DestroyTowerEntity.class, DataSerializers.BOOLEAN);

    //Other Parameters
    private TowerSpecs specs;
    private GolemType golemType;
    private  ArrayList<BlockPos> blocksToRemove = new ArrayList<>();
    private int rows;
    private int startTicks = 400;
    private int currentTicks = 0;
    private boolean golemDead = false;
    private Random random = new Random();
    private BlockPos removeBlock;

    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentRowName = "CurrentFloor";
    private final String initializedName = "Initialized";


    public DestroyTowerEntity(EntityType<DestroyTowerEntity> type, World world) {
        super(type, world);
    }

    public DestroyTowerEntity(GolemType golemType, BlockPos golemSpawn, World level) {
        super(BTEntityTypes.DESTROY_TOWER, level);

        this.golemType = golemType;

        // Set the start for the tower crumbling to 6 blocks above the Monolith and in the corner of the tower area.
        this.setCrumbleStart(golemSpawn.offset(-15, 6, -15));
    }


    public void getNextRow() {
        BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "In getNextRow");
        // Find the corner of the next row of blocks (3 y levels per row)
        BlockPos rowCorner = this.getCrumbleStart().below(this.getCurrentRow() * 3);

        // for each of th three rows add every single non-air block pos on that row to the arraylist
        for (int y = rowCorner.getY(); y > (rowCorner.getY() - 3); y--) {
            for (int x = rowCorner.getX(); x <= rowCorner.getX() + 30; x++) {
                for(int z = rowCorner.getZ(); z <= rowCorner.getZ() + 30; z++) {
                    BlockPos blockToAdd = new BlockPos(x, y, z);
                    if (this.level.getBlockState(blockToAdd) != Blocks.AIR.defaultBlockState()
                            && this.level.getBlockState(blockToAdd).getFluidState() != Fluids.FLOWING_WATER.defaultFluidState()) {
                        this.blocksToRemove.add(blockToAdd);
                        // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockToAdd);
                    }
                }
            }
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.blocksToRemove.size());
        }
        // add 1 to the row counter so that next time this is called it adds the blocks on the next row
        this.setCurrentRow(this.getCurrentRow() + 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.golemDead) {
            this.currentTicks++;

            // Check to see if data parameters have been initialized.
            if (!this.getInitialized()) {
                BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Initializing");
                this.specs = TowerSpecs.getTowerFromGolem(this.golemType); // Get tower specifics (height, crumble speed)
                this.setCrumbleSpeed(this.specs.getCrumbleSpeed());
                this.setCrumbleBottom(this.getCrumbleStart().getY() - this.specs.getHeight());
                this.rows = (int) Math.floor(this.specs.getHeight() / 3);
                this.setInitialized(true);

                // this.level.setBlock(this.getCrumbleStart(), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
                // this.level.setBlock(this.getCrumbleStart().offset(15, 1, 15), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
                // this.level.setBlock(this.getCrumbleStart().offset(30, 1, 30), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
            }
            if (this.currentTicks == 0) {

            } else if (this.currentTicks == 200) {

            }

            if (200 < this.currentTicks && this.currentTicks < 400) {
                if (currentTicks % 10 == 0) {
                    this.level.playSound(null, this.getCrumbleStart().below(6),
                            SoundEvents.AMBIENT_CAVE, SoundCategory.BLOCKS, 6.0F, 4F);
                }
            }
            // if the current number of ticks is greater than the wait time before the crumbling starts
            // also check that the number of ticks is equal to the crumble speed (so that this isn't called every tick)
            if (this.currentTicks > this.startTicks && this.currentTicks % this.getCrumbleSpeed() == 0 && this.getCurrentRow() < this.rows) {
                if (this.blocksToRemove.size() == 0) {
                    this.getNextRow();
                } else {
                    BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                    for (int i = 0; i < 35; i++) {
                        if (this.blocksToRemove.size() == 0) {
                            break;
                        } else if (this.blocksToRemove.size() == 1) {
                            this.removeBlock = this.blocksToRemove.remove(0);
                            this.level.setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), BlockFlags.DEFAULT);
                        } else {
                            this.removeBlock = this.blocksToRemove.remove(this.random.nextInt(this.blocksToRemove.size() - 1));
                            if (this.level.getBlockState(removeBlock).getFluidState().isSource()) {
                                this.level.setBlock(this.removeBlock, Blocks.DIRT.defaultBlockState(), BlockFlags.DEFAULT);
                                this.level.setBlock(this.removeBlock.east(), Blocks.DIRT.defaultBlockState(), BlockFlags.DEFAULT);
                                this.level.setBlock(this.removeBlock.west(), Blocks.DIRT.defaultBlockState(), BlockFlags.DEFAULT);
                                this.level.setBlock(this.removeBlock.north(), Blocks.DIRT.defaultBlockState(), BlockFlags.DEFAULT);
                                this.level.setBlock(this.removeBlock.south(), Blocks.DIRT.defaultBlockState(), BlockFlags.DEFAULT);
                                this.blocksToRemove.add(this.removeBlock.east());
                                this.blocksToRemove.add(this.removeBlock.west());
                                this.blocksToRemove.add(this.removeBlock.south());
                                this.blocksToRemove.add(this.removeBlock.north());
                            }
                            this.level.setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), BlockFlags.DEFAULT);
                        }
                        if (i == 20) {
                            this.level.addParticle(ParticleTypes.EXPLOSION,
                                    this.removeBlock.getX(), this.removeBlock.getY(), this.removeBlock.getZ(),
                                    .2D, .3D, .2D);
                            this.level.playSound(null, this.removeBlock,
                                    SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS,2.0F,
                                    1.0F);
                        }

                    }
                }
            } else if (this.getCurrentRow() == this.rows){
                // stop if we have done the final row already
                this.remove();
            }

            // log the current ticks, crumble speed, and row
            if (this.currentTicks % 30 == 0 ){
                BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.currentTicks + " Ticks | CrumbleSpeed " +
                        this.getCrumbleSpeed() + "  Row: " + this.getCurrentRow());
            }
        }
    }


    /**************************************************** DATA ****************************************************/



    @Override
    protected void defineSynchedData() {
        this.entityData.define(CRUMBLE_START_CORNER, BlockPos.ZERO);
        this.entityData.define(CRUMBLE_BOTTOM, 0);
        this.entityData.define(CRUMBLE_SPEED,  0);
        this.entityData.define(CURRENT_ROW, 0);
        this.entityData.define(INITIALIZED, false);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.put(this.crumbleStartName, this.newDoubleList(this.getCrumbleStart().getX(), this.getCrumbleStart().getY(), this.getCrumbleStart().getZ()));
        compound.putInt(this.crumbleBottomName, this.getCrumbleBottom());
        compound.putInt(this.crumbleSpeedName, this.getCrumbleSpeed());
        compound.putInt(this.currentRowName, this.getCurrentRow());
        compound.putBoolean(this.initializedName, this.getInitialized());
        compound.putString("GolemType", this.golemType.getSerializedName());
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
        this.setCurrentRow(compound.getInt(this.currentRowName) - 1);
        this.golemType = GolemType.getTypeForName(compound.getString("GolemType"));
        this.setGolemDead(true);
        this.setInitialized(true);
        this.currentTicks = 260;
    }



    /************************************************** DATA SET/GET **************************************************/

    public void setGolemDead(boolean bool) {
        this.golemDead = bool;
    }
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
    public void setCurrentRow(int floor) {
        this.entityData.set(CURRENT_ROW, floor);
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
    public Integer getCurrentRow() {
        return this.entityData.get(CURRENT_ROW);
    }

    /**
     * Define whether the entity's extra data has been set.
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
