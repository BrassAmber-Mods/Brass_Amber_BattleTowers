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
        return Math.round(this.crumbleSpeed * 10);
    }

    public int getHeight() {
        return this.height;
    }

    public static TowerSpecs getTowerFromGolem(GolemType golemType) {
        if (golemType != null) {
            if (golemType.equals(GolemType.LAND)) {
                return LAND;
            } else if (golemType.equals(GolemType.OCEAN)) {
                return OCEAN;
            } else if (golemType.equals(GolemType.NETHER)) {
                return NETHER;
            } else if (golemType.equals(GolemType.CORE)) {
                return CORE;
            } else if (golemType.equals(GolemType.END)) {
                return END;
            } else if (golemType.equals(GolemType.SKY)) {
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
