package com.BrassAmber.ba_bt.block.block;


import com.BrassAmber.ba_bt.block.tileentity.GolemChestBlockEntity;
import com.BrassAmber.ba_bt.block.tileentity.TowerChestBlockEntity;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class TowerChestBlock extends GolemChestBlock {

	public TowerChestBlock(BTChestType chestType, Properties properties) {
		super(chestType, properties, BTBlockEntityTypes.LAND_CHEST::get);

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BTBlockEntityTypes.LAND_CHEST.get().create(blockPos, blockState);
	}

}