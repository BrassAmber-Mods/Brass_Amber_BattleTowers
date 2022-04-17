package com.BrassAmber.ba_bt.util;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TowerSpecs implements StringRepresentable {
    EMPTY("",0,0, ""),
    LAND(new TranslatableComponent("name.battletowers.land").toString(), 112, 2, "#9BDAE7"),
    OCEAN(new TranslatableComponent("name.battletowers.ocean").toString(), 112, 2, "#EAE78A"),
    NETHER(new TranslatableComponent("name.battletowers.core").toString(), 112, 2, "#88EB63"),
    CORE(new TranslatableComponent("name.battletowers.nether").toString(), 112, 1, "#F79B3A"),
    END(new TranslatableComponent("name.battletowers.end").toString(), 112, 1, "#BA49EF"),
    SKY(new TranslatableComponent("name.battletowers.sky").toString(), 112, 1, "#FBC1EB");

    private final String name;
    private final int height;
    private final int crumbleSpeed;
    private final String colorCode;

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

    public String getTitleText1() {
        return this.getCapitalizedName() + new TranslatableComponent("title.battletowers.guardian_defeated_1");
    }

    public String getTitleText2() {
        return this.getCapitalizedName() + new TranslatableComponent("title.battletowers.guardian_defeated_2");
    }

    public String getTitleText3() {
        return this.getCapitalizedName() + new TranslatableComponent("title.battletowers.guardian_defeated_3");
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
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
