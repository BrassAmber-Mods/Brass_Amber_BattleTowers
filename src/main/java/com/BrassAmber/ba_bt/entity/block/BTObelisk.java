package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.block.tileentity.BTSpawnerBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.sound.BTMusics;
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
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BTObelisk extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<Integer> TOWER = SynchedEntityData.defineId(BTObelisk.class, EntityDataSerializers.INT);
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
    private MusicManager musicManager;
    private boolean hasPlayer;
    private boolean hasMusicPlayer;

    private int checkLayer;
    private int currentFloorY;
    private int spawnersFound;

    // Data Strings
    private final String towercenterName = "TowerCenter";
    private final String towerName = "Tower";

    private final CommandSourceStack source = createCommandSourceStack().withPermission(4);


    public BTObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.initialized = false;
        this.hasMusicPlayer = false;
        this.checkLayer = 1;
        this.currentFloorY = this.getOnPos().getY();
        // this.blocksBuilding = true;
        List<Integer> spawnerAmounts = this.towerSpawnerAmounts.get(this.getTower());
        SPAWNERS = Arrays.asList(new ArrayList<>(spawnerAmounts.get(0)), new ArrayList<>(spawnerAmounts.get(1)),
                new ArrayList<>(spawnerAmounts.get(2)), new ArrayList<>(spawnerAmounts.get(3)),
                new ArrayList<>(spawnerAmounts.get(4)), new ArrayList<>(spawnerAmounts.get(5)),
                new ArrayList<>(spawnerAmounts.get(6)), new ArrayList<>(spawnerAmounts.get(7)));

    }

    public BTObelisk(GolemType golemType, Level level) {
        this(GolemType.getObeliskFor(golemType), level);
        this.setTower(GolemType.getNumForType(golemType));
        List<Integer> spawnerAmounts = this.towerSpawnerAmounts.get(this.getTower());
        SPAWNERS = Arrays.asList(new ArrayList<>(spawnerAmounts.get(0)), new ArrayList<>(spawnerAmounts.get(1)),
                new ArrayList<>(spawnerAmounts.get(2)), new ArrayList<>(spawnerAmounts.get(3)),
                new ArrayList<>(spawnerAmounts.get(4)), new ArrayList<>(spawnerAmounts.get(5)),
                new ArrayList<>(spawnerAmounts.get(6)), new ArrayList<>(spawnerAmounts.get(7)));
    }

    public void findChestsAndSpawners(Level level) {
        BlockPos center = this.getOnPos();
        int currentFloorTopY = this.currentFloorY + 10;
        BlockPos corner = center.offset(-15, 0, -15);
        BlockPos oppositeCorner = center.offset(15, 0, 15);

        for (int x = corner.getX(); x < oppositeCorner.getX(); x++) {
            for (int z = corner.getZ(); z < oppositeCorner.getZ(); z++) {
                for (int y = currentFloorY; y <= currentFloorTopY; y++) {
                    this.checkPos(new BlockPos(x, y, z), level);
                }
            }
        }

        if (this.checkLayer == 8) {
            this.initialized = true;
        }
        else {
            this.checkLayer += 1;
            this.currentFloorY = currentFloorTopY + 1;
            this.spawnersFound = 0;
        }

    }

    public void checkPos(BlockPos toCheck, Level level) {
        try {
            Block block = level.getBlockState(toCheck).getBlock();
            if (block == BTBlocks.LAND_CHEST.get() || block == BTBlocks.LAND_GOLEM_CHEST.get()) {
                this.CHESTS.set(checkLayer-1, toCheck);
                BrassAmberBattleTowers.LOGGER.info("Found chest");
            } else if (block == BTBlocks.BT_LAND_SPAWNER.get()) {
                this.SPAWNERS.get(this.checkLayer-1).set(this.spawnersFound, toCheck);
                BrassAmberBattleTowers.LOGGER.info("Found spawner: " + this.checkLayer + " " + this.spawnersFound);
                this.spawnersFound += 1;
            }
        } catch (Exception e) {
            BrassAmberBattleTowers.LOGGER.info("Exception in Obelisk class, not a chest or spawner: " + level.getBlockState(toCheck).getBlock());
            e.printStackTrace();

        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide()) {
            if (!this.hasMusicPlayer) {
                this.musicManager = Minecraft.getInstance().getMusicManager();
                this.hasMusicPlayer = true;
            }
            return;
        }
        if (!this.initialized) {
            BrassAmberBattleTowers.LOGGER.info("Finding Chests for layer: " + this.checkLayer + "  At block level: " + this.currentFloorY);
            this.findChestsAndSpawners(this.level);
        }
        else {
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

                } else {
                    this.hasPlayer = true;
                }
            }

            if (this.tickCount % 10 == 0 && this.hasPlayer) {
                BrassAmberBattleTowers.LOGGER.info("Checking Spawners");
                this.checkSpawners(this.level);
            }

            if (this.tickCount == 0 || this.tickCount % 7200 == 0 && this.hasPlayer) {
                if (!this.musicManager.isPlayingMusic(BTMusics.GOLEM_FIGHT) && this.hasMusicPlayer) {
                    this.musicManager.stopPlaying();
                    this.musicManager.startPlaying(BTMusics.TOWER);
                }
            }
        }
    }


    private void checkSpawners(Level level) {
        int f = 0;
        for (int i = 0; i < this.SPAWNERS.size(); i++) {
            if (this.SPAWNERS.get(i).size() == 0) {
                if (level.getBlockEntity(this.CHESTS.get(i)) instanceof GolemChestBlockEntity chestBlockEntity) {
                    if (!chestBlockEntity.isUnlocked()) {
                        chestBlockEntity.setUnlocked(true);
                        this.chestUnlockingSound(level);
                    }
                    f++;
                }
            } else {
                this.SPAWNERS.get(i).removeIf(pos -> !(level.getBlockState(pos).is(BTBlocks.BT_LAND_SPAWNER.get())));
            }
        }

        if (this.SPAWNERS.size() == f) {
            if (level.getBlockEntity(this.CHESTS.get(7)) instanceof GolemChestBlockEntity chestBlockEntity) {
                chestBlockEntity.setUnlocked(true);
                this.chestUnlockingSound(level);
            }
        }
    }

    private void chestUnlockingSound(Level level) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
        for (ServerPlayer player: players) {
            if (this.horizontalDistanceTo(player) < 30) {
                level.playSound(null, player.blockPosition(), SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1f, 1.5f);
            }
        }
    }

    /**
     * Returns the horizontal distance.
     */
    public double horizontalDistanceTo(Entity entity) {
        double dX = this.getX() - entity.getX();
        double dZ = this.getZ() - entity.getZ();
        return Math.abs(Math.sqrt(dX * dX + dZ * dZ));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TOWER, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setTower(tag.getInt(this.towerName));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt(this.towerName, this.getTower());
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

    public void setTower(int num) {
        this.entityData.set(TOWER, num);
    }

    public int getTower() {
        return this.entityData.get(TOWER);
    }

    /************************************************** COMMANDS **************************************************/

    public void doCommand(String command) {

        this.level.getServer().getCommands().performCommand(this.source, command);
    }

    public void doNoOutputCommand(String command) {
        this.level.getServer().getCommands().performCommand(this.source.withSuppressedOutput(), command);
    }
}
