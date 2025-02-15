package com.brass_amber.ba_bt.block.block;

import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.function.Supplier;

public class TowerChestBlock extends BTChestBlock {
    public TowerChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> chestSupplier, Properties properties, GolemType type) {
        super(chestSupplier, properties, type);
        this.golemChest = false;
    }
}
