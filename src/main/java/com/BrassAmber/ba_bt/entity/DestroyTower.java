package com.BrassAmber.ba_bt.entity;

import java.util.*;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
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


import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.network.NetworkHooks;

public class DestroyTower extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<BlockPos> CRUMBLE_START_CORNER = SynchedEntityData.defineId(DestroyTower.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> CRUMBLE_BOTTOM = SynchedEntityData.defineId(DestroyTower.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CRUMBLE_SPEED = SynchedEntityData.defineId(DestroyTower.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_ROW = SynchedEntityData.defineId(DestroyTower.class, EntityDataSerializers.INT);

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
    private boolean hasPlayer;
    private final CommandSourceStack source = createCommandSourceStack().withPermission(4);

    private final double destroyPercentOfTower;
    
    // Data Strings
    private final String crumbleStartName = "CrumbleStart";
    private final String crumbleBottomName = "CrumbleBottom";
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentRowName = "CurrentFloor";


    public DestroyTower(EntityType<DestroyTower> type, Level level) {
        super(type, level);
        this.destroyPercentOfTower = BattleTowersConfig.landTowerCrumblePercent.get();
    }

    public DestroyTower(GolemType golemType, BlockPos golemSpawn, Level level) {
        super(BTEntityTypes.DESTROY_TOWER, level);

        this.golemType = golemType;
        this.destroyPercentOfTower = BattleTowersConfig.landTowerCrumblePercent.get();
        
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
        this.rows = (int) Math.floor((this.getCrumbleStart().getY() - this.getCrumbleBottom()) / 3F);
        this.initialized = true;

        // this.level.setBlock(this.getCrumbleStart(), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
        // this.level.setBlock(this.getCrumbleStart().offset(15, 1, 15), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
        // this.level.setBlock(this.getCrumbleStart().offset(30, 1, 30), Blocks.ACACIA_LOG.defaultBlockState(), BlockFlags.DEFAULT);
    }

    public void doCommand(String command) {

        this.level.getServer().getCommands().performCommand(this.source, command);
    }

    public void doNoOutputCommand(String command) {
        this.level.getServer().getCommands().performCommand(this.source.withSuppressedOutput(), command);
    }

    
    @Override
    public void tick() {
    	if(this.level.isClientSide()) {
    		return;
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

            if (Collections.frequency(playersClose, Boolean.FALSE) == players.size()) {
                this.hasPlayer = false;

            }  else {
                this.hasPlayer = true;
            }
        }

        if (this.golemDead && this.hasPlayer) {
            this.currentTicks++;

            // Check to see if data parameters have been initialized.
            if (!this.initialized) {
                this.init();
            }
            if (this.currentTicks == 1) {

                this.doCommand("/gamerule sendCommandFeedback false");
                this.doCommand("/title @a times 30 40 20");
                this.doCommand("/title @a title \"\"");
                this.doCommand("/title @a subtitle {\"text\":\"" + this.specs.getCapitalizedName()
                        + " Guardian Has Fallen\",\"color\":\"" + this.specs.getColorCode() + "\"}");

                this.level.playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 6.0F, 1F);
            } else if (this.currentTicks == 400) {
                this.doCommand("/title @a title \"\"");
                this.doCommand("/title @a subtitle {\"text\":\"Without it's energy... "
                        + "\",\"color\":\"#aaaaaa\"}");
                this.level.playSound(null, this.getCrumbleStart().below(6),
                        BTSoundEvents.TOWER_BREAK_START, SoundSource.AMBIENT, 6.0F, 1F);

            }  else if (this.currentTicks == 500) {
                this.doCommand("/title @a title \"\"");
                this.doCommand("/title @a subtitle {\"text\":\""
                        + "The tower will collapse...\",\"color\":\"#aa0000\"}");
                this.doNoOutputCommand("/gamerule sendCommandFeedback true");

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
                    	ExplosionPhysics explosion = new ExplosionPhysics(BTEntityTypes.PHYSICS_EXPLOSION, this.level);
                    	explosion.setPos(this.removeBlock.getX(), this.removeBlock.getY(), this.removeBlock.getZ());
                    	this.level.addFreshEntity(explosion);

                    }
                	for (int i = 0; i < 35; i++) {
                        if (this.blocksToRemove.isEmpty()) {
                            break;
                        } else if (this.blocksToRemove.size() == 1) {
                            this.removeBlock = this.blocksToRemove.remove(0);
                            this.level.removeBlock(this.removeBlock, false);
                        } else {
                            this.removeBlock = this.blocksToRemove.remove(this.random.nextInt(this.blocksToRemove.size() - 1));
                            //If the block is water => Add dirt around it and add the surroundings to the list
                            //TODO: Change this to remove the entire body of water
                            if (this.level.getBlockState(removeBlock).getFluidState().isSource()) {
                                this.removeBodyOfWater(this.removeBlock);
                            }
                            this.level.removeBlock(this.removeBlock, false);

                        }


                    }
                    if (this.currentTicks % 6 == 0 && !this.blocksToRemove.isEmpty()) {
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
                        this.level.removeBlock(this.removeBlock, false);

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
                    this.level.removeBlock(clear, false);
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
    	
    	waterPositions.forEach((pos) -> this.level.removeBlock(pos, false));
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
