package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BTLandObelisk extends BTAbstractObelisk {

    public BTLandObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BTLandObelisk(Level level) {
        super(GolemType.LAND, level);
    }

    @Override
    public void initialize() {
        this.musicDistance = 17;
        this.towerRange = 30;
        super.initialize();
    }

    @Override
    public void serverInitialize() {
        this.floorDistance = BattleTowersConfig.landFloorHeight.get();
        this.currentFloorY = this.getBlockY() - 1;
        this.chestBlock = BTBlocks.LAND_CHEST.get();
        this.golemChestBlock = BTBlocks.LAND_GOLEM_CHEST.get();
        this.spawnerBlock = BTBlocks.BT_LAND_SPAWNER.get();
        this.spawnerFillBlock = Blocks.STONE_BRICKS;
        super.serverInitialize();
    }
    @Override
    public void clientInitialize() {
        this.BOSS_MUSIC = BTSoundEvents.LAND_GOLEM_FIGHT_MUSIC;
        this.TOWER_MUSIC = BTSoundEvents.LAND_TOWER_MUSIC;
        super.clientInitialize();
    }



    @Override
    public void extraCheck(BlockPos toUpdate, Level level) {
        // Update Sand
        if (level.getBlockState(toUpdate) == Blocks.SAND.defaultBlockState()) {
            level.removeBlock(toUpdate, false);
            // BrassAmberBattleTowers.LOGGER.info("Sand? :" + level.getBlockState(toUpdate));
            level.setBlockAndUpdate(toUpdate, Blocks.SAND.defaultBlockState());
        }
    }
}
