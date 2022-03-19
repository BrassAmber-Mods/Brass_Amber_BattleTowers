package com.BrassAmber.ba_bt.util;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.entity.block.BTObelisk;
import com.BrassAmber.ba_bt.entity.block.BTMonolith;
import com.BrassAmber.ba_bt.init.BTEntityTypes;
import com.BrassAmber.ba_bt.init.BTItems;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public enum GolemType implements StringRepresentable {
	EMPTY("empty", "Empty"),
	LAND("land", "Bahrynz'muul, entombed watcher"),
	OCEAN("ocean", "Moraizu'un, idol of the depths"),
	CORE("core", "Vraag'iidra, wrath incarnate"),
	NETHER("nether", "Obthu'ryn, god of the core"),
	END("end", "Shul'losniir, warped guardian"),
	SKY("sky", "Veyhz'kriin, sky goddess");

	private final String name;
	private final String displayName;

	GolemType(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	/*********************************************************** Monolith Spawning ********************************************************/

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<?> getGolemFor(GolemType golemType) {
		return switch (golemType) {
			default -> BTEntityTypes.LAND_GOLEM.get();
			case OCEAN -> BTEntityTypes.OCEAN_GOLEM.get();
			case NETHER -> BTEntityTypes.NETHER_GOLEM.get();
			case CORE -> BTEntityTypes.CORE_GOLEM.get();
			case END -> BTEntityTypes.END_GOLEM.get();
			case SKY -> BTEntityTypes.SKY_GOLEM.get();
		};
	}

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<BTMonolith> getMonolithFor(GolemType golemType) {
		return switch (golemType) {
			default -> BTEntityTypes.LAND_MONOLITH.get();
			case OCEAN -> BTEntityTypes.OCEAN_MONOLITH.get();
			case NETHER -> BTEntityTypes.NETHER_MONOLITH.get();
			case CORE -> BTEntityTypes.CORE_MONOLITH.get();
			case END -> BTEntityTypes.END_MONOLITH.get();
			case SKY -> BTEntityTypes.SKY_MONOLITH.get();
		};
	}

	/*********************************************************** Obelisk ********************************************************/

	/**
	 * Get the correct Obelisk for the Golem Type.
	 * @return
	 */
	@NotNull
	public static EntityType<BTObelisk> getObeliskFor(GolemType golemType) {
		return switch (golemType) {
			default -> BTEntityTypes.LAND_OBELISK.get();
			case OCEAN -> BTEntityTypes.OCEAN_OBELISK.get();
			case NETHER -> BTEntityTypes.NETHER_OBELISK.get();
			case CORE -> BTEntityTypes.CORE_OBELISK.get();
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
			case NETHER -> BTItems.NETHER_MONOLITH.get();
			case CORE -> BTItems.CORE_MONOLITH.get();
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
			} else if (entityType.equals(BTEntityTypes.NETHER_MONOLITH.get())) {
				return NETHER;
			} else if (entityType.equals(BTEntityTypes.CORE_MONOLITH.get())) {
				return CORE;
			} else if (entityType.equals(BTEntityTypes.END_MONOLITH.get())) {
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
		switch (golemType) {
		case EMPTY:
		default:
			return null;
		case LAND:
			return BTItems.LAND_GUARDIAN_EYE.get();
		case OCEAN:
			return BTItems.OCEAN_GUARDIAN_EYE.get();
		case NETHER:
			return BTItems.NETHER_GUARDIAN_EYE.get();
		case CORE:
			return BTItems.CORE_GUARDIAN_EYE.get();
		case END:
			return BTItems.END_GUARDIAN_EYE.get();
		case SKY:
			return BTItems.SKY_GUARDIAN_EYE.get();
		}
	}

	/**
	 * Return the previous GolemType in fighting order.
	 */
	@Nullable
	public static GolemType getPreviousGolemType(GolemType golemType) {
		switch (golemType) {
		case EMPTY:
		case LAND:
		default:
			return EMPTY;
		case OCEAN:
			return LAND;
		case NETHER:
			return OCEAN;
		case CORE:
			return NETHER;
		case END:
			return CORE;
		case SKY:
			return END;
		}
	}

	/*********************************************************** Keys ********************************************************/

	@Nullable
	public static Item getKeyFor(GolemType golemType) {
		switch (golemType) {
		case EMPTY:
		default:
			return (Item) null;
		case LAND:
			return BTItems.LAND_MONOLOITH_KEY.get();
		case OCEAN:
			return BTItems.OCEAN_MONOLOITH_KEY.get();
		case NETHER:
			return BTItems.NETHER_MONOLOITH_KEY.get();
		case CORE:
			return BTItems.CORE_MONOLOITH_KEY.get();
		case END:
			return BTItems.END_MONOLOITH_KEY.get();
		case SKY:
			return BTItems.SKY_MONOLOITH_KEY.get();
		}
	}

	/*********************************************************** Extra ********************************************************/

	public static GolemType getTypeForName(String name) {
		switch (name) {
			case "empty":
			default:
				return null;
			case "land":
				return LAND;
			case "ocean":
				return OCEAN;
			case "nether":
				return NETHER;
			case "core":
				return CORE;
			case "end":
				return END;
			case "sky":
				return SKY;
		}
	}

	public static int getNumForType(GolemType type) {
		return switch (type) {
			case EMPTY, LAND -> 0;
			case OCEAN -> 1;
			case CORE -> 2;
			case NETHER -> 3;
			case END -> 4;
			case SKY -> 5;
		};
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name;
	}

	public Component getDisplayName() {
		return Component.nullToEmpty(displayName);
	}
}
