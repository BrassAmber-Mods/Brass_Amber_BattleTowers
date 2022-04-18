package com.BrassAmber.ba_bt.item.item;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.BTMonolith;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MonolithItem extends Item {
	private final GolemType monolithType;

	public MonolithItem(GolemType type, Item.Properties builder) {
		super(builder);
		this.monolithType = type;
	}

	/*********************************************************** Placement ********************************************************/

	/**
	 * Called when this item is used when targeting a Block
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		Direction clickedBlockFace = context.getClickedFace();
		BlockPos newPlacementPos = clickedBlockFace.equals(Direction.DOWN) ? clickedPos.relative(clickedBlockFace, 3) : clickedPos.relative(clickedBlockFace);
		if (!this.hasEnoughSpace(level, newPlacementPos, clickedBlockFace)) {
			return InteractionResult.FAIL;
		} else {
			double x = (double) newPlacementPos.getX();
			double y = (double) newPlacementPos.getY();
			double z = (double) newPlacementPos.getZ();
			List<Entity> list = level.getEntities((Entity) null, new AABB(x, y, z, x + 1.0D, y + 3.0D, z + 1.0D));
			if (!list.isEmpty()) {
				return InteractionResult.FAIL;
			} else {
				if (level instanceof ServerLevel) {
					double centerOnBlock = 0.5D;
					BTMonolith newBTMonolithEntity = new BTMonolith(GolemType.getMonolithFor(this.monolithType), level, x + centerOnBlock, y, z + centerOnBlock, level.getBlockState(newPlacementPos.below()));
					newBTMonolithEntity.setYRot(this.getPlacementDirection(context));
					level.addFreshEntity(newBTMonolithEntity);
				}

				// TODO Fix subtitles
				level.playSound(null, newPlacementPos, SoundEvents.IRON_GOLEM_STEP, SoundSource.BLOCKS, this.getSoundVolume() + 2.0F, this.getSoundPitch() + 1.0F);
				level.playSound(null, newPlacementPos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, this.getSoundVolume() - 0.7F, this.getSoundPitch());
				level.playSound(null, newPlacementPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, this.getSoundVolume() - 0.25F, this.getSoundPitch());
				context.getItemInHand().shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		}
	}

	/**
	 * Returns the correct placement rotation for the entity
	 */
	private float getPlacementDirection(UseOnContext context) {
		float angle = context.getHorizontalDirection().toYRot();
		// Invert placement facing east and west.
		return angle == 90 ? 270 : angle == 270 ? 90 : angle;
	}

	/**
	 * Checks if there are any Blocks in the way
	 */
	private boolean hasEnoughSpace(Level world, BlockPos pos, Direction clickedBlockFace) {
		for (int height = 0; height < 3; height++) {
			if (!world.isEmptyBlock(pos.offset(0, height, 0))) {
				return false;
			}
		}
		return true;
	}

	/*********************************************************** Characteristics ********************************************************/


	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(new TranslatableComponent("tooltip.ba_bt.monolith").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(BrassAmberBattleTowers.HOLD_SHIFT_TOOLTIP);
		}
	}

	/*********************************************************** Sounds ********************************************************/

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	private float getSoundVolume() {
		return 0.8F;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	private float getSoundPitch() {
		Random random = new Random();
		float avaragePitch = 0.0F;
		return (random.nextFloat() - random.nextFloat()) * 0.2F + avaragePitch;

	}
}
