package com.brass_amber.ba_bt.item.item;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.entity.block.BTAbstractObelisk;
import com.brass_amber.ba_bt.init.BTEntityTypes;
import com.brass_amber.ba_bt.init.BTExtras;
import com.brass_amber.ba_bt.util.BTUtil;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ResonanceStoneItem extends RecordItem {

    public Enchantment enchantment;
    public MobEffect effect;
    private final GolemType golemType;
    public boolean effectOn;
    private boolean initialized;

    public ResonanceStoneItem(String golemName, Properties properties, int length) {
        super(2, BTUtil.getTowerMusic(GolemType.getTypeForName(golemName)), properties, length);
        this.golemType = GolemType.getTypeForName(golemName);
        this.effectOn = false;
        this.initialized = false;
    }

    public boolean isEnchantable(ItemStack itemStack) {
        return itemStack.getCount() == 1;
    }
    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.SPYGLASS;
    }
    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 2000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int time) {
        int i = this.getUseDuration(itemStack) - time;
        if (i > 4 && !level.isClientSide()) {
            this.effectOn = !this.effectOn;
            BrassAmberBattleTowers.LOGGER.info("Resonance effect: " + this.effectOn);
        }
        super.releaseUsing(itemStack, level, entity, time);
    }

    public void addEnchantment(ItemStack stackInUse) {
        if (this.enchantment != null) {
            Map<Enchantment, Integer> map = Map.of(this.enchantment, 1);
            EnchantmentHelper.setEnchantments(map, stackInUse);
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        super.inventoryTick(itemStack, level, entity, i, b);
        if (!this.initialized) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (this.golemType) {
                case OCEAN -> {
                    this.enchantment = BTExtras.DEPTH_DROPPER.get();
                    this.effect = BTExtras.DEPTH_DROPPER_EFFECT.get();
                }
                default -> {
                    this.enchantment = null;
                    this.effect = null;
                }
            }
            this.initialized = true;
        }

        if (this.enchantment != null && EnchantmentHelper.getEnchantments(itemStack).containsKey(this.enchantment) && !level.isClientSide()) {
            if (entity instanceof LivingEntity player && this.effectOn) {
                player.forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(),80, 3), null);
            }
        }
    }
}
