package com.BrassAmber.ba_bt.entity;

import java.util.*;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;


import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.network.NetworkHooks;

public class DestroyTowerEntity extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<BlockPos> CRUMBLE_START_CORNER = SynchedEntityData.defineId(DestroyTowerEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> CRUMBLE_BOTTOM = SynchedEntityData.defineId(DestroyTowerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CRUMBLE_SPEED = SynchedEntityData.defineId(DestroyTowerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_ROW = SynchedEntityData.defineId(DestroyTowerEntity.class, EntityDataSerializers.INT);

    //Other Parameters
    private Boolean initialized = false;
    private TowerSpecs specs;
    private GolemType golemType;
    private List<BlockPos> blocksToRemove = new ArrayList<>();
    private int rows;
    private int startTicks = 600;
    private int currentTicks = 0;
    private boolean golemDead = false;
    private Random random = new Random();
    private BlockPos removeBlock;
    private boolean checkForGolem = true;

    private final double destroyPercentOfTower;
    
    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentRowName = "CurrentFloor";



    public DestroyTowerEntity(EntityType<DestroyTowerEntity> type, Level level) {
        super(type, level);
        this.destroyPercentOfTower = BattleTowersConfig.towerCrumblePercent.get();
    }

    public DestroyTowerEntity(GolemType golemType, BlockPos golemSpawn, Level level) {
        super(BTEntityTypes.DESTROY_TOWER, level);

        this.golemType = golemType;
        this.destroyPercentOfTower = BattleTowersConfig.towerCrumblePercent.get();
        
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
                    if (this.level.getBlockState(blockToAdd) != Blocks.AIR.defaultBlockState()
                            && this.level.getBlockState(blockToAdd).getFluidState() != Fluids.FLOWING_WATER.defaultFluidState()) {
                        this.blocksToRemove.add(blockToAdd);
                        // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockToAdd);
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
        this.setCrumbleBottom(this.getCrumbleStart().getY() - (int)Math.round(this.specs.getHeight() * this.destroyPercentOfTower));
        this.rows = (int) Math.floor((this.getCrumbleStart().getY() - this.getCrumbleBottom()) / 3);
        this.initialized = true;

        // this.level.setBlock(this.getCrumbleStart(), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
        // this.level.setBlock(this.getCrumbleStart().offset(15, 1, 15), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
        // this.level.setBlock(this.getCrumbleStart().offset(30, 1, 30), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
    }

    
    @Override
    public void tick() {
    	if(this.level.isClientSide()) {
    		return;
    	}
        super.tick();
        if (this.checkForGolem) {

            BTGolemEntityAbstract golem = this.level.getNearestEntity(BTGolemEntityAbstract.class, TargetingConditions.DEFAULT, null, this.getX(), this.getY(), this. getZ(), this.getBoundingBox().inflate(15.0D, 10.0D, 15.0D));
            if (golem == null) {
                this.setGolemDead(true);

            } else {
                this.setGolemDead(false);
                this.currentTicks = 0;
            }
        }
        if (this.golemDead) {
            this.currentTicks++;

            // Check to see if data parameters have been initialized.
            if (!this.initialized) {
                this.init();
            }
            if (this.currentTicks == 1) {

                for (ServerPlayer player : this.level.getServer().overworld().players()
                     ) {
                    player.connection.handleChat(new ServerboundChatPacket("/gamerule sendCommandFeedback false"));
                    player.connection.handleChat(new ServerboundChatPacket("/title @a[distance=0..2] times 30 40 20"));
                    player.connection.handleChat(new ServerboundChatPacket("/title @a[distance=0..2] title \"\""));
                    player.connection.handleChat(new ServerboundChatPacket("/title @a[distance=0..2] subtitle {\"text\":\"" + this.specs.getCapitalizedName()
                            + " Guardian Has Fallen\",\"color\":\"" + this.specs.getColorCode() + "\"}"));
                }
                this.level.playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 6.0F, 1F);
            } else if (this.currentTicks == 400) {
                for (ServerPlayer player : this.level.getServer().overworld().players()
                ) {
                    player.connection.handleChat(new ServerboundChatPacket("/title @a[distance=0..2] title \"\""));
                    player.connection.handleChat(new ServerboundChatPacket("/title @a[distance=0..2] subtitle {\"text\":\"Without it's energy... "
                            + "\",\"color\":\"#aaaaaa\"}"));

                }
                this.level.playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 6.0F, 1F);

            }  else if (this.currentTicks == 500) {
                for (ServerPlayer player : this.level.getServer().overworld().players()
                ) {
                    player.connection.handleChat(new ServerboundChatPacket("/title @p[distance=0..2] title \"\""));
                    player.connection.handleChat(new ServerboundChatPacket("/title @a[distance=0..2] subtitle {\"text\":\""
                            + "The tower will collapse...\",\"color\":\"#aa0000\"}"));
                    player.connection.handleChat(new ServerboundChatPacket("/gamerule sendCommandFeedback true"));
                }
            }else if (this.currentTicks == 600) {
                this.level.playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 6.0F, 1F);
            }


            // if the current number of ticks is greater than the wait time before the crumbling starts
            // also check that the number of ticks is equal to the crumble speed (so that this isn't called every tick)
            if (this.currentTicks > this.startTicks && this.currentTicks % this.getCrumbleSpeed() == 0 && this.getCurrentRow() < this.rows) {
                if (this.currentTicks % 240 == 0) {
                    this.level.playSound(null, this.getCrumbleStart().below(this.getCurrentRow()*3),
                            BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 6F, 1F);
                }
                if (this.blocksToRemove.size() == 0) {
                    this.getNextRow();
                } else {
                   // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                    if(this.random.nextDouble() <= 0.125 && this.removeBlock != null) {
                    	//Fancy physics stuff
                        // TODO ONCE ENTITY IS REWORKED, ONLY CALL ONCE PER FLOOR
                    	ExplosionPhysicsEntity explosion = new ExplosionPhysicsEntity(BTEntityTypes.PHYSICS_EXPLOSION, this.level);
                    	explosion.setPos(this.removeBlock.getX(), this.removeBlock.getY(), this.removeBlock.getZ());
                    	
                    	this.level.addFreshEntity(explosion);

                    }
                	for (int i = 0; i < 35; i++) {
                        if (this.blocksToRemove.isEmpty()) {
                            break;
                        } else if (this.blocksToRemove.size() == 1) {
                            this.removeBlock = this.blocksToRemove.remove(0);
                            this.level.setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(),  2);
                        } else {
                            this.removeBlock = this.blocksToRemove.remove(this.random.nextInt(this.blocksToRemove.size() - 1));
                            //If the block is water => Add dirt around it and add the surroundings to the list
                            //TODO: Change this to remove the entire body of water
                            if (this.level.getBlockState(removeBlock).getFluidState().isSource()) {
                                this.removeBodyOfWater(this.removeBlock);
                            }
                            this.level.setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), 64);

                        }


                    }
                    if (this.currentTicks % 6 == 0) {
                        this.level.gameEvent(GameEvent.EXPLODE, this.removeBlock);
                        this.level.playSound(null, this.removeBlock, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,
                                2.0F, 1.0F);
                    }

                }
            } else if (this.getCurrentRow() >= this.rows){
                // stop if we have done the final row already
                BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG, "In Ending Sequence");
                this.getNextRow();
                for (int i = 0; i < 60; i++) {
                    if (this.blocksToRemove.isEmpty()) {
                        break;
                    } else if (this.blocksToRemove.size() == 1) {
                        this.removeBlock = this.blocksToRemove.remove(0);
                        this.level.setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), 2);
                    } else {
                        this.removeBlock = this.blocksToRemove.remove(this.random.nextInt(this.blocksToRemove.size() - 1));
                        if (this.level.getBlockState(removeBlock).getFluidState().isSource()) {
                            this.removeBodyOfWater(this.removeBlock);
                        }
                        this.level.setBlock(this.removeBlock, Blocks.AIR.defaultBlockState(), 2);

                    }
                }
                List<BlockPos> shouldBeEmptySpace = new ArrayList<>();
                Integer yForClear = this.getCrumbleStart().getY();
                for (int y = yForClear; y > this.getCrumbleBottom() + 3; y--) {
                    for (int x = this.getCrumbleStart().getX(); x <= this.getCrumbleStart().getX() + 30; x++) {
                        for(int z = this.getCrumbleStart().getZ(); z <= this.getCrumbleStart().getZ() + 30; z++) {
                            BlockPos blockToAdd = new BlockPos(x, y, z);
                            if (this.level.getBlockState(blockToAdd) != Blocks.AIR.defaultBlockState()
                                    && this.level.getBlockState(blockToAdd).getFluidState() != Fluids.FLOWING_WATER.defaultFluidState()) {
                                shouldBeEmptySpace.add(blockToAdd);
                                // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, blockToAdd);
                            }
                        }
                    }
                    //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.blocksToRemove.size());
                }
                for (BlockPos clear: shouldBeEmptySpace) {
                    this.level.setBlock(clear, Blocks.AIR.defaultBlockState(), 2);
                }
                this.remove(RemovalReason.DISCARDED);
            }

            // log the current ticks, crumble speed, and row
            if (this.currentTicks % 30 == 0 ){
                BrassAmberBattleTowers.LOGGER.log(org.apache.logging.log4j.Level.DEBUG, this.currentTicks + " Ticks | CrumbleSpeed " +
                        this.getCrumbleSpeed() + "  Row: " + this.getCurrentRow());
            }
        }
    }


    private void removeBodyOfWater(BlockPos start) {
    	Set<BlockPos> waterPositions = new HashSet<>();
    	removeBodyOWater(waterPositions, start);
    	
    	waterPositions.forEach((pos) -> this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2));
	}
    
    private void removeBodyOWater(Set<BlockPos> storage, BlockPos position) {
    	if(!this.level.getBlockState(position).getFluidState().isSource()) {
    		return;
    	}
    	if(!storage.contains(position)) {
    		storage.add(position);
    	} else {
    		return;
    	}
    	removeBodyOWater(storage, position.north());
    	removeBodyOWater(storage, position.east());
    	removeBodyOWater(storage, position.south());
    	removeBodyOWater(storage, position.west());
    	removeBodyOWater(storage, position.above());
    	removeBodyOWater(storage, position.below());
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
        compound.put(this.crumbleStartName, this.newDoubleList(this.getCrumbleStart().getX(), this.getCrumbleStart().getY(), this.getCrumbleStart().getZ()));
        compound.putInt(this.crumbleBottomName, this.getCrumbleBottom());
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


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
