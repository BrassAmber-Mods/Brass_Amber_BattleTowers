package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.IStringSerializable;

public enum TowerSpecs implements IStringSerializable {
    EMPTY("",0,0F),
    LAND("land", 112, 1.0F),
    OCEAN("ocean", 112, 1.2F),
    NETHER("nether", 112, 1.4F),
    CORE("core", 112, 1.6F),
    END("end", 112, 1.8F),
    SKY("sky", 112, 2.0F);

    private String name;
    private int height;
    private float crumbleSpeed;

    TowerSpecs(String name, int height, float crumbleSpeed) {
        this.name = name;
        this.height = height;
        this.crumbleSpeed = crumbleSpeed;
    }

    public int getCrumbleSpeed() {
        return Math.round(this.crumbleSpeed * 20);
    }

    public int getHeight() {
        return this.height;
    }

    public static TowerSpecs getTowerFromMonolith(MonolithEntity monolithEntity) {
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

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
