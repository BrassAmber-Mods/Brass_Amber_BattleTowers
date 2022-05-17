package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;
import com.BrassAmber.ba_bt.entity.hostile.BTCultist;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.init.BTItems;
import com.BrassAmber.ba_bt.sound.BTMusics;
import com.BrassAmber.ba_bt.util.BTUtil;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.BrassAmber.ba_bt.util.BTUtil.doNoOutputPostionedCommand;
import static com.BrassAmber.ba_bt.util.BTUtil.horizontalDistanceTo;

public class BTObelisk extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<Integer> TOWER = SynchedEntityData.defineId(BTObelisk.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SPAWNERS_DESTROYED = SynchedEntityData.defineId(BTObelisk.class, EntityDataSerializers.INT);

    private final List<List<Integer>> towerSpawnerAmounts = Arrays.asList(
            Arrays.asList(2, 2, 2, 2, 3, 3, 3, 4),
            Arrays.asList(2, 2, 2, 3, 3, 3, 4, 4),
            Arrays.asList(2, 2, 3, 3, 3, 4, 4, 4),
            Arrays.asList(3, 3, 3, 3, 3, 4, 4, 5),
            Arrays.asList(3, 3, 3, 3, 4, 4, 4, 5),
            Arrays.asList(3, 3, 3, 4, 4, 4, 5, 5)
    );
    private List<BlockPos> CHESTS = new ArrayList<>(9);
    private List<List<BlockPos>> SPAWNERS;


    //Other Parameters
    private boolean initialized;
    private boolean hasPlayer;
    private int checkLayer;
    private int currentFloorY;
    private int spawnersFound;
    private int totalSpawners;
    private boolean createSpawnerList;
    private boolean doCheck;


    private GolemType golemType;
    private boolean justSpawnedKey;

    // Data Strings
    private final String towerName = "Tower";
    private final String spawnersDestroyedName = "SpawnersDestroyed";
    private final String golemTypeName = "GolemType";
    private final String updatedSandName = "UpdatedSand";

    private int timeSinceAmbientMusic;
    private int lastMusicStart;
    private boolean canCheck;

    public BTObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.initialized = false;
        this.checkLayer = 1;
        this.currentFloorY = this.getBlockY() - 1;
        // this.blocksBuilding = true;
        this.timeSinceAmbientMusic = 7000;
        this.lastMusicStart = 0;
        this.createSpawnerList = true;
        this.doCheck = true;

    }

    public BTObelisk(GolemType golemType, Level level) {
        this(GolemType.getObeliskFor(golemType), level);
        this.golemType = golemType;
        this.setTower(GolemType.getNumForType(golemType));
    }

    public void findChestsAndSpawners(Level level) {
        if (this.checkLayer == 1) {
            this.currentFloorY = this.getBlockY() - 1;
        }

        BlockPos center = this.getOnPos();
        int currentFloorTopY = this.currentFloorY + 11;

        BrassAmberBattleTowers.LOGGER.info("Floor y: " + this.currentFloorY + " Top y: " + currentFloorTopY);
        BlockPos corner = center.offset(-16, 0, -16);
        BlockPos oppositeCorner = center.offset(16, 0, 16);

        for (int x = corner.getX(); x < oppositeCorner.getX(); x++) {
            for (int z = corner.getZ(); z < oppositeCorner.getZ(); z++) {
                for (int y = currentFloorY; y <= currentFloorTopY; y++) {
                    this.checkPos(new BlockPos(x, y, z), level);
                    this.updateSand(new BlockPos(x, y, z), level);
                }
            }
        }

        if (this.CHESTS.size() != this.checkLayer) {
            this.CHESTS.add(null);
        }

        if (this.checkLayer == 8) {
            this.initialized = true;
        }
        else {
            this.checkLayer += 1;
            this.currentFloorY = currentFloorTopY;
            this.spawnersFound = 0;
        }

    }

    public void checkPos(BlockPos toCheck, Level level) {
        try {
            Block block = level.getBlockState(toCheck).getBlock();
            if (block == BTBlocks.LAND_CHEST.get()) {
                this.CHESTS.add(toCheck);
                // BrassAmberBattleTowers.LOGGER.info("Found chest");
            } else if (block == BTBlocks.BT_LAND_SPAWNER.get()) {
                this.SPAWNERS.get(this.checkLayer-1).add(toCheck);
                // BrassAmberBattleTowers.LOGGER.info("Found spawner: " + this.checkLayer + " " + this.spawnersFound);
                BrassAmberBattleTowers.LOGGER.info(this.SPAWNERS.get(this.checkLayer-1).size());
                this.spawnersFound += 1;
            }
        } catch (Exception e) {
            BrassAmberBattleTowers.LOGGER.info("Exception in Obelisk class, not a chest or spawner: " + level.getBlockState(toCheck).getBlock());
            e.printStackTrace();

        }
    }

    public void updateSand(BlockPos toUpdate, Level level) {
        if (level.getBlockState(toUpdate) == Blocks.SAND.defaultBlockState()) {
            level.removeBlock(toUpdate, false);
            BrassAmberBattleTowers.LOGGER.info("Sand? :" + level.getBlockState(toUpdate));
            level.setBlockAndUpdate(toUpdate, Blocks.SAND.defaultBlockState());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.timeSinceAmbientMusic < 7000) {
            this.timeSinceAmbientMusic = this.tickCount - this.lastMusicStart;
        }

        if (this.level.isClientSide()) {
            if (((ClientLevel)this.level).players().size() == 0) {
                return;
            }
            double playerDistance = horizontalDistanceTo(this, ((ClientLevel)this.level).players().get(0));
            boolean hasClientPlayer = playerDistance < 30;
            MusicManager music = ((ClientLevel) this.level).minecraft.getMusicManager();

            if (!music.isPlayingMusic(BTMusics.GOLEM_FIGHT)) {
                // BrassAmberBattleTowers.LOGGER.info("Player: " + hasClientPlayer + " time since music: " + this.timeSinceAmbientMusic);
                if (hasClientPlayer && this.timeSinceAmbientMusic == 7000 && playerDistance < 17) {
                    music.nextSongDelay = 6900;
                    music.stopPlaying();
                    ((ClientLevel) this.level).minecraft.getMusicManager().startPlaying(BTMusics.TOWER);
                    this.lastMusicStart = this.tickCount;
                    this.timeSinceAmbientMusic = 0;
                } else if (!hasClientPlayer && music.isPlayingMusic(BTMusics.TOWER)) {
                    music.nextSongDelay = 500;
                    music.stopPlaying();
                    this.timeSinceAmbientMusic = 7000;
                }
            }
            return;
        }

        if (!this.initialized) {
            // BrassAmberBattleTowers.LOGGER.info("Finding Chests for layer: " + this.checkLayer + "  At block level: " + this.currentFloorY);
            if (this.createSpawnerList) {
                List<Integer> spawnerAmounts = this.towerSpawnerAmounts.get(this.getTower());
                this.SPAWNERS = Arrays.asList(new ArrayList<>(spawnerAmounts.get(0)), new ArrayList<>(spawnerAmounts.get(1)),
                        new ArrayList<>(spawnerAmounts.get(2)), new ArrayList<>(spawnerAmounts.get(3)),
                        new ArrayList<>(spawnerAmounts.get(4)), new ArrayList<>(spawnerAmounts.get(5)),
                        new ArrayList<>(spawnerAmounts.get(6)), new ArrayList<>(spawnerAmounts.get(7)));
                for (int num:  spawnerAmounts) {
                    this.totalSpawners += num;
                }
                this.createSpawnerList = false;
            }
            this.findChestsAndSpawners(this.level);
            return;
        }

        if (this.doCheck) {
            try {
                List<?> list = this.level.getEntitiesOfClass(BTMonolith.class, this.getBoundingBox().inflate(15, 110, 15));
                this.canCheck = list.size() != 0;
                if (!this.canCheck) {
                    try {
                        List<?> list2 = this.level.getEntitiesOfClass(BTAbstractGolem.class, this.getBoundingBox().inflate(15, 110, 15));
                        this.canCheck = list2.size() != 0;
                    } catch (Exception f) {
                        BrassAmberBattleTowers.LOGGER.info("Exception finding Golem: " + f);
                    }
                }
            } catch (Exception e) {
                BrassAmberBattleTowers.LOGGER.info("Exception finding Monolith: " + e);
            }
        }

        if (canCheck) {
            List<ServerPlayer> players = Objects.requireNonNull(this.level.getServer()).getPlayerList().getPlayers();
            List<Boolean> playersClose = new ArrayList<>();
            for (ServerPlayer player : players
            ) {
                if (horizontalDistanceTo(this, player) < 30) {
                    playersClose.add(Boolean.TRUE);
                    // BrassAmberBattleTowers.LOGGER.info("Player " +  this.horizontalDistanceTo(player) + " blocks away");
                } else {
                    playersClose.add(Boolean.FALSE);
                }

            }

            this.hasPlayer = Collections.frequency(playersClose, Boolean.TRUE) > 0;

            int timeCheck = this.random.nextInt(4,6) * 10;

            if (this.tickCount % timeCheck == 0) {
                List<BTCultist> cultists = this.level.getEntitiesOfClass(BTCultist.class, this.getBoundingBox().inflate(15, 110, 15));
                if (cultists.size() < 10) {
                    int floor = this.blockPosition().getY() + this.random.nextInt(0,8) * 11;
                    int x = this.blockPosition().getX() + this.random.nextInt(-12, 12);
                    int y = floor + this.random.nextInt(0, 9);
                    int z = this.blockPosition().getZ() + this.random.nextInt(-12, 12);

                    this.createCultistEntity((ServerLevel) this.level, new BlockPos(x, y, z));
                }
            }

            if (this.tickCount % 20 == 0 && this.hasPlayer) {
                // BrassAmberBattleTowers.LOGGER.info("Checking Spawners");
                this.checkSpawners(this.level);
            }
        }

    }

    protected void createCultistEntity(ServerLevel serverWorld, BlockPos spawn) {
        // BrassAmberBattleTowers.LOGGER.info("Trying to spawn cultist at: " + spawn);
        double distance = horizontalDistanceTo(this, spawn.getX(), spawn.getZ());
        boolean canSpawn = SpawnPlacements.checkSpawnRules(BTEntityTypes.BT_CULTIST.get(), serverWorld, MobSpawnType.EVENT, spawn, this.random);

        if (canSpawn && (distance < 11.5D) && serverWorld.getBlockState(spawn.above()).isAir()) {
            Entity entity = BTEntityTypes.BT_CULTIST.get().create(serverWorld);
            if (entity instanceof  BTCultist cultist) {
                cultist.setPos(spawn.getX(), spawn.getY(), spawn.getZ());
                cultist.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.TRIGGERED, null, null);
                serverWorld.addFreshEntity(cultist);
                // BrassAmberBattleTowers.LOGGER.info("Success");
            }
        } else {
            // BrassAmberBattleTowers.LOGGER.info("Fail");
        }
    }

    private void checkSpawners(Level level) {
        if (this.CHESTS.size() != 0 && this.SPAWNERS.size() != 0) {
            for (int i = 0; i < this.SPAWNERS.size(); i++) {
                if (this.SPAWNERS.get(i).size() == 0) {
                    if (this.CHESTS.get(i) != null && level.getBlockEntity(this.CHESTS.get(i)) instanceof TowerChestBlockEntity chestBlockEntity) {
                        if (!chestBlockEntity.isUnlocked()) {
                            chestBlockEntity.setUnlocked(true);
                            this.chestUnlockingSound(level);
                        }
                    }
                } else {
                    List<BlockPos> poss = this.SPAWNERS.get(i);
                    for (int x = 0; x < poss.size(); x++) {
                        BlockPos blockPos = poss.get(x);
                        if (!(level.getBlockState(blockPos).is(BTBlocks.BT_LAND_SPAWNER.get()))) {
                            this.SPAWNERS.get(i).remove(blockPos);
                            this.setSpawnersDestroyed(this.getSpawnersDestroyed() + 1);
                            BrassAmberBattleTowers.LOGGER.info(this.getSpawnersDestroyed());
                        }
                    }
                    if (!this.justSpawnedKey && (this.getSpawnersDestroyed() == 6 || this.getSpawnersDestroyed() == 14 || this.getSpawnersDestroyed() == this.totalSpawners)) {
                        if (level.getBlockEntity(this.CHESTS.get(i)) instanceof ChestBlockEntity chest) {
                            chest.setLootTable(BrassAmberBattleTowers.locate("chests/land_tower/" + (i+1) + "key"), this.random.nextLong());
                        }
                        else {
                            doNoOutputPostionedCommand(this, "give @p ba_bt:" + GolemType.getKeyFor(this.golemType).getRegistryName(), new Vec3(this.blockPosition().getX(), this.blockPosition().getY() + (11 * i), this.blockPosition().getZ()));
                        }
                        this.justSpawnedKey = true;
                    }
                    else if (justSpawnedKey && (this.getSpawnersDestroyed() == 7 || this.getSpawnersDestroyed() == 15 || this.getSpawnersDestroyed() == this.totalSpawners + 1)) {
                        this.justSpawnedKey = false;
                    }
                }
            }
        }
        if (this.SPAWNERS.size() == 0) {
            this.doCheck = false;
            this.canCheck = false;
        }
    }

    private void chestUnlockingSound(Level level) {
        List<ServerPlayer> players = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers();
        for (ServerPlayer player: players) {
            if (horizontalDistanceTo(this, player) < 30) {
                level.playSound(null, player.blockPosition(), SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1f, 1.5f);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TOWER, 0);
        this.entityData.define(SPAWNERS_DESTROYED, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setTower(tag.getInt(this.towerName));
        this.setSpawnersDestroyed(tag.getInt(this.spawnersDestroyedName));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt(this.towerName, this.getTower());
        tag.putInt(this.spawnersDestroyedName, this.getSpawnersDestroyed());
        if (this.level.isClientSide()) {
            ((ClientLevel) this.level).minecraft.getMusicManager().stopPlaying();
        }
    }

    /*************************************** Characteristics & Properties *******************************************/

    /**
     * Called when a user uses the creative pick block button on this entity.
     * @return An ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
     * (Empty ItemStack is an ItemStack of '(Item) null')
     */
    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack((Item) null);
    }


    /**
     * {@link PushReaction.IGNORE} is the only valid option for an entity I think to stop piston interaction
     * TODO I want this to Block the pistons movement
     *
     * Used in: {@link PistonTileEntity.moveCollidedEntities method}
     */
    @SuppressWarnings("JavadocReference")
    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     * (Like arrows and stuff.)
     */
    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    /**
     * Block movement through this entity
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /***************************************************** Breaking *************************************************/

    /**
     * Called by the /kill command.
     */
    @Override
    public void kill() {
        Player player = this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 50, EntitySelector.NO_SPECTATORS);

        if (player != null && player.isCreative()) {
            BrassAmberBattleTowers.LOGGER.info("Item: " + player.getItemInHand(InteractionHand.MAIN_HAND).getItem());
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.CLAY_BALL) {
                this.remove(RemovalReason.KILLED);
            } else {
                // Do nothing to prevent people deleting a Monolith by accident.
                BrassAmberBattleTowers.LOGGER.info("Used the /kill command. However, an Obelisk has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
            }
        }
        else {
            // Do nothing to prevent people deleting a Monolith by accident.
            BrassAmberBattleTowers.LOGGER.info("Used the /kill command. However, an Obelisk has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
        }

    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!(source.getMsgId().equals("player"))) {
            return false;
        } else {
            if (this.isAlive() && !this.level.isClientSide() && source.isCreativePlayer()) {
                Player player = (Player) source.getEntity();
                if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.CLAY_BALL) {
                    this.remove(RemovalReason.KILLED);
                }
            }
            return true;
        }
    }

    @Override
    public MobCategory getClassification(boolean forSpawnCount) {
        return MobCategory.AMBIENT;
    }

    /************************************************** DATA SET/GET **************************************************/

    public void setTower(int num) {
        this.entityData.set(TOWER, num);
    }

    public int getTower() {
        return this.entityData.get(TOWER);
    }

    public void setSpawnersDestroyed(int num) {
        this.entityData.set(SPAWNERS_DESTROYED, num);
    }

    public int getSpawnersDestroyed() {
        return this.entityData.get(SPAWNERS_DESTROYED);
    }

    /************************************************** COMMANDS **************************************************/



    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
