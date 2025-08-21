package com.brass_amber.ba_bt.entity.block;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.sound.BTMusic.LAND_GOLEM_FIGHT_MUSIC;
import static com.brass_amber.ba_bt.sound.BTMusic.LAND_TOWER_MUSIC;

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
        this.floorDistance = 11;
        this.currentFloorY = this.getBlockY() - 1;
        this.chestBlock = BTBlocks.LAND_CHEST.get();
        this.golemChestBlock = BTBlocks.LAND_GOLEM_CHEST.get();
        this.spawnerBlock = BTBlocks.LAND_SPAWNER.get();
        this.spawnerFillBlock = Blocks.STONE_BRICKS;
        this.golemChestLootTypes = new ArrayList<>(List.of("armor", "weapon", "gem"));
        this.towerChestLootTypes = new ArrayList<>(List.of("armor", "weapon", "metal", "consumable"));
        this.golemLoot = new ItemStack[]{Items.STONE_BRICKS.getDefaultInstance(), Items.CLAY.getDefaultInstance(), Items.DIAMOND.getDefaultInstance()};
        super.serverInitialize();
    }

    @Override
    public void clientInitialize() {
        this.BOSS_MUSIC = LAND_GOLEM_FIGHT_MUSIC;
        this.TOWER_MUSIC = LAND_TOWER_MUSIC;
        super.clientInitialize();
    }

    @Override
    public void extraCheck(BlockPos toUpdate, Level level) {
        // Update Sand
        if (level.getBlockState(toUpdate) == Blocks.SAND.defaultBlockState()) {
            level.removeBlock(toUpdate, false);
            // BrassAmberBattleTowers.LOGGER.debug("Sand? :" + level.getBlockState(toUpdate));
            level.setBlockAndUpdate(toUpdate, Blocks.SAND.defaultBlockState());
        }
    }
}
