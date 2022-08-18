package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static com.BrassAmber.ba_bt.util.BTUtil.doNoOutputCommand;

public class BTLandObelisk extends BTAbstractObelisk {

    public BTLandObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.BOSS_MUSIC = BTSoundEvents.LAND_GOLEM_FIGHT_MUSIC;
        this.TOWER_MUSIC = BTSoundEvents.LAND_TOWER_MUSIC;
        this.musicDistance = 17;
        this.towerRange = 30;

    }

    public BTLandObelisk(Level level) {
        super(GolemType.LAND, level);
    }

    @Override
    public void serverInitialize() {
        this.floorDistance = 11;
        this.currentFloorY = this.getBlockY() - 1;
        this.chestBlock = BTBlocks.LAND_CHEST.get();
        this.spawnerBlock = BTBlocks.BT_LAND_SPAWNER.get();
        this.woolBlock = Blocks.GREEN_WOOL;
        this.spawnerFillBlock = Blocks.STONE_BRICKS;
    }

    @Override
    public void extraCheck(BlockPos toUpdate, Level level) {
        // Update Sand
        if (level.getBlockState(toUpdate) == Blocks.SAND.defaultBlockState()) {
            level.removeBlock(toUpdate, false);
            BrassAmberBattleTowers.LOGGER.info("Sand? :" + level.getBlockState(toUpdate));
            level.setBlockAndUpdate(toUpdate, Blocks.SAND.defaultBlockState());
        }
    }
}
