package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.IStringSerializable;

public enum TowerSpecs implements IStringSerializable {
    EMPTY("",0,0),
    LAND("land", 112, 2),
    OCEAN("ocean", 112, 2),
    NETHER("nether", 112, 2),
    CORE("core", 112, 1),
    END("end", 112, 1),
    SKY("sky", 112, 1);

    private String name;
    private int height;
    private int crumbleSpeed;

    TowerSpecs(String name, int height, int crumbleSpeed) {
        this.name = name;
        this.height = height;
        this.crumbleSpeed = crumbleSpeed;
    }

    public int getCrumbleSpeed() {
        return this.crumbleSpeed;
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
