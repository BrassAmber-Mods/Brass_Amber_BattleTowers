package com.brass_amber.ba_bt.inventory;

import com.brass_amber.ba_bt.init.BTContainerTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class BTChestMenu extends ChestMenu {

    private BTChestMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, int rows) {
        super(menuType, containerId, playerInventory, new SimpleContainer(9 * rows), rows);
    }

    public static BTChestMenu eightRows(int containerId, Inventory playerInventory) {
        return new BTChestMenu(BTContainerTypes.GENERIC_9x8.get(),  containerId, playerInventory, 8);
    }

    public static BTChestMenu eightRows(int containerId, Inventory playerInventory, Container container) {
        return new BTChestMenu(BTContainerTypes.GENERIC_9x8.get(),  containerId, playerInventory, container, 8);
    }

    private BTChestMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, Container container, int rows) {
        super(menuType, containerId, playerInventory, container, rows);
    }
}
