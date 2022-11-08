package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.TowerSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.BrassAmber.ba_bt.util.BTUtil.doNoOutputCommand;
import static com.BrassAmber.ba_bt.util.BTUtil.distanceTo2D;

public class OceanDestructionEntity extends  Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<Integer> CRUMBLE_SPEED = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_ROW = SynchedEntityData.defineId(OceanDestructionEntity.class, EntityDataSerializers.INT);

    //Other Parameters
    private Boolean initialized = false;
    private TowerSpecs specs;
    private GolemType golemType;

    @SuppressWarnings("FieldMayBeFinal")
    private List<BlockPos> blocksToRemove;
    private int currentRowY = -64;
    private int crumbleStop = 0;

    private int startTicks = 600;
    private int currentTicks = 0;


    private boolean golemDead = false;
    private final Random random = new Random();
    private boolean checkForGolem = true;
    private boolean hasPlayer = false;

    private ArrayList<FallingBlockEntity> fallingBlocks;

    // Data Strings
    private final String crumbleSpeedName = "CrumbleSpeed";
    private final String currentRowName = "CurrentFloor";

    public OceanDestructionEntity(EntityType<OceanDestructionEntity> type, Level level) {
        super(type, level);
        this.golemType = GolemType.OCEAN;

        this.startTicks = BattleTowersConfig.oceanTimeBeforeCollapse.get();

        this.blocksToRemove = new ArrayList<>();
        this.fallingBlocks = new ArrayList<>();
    }

    public OceanDestructionEntity(Level level) {
        super(BTEntityTypes.OCEAN_DESTRUCTION.get(), level);
        this.golemType = GolemType.OCEAN;

        this.blocksToRemove = new ArrayList<>();
    }

    public void getNextRow() {
        //BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "In getNextRow");

        // for each of the three rows add every single non-air block pos on that row to the arraylist
        for (int y = this.currentRowY; y < this.currentRowY + 4; y++) {
            for (int x = this.getBlockX() - 17; x <= this.getBlockX() + 17; x++) {
                for(int z = this.getBlockZ() - 17; z <= this.getBlockZ() + 17; z++) {
                    BlockPos blockToAdd = new BlockPos(x, y, z);
                    FluidState fluidState = this.level.getFluidState(blockToAdd);
                    if (distanceTo2D(this, blockToAdd) < 17.5D) {
                        if (!fluidState.isEmpty()) {
                            this.level.setBlock(blockToAdd, BTBlocks.BT_AIR_FILL.get().defaultBlockState(), 2);
                        } else if (!this.level.getBlockState(blockToAdd).isAir()){
                            this.blocksToRemove.add(blockToAdd);
                        }
                    }
                }
            }
            BrassAmberBattleTowers.LOGGER.info("Blocks to remove size " + this.blocksToRemove.size());
        }
        // add 1 to the row counter so that next time this is called it adds the blocks on the next row
        this.currentRowY = this.currentRowY + 4;
        this.setCurrentRow(this.getCurrentRow() + 1);
    }

    private void init() {
        BrassAmberBattleTowers.LOGGER.debug("Initializing");
        this.specs = TowerSpecs.getTowerFromGolem(this.golemType); // Get tower specifics (height, crumble speed)
        this.setCrumbleSpeed(this.specs.getCrumbleSpeed());
        this.crumbleStop = this.getBlockY() + (int)Math.round(this.specs.getHeight() * BattleTowersConfig.oceanTowerCrumblePercent.get() -1);
        this.fallingBlocks = new ArrayList<>();
        this.initialized = true;
        this.currentRowY = -64 + (this.getCurrentRow()-1) * 4;
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

        if (this.currentRowY < this.getBlockY() && this.getCrumbleSpeed() != 1) {
            this.setCrumbleSpeed(1);
        } else {
            this.setCrumbleSpeed(2);
        }
        // New list to save the id pf the entities that should be removed
        ArrayList<FallingBlockEntity> removeFallEntity = new ArrayList<>();
        for (int i = 0; i < this.fallingBlocks.size(); i++) {
            // Check each block in the Falling entities list
            FallingBlockEntity blockEntity = this.fallingBlocks.get(i);
            if (blockEntity.getY() < this.currentRowY - 10) {
                // remove if too far down
                removeFallEntity.add(this.fallingBlocks.remove(i));
            }
        }
        // Reverse list of Ids so that we remove from the end first, thereby avoiding a call to an item already removed
        Collections.reverse(removeFallEntity);
        for (int i = 0; i < removeFallEntity.size(); i++) {
            FallingBlockEntity blockEntity = removeFallEntity.remove(i);
            blockEntity.kill();
        }

        super.tick();
        if (this.checkForGolem) {

            List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(50.0D, 100.0D, 50.0D));
            boolean deadGolem = true;

            for (Entity entity: entities
            ) {
                try {
                    // noinspection unused 'golem'- is used, essentially a check for an entity that is a golem which sets the "deadGolem" value to false.
                    BTAbstractGolem golem = (BTAbstractGolem) entity;
                    deadGolem = false;
                } catch (Exception ignored) {}
            }

            if (deadGolem) {
                this.setGolemDead(true);

            } else {
                this.setGolemDead(false);
                this.currentTicks = 0;
            }
        }

        @SuppressWarnings("ConstantConditions")
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

            } else if (this.currentTicks == 500) {
                doNoOutputCommand(this,"/title @a title \"\"");
                doNoOutputCommand(this,"/title @a subtitle {\"text\":\"" + this.specs.getTitleText3()
                        + "\",\"color\":\"#aa0000\"}");

            } else if (this.currentTicks == 600) {
                this.level.playSound(null, this.blockPosition().above(6),
                        BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 4.0F, 1F);
            }

            // if the current number of ticks is greater than the wait time before the crumbling starts
            // also check that the number of ticks is equal to the crumble speed (so that this isn't called every tick)
            if (this.currentTicks > this.startTicks && this.currentTicks % this.getCrumbleSpeed() == 0) {
                if (this.currentTicks % 240 == 0) {
                    this.level.playSound(null, this.blockPosition().above(this.getCurrentRow()*4),
                            BTSoundEvents.TOWER_BREAK_CRUMBLE, SoundSource.AMBIENT, 4F, 1F);
                }

                if (this.blocksToRemove.size() < 20) {
                    this.getNextRow();
                }

                // BrassAmberBattleTowers.LOGGER.debug(this.blocksToRemove);
                int numOfBlocks = 64;

                if (this.currentRowY < this.getBlockY()) {
                     numOfBlocks = 128;
                }

                if (this.currentRowY < this.level.getSeaLevel() + 3 && this.currentRowY < this.crumbleStop){
                    // BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Removing row");
                    boolean falling = false;
                    for (int i = 0; i < numOfBlocks; i++) {
                        if (this.blocksToRemove.isEmpty()) {
                            break;
                        } else {

                            // Get random integer, if size is 1 get item at index 0
                            int randomIndex = 0;
                            if (this.blocksToRemove.size() != 1) {
                                randomIndex = this.random.nextInt(this.blocksToRemove.size() - 1);
                            }

                            // BrassAmberBattleTowers.LOGGER.debug("Remove Index = " + randomIndex);

                            BlockPos removeBlock = this.blocksToRemove.get(randomIndex);
                            BlockState state = this.level.getBlockState(removeBlock);


                            if (this.level.getBlockState(removeBlock.below()).isAir() || this.level.getBlockState(removeBlock.below()).is(Blocks.WATER)) {

                                final Vec3 velocity = new Vec3(0D, 0.5D, 0D);

                                this.level.setBlock(removeBlock, BTBlocks.BT_AIR_FILL.get().defaultBlockState(), 2);
                                if (falling) {
                                    FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
                                            this.level,
                                            removeBlock,
                                            state
                                    );

                                    fallingBlock.setInvulnerable(true);
                                    fallingBlock.dropItem = false;
                                    fallingBlock.setDeltaMovement(velocity);

                                    this.fallingBlocks.add(fallingBlock);


                                    falling = false;
                                } else {
                                    falling = true;
                                }
                                this.blocksToRemove.remove(randomIndex);
                            }

                        }

                    }

                }
            } else if (this.currentRowY >= this.level.getSeaLevel() || this.currentRowY >= this.crumbleStop){
                // stop if we have done the final row already
                BrassAmberBattleTowers.LOGGER.debug("In Ending Sequence");
                for (int y = -64; y < this.level.getSeaLevel(); y++) {
                    for (int x = this.getBlockX() - 16; x <= this.getBlockX() + 16; x++) {
                        for(int z = this.getBlockZ() - 16; z <= this.getBlockZ() + 16; z++) {
                            BlockPos blockToDelete = new BlockPos(x, y, z);
                            BlockState blockState = this.level.getBlockState(blockToDelete);
                            if (distanceTo2D(this, blockToDelete) < 15.5D) {
                                if (blockState.is(BTBlocks.BT_AIR_FILL.get()) || this.level.isWaterAt(blockToDelete)) {
                                    this.level.setBlock(blockToDelete, Blocks.AIR.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }

                this.remove(Entity.RemovalReason.DISCARDED);
            }

            // log the current ticks, crumble speed, and row
            if (this.currentTicks % 30 == 0 ){
                BrassAmberBattleTowers.LOGGER.debug(this.currentTicks + " Ticks | CrumbleSpeed " +
                        this.getCrumbleSpeed() + "   " + this.getCurrentRow() + " Row | Row Y " +
                        this.currentRowY + " Crumble Stop " + this.crumbleStop);
            }
        }
    }

    /**************************************************** DATA ****************************************************/



    @Override
    protected void defineSynchedData() {
        this.entityData.define(CRUMBLE_SPEED,  0);
        this.entityData.define(CURRENT_ROW, 0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt(this.crumbleSpeedName, this.getCrumbleSpeed());
        compound.putInt(this.currentRowName, this.getCurrentRow());
        compound.putString("GolemType", this.golemType.getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
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

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
