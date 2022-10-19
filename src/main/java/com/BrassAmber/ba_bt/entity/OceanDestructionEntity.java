package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.BTUtil;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.BrassAmber.ba_bt.util.BTUtil.doNoOutputCommand;
import static com.BrassAmber.ba_bt.util.BTUtil.distanceTo2D;

public class OceanDestructionEntity extends  Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<BlockPos> CRUMBLE_START_CORNER = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> CRUMBLE_TOP = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TOWER_BOTTOM = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CRUMBLE_SPEED = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_ROW = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.INT);

    //Other Parameters
    private Boolean initialized = false;
    private TowerSpecs specs;
    private GolemType golemType;
    private List<BlockPos> blocksToRemove = new ArrayList<>();
    private BlockPos currentRowY;
    private int rowsBelowTower;
    private int rowsOfTower;
    private int startTicks = 600;
    private int currentTicks = 0;
    private boolean golemDead = false;
    private final Random random = new Random();
    private BlockPos removeBlock;
    private boolean checkForGolem = true;
    private boolean hasPlayer;


    private ArrayList<FallingBlockEntity> fallingBlocks;

    private final double destroyPercentOfTower;

    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "TowerBottom";
    private final String crumbleTopName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentRowName = "CurrentFloor";

    public OceanDestructionEntity(EntityType<OceanDestructionEntity> type, Level level) {
        super(type, level);
        this.destroyPercentOfTower = BattleTowersConfig.oceanTowerCrumblePercent.get();
        this.startTicks = BattleTowersConfig.oceanTimeBeforeCollapse.get();
        this.fallingBlocks = new ArrayList<>();
        this.currentRowY = BlockPos.ZERO;
        this.rowsBelowTower = 0;
        this.rowsOfTower = 0;
        this.removeBlock = BlockPos.ZERO;
        this.hasPlayer = false;
    }

    public OceanDestructionEntity(BlockPos golemSpawn, Level level) {
        super(BTEntityTypes.OCEAN_DESTRUCTION.get(), level);

        this.golemType = GolemType.OCEAN;
        this.destroyPercentOfTower = BattleTowersConfig.oceanTowerCrumblePercent.get();

        // Set the start for the tower crumbling to 6 blocks above the Monolith and in the corner of the tower area.
        this.setCrumbleStart(golemSpawn.offset(-15, 0, -15).atY(-64));
    }

    public void getNextRow() {
        //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "In getNextRow");
        // Find the corner of the next row of blocks (3 y levels per row)
        this.currentRowY.offset(0, 4, 0);
        int startx = this.getCrumbleStart().getX();
        int startz = this.getCrumbleStart().getZ();

        // for each of the three rows add every single non-air block pos on that row to the arraylist
        for (int y = this.currentRowY.getY(); y < (this.currentRowY.getY() + 3); y++) {
            for (int x = startx; x <= startx + 30; x++) {
                for(int z = startz; z <= startz + 30; z++) {
                    BlockPos blockToAdd = new BlockPos(x, y, z);
                    if (BTUtil.distanceTo2D(this, blockToAdd) < 13.5D) {
                        if (!this.level.isWaterAt(blockToAdd) && !this.level.getBlockState(blockToAdd).isAir()) {
                            this.blocksToRemove.add(blockToAdd);
                            // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockToAdd);
                        }
                    }
                }
            }
            //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.blocksToRemove.size());
        }
        // add 1 to the row counter so that next time this is called it adds the blocks on the next row
        this.setCurrentRow(this.getCurrentRow() + 1);
    }

    private void init() {
        BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG, "Initializing");
        this.specs = TowerSpecs.getTowerFromGolem(this.golemType); // Get tower specifics (height, crumble speed)
        this.setCrumbleSpeed(this.specs.getCrumbleSpeed());
        this.setCrumbleTop(this.getBlockY() + (int)Math.round(this.specs.getHeight() * this.destroyPercentOfTower));
        this.rowsOfTower = (int) Math.floor((this.getCrumbleTop() - this.blockPosition().getY()) / 3F);
        double landBelowTower = this.blockPosition().getY() - this.getCrumbleStart().getY();
        if (landBelowTower % 4 != 0) {
            this.setCrumbleStart(this.getCrumbleStart().below((int) (landBelowTower % 4)));
        }
        this.rowsBelowTower = (int) Math.floor((this.blockPosition().getY() - this.getCrumbleStart().getY()) / 4f);
        this.fallingBlocks = new ArrayList<>();
        this.initialized = true;
        this.currentRowY = this.getCrumbleStart().offset(0, this.getCurrentRow() * 4, 0);
    }

    @Override
    public void tick() {
        if(this.level.isClientSide()) {
            return;
        }

        // Check to see if data parameters have been initialized.
        if (!this.initialized) {
            this.init();
        }

        if (this.getCurrentRow() < this.rowsBelowTower + 1 && this.getCrumbleSpeed() != 1) {
            this.setCrumbleSpeed(1);
        } else {
            this.setCrumbleSpeed(2);
        }
        // New list to save the id pf the entities that should be removed
        ArrayList<Integer> removeFallEntity = new ArrayList<>();
        for (int i = 0; i < this.fallingBlocks.size(); i++) {
            // Check each block in the Falling entities list
            FallingBlockEntity blockEntity = this.fallingBlocks.get(i);
            if (blockEntity.getY() < -69 + (this.getCurrentRow() * 3)) {
                // remove if too far down
                blockEntity.setRemoved(RemovalReason.DISCARDED);
                removeFallEntity.add(i);
            }
        }
        // Reverse list of Ids so that we remove from the end first, thereby avoiding a call to an item already removed
        Collections.reverse(removeFallEntity);
        for (int i = 0; i <removeFallEntity.size(); i++) {
            //noinspection SuspiciousListRemoveInLoop
            this.fallingBlocks.remove(i);
        }

        super.tick();
        if (this.checkForGolem) {

            List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(50.0D, 100.0D, 50.0D));
            boolean deadGolem = true;

            for (Entity entity: entities
            ) {
                try {
                    BTAbstractGolem golem = (BTAbstractGolem) entity;
                    deadGolem = false;
                } catch (Exception ignored) {

                }
            }

            if (deadGolem) {
                this.setGolemDead(true);

            } else {
                this.setGolemDead(false);
                this.currentTicks = 0;
            }
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

            this.hasPlayer = Collections.frequency(playersClose, Boolean.FALSE) != players.size();
        }

        if (this.golemDead && this.hasPlayer) {
            this.currentTicks++;


            if (this.currentTicks == 1) {


                doNoOutputCommand(this,"/title @a times 30 40 20");
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\" " + this.specs.getTitleText1()
                        + "\",\"color\":\"" + this.specs.getColorCode() + "\"}");

                this.level.playSound(null, this.blockPosition().above(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 4.0F, 1F);
            } else if (this.currentTicks == 400) {
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\"" + this.specs.getTitleText2()
                        + " \",\"color\":\"#aaaaaa\"}");
                this.level.playSound(null, this.blockPosition().above(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 4.0F, 1F);

            }  else if (this.currentTicks == 500) {
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\"" + this.specs.getTitleText3()
                        + "\",\"color\":\"#aa0000\"}");

            }else if (this.currentTicks == 600) {
                this.level.playSound(null, this.blockPosition().above(6),
                        BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 4.0F, 1F);
            }


            // if the current number of ticks is greater than the wait time before the crumbling starts
            // also check that the number of ticks is equal to the crumble speed (so that this isn't called every tick)
            if (this.currentTicks > this.startTicks && this.currentTicks % this.getCrumbleSpeed() == 0 && this.getCurrentRow() < this.rowsOfTower + this.rowsBelowTower) {
                if (this.currentTicks % 240 == 0) {
                    this.level.playSound(null, this.getCrumbleStart().above(this.getCurrentRow()*3),
                            BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 4F, 1F);
                }
                if (this.blocksToRemove.size() < 20) {
                    // Water checks/removal is done inside this method to prevent flowing water + explosions
                    this.getNextRow();
                } else {
                    // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                    for (int i = 0; i < 48; i++) {
                        if (this.blocksToRemove.isEmpty()) {
                            break;
                        } else {
                            // Get random integer, if size is 1 get item at index 0
                            int randomIndex = 0;
                            if (this.blocksToRemove.size() != 1) {
                                randomIndex = this.random.nextInt(this.blocksToRemove.size() - 1);
                            }
                            this.removeBlock = this.blocksToRemove.get(randomIndex);

                            BlockState state = this.level.getBlockState(this.removeBlock);
                            if (this.level.getBlockEntity(this.removeBlock) != null) {
                                this.level.setBlock(this.removeBlock, Blocks.WATER.defaultBlockState(), 2);
                            } else if (
                                    !state.getFluidState().isSource()
                                    && (this.level.getBlockState(this.removeBlock.below()).isAir() || this.level.isWaterAt(this.removeBlock.below()))
                            ) {

                                final Vec3 velocity = new Vec3(0D, 0.5D, 0D);

                                this.level.removeBlock(this.removeBlock, true);

                                FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
                                        this.level,
                                        this.removeBlock,
                                        state
                                );
                                fallingBlock.setInvulnerable(true);
                                fallingBlock.dropItem = false;
                                fallingBlock.setDeltaMovement(velocity);

                                this.blocksToRemove.remove(randomIndex);
                            }
                        }

                    }

                }
            } else if (this.getCurrentRow() >= this.rowsBelowTower + this.rowsOfTower){
                // stop if we have done the final row already
                BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG, "In Ending Sequence");
                this.remove(Entity.RemovalReason.DISCARDED);
            }

            // log the current ticks, crumble speed, and row
            if (this.currentTicks % 30 == 0 ){
                BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG, this.currentTicks + " Ticks | CrumbleSpeed " +
                        this.getCrumbleSpeed() + "  Row: " + this.getCurrentRow());
            }
        }
    }

    /**************************************************** DATA ****************************************************/



    @Override
    protected void defineSynchedData() {
        this.entityData.define(CRUMBLE_START_CORNER, BlockPos.ZERO);
        this.entityData.define(CRUMBLE_TOP, 0);
        this.entityData.define(CRUMBLE_SPEED,  0);
        this.entityData.define(CURRENT_ROW, 0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put(this.crumbleStartName, this.newDoubleList(this.getCrumbleStart().getX(), this.getCrumbleStart().getY(), this.getCrumbleStart().getZ()));
        compound.putInt(this.crumbleTopName, this.getCrumbleTop());
        compound.putInt(this.crumbleSpeedName, this.getCrumbleSpeed());
        compound.putInt(this.currentRowName, this.getCurrentRow());
        compound.putString("GolemType", this.golemType.getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        ListTag startPos = compound.getList(this.crumbleStartName, 6);
        double x = startPos.getDouble(0);
        double y = startPos.getDouble(1);
        double z = startPos.getDouble(2);
        this.setCrumbleStart(new BlockPos(x, y, z));
        this.setCrumbleTop(compound.getInt(this.crumbleTopName));
        this.setCrumbleSpeed(compound.getInt(this.crumbleSpeedName));
        this.setCurrentRow(compound.getInt(this.currentRowName) - 1);
        this.golemType = GolemType.getTypeForName(compound.getString("GolemType"));
        this.setGolemDead(false);
        this.checkForGolem = true;
        this.initialized = false;
        this.currentTicks = 580;
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
    public void setCrumbleTop(Integer bottomPos) {
        this.entityData.set(CRUMBLE_TOP, bottomPos);
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
     * Define Bottom of Tower.
     */
    public void setTowerBottom(Integer bottomY) {this.entityData.set(TOWER_BOTTOM, bottomY);}

    /**
     * Define the start position.
     */
    public BlockPos getCrumbleStart() {
        return this.entityData.get(CRUMBLE_START_CORNER);
    }

    /**
     * Define the bottom position.
     */
    public Integer getCrumbleTop() {
        return this.entityData.get(CRUMBLE_TOP);
    }
    /**
     * Define the crumble speed.
     */
    public Integer getCrumbleSpeed() {
        return this.entityData.get(CRUMBLE_SPEED);
    }
    /**
     * Define the current floor.
     */
    public Integer getCurrentRow() {
        return this.entityData.get(CURRENT_ROW);
    }

    public Integer getTowerBottom() {return this.entityData.get(TOWER_BOTTOM);}

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
