package com.BrassAmber.ba_bt.item;

import java.util.List;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MonolithKeyItem extends Item {

	public MonolithKeyItem(Item.Properties builder) {
		super(builder);
	}
	
	/**
	 * Called to trigger the item's "innate" right click behavior.
	 */
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
		// Used for SoundEvent testing
		player.playSound(BTSoundEvents.ENTITY_GOLEM_AMBIENT, 1.0f, 1.0f);
		return super.use(worldIn, player, hand);
	}
	
	/*********************************************************** Characteristics ********************************************************/

	/**
	 * Returns true if this item has an enchantment glint. By default, this returns <code>stack.isItemEnchanted()</code>,
	 * but other items can override it (for instance, written books always return true).
	 *  
	 * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
	 * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
	 */
	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
	
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(new TranslationTextComponent("tooltip.battletowers.monolith_key").withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GRAY));
		} else {
			tooltip.add(BrassAmberBattleTowers.HOLD_SHIFT_TOOLTIP);
		}
	}
}
