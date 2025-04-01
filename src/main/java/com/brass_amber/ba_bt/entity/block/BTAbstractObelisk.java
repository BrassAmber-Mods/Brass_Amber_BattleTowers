package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.block.block.BTSpawnerBlock;
import com.brass_amber.ba_bt.block.blockentity.DataMarkerBlockEntity;
import com.brass_amber.ba_bt.block.blockentity.BTChestBlockEntity;
import com.brass_amber.ba_bt.block.blockentity.spawner.BTAbstractSpawnerBlockEntity;
import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.item.item.ResonanceStoneItem;
import com.brass_amber.ba_bt.util.BTStatics;
import com.brass_amber.ba_bt.util.BTUtil;
import com.brass_amber.ba_bt.util.GolemType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.Music;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.brass_amber.ba_bt.BABTMain.SAVE_TOWERS;
import static com.brass_amber.ba_bt.util.BTStatics.*;
import static com.brass_amber.ba_bt.util.BTUtil.*;

@SuppressWarnings("DanglingJavadoc")
public class BTAbstractObelisk extends Entity {
    // Parameters that must be saved
    private static final EntityDataAccessor<Integer> SPAWNERS_DESTROYED = SynchedEntityData.defineId(BTAbstractObelisk.class, EntityDataSerializers.INT);

    @SuppressWarnings("FieldMayBeFinal")
    private List<BlockPos> CHESTS = new ArrayList<>(9);
    private List<List<BlockPos>> SPAWNERS;
    private List<Integer> spawnerAmounts;
    private List<Integer> keySpawnerAmounts;
    protected List<EntityType<?>> towerMobs;

    //Other Parameters
    private boolean initialized;
    protected boolean clientInitialized;
    protected boolean serverInitialized;
    protected int checkLayer;
    protected int currentFloorY;

    private boolean doCheck;

    protected MusicManager music;
    protected int musicDistance;
    protected int towerRange;
    protected int enemySpawnRange;

    protected GolemType golemType;
    private boolean justSpawnedKey;

    public Music TOWER_MUSIC;
    public Music BOSS_MUSIC;

    // Data Strings
    private final String towerName = "Tower";
    private final String spawnersDestroyedName = "SpawnersDestroyed";
    private final String crystalSpawnedName = "CrystalSpawned";

    protected boolean musicPlaying;
    protected boolean canCheck;
    private boolean golemSpawned = false;
    private Class<? extends Entity> specialEnemy;
    private boolean chestsFound;
    public boolean hasPlayer;
    public EntityType<?> lastSpawnerType;

    protected int floorDistance;
    protected boolean floorChestFound;
    protected Block chestBlock;
    protected Block golemChestBlock;
    protected Block spawnerBlock;
    protected Block spawnerFillBlock;
    protected Block spawnerMarker;
    protected List<List<Integer>> perFloorData;
    protected List<Integer> floorData;
    protected BTChestBlockEntity golemChest;
    protected List<String> golemChestLootTypes;
    protected List<String> towerChestLootTypes;
    protected ItemStack[] golemLoot;
    public boolean displayCrystal = true;
    private boolean crystalSpawned = false;

