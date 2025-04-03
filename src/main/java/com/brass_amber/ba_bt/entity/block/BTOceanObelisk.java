package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTExtras;
import com.brass_amber.ba_bt.util.BTUtil;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.brass_amber.ba_bt.BattleTowersConfig.minimalOceanCarving;
import static com.brass_amber.ba_bt.sound.BTMusic.OCEAN_GOLEM_FIGHT_MUSIC;
import static com.brass_amber.ba_bt.sound.BTMusic.OCEAN_TOWER_MUSIC;
import static com.brass_amber.ba_bt.util.BTStatics.towerBlocks;
import static com.brass_amber.ba_bt.util.BTUtil.*;

public class BTOceanObelisk extends BTAbstractObelisk {

    private final List<BlockState> avoidBlocks = towerBlocks.get(GolemType.getNumForType(GolemType.OCEAN));
    private final List<BlockState> corals = List.of(Blocks.BRAIN_CORAL.defaultBlockState(),
            Blocks.BUBBLE_CORAL.defaultBlockState(), Blocks.FIRE_CORAL.defaultBlockState(),
            Blocks.HORN_CORAL.defaultBlockState(), Blocks.TUBE_CORAL.defaultBlockState());

    private int noise;
    private int westWall;
    private int northWall;
    private int eastWall;
    private int southWall;
    private int top;
    private int bottom;
    private int currentCarveLayer;
    private double wallDistance;
    private int nextStep;
    private int distanceChange;
    private boolean oceanCarved;

    private final String oceanCarvedName = "OceanCarved";


