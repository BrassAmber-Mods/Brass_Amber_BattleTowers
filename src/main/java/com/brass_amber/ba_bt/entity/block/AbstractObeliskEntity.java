package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.block.blockentity.chest.BTChestBlockEntity;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.util.TowerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.util.BTUtil.distanceTo2D;

public abstract class AbstractObeliskEntity extends Entity {
    public static final EntityDataSerializer<ObeliskState> OBELISK_STATE_SERIALIZER = EntityDataSerializer.simpleEnum(ObeliskState.class);
    private static final EntityDataAccessor<ObeliskState> OBELISK_STATE = SynchedEntityData.defineId(AbstractObeliskEntity.class, OBELISK_STATE_SERIALIZER);
    private static final EntityDataAccessor<Boolean> HAS_CRYSTAL = SynchedEntityData.defineId(AbstractObeliskEntity.class, EntityDataSerializers.BOOLEAN);

    private List<BlockPos> chestPositions = new ArrayList<>(9);
    private List<List<BlockPos>> spawnerPositions;
    private int spawnerToNextKey;
    private List<Integer> keySpawnerAmounts;
    protected List<EntityType<?>> towerMobs;

    public Class<? extends Entity> specialEnemy;
    public boolean chestsFound;
    public boolean addKeyToLoot;
    public boolean crystalSpawned;
    public boolean destructionSpawned;

    // Data Strings
    private final String golemType = "Tower";
    private final String spawnersDestroyedName = "SpawnersDestroyed";
    private final String crystalSpawnedName = "CrystalSpawned";
    private final String generationStateName = "GenerationState";
    private final String golemSpawnedName = "GolemSpawned";
    private final String golemDeadName = "GolemDead";
    private final String hasPlayerName = "HasPlayer";
    private final String destructionSpawnedName = "DestructionSpawned";

    protected MusicManager music;
    protected boolean musicPlaying;
    protected int musicDistance;

    protected TowerType towerType;
    protected int towerRange;

    protected GenerationState generationState = GenerationState.REMOVE_MOTION_BLOCKS;
    protected ObeliskState obeliskState = ObeliskState.TOWER_SETUP;
    protected List<BlockPos> toRemove;
    protected int floorDistance;
    public EntityType<?> lastSpawnerType;
    public boolean fromItem;

    protected List<List<Integer>> perFloorData;
    protected List<Integer> floorData;
    protected BTChestBlockEntity golemChest;
    protected ArrayList<String> golemChestLootTypes;
    protected ArrayList<String> towerChestLootTypes;
    protected ItemStack[] golemLoot;
    protected AABB entityCheckAABB;
    protected int enemySpawnRange;

