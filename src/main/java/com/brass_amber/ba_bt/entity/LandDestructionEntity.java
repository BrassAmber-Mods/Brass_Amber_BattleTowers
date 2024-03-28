package com.brass_amber.ba_bt.entity;

import java.util.*;

import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import com.brass_amber.ba_bt.util.BTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.util.GolemType;
import com.brass_amber.ba_bt.util.TowerSpecs;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

import static com.brass_amber.ba_bt.sound.BTSoundEvents.TOWER_COLLAPSE_MUSIC;
import static com.brass_amber.ba_bt.util.BTUtil.*;

public class LandDestructionEntity extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<BlockPos> CRUMBLE_START_CORNER = SynchedEntityData.defineId(LandDestructionEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> CRUMBLE_BOTTOM = SynchedEntityData.defineId(LandDestructionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CRUMBLE_SPEED = SynchedEntityData.defineId(LandDestructionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_ROW = SynchedEntityData.defineId(LandDestructionEntity.class, EntityDataSerializers.INT);

    //Other Parameters
    private Boolean initialized = false;
    private TowerSpecs specs;
    private GolemType golemType;
    private List<BlockPos> blocksToRemove = new ArrayList<>();
    private int rows;
    private int startTicks = 600;
    private int currentTicks = 0;
    private boolean golemDead = false;
    private final Random random = new Random();
    private BlockPos removeBlock;
    private boolean checkForGolem = true;
    private boolean hasPlayer;
    
    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentRowName = "CurrentFloor";


    public LandDestructionEntity(EntityType<LandDestructionEntity> type, Level level) {
        super(type, level);
        this.startTicks = BattleTowersConfig.landTimeBeforeCollapse.get() * 20;
    }

    public LandDestructionEntity(BlockPos golemSpawn, Level level) {
        super(BTEntityType.LAND_DESTRUCTION.get(), level);
        this.golemType = GolemType.LAND;
        
        // Set the start for the tower crumbling to 6 blocks above the Monolith and in the corner of the tower area.
        this.setCrumbleStart(golemSpawn.offset(-15, 6, -15));
    }


    public void getNextRow() {
        //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "In getNextRow");
        // Find the corner of the next row of blocks (3 y levels per row)
        BlockPos rowCorner = this.getCrumbleStart().below(this.getCurrentRow() * 3);

        // for each of th three rows add every single non-air block pos on that row to the arraylist
        for (int y = rowCorner.getY(); y > (rowCorner.getY() - 3); y--) {
            for (int x = rowCorner.getX(); x <= rowCorner.getX() + 30; x++) {
                for(int z = rowCorner.getZ(); z <= rowCorner.getZ() + 30; z++) {
                    BlockPos blockToAdd = new BlockPos(x, y, z);
                    if (BTUtil.distanceTo2D(this, blockToAdd) < 14.5D) {
                        if (this.level().isWaterAt(blockToAdd)) {
                            this.level().setBlock(blockToAdd, Blocks.AIR.defaultBlockState(), 3);
                        } else if (!this.level().getBlockState(blockToAdd).isAir()) {
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
    	BrassAmberBattleTowers.LOGGER.debug("Initializing");
        this.specs = TowerSpecs.getTowerFromGolem(this.golemType); // Get tower specifics (height, crumble speed)
        this.setCrumbleSpeed(this.specs.getCrumbleSpeed());
        this.setCrumbleBottom(
                this.getCrumbleStart().getY() - (int)Math.round(
                        (BattleTowersConfig.landObeliskSpawnDistance.get() + 20) * BattleTowersConfig.landTowerCrumblePercent.get()
                )
        );
        this.rows = (int) Math.floor((this.getCrumbleStart().getY() - this.getCrumbleBottom()) / 3F);
        this.initialized = true;

        // this.level().setBlock(this.getCrumbleStart(), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
        // this.level().setBlock(this.getCrumbleStart().offset(15, 1, 15), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
        // this.level().setBlock(this.getCrumbleStart().offset(30, 1, 30), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.checkForGolem) {

            List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(50.0D, 100.0D, 50.0D));
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
        boolean alivePlayer = this.level().hasNearbyAlivePlayer(this.getX(), this.getY(), this.getZ(), 100D);
        if (alivePlayer) {
            //noinspection ConstantConditions
            this.hasPlayer = BTUtil.distanceTo2D(this, this.level().getNearestPlayer(this, 100D)) < 125;
        } else {
            this.hasPlayer = false;
        }
        if (this.golemDead && this.level().isClientSide()) {
            MusicManager music = Minecraft.getInstance().getMusicManager();
            if (hasPlayer) {
                if (!music.isPlayingMusic(TOWER_COLLAPSE_MUSIC)) {
                    music.stopPlaying();
                    music.startPlaying(TOWER_COLLAPSE_MUSIC);
                }
            }
            else {
                music.stopPlaying();
            }
            return;
        }

        if (this.golemDead && this.hasPlayer) {
            this.currentTicks++;

            // Check to see if data parameters have been initialized.
            if (!this.initialized) {
                this.init();
            }
            if (this.currentTicks == 1) {


                doNoOutputCommand(this,"/title @a times 30 40 20");
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\" " + this.specs.getTitleText1()
                        + "\",\"color\":\"" + this.specs.getColorCode() + "\"}");

                this.level().playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 4.0F, 1F);
            } else if (this.currentTicks == 400) {
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\"" + this.specs.getTitleText2()
                        + " \",\"color\":\"#aaaaaa\"}");
                this.level().playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 4.0F, 1F);

            }  else if (this.currentTicks == 500) {
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\"" + this.specs.getTitleText3()
                        + "\",\"color\":\"#aa0000\"}");

            }else if (this.currentTicks == 600) {
                this.level().playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 4.0F, 1F);
            }


            // if the current number of ticks is greater than the wait time before the crumbling starts
            // also check that the number of ticks is equal to the crumble speed (so that this isn't called every tick)
            if (this.currentTicks > this.startTicks && this.currentTicks % this.getCrumbleSpeed() == 0 && this.getCurrentRow() < this.rows) {
                if (this.currentTicks % 240 == 0) {
                    this.level().playSound(null, this.getCrumbleStart().below(this.getCurrentRow()*3),
                            BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 4F, 1F);
                }
                if (this.blocksToRemove.isEmpty()) {
                    // Water checks/removal is done inside this method to prevent flowing water + explosions
                    this.getNextRow();
                } else {
                   // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                    if(this.random.nextDouble() <= 0.125 && this.removeBlock != null) {
                    	//Fancy physics stuff
                    	ExplosionPhysics explosion = new ExplosionPhysics(BTEntityType.PHYSICS_EXPLOSION.get(), this.level());
                    	explosion.setPos(this.removeBlock.getX(), this.removeBlock.getY(), this.removeBlock.getZ());
                    	this.level().addFreshEntity(explosion);

                    }
                	for (int i = 0; i < 35; i++) {
                        if (this.blocksToRemove.isEmpty()) {
                            break;
                        } else {
                            // Get random integer, if size is 1 get item at index 0
                            int randomIndex;
                            if (this.blocksToRemove.size() <= 1) {
                                randomIndex = 0;
                            } else {
                                randomIndex = this.random.nextInt(this.blocksToRemove.size() - 1);
                            }

                            this.removeBlock = this.blocksToRemove.remove(randomIndex);
                            this.level().removeBlock(this.removeBlock, false);
                        }

                    }
                    if (this.currentTicks % 6 == 0 && !this.blocksToRemove.isEmpty()) {
                        this.level().gameEvent(this, GameEvent.EXPLODE, this.removeBlock);
                        this.level().playSound(null, this.removeBlock, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,
                                2.0F, 1.0F);
                    }

                }
            } else if (this.getCurrentRow() >= this.rows){
                // stop if we have done the final row already
                BrassAmberBattleTowers.LOGGER.debug("In Ending Sequence");
                this.getNextRow();
                for (int i = 0; i < 60; i++) {
                    if (this.blocksToRemove.isEmpty()) {
                        break;
                    } else if (this.blocksToRemove.size() == 1) {
                        this.removeBlock = this.blocksToRemove.remove(0);
                        this.level().setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), 2);
                    } else {
                        this.removeBlock = this.blocksToRemove.remove(this.random.nextInt(this.blocksToRemove.size() - 1));
                        if (!this.level().getBlockState(this.removeBlock).getFluidState().isEmpty() ){
                            this.level().setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), 2);
                        }
                        this.level().removeBlock(this.removeBlock, false);
                    }
                }
                List<BlockPos> shouldBeEmptySpace = new ArrayList<>();
                int yForClear = this.getCrumbleStart().getY();
                for (int y = yForClear; y > this.getCrumbleBottom() + 3; y--) {
                    for (int x = this.getCrumbleStart().getX(); x <= this.getCrumbleStart().getX() + 30; x++) {
                        for(int z = this.getCrumbleStart().getZ(); z <= this.getCrumbleStart().getZ() + 30; z++) {
                            BlockPos blockToAdd = new BlockPos(x, y, z);
                            if (((!this.level().getBlockState(blockToAdd).isAir() || this.level().isWaterAt(blockToAdd))
                                            && BTUtil.distanceTo2D(this, blockToAdd) < 14.5)
                            ) {
                                shouldBeEmptySpace.add(blockToAdd);
                                // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockToAdd);
                            }
                        }
                    }
                    //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.blocksToRemove.size());
                }
                for (BlockPos clear: shouldBeEmptySpace) {
                    if (this.level().isWaterAt(clear)) {
                        removeBodyOfWater(clear, this.level());
                    } else {
                        this.level().removeBlock(clear, false);
                    }

                }
                this.remove(RemovalReason.DISCARDED);
            }

            /*
            // log the current ticks, crumble speed, and row
            if (this.currentTicks % 120 == 0 ){
                BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG, this.currentTicks + " Ticks | CrumbleSpeed " +
                        this.getCrumbleSpeed() + "  Row: " + this.getCurrentRow());
            }
            */
        }
    }

	/**************************************************** DATA ****************************************************/



    @Override
    protected void defineSynchedData() {
        this.entityData.define(CRUMBLE_START_CORNER, BlockPos.ZERO);
        this.entityData.define(CRUMBLE_BOTTOM, 0);
        this.entityData.define(CRUMBLE_SPEED,  0);
        this.entityData.define(CURRENT_ROW, 0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put(this.crumbleStartName, BTUtil.newIntList(this.getCrumbleStart().getX(), this.getCrumbleStart().getY(), this.getCrumbleStart().getZ()));
        compound.putInt(this.crumbleBottomName, this.getCrumbleBottom());
        compound.putInt(this.crumbleSpeedName, this.getCrumbleSpeed());
        compound.putInt(this.currentRowName, this.getCurrentRow());
        compound.putString("GolemType", this.golemType.getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        ListTag startPos = compound.getList(this.crumbleStartName, 6);
        int x = startPos.getInt(0);
        int y = startPos.getInt(1);
        int z = startPos.getInt(2);
        this.setCrumbleStart(new BlockPos(x, y, z));
        this.setCrumbleBottom(compound.getInt(this.crumbleBottomName));
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

}
