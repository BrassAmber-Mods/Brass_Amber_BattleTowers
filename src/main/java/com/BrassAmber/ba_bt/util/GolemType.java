package com.BrassAmber.ba_bt.util;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.BrassAmber.ba_bt.item.BTItems;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public enum GolemType implements IStringSerializable {
	EMPTY("empty"),
	LAND("land"),
	OCEAN("ocean"),
	NETHER("nether"),
	CORE("core"),
	END("end"),
	SKY("sky");

	private String name;

	GolemType(String name) {
		this.name = name;
	}

	/*********************************************************** Monolith Spawning ********************************************************/

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	@Nullable
	public static EntityType<?> getGolemFor(GolemType golemType) {
		switch (golemType) {
		default:
		case LAND:
			return BTEntityTypes.LAND_GOLEM;
		case OCEAN:
			return BTEntityTypes.OCEAN_GOLEM;
		case NETHER:
			return BTEntityTypes.NETHER_GOLEM;
		case CORE:
			return BTEntityTypes.CORE_GOLEM;
		case END:
			return BTEntityTypes.END_GOLEM;
		case SKY:
			return BTEntityTypes.SKY_GOLEM;

		}
	}

	/*********************************************************** Monolith ********************************************************/

	/**
	 * Get the correct Monolith Item for the Correct Monolith Entity.
	 */
	@Nullable
	public static Item getMonolithItemFor(GolemType golemType) {
		switch (golemType) {
		case EMPTY:
		default:
			return (Item) null;
		case LAND:
			return BTItems.LAND_MONOLITH;
		case OCEAN:
			return BTItems.OCEAN_MONOLITH;
		case NETHER:
			return BTItems.NETHER_MONOLITH;
		case CORE:
			return BTItems.CORE_MONOLITH;
		case END:
			return BTItems.END_MONOLITH;
		case SKY:
			return BTItems.SKY_MONOLITH;

		}
	}

	/**
	 * Return the correct GolemType for each Monolith Entity.
	 */
	public static GolemType getTypeForMonolith(MonolithEntity monolithEntity) {
		EntityType<?> entityType = monolithEntity.getEntity().getType();
		if (entityType != null) {
			if (entityType.equals(BTEntityTypes.LAND_MONOLITH)) {
				return LAND;
			} else if (entityType.equals(BTEntityTypes.OCEAN_MONOLITH)) {
				return OCEAN;
			} else if (entityType.equals(BTEntityTypes.NETHER_MONOLITH)) {
				return NETHER;
			} else if (entityType.equals(BTEntityTypes.CORE_MONOLITH)) {
				return CORE;
			} else if (entityType.equals(BTEntityTypes.END_MONOLITH)) {
				return END;
			} else if (entityType.equals(BTEntityTypes.SKY_MONOLITH)) {
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
			return (Item) null;
		case LAND:
			return BTItems.LAND_GUARDIAN_EYE;
		case OCEAN:
			return BTItems.OCEAN_GUARDIAN_EYE;
		case NETHER:
			return BTItems.NETHER_GUARDIAN_EYE;
		case CORE:
			return BTItems.CORE_GUARDIAN_EYE;
		case END:
			return BTItems.END_GUARDIAN_EYE;
		case SKY:
			return BTItems.SKY_GUARDIAN_EYE;
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
			return BTItems.LAND_MONOLOITH_KEY;
		case OCEAN:
			return BTItems.OCEAN_MONOLOITH_KEY;
		case NETHER:
			return BTItems.NETHER_MONOLOITH_KEY;
		case CORE:
			return BTItems.CORE_MONOLOITH_KEY;
		case END:
			return BTItems.END_MONOLOITH_KEY;
		case SKY:
			return BTItems.SKY_MONOLOITH_KEY;
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

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