    public AbstractObeliskEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.towerType = TowerType.getTypeForObeliskEntity(this);
        this.blocksBuilding = true; // BlocksMovement ?
        this.musicPlaying = false;
        this.musicDistance = 0;
        this.towerRange = 0;
        this.lastSpawnerType = EntityType.IRON_GOLEM;
        this.crystalSpawned = false;
        this.enemySpawnRange = 13;
        this.entityCheckAABB = this.getBoundingBox().inflate(this.towerRange, 115, this.towerRange);
        this.toRemove = new ArrayList<>();
        if (level.isClientSide()) {
            this.music = Minecraft.getInstance().getMusicManager();
        }
        this.fromItem = false;
        this.setInvulnerable(true);
        this.invulnerableTime = 999999999;
    }

    public void setFromItem() {
        this.obeliskState = ObeliskState.FROM_ITEM;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide() && this.tickCount % 20 == 0) {
            this.clientTick();
        }

        switch (this.obeliskState) {
            case TOWER_SETUP -> this.towerSetup();
            case TOWER_GENERATION -> this.towerGeneration();
            case ACTIVE_TOWER -> this.activeTower();
            case SPAWNERS_DESTROYED -> this.spawnersDestroyed();
            case GOLEM_SPAWNED -> this.golemSpawned();
            case TOWER_COLLAPSING -> this.towerCollapsing();
            case TOWER_INACTIVE -> this.towerInactive();
            default -> this.fromItem();
        }
    }

    public void clientTick() {
        ClientLevel client = (ClientLevel) this.level();
        Player player = client.getNearestPlayer(this, this.musicDistance * 1.5);
        if (player != null && this.getObeliskState().ordinal() < ObeliskState.GOLEM_SPAWNED.ordinal()) {
            if (this.distanceTo(player) <= this.musicDistance && !this.music.isPlayingMusic(this.towerType.getTowerMusic())) {
                this.music.stopPlaying();
                this.music.startPlaying(this.towerType.getTowerMusic());
                this.musicPlaying = true;
            }
        } else {
            this.music.stopPlaying(this.towerType.getTowerMusic());
        }
    }

    /************************************************** DATA SET/GET **************************************************/

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OBELISK_STATE, ObeliskState.TOWER_SETUP);
        this.entityData.define(HAS_CRYSTAL, false);
    }

    public void setObeliskState(ObeliskState state) {
        this.entityData.set(OBELISK_STATE, state);
    }

    public ObeliskState getObeliskState() {
        return this.entityData.get(OBELISK_STATE);
    }

    public void setHasCrystal(boolean spawned) {
        this.entityData.set(HAS_CRYSTAL, spawned);
    }

    public boolean getHasCrystal() {
        return this.entityData.get(HAS_CRYSTAL);
    }

    /************************************************** GENERATION **************************************************/

    public enum GenerationState {
        REMOVE_MOTION_BLOCKS(0),
        GATHER_AREA_BLOCKS(1),
        REMOVE_AREA_BLOCKS(2),
        ADD_AREA_FEATURES(3),
        FINISHED(4);

        private final int value;

        GenerationState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static BTAbstractObelisk.GenerationState getState(int value) {
            return switch (value) {
                case 1, 2 -> BTAbstractObelisk.GenerationState.GATHER_AREA_BLOCKS;
                case 3 -> BTAbstractObelisk.GenerationState.ADD_AREA_FEATURES;
                case 4 -> BTAbstractObelisk.GenerationState.FINISHED;
                default -> BTAbstractObelisk.GenerationState.REMOVE_MOTION_BLOCKS;
            };
        }
    }

    public void removeMotionActiveBlocks() {
        this.generationState = GenerationState.GATHER_AREA_BLOCKS;
    }

    public void gatherAreaBlocks() {
        this.generationState = GenerationState.REMOVE_AREA_BLOCKS;
    }

    public void removeAreaBlocks() {
        this.generationState = GenerationState.ADD_AREA_FEATURES;
    }

    public void addAreaFeatures() {
        this.generationState = GenerationState.FINISHED;
    }

    /************************************************** GENERATION **************************************************/

    public enum ObeliskState {
        FROM_ITEM(),
        TOWER_SETUP(),
        TOWER_GENERATION(),
        ACTIVE_TOWER(),
        SPAWNERS_DESTROYED(),
        GOLEM_SPAWNED(),
        TOWER_COLLAPSING(),
        TOWER_INACTIVE();


        ObeliskState() {
        }

        public static ObeliskState getState(int value) {
            return value > 0 && value < 3 ? TOWER_SETUP : ObeliskState.values()[value];
        }
    }

    public void fromItem() {
        // Always Empty
    }

    public void towerSetup() {
        BlockPos floorBottom = this.getOnPos().offset(-15, 0, -15);
        BlockPos floorTop = this.blockPosition().offset(-15, +this.floorDistance, -15);
        for (int i = 0; i < 8; i++) {
            for (BlockPos toCheck : BlockPos.betweenClosed(floorBottom, floorTop)) {
                // This is here to avoid unnecessary variable passing to checkPos()
                if (this.level().getBlockState(toCheck).getBlock() == BTBlocks.SPAWNER_MARKER.get()) {
                    // BrassAmberBattleTowers.LOGGER.debug(toCheck + " " + this.level().getBlockState(toCheck));
                    spawnersSet = this.setSpawnerBlock(toCheck.immutable(), this.checkLayer, level, spawnersSet);
                }
                this.checkPos(toCheck.immutable(), level);
                this.extraCheck(toCheck.immutable(), level);
            }
            this.checkPos(
                    BlockPos.betweenClosedStream(floorBottom, floorTop)
                            .filter(blockPos -> distanceTo2D(this, blockPos) < 13)
                            .toList(), i
            );

            floorBottom.above(this.floorDistance);
            floorTop.above(this.floorDistance);
        }
    }

    public void checkPos(List<BlockPos> blockPosList, int floorId) {
        for (BlockPos blockPos : blockPosList) {
            BlockState blockState = this.level().getBlockState(blockPos);


        }


    }

    public void towerGeneration() {
        if (this.generationState != GenerationState.FINISHED) {
            switch (this.generationState) {
                case REMOVE_MOTION_BLOCKS -> this.removeMotionActiveBlocks();
                case GATHER_AREA_BLOCKS -> this.gatherAreaBlocks();
                case REMOVE_AREA_BLOCKS -> this.removeAreaBlocks();
                case ADD_AREA_FEATURES -> this.addAreaFeatures();
            }
        }
    }

    public void activeTower() {

    }

    public void spawnersDestroyed() {

    }

    public void golemSpawned() {

    }

    public void towerCollapsing() {

    }

    public void towerInactive() {

    }
}
