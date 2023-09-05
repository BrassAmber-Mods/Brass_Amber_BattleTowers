package com.brass_amber.ba_bt.util;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BTCreativeTab {

	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BrassAmberBattleTowers.MOD_ID);

	public static RegistryObject<CreativeModeTab> BT_TAB = CREATIVE_MODE_TABS.register("bt_tab", () ->
			CreativeModeTab.builder().icon(() -> new ItemStack(BTBlocks.TAB_ICON.get()))
					.title(Component.translatable("creativemodetab.bt_tab")).build());

	public static void register(IEventBus eventBus) {
		CREATIVE_MODE_TABS.register(eventBus);
	}

}