package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTExtras;
import com.brass_amber.ba_bt.util.BTUtil;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;
import static com.brass_amber.ba_bt.sound.BTMusic.OCEAN_GOLEM_FIGHT_MUSIC;
import static com.brass_amber.ba_bt.sound.BTMusic.OCEAN_TOWER_MUSIC;
import static com.brass_amber.ba_bt.util.BTStatics.towerBlocks;
import static com.brass_amber.ba_bt.util.BTUtil.*;
import static java.lang.Math.abs;

public class BTOceanObelisk extends BTAbstractObelisk {

    private final List<Block> avoidBlocks = towerBlocks.get(GolemType.OCEAN.ordinal());
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
    private boolean golemDead = false;


    public BTOceanObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BTOceanObelisk(Level level) {
        super(GolemType.OCEAN, level);
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
        this.golemChestLootTypes = new ArrayList<>(List.of("armor", "weapon", "gem"));
        this.towerChestLootTypes = new ArrayList<>(List.of("armor", "weapon", "plant", "water_plant"));
        this.golemLoot = new ItemStack[]{Items.PRISMARINE_BRICKS.getDefaultInstance(), Items.PRISMARINE.getDefaultInstance(), Items.HEART_OF_THE_SEA.getDefaultInstance()};

        this.noise = 60 + ((random.nextInt(2) + 1) * 4);
        if (minimalOceanCarving) {
            this.noise /= 2;
        }

        this.top = this.getBlockY() - 2;
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

        /* BrassAmberBattleTowers.LOGGER.debug("Walls W,N,E,S " + this.westWall + " " + this.northWall + " " + this.eastWall + " " + this.southWall);
        BrassAmberBattleTowers.LOGGER.debug("Noise " + this.noise);**/
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // BABTMain.LOGGER.debug("Ocean Carved in read data " + this.oceanCarved);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }


        try {
            List<?> list2 = this.level().getEntitiesOfClass(BTAbstractGolem.class, this.entityCheckAABB);
            this.golemDead = list2.isEmpty() && this.golemSpawned;
        } catch (Exception f) {

            BABattleTowers.LOGGER.error("Exception finding Golem: " + f);
        }

        if (this.tickCount % 100 == 0 && this.hasPlayer && !this.golemDead) {
            List<Player> players = this.level().getNearbyPlayers(TargetingConditions.forNonCombat().range(this.towerRange), null,  this.entityCheckAABB);

            for (Player player : players
            ) {
                boolean acceptableY = player.getBlockY() < this.getBlockY() - 1 && player.getBlockY() > this.bottom;
                boolean not_survival = player.isCreative() || player.isSpectator();
                if (player.isInWater() && acceptableY && !not_survival) {
                    // BrassAmberBattleTowers.LOGGER.debug("Set effects");
                    player.forceAddEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, true), player);
                    player.forceAddEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1,true, true), player);
                    // player.forceAddEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 360, 0,true, true), player);

                }
                else if (player.hasEffect(BTExtras.DEPTH_DROPPER_EFFECT.get())){
                    player.removeEffect(BTExtras.DEPTH_DROPPER_EFFECT.get());
                    player.removeEffect(MobEffects.NIGHT_VISION);
                }
            }

            for (Entity entity: level().getEntities(this, this.entityCheckAABB, entity -> entity.isAlive() && entity.isInWater())) {
                if (distanceTo2D(this, entity) < towerRange && entity instanceof LivingEntity && depthDropperAffectsMobs) {
                    ((LivingEntity) entity).forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(), 15, 1,true, true), entity);
                }
            }

        }
    }

    public void gatherAreaBlocks() {
        // BrassAmberBattleTowers.LOGGER.debug(this.level().isClientSide());

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Block block;

        while (this.currentCarveLayer > this.bottom) {
            BABattleTowers.LOGGER.debug("Round of carving: {}", this.currentCarveLayer);
            int bottomRange = this.currentCarveLayer + this.floorDistance;
            if (this.currentCarveLayer - this.bottom < abs(this.floorDistance) + 1) {
                bottomRange = this.bottom;
            }
            // BrassAmberBattleTowers.LOGGER.debug("Bottom Range: " + bottomRange);
            for (int y = this.currentCarveLayer; y >= bottomRange; y--) {
                if (y == this.bottom + 37 || y == this.bottom + 73) {
                    if (minimalOceanCarving) {
                        this.wallDistance -= 2;
                    } else {
                        this.wallDistance -= 8;
                    }
                } else if ((this.top - y) % this.nextStep == 0) {
                    this.wallDistance -= this.distanceChange;
                    this.nextStep = random.nextInt(4)+8;
                    if (y > this.bottom + 33) {
                        this.distanceChange = random.nextInt(2)+1;
                    } else {
                        this.distanceChange = 1;
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
                                    if (this.level().getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.KELP_PLANT) {
                                        this.toRemove.add(blockpos$mutableblockpos);
                                    } else if (!this.level().isWaterAt(blockpos$mutableblockpos)){
                                        if (distance2d < this.wallDistance - 2) {
                                            this.toRemove.add(blockpos$mutableblockpos.immutable());
                                        } else if (distance2d < this.wallDistance - 1) {
                                            if (random.nextInt(50) > 30) {
                                                this.level().setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                            } else {
                                                this.level().setBlock(blockpos$mutableblockpos, Blocks.GRAVEL.defaultBlockState(), 2);
                                            }
                                        } else if (distance2d < this.wallDistance && !this.avoidBlocks.contains(block)) {
                                            this.level().setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                                        }
                                    }
                                } else if (!this.avoidBlocks.contains(block)) {
                                    this.toRemove.add(blockpos$mutableblockpos.immutable());
                                }
                            }
                        }
                    }
                }
            }
            this.currentCarveLayer = bottomRange;
            // BrassAmberBattleTowers.LOGGER.debug("This Round of carving: " + this.currentCarveLayer);
        }

        this.generationState = GenerationState.REMOVE_AREA_BLOCKS;
    }

    @Override
    public void removeAreaBlocks() {
        int removeSize = this.toRemove.size();
        BABattleTowers.LOGGER.debug("Removing blocks: {}", removeSize);
        if (removeSize > 0) {
            for (int i = 0; i < Math.min(removeSize, 2048); i++) {
                this.level().setBlock(this.toRemove.remove(0), Blocks.WATER.defaultBlockState(), 2);
            }
            doNoOutputCommand(this, "/kill @e[distance=0..100,type=item,nbt={Item:{id:'minecraft:kelp'}}]");
        } else {
            this.generationState = GenerationState.ADD_AREA_FEATURES;
        }
    }

    public void addAreaFeatures() {
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
                                    && !avoidBlocks.contains(this.level().getBlockState(blockAbove).getBlock())
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
        this.generationState = GenerationState.FINISHED;
    }
}
