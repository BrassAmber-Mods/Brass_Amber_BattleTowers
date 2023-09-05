package com.brass_amber.ba_bt.util;

import javax.annotation.Nullable;

import com.brass_amber.ba_bt.entity.LandDestructionEntity;
import com.brass_amber.ba_bt.entity.OceanDestructionEntity;
import com.brass_amber.ba_bt.entity.block.BTAbstractObelisk;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.entity.hostile.BTCultist;
import com.brass_amber.ba_bt.entity.hostile.PlatinumSkeleton;
import com.brass_amber.ba_bt.init.BTBlockEntityTypes;
import com.brass_amber.ba_bt.init.BTEntityTypes;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public enum GolemType implements StringRepresentable {
	EMPTY("empty", Component.literal("Empty")),
	LAND("land", Component.translatable("entity.ba_bt.land_golem")),
	OCEAN("ocean", Component.translatable("entity.ba_bt.ocean_golem")),
	CORE("core", Component.translatable("entity.ba_bt.core_golem")),
	NETHER("nether", Component.translatable("entity.ba_bt.nether_golem")),
	END("end", Component.translatable("entity.ba_bt.end_golem")),
	SKY("sky", Component.translatable("entity.ba_bt.sky_golem")),
	CITY("city", Component.literal("~"));

	private final String name;
	private final Component displayName;

	GolemType(String name, Component displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	/*********************************************************** Monolith Spawning ********************************************************/

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<?> getGolemFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY, LAND -> BTEntityTypes.LAND_GOLEM.get();
			case OCEAN -> BTEntityTypes.OCEAN_GOLEM.get();
			case CORE -> BTEntityTypes.CORE_GOLEM.get();
			case NETHER -> BTEntityTypes.NETHER_GOLEM.get();
			case END -> BTEntityTypes.END_GOLEM.get();
			case SKY -> BTEntityTypes.SKY_GOLEM.get();
		};
	}

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<BTMonolith> getMonolithFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY, LAND -> BTEntityTypes.LAND_MONOLITH.get();
			case OCEAN -> BTEntityTypes.OCEAN_MONOLITH.get();
			case CORE -> BTEntityTypes.CORE_MONOLITH.get();
			case NETHER -> BTEntityTypes.NETHER_MONOLITH.get();
			case END -> BTEntityTypes.END_MONOLITH.get();
			case SKY -> BTEntityTypes.SKY_MONOLITH.get();
		};
	}

	/*********************************************************** Obelisk ********************************************************/

	/**
	 * Get the correct Obelisk for the Golem Type.
	 */
	@NotNull
	public static EntityType<BTAbstractObelisk> getObeliskFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY, LAND -> BTEntityTypes.LAND_OBELISK.get();
			case OCEAN -> BTEntityTypes.OCEAN_OBELISK.get();
			case CORE -> BTEntityTypes.CORE_OBELISK.get();
			case NETHER -> BTEntityTypes.NETHER_OBELISK.get();
			case END -> BTEntityTypes.END_OBELISK.get();
			case SKY -> BTEntityTypes.SKY_OBELISK.get();
		};
	}


	/*********************************************************** Monolith ********************************************************/

	/**
	 * Get the correct Monolith Item for the Correct Monolith Entity.
	 */
	@Nullable
	public static Item getMonolithItemFor(GolemType golemType) {
		return switch (golemType) {
			default -> null;
			case LAND -> BTItems.LAND_MONOLITH.get();
			case OCEAN -> BTItems.OCEAN_MONOLITH.get();
			case CORE -> BTItems.CORE_MONOLITH.get();
			case NETHER -> BTItems.NETHER_MONOLITH.get();
			case END -> BTItems.END_MONOLITH.get();
			case SKY -> BTItems.SKY_MONOLITH.get();
		};
	}

	/**
	 * Return the correct GolemType for each Monolith Entity.
	 */
	public static GolemType getTypeForMonolith(BTMonolith BTMonolithEntity) {
		EntityType<?> entityType = BTMonolithEntity.getMonolithType();
		if (entityType != null) {
			if (entityType.equals(BTEntityTypes.LAND_MONOLITH.get())) {
				return LAND;
			} else if (entityType.equals(BTEntityTypes.OCEAN_MONOLITH.get())) {
				return OCEAN;
			} else if (entityType.equals(BTEntityTypes.CORE_MONOLITH.get())) {
				return CORE;
			}else if (entityType.equals(BTEntityTypes.NETHER_MONOLITH.get())) {
				return NETHER;
			}  else if (entityType.equals(BTEntityTypes.END_MONOLITH.get())) {
				return END;
			} else if (entityType.equals(BTEntityTypes.SKY_MONOLITH.get())) {
				return SKY;
			}
		}

		// Couldn't get EntityType
		return EMPTY;
	}

	/*********************************************************** Eyes ********************************************************/

	/**
	 * Return the matching Guardian Eye for each GolemType.
	 */
	@Nullable
	public static Item getEyeFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY -> null;
			case LAND -> BTItems.LAND_GUARDIAN_EYE.get();
			case OCEAN -> BTItems.OCEAN_GUARDIAN_EYE.get();
			case CORE -> BTItems.CORE_GUARDIAN_EYE.get();
			case NETHER -> BTItems.NETHER_GUARDIAN_EYE.get();
			case END -> BTItems.END_GUARDIAN_EYE.get();
			case SKY -> BTItems.SKY_GUARDIAN_EYE.get();
		};
	}

	/**
	 * Return the previous GolemType in fighting order.
	 */
	@Nullable
	public static GolemType getPreviousGolemType(GolemType golemType) {
		return switch (golemType) {
			default -> null;
			case EMPTY, LAND -> EMPTY;
			case OCEAN -> LAND;
			case CORE -> OCEAN;
			case NETHER -> CORE;
			case END -> NETHER;
			case SKY -> END;
		};
	}

	/*********************************************************** Keys ********************************************************/

	@Nullable
	public static Item getKeyFor(GolemType golemType) {
		return switch (golemType) {
			default -> null;
			case LAND -> BTItems.LAND_MONOLOITH_KEY.get();
			case OCEAN -> BTItems.OCEAN_MONOLOITH_KEY.get();
			case CORE -> BTItems.CORE_MONOLOITH_KEY.get();
			case NETHER -> BTItems.NETHER_MONOLOITH_KEY.get();
			case END -> BTItems.END_MONOLOITH_KEY.get();
			case SKY -> BTItems.SKY_MONOLOITH_KEY.get();
		};
	}

	/*********************************************************** Extra ********************************************************/

	public static GolemType getTypeForName(String name) {
		return switch (name) {
			default -> null;
			case "land" -> LAND;
			case "ocean" -> OCEAN;
			case "core" -> CORE;
			case "nether" -> NETHER;
			case "end" -> END;
			case "sky" -> SKY;
			case "city" -> CITY;
		};
	}

	public static Entity getDestructionEntity(GolemType golemType, Level level, BlockPos blockPos) {
		Entity destruction = null;
		switch (golemType) {
			default -> {
			}
			case LAND -> {
				destruction = new LandDestructionEntity(blockPos, level);
				blockPos = blockPos.above(6);
			}
			case OCEAN -> {
				destruction = new OceanDestructionEntity(level);
				blockPos = blockPos.atY(level.getSeaLevel() - 88);
			}
		}
		if (destruction != null) {
			destruction.setPos(blockPos.getX() + .25D, blockPos.getY(), blockPos.getZ() + .25D);
		}

		return destruction;
	}

	public static Entity getSpecialEnemy(GolemType golemType, ServerLevel serverLevel) {
		return switch (golemType) {
			default -> null;
			case LAND -> BTEntityTypes.BT_CULTIST.get().create(serverLevel);
			case OCEAN -> EntityType.GUARDIAN.create(serverLevel);
			case CORE -> EntityType.MAGMA_CUBE.create(serverLevel);
			case NETHER -> EntityType.WITHER_SKELETON.create(serverLevel);
			case END -> EntityType.ENDERMAN.create(serverLevel);
			case SKY -> BTEntityTypes.PLATINUM_SKELETON.get().create(serverLevel);
		};
	}

	public static EntityType<?> getSpecialEnemyType(GolemType golemType) {
		return switch (golemType) {
			default -> null;
			case LAND -> BTEntityTypes.BT_CULTIST.get();
			case OCEAN -> EntityType.GUARDIAN;
			case CORE -> EntityType.MAGMA_CUBE;
			case NETHER -> EntityType.WITHER_SKELETON;
			case END -> EntityType.ENDERMAN;
			case SKY -> BTEntityTypes.PLATINUM_SKELETON.get();
		};
	}

	public static Class<? extends Entity> getSpecialEnemyClass(GolemType golemType) {
		return switch (golemType) {
			default -> null;
			case LAND -> BTCultist.class;
			case OCEAN -> Guardian.class;
			case CORE -> MagmaCube.class;
			case NETHER -> WitherSkeleton.class;
			case END -> EnderMan.class;
			case SKY -> PlatinumSkeleton.class;
		};
	}

	public static Integer getNumForType(GolemType golemType) {
		return switch (golemType) {
			default -> null;
			case LAND -> 0;
			case OCEAN -> 1;
			case CORE -> 2;
			case NETHER -> 3;
			case END -> 4;
			case SKY -> 5;
			case CITY -> 6;
		};
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name;
	}

	public Component getDisplayName() {
		return this.displayName;
	}
}