    public BTAbstractObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.initialized = false;
        this.clientInitialized = false;
        this.serverInitialized = false;
        this.checkLayer = 1;
        this.blocksBuilding = true;
        this.musicPlaying = false;
        this.doCheck = true;
        this.hasPlayer = false;
        this.canCheck = true;
        this.musicDistance = 0;
        this.towerRange = 0;
        this.floorChestFound = false;
        this.lastSpawnerType = EntityType.IRON_GOLEM;
    }

    public BTAbstractObelisk(GolemType golemType, Level level) {
        this(GolemType.getObeliskFor(golemType), level);
        this.golemType = golemType;
        this.chestsFound = false;
    }

    public void initialize() {
        this.initialized = true;
        this.enemySpawnRange = 15;
    }

    public void clientInitialize() {
        this.music = Minecraft.getInstance().getMusicManager();
        this.clientInitialized = true;
        this.golemType = GolemType.getTypeForObelisk(this);
    }

    public void serverInitialize() {
        int golemNum = GolemType.getNumForType(this.golemType);
        this.keySpawnerAmounts = towerChestUnlocking.get(golemNum);
        this.spawnerAmounts = towerSpawnerAmounts.get(golemNum);
        if (!this.chestsFound) {
            this.SPAWNERS = Arrays.asList(new ArrayList<>(this.spawnerAmounts.get(0)), new ArrayList<>(this.spawnerAmounts.get(1)),
                    new ArrayList<>(this.spawnerAmounts.get(2)), new ArrayList<>(this.spawnerAmounts.get(3)),
                    new ArrayList<>(this.spawnerAmounts.get(4)), new ArrayList<>(this.spawnerAmounts.get(5)),
                    new ArrayList<>(this.spawnerAmounts.get(6)), new ArrayList<>(this.spawnerAmounts.get(7)));

        }

        this.specialEnemy = GolemType.getSpecialEnemyClass(this.golemType);
        this.towerMobs = BTStatics.towerMobs.get(golemNum);
        this.perFloorData = towerSpawnerData.get(golemNum);
        this.floorData = this.perFloorData.get(0);
        this.spawnerMarker = BTBlocks.SPAWNER_MARKER.get();
        this.serverInitialized = true;
    }

    public void findChestsAndSpawners(Level level) {
        // Monoliths are always centered on their floor
        BlockPos center = this.getOnPos();
        int nextFloorY;
        if (this.checkLayer == 8) {
            nextFloorY = this.currentFloorY + this.floorDistance + this.floorDistance;
        } else {
            nextFloorY = this.currentFloorY + this.floorDistance;
        }


        // BrassAmberBattleTowers.LOGGER.info("Floor y: " + this.currentFloorY + " Next y: " + nextFloorY);

        // Get corners of tower area.
        BlockPos corner = center.offset(-15, 0, -15);
        BlockPos oppositeCorner = center.offset(15, 0, 15);
        BlockPos toCheck;
        int spawnersSet = 0;

        // Certain tower have obelisks at the 'top' instead of the bottom -ie ocean, and we need to make sure we use the
        // correct lower number of currentFloorY and nextFloorY
        int bottomOfFloor = Math.min(this.currentFloorY, nextFloorY);
        int topOfFloor = Math.max(this.currentFloorY, nextFloorY);

        // Check all blocks, not needed for land technically (could just check inside tower) but will be useful for
        // Nether/End Towers
        for (int x = corner.getX(); x < oppositeCorner.getX(); x++) {
            for (int z = corner.getZ(); z < oppositeCorner.getZ(); z++) {
                for (int y = bottomOfFloor; y <= topOfFloor; y++) {
                    toCheck = new BlockPos(x, y, z);
                    // This is here to avoid unnecessary variable passing to checkPos()
                    if (level.getBlockState(toCheck).getBlock() == this.spawnerMarker) {
                        // BrassAmberBattleTowers.LOGGER.info(toCheck + " " + this.level().getBlockState(toCheck));
                        spawnersSet = this.setSpawnerBlock(toCheck, this.checkLayer, level, spawnersSet);
                    }
                    this.checkPos(toCheck, level);
                    this.extraCheck(toCheck, level);
                }
            }
        }

        // In case a chest has previously been removed (tower partially completed) fill empty spot with null value
        // Ensures that Index of chest in chest-list matches the floor its on
        // I.E. chest for floor 6 is ID 5 (-1 for list index) even when the chests on floors 3 and 4 are missing
        if (this.CHESTS.size() != this.checkLayer) {
            this.CHESTS.add(null);
        }

        while (this.SPAWNERS.get(this.checkLayer-1).size() < this.spawnerAmounts.get(this.checkLayer-1)) {
            this.SPAWNERS.get(this.checkLayer-1).add(null);
        }

        if (this.checkLayer == 8) {
            this.chestsFound = true;
        }
        else {
            this.checkLayer += 1;
            if (this.checkLayer % 2 == 0 && this.checkLayer < 8) {
                this.floorData = this.perFloorData.get(this.checkLayer / 2);
            }
            this.currentFloorY = nextFloorY;
            this.floorChestFound = false;
        }

    }

    protected int setSpawnerBlock(BlockPos pos, int floor, Level level, int spawnersSet) {
        if (spawnersSet < this.spawnerAmounts.get(floor-1)) {
            level.setBlock(pos, this.spawnerBlock.defaultBlockState(), 2);
            // BrassAmberBattleTowers.LOGGER.info("Set Spawner at: " +pos + " floor: " + floor + " Spawners: " + spawnersSet);
            return spawnersSet + 1;
        } else {
            level.setBlock(pos, this.spawnerFillBlock.defaultBlockState(), 2);
            return spawnersSet;
        }
    }

    public void checkPos(BlockPos toCheck, Level level) {
        try {
            Block block = level.getBlockState(toCheck).getBlock();
            // Make sure that if there is a double Tower chest, only the first instance of the tower chest is added
            if (block == this.chestBlock && !this.floorChestFound) {
                this.CHESTS.add(toCheck);
                this.floorChestFound = true;
                // BrassAmberBattleTowers.LOGGER.info("Found chest");
            } else if (block == this.spawnerBlock || block == Blocks.SPAWNER) {
                this.SPAWNERS.get(this.checkLayer-1).add(toCheck);
                BlockEntity entity = level.getBlockEntity(toCheck);
                EntityType<?> nextSpawnerEntity;
                if (entity instanceof BTAbstractSpawnerBlockEntity btspawnerEntity) {
                    // Prevent floors from having all spawners of one type (i.e all skeletons) unless list only has one possible mob
                    do {
                        nextSpawnerEntity = this.towerMobs.get(this.random.nextInt(this.towerMobs.size()));
                    } while (nextSpawnerEntity == this.lastSpawnerType && this.towerMobs.size() > 1);
                    btspawnerEntity.getSpawner().setEntityId(nextSpawnerEntity, level, level.getRandom(), toCheck);
                    btspawnerEntity.getSpawner().setBtSpawnData(
                            this.floorData.get(0), this.floorData.get(1), this.floorData.get(2),
                            this.floorData.get(3), this.floorData.get(4), this.floorData.get(5),
                            this.blockPosition(), this.enemySpawnRange
                    );
                }
                // BrassAmberBattleTowers.LOGGER.info("Found spawner: " + this.checkLayer + " " + this.spawnersFound);
                // BrassAmberBattleTowers.LOGGER.info(this.SPAWNERS.get(this.checkLayer-1).size());
            } else if (block == this.golemChestBlock) {
                this.golemChest = (BTChestBlockEntity) level.getBlockEntity(toCheck);
                // BrassAmberBattleTowers.LOGGER.info("Found Golem Chest");
            } else if (block == BTBlocks.DATA_MARKER.get()) {
                this.processDataMarker(toCheck, level);
            }
        } catch (Exception e) {
            BABTMain.LOGGER.info("Exception in Obelisk class, not a chest or spawner: " + level.getBlockState(toCheck).getBlock());
            e.printStackTrace();

        }
    }

    private void processDataMarker(BlockPos toProcess, Level level) {
        DataMarkerBlockEntity dataMarker = (DataMarkerBlockEntity) level.getBlockEntity(toProcess);

        Rotation rotation = SAVE_TOWERS.getTowerRotation(GolemType.getNumForType(this.golemType), level.getChunkAt(this.blockPosition()).getPos());

        BlockState placeState = dataMarker.getPlaceBlockState();
        placeState.rotate(level, toProcess, rotation);

        // Get and clean lootTypes list (this accounts for datamarkers with empty lists)
        ArrayList<String> lootTypes = dataMarker.getLootTypes();
        lootTypes.removeIf((type) -> type.equals("Invalid"));
        lootTypes.removeIf(String::isEmpty);

        // Rarity = half of tower level (0-1 = 0, 2-3 = 1, etc)
        int rarity = dataMarker.getRarity();
        if (rarity <= -1) {
            rarity = rarity + 1 + this.checkLayer - 1 < 2 ? 0 : this.checkLayer / 2;
        }

        level.setBlockAndUpdate(toProcess, placeState);

        BlockEntity blockEntity = level.getBlockEntity(toProcess);

        if (blockEntity == null) {
            return;
        }

        blockEntity.load(dataMarker.getNbt());

        if (lootTypes.isEmpty()) {
            return;
        }

        BaseContainerBlockEntity placedEntity;
        try {
            placedEntity = (BaseContainerBlockEntity) level.getBlockEntity(toProcess);
        } catch (ClassCastException e) {
            return;
        }

        LootParams lootparams =  (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(toProcess)).create(LootContextParamSets.CHEST);
        LootContext lootcontext = (new LootContext.Builder(lootparams)).create(null);

        Pair<List<Item>, List<Integer>> itemsAmounts =  createItems(rarity, lootTypes, this.random, true);
        btListFill(itemsAmounts.getFirst(), itemsAmounts.getSecond(), placedEntity, lootcontext);
    }


    public void extraCheck(BlockPos toUpdate, Level level) {}

    @Override
    public void tick() {
        super.tick();

        if (!this.initialized) {
            // BrassAmberBattleTowers.LOGGER.info("Finding Chests for layer: " + this.checkLayer + "  At block level: " + this.currentFloorY);
            this.initialize();
            return;
        }

        if (this.level().isClientSide()) {
            this.clientTick();
            return;
        }

        // Only happens server-side

        if (!this.serverInitialized) {
            this.serverInitialize();
            return;
        }

        if (!this.displayCrystal && !this.crystalSpawned) {
            this.spawnAtLocation(GolemType.getResonanceCrystalForType(this.golemType), 1);
            this.crystalSpawned = true;
        }

        if (!this.chestsFound) {
            this.findChestsAndSpawners(this.level());
        }
        if (this.doCheck) {
            try {
                List<?> list = this.level().getEntitiesOfClass(BTMonolith.class, this.getBoundingBox().inflate(15, 110, 15));
                this.canCheck = !list.isEmpty();
                if (!this.canCheck) {
                    try {
                        List<?> list2 = this.level().getEntitiesOfClass(BTAbstractGolem.class, this.getBoundingBox().inflate(15, 110, 15));
                        this.canCheck = !list2.isEmpty();
                        if (!this.golemSpawned) {
                            this.golemSpawned = true;
                        }
                    } catch (Exception f) {
                        BABTMain.LOGGER.error("Exception finding Golem: " + f);
                    }
                }
            } catch (Exception e) {
                BABTMain.LOGGER.error("Exception finding Monolith: " + e);
            }
        }


        if (this.canCheck) {
            List<ServerPlayer> players = Objects.requireNonNull(this.level().getServer()).getPlayerList().getPlayers();
            List<Boolean> playersClose = new ArrayList<>();
            for (ServerPlayer player : players
            ) {
                if (distanceTo2D(this, player) < this.towerRange) {
                    playersClose.add(Boolean.TRUE);
                    // BrassAmberBattleTowers.LOGGER.info("Player " +  distanceTo2D(this, player) + " blocks away");
                } else {
                    playersClose.add(Boolean.FALSE);
                }
            }

            this.hasPlayer = Collections.frequency(playersClose, Boolean.TRUE) > 0;

            int timeCheck = (this.random.nextInt(2) + 4) * 40;

            if (this.tickCount % timeCheck == 0) {
                List<? extends Entity> specialEnemies = this.level().getEntitiesOfClass(this.specialEnemy, this.getBoundingBox().inflate(15, 110, 15));
                if (specialEnemies.size() < 10) {
                    int floor = this.blockPosition().getY() + this.random.nextInt(8) * 11;
                    int x = this.blockPosition().getX() + this.random.nextInt(24) - 12;
                    int y = floor + this.random.nextInt(9);
                    int z = this.blockPosition().getZ() + this.random.nextInt(24) - 12;

                    ServerLevel serverWorld = (ServerLevel) this.level();

                    switch (this.golemType) {
                        case LAND -> this.spawnSpecialEnemy(serverWorld, new BlockPos(x, y, z),
                                0D, 11.5D, true);
                        case OCEAN -> this.spawnSpecialEnemy(serverWorld, new BlockPos(x, y, z),
                                12.5D, 17.5D, false);
                        case CORE -> this.spawnSpecialEnemy(serverWorld, new BlockPos(x, y, z),
                                12.5D, 17.5D, true);
                    }
                }
            }

            if (this.tickCount % 20 == 0 && this.hasPlayer) {
                // BrassAmberBattleTowers.LOGGER.info("Checking Spawners");
                this.checkSpawners(this.level());
                // BrassAmberBattleTowers.LOGGER.info(this.towerEffect + " effect ");
            }
        } else if (this.golemChest != null) {
            if (this.chestsFound && this.initialized && this.tickCount > 40 && !this.golemChest.isUnlocked()) {
                try {
                    BABTMain.LOGGER.debug("Chest " + this.golemChest);
                    this.golemChest.setUnlocked(true);

                    for (int i = 2; i < 7; i++) {
                        this.golemChest.setItem(i, this.golemLoot[0]);
                    }
                    for (int i = 19; i < 24; i++) {
                        this.golemChest.setItem(i, this.golemLoot[1]);
                    }
                    for (int i = 10; i < 17; i++) {
                        this.golemChest.setItem(i, this.golemLoot[0]);
                    }
                    this.golemChest.setItem(13, this.golemLoot[2]);

                    LootParams lootparams =  (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.golemChest.getBlockPos())).create(LootContextParamSets.CHEST);
                    LootContext lootcontext = (new LootContext.Builder(lootparams)).create(null);
                    String lootPath = GolemType.getTowerChestPool(this.golemType, 8);
                    if (!lootPath.equals("")) {
                        btFill(this.getServer().getLootData().getLootTable(new ResourceLocation(lootPath)), this.golemChest, lootcontext, lootparams);
                    }
                    else {
                        Pair<List<Item>, List<Integer>> itemsAmounts =  createItems(4, new ArrayList<>(this.golemChestLootTypes), this.random, false);
                        btListFill(itemsAmounts.getFirst(), itemsAmounts.getSecond(), this.golemChest, lootcontext);
                    }


                    // getGolemLootTable(GolemType.getNumForType(this.golemType)).fill(this.golemChest, lootparams, this.random.nextLong());
                    this.chestUnlockingSound(this.level());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clientTick() {
        ClientLevel client = (ClientLevel)this.level();
        if (!this.clientInitialized) {
            this.clientInitialize();
        }
        if (client.players().isEmpty()) {
            return;
        }

        if (this.doCheck) {
            try {
                List<?> list = client.getEntitiesOfClass(BTMonolith.class, this.getBoundingBox().inflate(15, 110, 15));
                this.canCheck = !list.isEmpty();
                if (!this.canCheck) {
                    try {
                        List<?> list2 = this.level().getEntitiesOfClass(BTAbstractGolem.class, this.getBoundingBox().inflate(15, 110, 15));
                        this.canCheck = !list2.isEmpty();
                        if (!this.golemSpawned) {
                            this.golemSpawned = true;
                        }
                    } catch (Exception f) {
                        BABTMain.LOGGER.error("Exception finding Golem: " + f);
                    }
                }
            } catch (Exception e) {
                BABTMain.LOGGER.error("Exception finding Monolith: " + e);
            }
        }

        // Make sure we have a player within range.
        boolean hasClientPlayer = client.hasNearbyAlivePlayer(this.getX(), this.getY(), this.getZ(), 100D);
        boolean playerInTowerRange;
        boolean playerInMusicRange;

        if (hasClientPlayer && !this.golemSpawned) {
            //noinspection ConstantConditions
            playerInTowerRange = BTUtil.distanceTo2D(this, client.getNearestPlayer(this, 100D)) <= this.towerRange;
            //noinspection ConstantConditions
            playerInMusicRange = BTUtil.distanceTo2D(this, client.getNearestPlayer(this, 100D)) < this.musicDistance;
        } else {
            playerInTowerRange = false;
            playerInMusicRange = false;
        }

        boolean tryPlayMusic = false;

        if (this.BOSS_MUSIC == null) {
            tryPlayMusic = playerInTowerRange;
            // BrassAmberBattleTowers.LOGGER.info("No Boss Music");

        } else {
            // BrassAmberBattleTowers.LOGGER.info("Player: " + true + "  In Music Range: " + playerInMusicRange + " Tower music playing?: " + this.musicPlaying);
            if (!this.music.isPlayingMusic(this.BOSS_MUSIC)) {
                tryPlayMusic = playerInTowerRange;
            }

        }

        if (tryPlayMusic) {
            if (playerInMusicRange && !this.music.isPlayingMusic(this.TOWER_MUSIC)) {
                this.music.stopPlaying();
                this.music.startPlaying(this.TOWER_MUSIC);
                this.musicPlaying = true;
            }
        } else {
            if (this.musicPlaying) {
                this.music.stopPlaying();
                this.musicPlaying = false;
            }
        }
    }

    protected void spawnSpecialEnemy(ServerLevel serverWorld, BlockPos spawn, double lowerRadiusBound,
                                     double upperRadiusBound, boolean checkOnGround) {
        // BrassAmberBattleTowers.LOGGER.info("Trying to spawn cultist at: " + spawn);
        double distance = BTUtil.distanceTo2D(this, spawn.getX(), spawn.getZ());
        boolean canSpawn = SpawnPlacements.checkSpawnRules(GolemType.getSpecialEnemyType(this.golemType), serverWorld, MobSpawnType.STRUCTURE, spawn, this.random);
        boolean acceptableDistance = lowerRadiusBound < distance && distance < upperRadiusBound;
        boolean onGround;
        boolean specialEnemyCap;

        List<?> nearby = serverWorld.getEntitiesOfClass(GolemType.getSpecialEnemyClass(this.golemType), this.getBoundingBox().inflate(15, 110, 15));
        specialEnemyCap = nearby.size() > 4;
        if (checkOnGround) {
            onGround = !serverWorld.getBlockState(spawn.below()).isAir() && serverWorld.getBlockState(spawn).isAir();
        } else {
            onGround = true;
        }

        if (!specialEnemyCap && canSpawn && acceptableDistance && onGround && serverWorld.getBlockState(spawn.above()).isAir()) {
            Entity entity = GolemType.getSpecialEnemy(this.golemType, serverWorld);
            if (entity instanceof Mob mob) {
                mob.setPos(spawn.getX() + .5, spawn.getY(), spawn.getZ() + .5);
                net.minecraftforge.event.ForgeEventFactory.onFinalizeSpawn(mob, serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.TRIGGERED, null, null);
                serverWorld.addFreshEntity(entity);
                // BrassAmberBattleTowers.LOGGER.info("Success");
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void checkSpawners(Level level) {
        // Make sure there are chests && spawners in the tower (tower has not been cleared)
        BlockPos chestPos;
        List<BlockPos> chests = new ArrayList<>(this.CHESTS);
        chests.removeIf(Objects::isNull);

        List<List<BlockPos>> spawners = new ArrayList<>(this.SPAWNERS);
        spawners.removeIf(Objects::isNull);

        if (spawners.isEmpty() || chests.isEmpty()) {
            this.doCheck = false;
            this.canCheck = false;
        } else {
            // Main loop to iterate over each 'floor' contained in the spawners list
            for (int i = 0; i < this.SPAWNERS.size(); i++) {
                List<BlockPos> positions = this.SPAWNERS.get(i);
                if (positions != null) {
                    positions.removeIf(Objects::isNull);
                    if (positions.isEmpty()) {
                        // If no spawners left on the floor unlock the chest.
                        chestPos = this.CHESTS.get(i);
                        if (chestPos != null && level.getBlockEntity(chestPos) instanceof BTChestBlockEntity chest) {
                            if (!chest.isUnlocked()) {
                                chest.setUnlocked(true);
                                LootParams lootparams =  (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(chestPos)).create(LootContextParamSets.CHEST);
                                LootContext lootcontext = (new LootContext.Builder(lootparams)).create(null);
                                assert chest != null: "BTObelisk: Not a BTChest";
                                String lootPath = GolemType.getTowerChestPool(this.golemType, i);
                                int rarity = i < 2 ? 0 : i / 2;
                                if (!lootPath.isEmpty()) {
                                    btFill(this.getServer().getLootData().getLootTable(new ResourceLocation(lootPath)), chest, lootcontext, lootparams);
                                }
                                else {
                                    Pair<List<Item>, List<Integer>> itemsAmounts =  createItems(rarity, new ArrayList<>(this.towerChestLootTypes), this.random, false);
                                    btListFill(itemsAmounts.getFirst(), itemsAmounts.getSecond(), chest, lootcontext);
                                }
                                // BTUtil.btFill(getLootTable(GolemType.getNumForType(this.golemType), i), chest, lootcontext, lootparams);
                                this.chestUnlockingSound(level);
                                this.CHESTS.set(i, null);
                            }
                        }
                        this.SPAWNERS.set(i, null);
                    } else {
                        positions = this.SPAWNERS.get(i);
                        for (int x = 0; x < positions.size(); x++) {
                            if (positions.get(x) != null) {
                                BlockPos blockPos = positions.get(x);
                                if (!(level.getBlockState(blockPos).getBlock() instanceof BTSpawnerBlock)) {
                                    this.SPAWNERS.get(i).set(x, null);
                                    this.setSpawnersDestroyed(this.getSpawnersDestroyed() + 1);
                                    BABTMain.LOGGER.info("Spawners Destroyed: {}", this.getSpawnersDestroyed());
                                }
                            }

                            if (this.keySpawnerAmounts.contains(this.getSpawnersDestroyed()) && !justSpawnedKey) {
                                if (this.CHESTS.get(i) != null && level.getBlockEntity(this.CHESTS.get(i)) instanceof BTChestBlockEntity chest) {
                                    // chest.setLootTable(BrassAmberBattleTowers.locate("chests/" + GolemType.getNameForNum(this.getTower())+ "_tower/" + (i+1) + "key"), this.random.nextLong());
                                    chest.setItem(13, GolemType.getKeyFor(this.golemType).getDefaultInstance());
                                }
                                else if (this.CHESTS.get(i) == null) {
                                    doNoOutputPostionedCommand(this, "/give @p ba_bt:" + GolemType.getKeyFor(this.golemType).getDescriptionId(), new Vec3(this.blockPosition().getX(), this.blockPosition().getY() + (11 * i), this.blockPosition().getZ()));
                                }
                                this.justSpawnedKey = true;
                            }
                        }
                    }
                }

            }
        }

    }

    private void chestUnlockingSound(Level level) {
        List<ServerPlayer> players = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers();
        for (ServerPlayer player: players) {
            if (BTUtil.distanceTo2D(this, player) < 30) {
                level.playSound(null, player.blockPosition(), SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1f, 1.5f);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SPAWNERS_DESTROYED, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (!this.level().isClientSide()) {
            // BrassAmberBattleTowers.LOGGER.info("Reading obelisk data");
            // BrassAmberBattleTowers.LOGGER.info("Reading obelisk data " + tag);
            this.golemType = GolemType.getTypeForName(tag.getString(towerName));
            this.setSpawnersDestroyed(tag.getInt(spawnersDestroyedName));
            this.crystalSpawned = tag.getBoolean(crystalSpawnedName);
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        if (this.level().isClientSide()) {
            music.stopPlaying();
        } else {
            BABTMain.LOGGER.info("Setting obelisk data");
            tag.putString(towerName, this.golemType.getSerializedName());
            tag.putInt(spawnersDestroyedName, this.getSpawnersDestroyed());
            tag.putBoolean(crystalSpawnedName, this.crystalSpawned);
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
     * {@link PushReaction.BLOCK} is the valid option for an entity to stop piston interaction
     * Used in: {@link PistonTileEntity.moveCollidedEntities method}
     */
    @SuppressWarnings("JavadocReference")
    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.BLOCK;
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
        return true;
    }

    /***************************************************** Breaking *************************************************/

    /**
     * Called by the /kill command.
     */
    @Override
    public void kill() {
        Player player = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 50, EntitySelector.NO_SPECTATORS);

        if (player != null && player.isCreative()) {
            BABTMain.LOGGER.info("Item: " + player.getItemInHand(InteractionHand.MAIN_HAND).getItem());
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.CLAY_BALL) {
                this.remove(RemovalReason.KILLED);
            } else {
                // Do nothing to prevent people deleting a Monolith by accident.
                BABTMain.LOGGER.info("Used the /kill command. However, an Obelisk has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
            }
        }
        else {
            // Do nothing to prevent people deleting a Monolith by accident.
            BABTMain.LOGGER.info("Used the /kill command. However, an Obelisk has been saved at: " + Math.round(this.getX()) + "X " + Math.round(this.getY()) + "Y " + Math.round(this.getZ()) + "Z.");
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
            if (this.isAlive() && !this.level().isClientSide() && source.isCreativePlayer()) {
                Player player = (Player) source.getEntity();
                if (player != null && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.CLAY_BALL) {
                    this.remove(RemovalReason.KILLED);
                }
            }
            return true;
        }
    }

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.getItem() instanceof ResonanceStoneItem stoneItem) {
            stoneItem.addEnchantment(itemInHand);
            return InteractionResult.SUCCESS;
        } else if (itemInHand.is(GolemType.getEyeFor(this.golemType)) && this.golemSpawned) {
            this.displayCrystal = false;
            return InteractionResult.CONSUME;
        } else {
            return super.interact(player, hand);
        }
    }

    @Override
    public MobCategory getClassification(boolean forSpawnCount) {
        return MobCategory.AMBIENT;
    }

    /************************************************** DATA SET/GET **************************************************/

    public void setSpawnersDestroyed(int num) {
        this.entityData.set(SPAWNERS_DESTROYED, num);
        this.justSpawnedKey = false;
    }

    public int getSpawnersDestroyed() {
        return this.entityData.get(SPAWNERS_DESTROYED);
    }

    /************************************************** COMMANDS **************************************************/
}