    public BTOceanObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.oceanCarved = false;
    }

    public BTOceanObelisk(Level level) {
        super(GolemType.OCEAN, level);
        this.oceanCarved = false;
    }


    @Override
    public void initialize() {
        this.musicDistance = 58;
        this.towerRange = 62;
        super.initialize();
        this.enemySpawnRange = 15;
    }

    @Override
    public void clientInitialize() {
        this.BOSS_MUSIC = OCEAN_GOLEM_FIGHT_MUSIC;
        this.TOWER_MUSIC = OCEAN_TOWER_MUSIC;
        super.clientInitialize();
    }

    public void serverInitialize() {
        this.floorDistance = -12;

        this.chestBlock = BTBlocks.OCEAN_CHEST.get();
        this.golemChestBlock = BTBlocks.OCEAN_GOLEM_CHEST.get();
        this.spawnerBlock = BTBlocks.OCEAN_SPAWNER.get();
        this.spawnerFillBlock = Blocks.PRISMARINE_BRICKS;
        this.golemChestLootTypes = List.of("Weapon", "Armor", "Gem");
        this.towerChestLootTypes = List.of("Weapon", "Armor", "Metal", "Gem", "Water Plant");
        this.golemLoot = new ItemStack[]{Items.PRISMARINE_BRICKS.getDefaultInstance(), Items.PRISMARINE.getDefaultInstance(), Items.HEART_OF_THE_SEA.getDefaultInstance()};

        if (minimalOceanCarving.get()) {
            this.noise = 30 + ((random.nextInt(2) + 1) * 4);
        } else {
            this.noise = 60 + ((random.nextInt(2) + 1) * 4);
        }

        this.top = this.getBlockY() - 12;
        this.bottom = this.getBlockY() - 110;

        this.currentFloorY = this.getBlockY() - 2;
        this.currentCarveLayer = this.top;
        this.wallDistance = this.noise -.5;
        this.nextStep = random.nextInt(4) + 8;
        this.distanceChange = random.nextInt(3);


        this.westWall = this.getBlockX() - this.noise;
        this.northWall = this.getBlockZ() - this.noise;
        this.eastWall = this.getBlockX() + this.noise;
        this.southWall = this.getBlockZ() + this.noise;

        super.serverInitialize();

        /* BrassAmberBattleTowers.LOGGER.info("Walls W,N,E,S " + this.westWall + " " + this.northWall + " " + this.eastWall + " " + this.southWall);
        BrassAmberBattleTowers.LOGGER.info("Noise " + this.noise);**/
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        tag.putBoolean(oceanCarvedName, this.oceanCarved);
        super.addAdditionalSaveData(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.oceanCarved = tag.getBoolean(oceanCarvedName);
        // BABTMain.LOGGER.info("Ocean Carved in read data " + this.oceanCarved);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }

        if (!this.oceanCarved && this.serverInitialized && this.tickCount % 120 <= 5) {
            this.carveOcean();
            doNoOutputCommand(this, "/kill @e[distance=0..100,type=item,nbt={Item:{id:'minecraft:kelp'}}]");
            return;
        }

        if (this.tickCount % 100 <= 5 && this.hasPlayer && this.canCheck) {
            List<ServerPlayer> players = Objects.requireNonNull(this.level().getServer()).getPlayerList().getPlayers();
            for (ServerPlayer player : players
            ) {
                boolean acceptableY = player.getBlockY() < this.getBlockY() - 13 && player.getBlockY() > this.bottom;
                if (BTUtil.distanceTo2D(this, player) < this.towerRange && player.isInWater() && acceptableY) {
                    // BrassAmberBattleTowers.LOGGER.debug("Set effects");
                    player.forceAddEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, true), player);
                    player.forceAddEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1,true, true), player);
                    player.forceAddEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0,true, true), player);
                    player.forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(), 100, 1,true, true), player);

                }
                else if (player.hasEffect(BTExtras.DEPTH_DROPPER_EFFECT.get())){
                    player.removeEffect(BTExtras.DEPTH_DROPPER_EFFECT.get());
                    player.removeEffect(MobEffects.NIGHT_VISION);
                }
            }

            for (Entity entity: ((ServerLevel) level()).getEntities(EntityType.DROWNED, EntitySelector.LIVING_ENTITY_STILL_ALIVE)) {
                if (distanceTo2D(this, entity) < towerRange) {
                    ((LivingEntity) entity).forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(), 100, 1,true, true), entity);
                }
            }

            for (Entity entity: ((ServerLevel) level()).getEntities(EntityType.GUARDIAN, EntitySelector.LIVING_ENTITY_STILL_ALIVE)) {
                if (distanceTo2D(this, entity) < towerRange) {
                    ((LivingEntity) entity).forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(), 100, 1,true, true), entity);
                }
            }
        }
    }

    public void carveOcean() {
        // BrassAmberBattleTowers.LOGGER.info(this.level().isClientSide());
        // BABTMain.LOGGER.info("Round of carving: " + this.currentCarveLayer);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Block block;
        if (this.currentCarveLayer >= this.bottom) {
            int bottomRange = this.currentCarveLayer + this.floorDistance;
            if (this.currentCarveLayer - this.bottom < 20) {
                bottomRange = this.bottom;
            }
            // BrassAmberBattleTowers.LOGGER.info("Bottom Range: " + bottomRange);
            for (int y = this.currentCarveLayer; y >= bottomRange; y--) {
                if (y == this.bottom + 36 || y == this.bottom + 72) {
                    if (minimalOceanCarving.get()) {
                        this.wallDistance -= 2;
                    } else {
                        this.wallDistance -= 8;
                    }
                } else if ((this.top - y) % this.nextStep == 0) {
                    this.wallDistance -= this.distanceChange;
                    this.nextStep = random.nextInt(4)+8;
                    if (y > this.bottom + 33) {
                        this.distanceChange = random.nextInt(3)+1;
                    } else {
                        this.distanceChange = random.nextInt(2)+1;
                    }
                }
                if (y > this.bottom) {
                    for (int x = this.westWall; x <= this.eastWall; x++) {
                        for (int z = this.northWall; z <= this.southWall; z++) {
                            blockpos$mutableblockpos.set(x, y, z);
                            block = this.level().getBlockState(blockpos$mutableblockpos).getBlock();
                            double distance2d = BTUtil.distanceTo2D(this, blockpos$mutableblockpos);
                            if (y > this.bottom) {
                                if (distance2d > 15.5D) {
                                    if  (this.level().getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.KELP_PLANT) {
                                        this.level().setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 3);

                                    } else if (!this.level().isWaterAt(blockpos$mutableblockpos) && !avoidBlocks.contains(this.level().getBlockState(blockpos$mutableblockpos))){
                                        if (distance2d < this.wallDistance - 2) {
                                            this.level().setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                                        } else if (distance2d < this.wallDistance - 1) {
                                            if (random.nextInt(50) > 30) {
                                                this.level().setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                            } else {
                                                this.level().setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                            }
                                        } else if (distance2d < this.wallDistance && !this.avoidBlocks.contains(block.defaultBlockState())) {
                                            this.level().setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                        }
                                    }
                                } else if (!this.avoidBlocks.contains(block.defaultBlockState())) {
                                    this.level().setBlock(blockpos$mutableblockpos, Blocks.WATER.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                } else if (y == this.bottom){
                    this.addVegetation();
                }
            }
            this.currentCarveLayer = bottomRange;
            // BrassAmberBattleTowers.LOGGER.info("This Round of carving: " + this.currentCarveLayer);
        }

        if (this.currentCarveLayer == this.bottom) {
            this.oceanCarved = true;

        }
        BABTMain.LOGGER.info("Ocean Carved : " + this.oceanCarved);
    }

    public void addVegetation() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos blockAbove;
        for (int y = this.top; y > this.bottom - 1; y--) {
            for (int x = this.westWall; x <= this.eastWall; x++) {
                for (int z = this.northWall; z <= this.southWall; z++) {
                    blockpos$mutableblockpos.set(x, y, z);
                    blockAbove = blockpos$mutableblockpos.above();
                    if (
                            !this.level().isWaterAt(blockpos$mutableblockpos) && this.level().isWaterAt(blockAbove)
                                    && distanceTo2D(this, blockpos$mutableblockpos) < this.noise +5
                                    && !avoidBlocks.contains(this.level().getBlockState(blockpos$mutableblockpos).getBlock().defaultBlockState())
                    ) {
                        float vegetation = random.nextFloat();
                        if (vegetation > .80) {
                            if (vegetation > .85) {
                                this.level().setBlock(blockAbove, Blocks.SEAGRASS.defaultBlockState(), 2);
                            } else {
                                this.level().setBlock(blockAbove, Blocks.TALL_SEAGRASS.defaultBlockState(), 2);
                            }
                        } else if (vegetation > .70) {
                            this.level().setBlock(blockAbove, Blocks.KELP_PLANT.defaultBlockState(), 2);
                        } else if (vegetation > .60) {
                            this.level().setBlock(blockAbove, corals.get(random.nextInt(5)), 2);
                        }
                    }
                }
            }
        }
    }
}
