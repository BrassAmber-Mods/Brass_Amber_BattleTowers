package com.BrassAmber.ba_bt.item.item;

import com.BrassAmber.ba_bt.entity.block.BTAbstractObelisk;
import com.BrassAmber.ba_bt.init.BTBlocks;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.init.BTExtras;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import com.BrassAmber.ba_bt.util.BTUtil;
import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResonanceStoneItem extends RecordItem {

    public Enchantment enchantment;
    public MobEffect effect;
    private final GolemType golemType;
    public boolean effectOn = false;
    private boolean initialized = false;
    private @NotNull EntityType<BTAbstractObelisk> obelisk;

    public ResonanceStoneItem(String golemName, Properties properties) {
        super(2, BTUtil.getTowerMusic(GolemType.getTypeForName(golemName)), properties);
        this.golemType = GolemType.getTypeForName(golemName);
    }

    public boolean isEnchantable(ItemStack itemStack) {
        return itemStack.getCount() == 1;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        this.effectOn = !this.effectOn;
        return super.use(level, player, hand);
    }

    public void addEnchantment(ItemStack stackInUse) {
        Map<Enchantment, Integer> map = Map.of(this.enchantment, 1);
        EnchantmentHelper.setEnchantments(map, stackInUse);
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
                    this.obelisk = BTEntityTypes.OCEAN_OBELISK.get();
                }
                default -> {
                    this.enchantment = null;
                    this.effect = null;
                }
            }
            this.initialized = true;
        }


        if (this.enchantment != null && EnchantmentHelper.getEnchantments(itemStack).containsKey(this.enchantment)) {
            if (entity instanceof LivingEntity living && this.effectOn) {
                living.forceAddEffect(new MobEffectInstance(BTExtras.DEPTH_DROPPER_EFFECT.get(),60, 2), living);
            }
        }
    }
}
