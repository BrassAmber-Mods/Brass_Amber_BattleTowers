package com.BrassAmber.ba_bt.item.item;

import java.util.List;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MonolithItem extends Item {
	private final EntityType<MonolithEntity> monolithEntityType;

	public MonolithItem(EntityType<MonolithEntity> monolithEntityType, Item.Properties builder) {
		super(builder);
		this.monolithEntityType = monolithEntityType;
	}

	/*********************************************************** Placement ********************************************************/

	/**
	 * Called when this item is used when targetting a Block
	 */
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		Direction clickedBlockFace = context.getClickedFace();
		BlockPos newPlacementPos = clickedBlockFace.equals(Direction.DOWN) ? clickedPos.relative(clickedBlockFace, 3) : clickedPos.relative(clickedBlockFace);
		if (!this.hasEnoughSpace(world, newPlacementPos, clickedBlockFace)) {
			return ActionResultType.FAIL;
		} else {
			double x = (double) newPlacementPos.getX();
			double y = (double) newPlacementPos.getY();
			double z = (double) newPlacementPos.getZ();
			List<Entity> list = world.getEntities((Entity) null, new AxisAlignedBB(x, y, z, x + 1.0D, y + 3.0D, z + 1.0D));
			if (!list.isEmpty()) {
				return ActionResultType.FAIL;
			} else {
				if (world instanceof ServerWorld) {
					double centerOnBlock = 0.5D;
					MonolithEntity newMonolithEntity = new MonolithEntity(this.monolithEntityType, world, x + centerOnBlock, y, z + centerOnBlock);
					newMonolithEntity.yRot = this.getPlacementDirection(context);
					world.addFreshEntity(newMonolithEntity);
				}

				// TODO Fix subtitles
				world.playSound(null, newPlacementPos, SoundEvents.IRON_GOLEM_STEP, SoundCategory.BLOCKS, this.getSoundVolume() + 2.0F, this.getSoundPitch() + 1.0F);
				world.playSound(null, newPlacementPos, SoundEvents.ANVIL_PLACE, SoundCategory.BLOCKS, this.getSoundVolume() - 0.7F, this.getSoundPitch());
				world.playSound(null, newPlacementPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, this.getSoundVolume() - 0.25F, this.getSoundPitch());
				context.getItemInHand().shrink(1);
				return ActionResultType.sidedSuccess(world.isClientSide());
			}
		}
	}

	/**
	 * Returns the correct placement rotation for the entity
	 */
	private float getPlacementDirection(ItemUseContext context) {
		float angle = context.getHorizontalDirection().toYRot();
		// Invert placement facing east and west.
		return angle == 90 ? 270 : angle == 270 ? 90 : angle;
	}

	/**
	 * Checks if there are any Blocks in the way
	 */
	private boolean hasEnoughSpace(World world, BlockPos pos, Direction clickedBlockFace) {
		for (int height = 0; height < 3; height++) {
			if (!world.isEmptyBlock(pos.offset(0, height, 0))) {
				return false;
			}
		}
		return true;
	}

	/*********************************************************** Characteristics ********************************************************/

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			tooltip.add(new TranslationTextComponent("tooltip.battletowers.monolith").withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GRAY));
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
		float avaragePitch = 0.0F;
		return (random.nextFloat() - random.nextFloat()) * 0.2F + avaragePitch;
	}
}
