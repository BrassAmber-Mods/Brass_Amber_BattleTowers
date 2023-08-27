package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BTCreativeTab {

	public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB.registry(), BrassAmberBattleTowers.MODID);

	/*public static RegistryObject<CreativeModeTab> BT_TAB = CREATIVE_MODE_TABS.register("bt_tab", () ->
			CreativeModeTab.builder().icon(() -> new ItemStack(BTBlocks.TAB_ICON.get()))
					.title(Component.translatable("creativemodetab.ba_bt.bt_tab")).build());
	*/
}