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
	CORE("core"),
	NETHER("nether"),
	END("end"),
	SKY("sky"),
	OCEAN("ocean");

	private String name;

	GolemType(String name) {
		this.name = name;
	}

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static GolemType getTypeForMonolith(MonolithEntity monolithEntity) {
		EntityType<?> entityType = monolithEntity.getEntity().getType();
		if (entityType != null) {
			if (entityType.equals(BTEntityTypes.LAND_MONOLITH)) {
				return LAND;
			} else if (entityType.equals(BTEntityTypes.CORE_MONOLITH)) {
				return CORE;
			} else if (entityType.equals(BTEntityTypes.NETHER_MONOLITH)) {
				return NETHER;
			} else if (entityType.equals(BTEntityTypes.END_MONOLITH)) {
				return END;
			} else if (entityType.equals(BTEntityTypes.SKY_MONOLITH)) {
				return SKY;
			} else if (entityType.equals(BTEntityTypes.OCEAN_MONOLITH)) {
				return OCEAN;
			}
		}

		// Couldn't get EntityType
		return EMPTY;
	}

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	@Nullable
	public static EntityType<?> getGolemFor(GolemType golemType) {
		switch (golemType) {
		default:
		case LAND:
			return BTEntityTypes.LAND_GOLEM;
		case CORE:
			return BTEntityTypes.CORE_GOLEM;
		case NETHER:
			return BTEntityTypes.NETHER_GOLEM;
		case END:
			return BTEntityTypes.END_GOLEM;
		case SKY:
			return BTEntityTypes.SKY_GOLEM;
		case OCEAN:
			return BTEntityTypes.OCEAN_GOLEM;
		}
	}

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	@Nullable
	public static Item getMonolithItemFor(GolemType golemType) {
		switch (golemType) {
		case EMPTY:
		default:
			return (Item) null;
		case LAND:
			return BTItems.LAND_MONOLITH;
		case CORE:
			return BTItems.CORE_MONOLITH;
		case NETHER:
			return BTItems.NETHER_MONOLITH;
		case END:
			return BTItems.END_MONOLITH;
		case SKY:
			return BTItems.SKY_MONOLITH;
		case OCEAN:
			return BTItems.OCEAN_MONOLITH;
		}
	}

	@Nullable
	public static Item getEyeFor(GolemType golemType) {
		switch (golemType) {
		case EMPTY:
		default:
			return (Item) null;
		case LAND:
			return BTItems.LAND_GUARDIAN_EYE;
		case CORE:
			return BTItems.CORE_GUARDIAN_EYE;
		case NETHER:
			return BTItems.NETHER_GUARDIAN_EYE;
		case END:
			return BTItems.END_GUARDIAN_EYE;
		case SKY:
			return BTItems.SKY_GUARDIAN_EYE;
		case OCEAN:
			return BTItems.OCEAN_GUARDIAN_EYE;
		}
	}

	@Nullable
	public static Item getKeyFor(GolemType golemType) {
		switch (golemType) {
		case EMPTY:
		default:
			return (Item) null;
		case LAND:
			return BTItems.LAND_MONOLOITH_KEY;
		case CORE:
			return BTItems.CORE_MONOLOITH_KEY;
		case NETHER:
			return BTItems.NETHER_MONOLOITH_KEY;
		case END:
			return BTItems.END_MONOLOITH_KEY;
		case SKY:
			return BTItems.SKY_MONOLOITH_KEY;
		case OCEAN:
			return BTItems.OCEAN_MONOLOITH_KEY;
		}
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
