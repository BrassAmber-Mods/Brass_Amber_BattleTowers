package com.BrassAmber.ba_bt.util;

import javax.annotation.Nullable;

import com.BrassAmber.ba_bt.block.block.TotemBlock.TotemType;
import com.BrassAmber.ba_bt.item.BTItems;

import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public enum GolemType implements IStringSerializable {
	EMPTY("empty"), // Used in the Totem, TODO
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
	
	@Nullable
	public Item getReturnItem(TotemType totemType) {
		switch (totemType) {
		case EMPTY: default:
			return null;
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

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
