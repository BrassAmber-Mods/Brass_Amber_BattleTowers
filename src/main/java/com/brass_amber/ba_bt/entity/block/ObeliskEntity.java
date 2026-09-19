package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.registries.EntityData;
import com.brass_amber.ba_bt.registries.LocalModification.GenerationState;
import com.brass_amber.ba_bt.registries.LocalModification;
import com.brass_amber.ba_bt.registries.TowerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class ObeliskEntity extends Entity {
    public static final EntityDataSerializer<ObeliskState> OBELISK_STATE_SERIALIZER = EntityDataSerializer.simpleEnum(ObeliskState.class);
    private static final EntityDataAccessor<ObeliskState> OBELISK_STATE = SynchedEntityData.defineId(ObeliskEntity.class, OBELISK_STATE_SERIALIZER);
    private static final EntityDataAccessor<Boolean> HAS_CRYSTAL = SynchedEntityData.defineId(ObeliskEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState powered = new AnimationState();

    public EntityData btEntityData;

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
    private final String spawnersDestroyedName = "SpawnersDestroyed";
    private final String crystalSpawnedName = "CrystalSpawned";
    private final String generationStateName = "GenerationState";
    private final String towerStateName = "GenerationState";
    private final String golemSpawnedName = "GolemSpawned";
    private final String golemDeadName = "GolemDead";

    protected MusicManager music;
    protected boolean musicPlaying;
    protected int towerRange;

    protected GenerationState generationState = GenerationState.REMOVE_MOTION_BLOCKS;
    protected ObeliskState obeliskState = ObeliskState.TOWER_SETUP;
    protected List<BlockPos> blocksToRemove;

    public EntityType<?> lastSpawnerType;

    protected AABB entityCheckAABB;

    public ObeliskEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true; // BlocksMovement ?
        this.towerRange = 0;
        this.lastSpawnerType = EntityType.IRON_GOLEM;
        this.crystalSpawned = false;
        this.entityCheckAABB = this.getBoundingBox().inflate(this.towerRange, 115, this.towerRange);
        this.blocksToRemove = new ArrayList<>();
        if (level.isClientSide()) {
            this.music = Minecraft.getInstance().getMusicManager();
        }
        this.setInvulnerable(true);
        this.invulnerableTime = 999999999;
    }

    public void setBtEntityData(ResourceKey<TowerData> dataKey) {
        Registry<TowerData> towerDataReg = this.level().getServer().registryAccess().registryOrThrow(BTRegistries.Keys.TOWER_DATA);
        TowerData towerData = towerDataReg.getOrThrow(dataKey);
        this.btEntityData = towerData.getEntityData();

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

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    public void clientTick() {
        ClientLevel client = (ClientLevel) this.level();
        Player player = client.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 50, true);
        if (player != null && this.getObeliskState().ordinal() < ObeliskState.GOLEM_SPAWNED.ordinal()) {
            if (!this.music.isPlayingMusic(this.btEntityData.getTowerMusic())) {
                this.music.stopPlaying();
                this.music.startPlaying(this.btEntityData.getTowerMusic());
                this.musicPlaying = true;
            }
        } else {
            this.music.stopPlaying(this.btEntityData.getTowerMusic());
        }
    }

    /**
     * Ensure music is stopped when unloaded.
     **/
    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.level().isClientSide() ) {
            if (this.music.isPlayingMusic(this.btEntityData.getTowerMusic())) {
                this.music.stopPlaying();
            }
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
        /*
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
        */
    }

    public void checkPos(List<BlockPos> blockPosList, int floorId) {
        for (BlockPos blockPos : blockPosList) {
            BlockState blockState = this.level().getBlockState(blockPos);


        }


    }

    public void towerGeneration() {
        if (this.btEntityData.getLocalModification().generationState != LocalModification.GenerationState.FINISHED) {
            this.btEntityData.getLocalModification().run(this);
        }
    }

    public void activeTower() {

    }

    public void checkSpawners() {

    }

    public void addChestLoot(RandomizableContainerBlockEntity containerBlockEntity, ResourceLocation lootTable) {
        containerBlockEntity.setLootTable(lootTable, this.level().random.nextLong());
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
