package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.entity.BTEntityTypes;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TowerSpecs implements IStringSerializable {
    EMPTY("",0,0, ""),
    LAND("land", 112, 2, "#9BDAE7"),
    OCEAN("ocean", 112, 2, "#EAE78A"),
    NETHER("nether", 112, 2, "#88EB63"),
    CORE("core", 112, 1, "#F79B3A"),
    END("end", 112, 1, "#BA49EF"),
    SKY("sky", 112, 1, "#FBC1EB");

    private String name;
    private int height;
    private int crumbleSpeed;
    private String colorCode;

    TowerSpecs(String name, int height, int crumbleSpeed, String colorCode) {
        this.name = name;
        this.height = height;
        this.crumbleSpeed = crumbleSpeed;
        this.colorCode = colorCode;
    }

    public int getCrumbleSpeed() {
        return this.crumbleSpeed;
    }

    public int getHeight() {
        return this.height;
    }

    public String getColorCode() {
        return this.colorCode;
    }

    public String getCapitalizedName() {
        return this.name.substring(0,1).toUpperCase() + this.name.substring(1);
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
