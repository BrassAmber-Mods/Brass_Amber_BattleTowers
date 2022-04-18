package com.BrassAmber.ba_bt.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TowerSpecs implements StringRepresentable {
    EMPTY(null,0,0, ""),
    LAND(new TranslatableComponent("name.ba_bt.land"), 112, 2, "#9BDAE7"),
    OCEAN(new TranslatableComponent("name.ba_bt.ocean"), 112, 2, "#EAE78A"),
    NETHER(new TranslatableComponent("name.ba_bt.core"), 112, 2, "#88EB63"),
    CORE(new TranslatableComponent("name.ba_bt.nether"), 112, 1, "#F79B3A"),
    END(new TranslatableComponent("name.ba_bt.end"), 112, 1, "#BA49EF"),
    SKY(new TranslatableComponent("name.ba_bt.sky"), 112, 1, "#FBC1EB");

    private final Component name;
    private final int height;
    private final int crumbleSpeed;
    private final String colorCode;
    private final Component defeat1 = new TranslatableComponent("title.ba_bt.guardian_defeated_1");
    private final Component defeat2 = new TranslatableComponent("title.ba_bt.guardian_defeated_2");
    private final Component defeat3 = new TranslatableComponent("title.ba_bt.guardian_defeated_3");

    TowerSpecs(Component name, int height, int crumbleSpeed, String colorCode) {
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

    public Component getCapitalizedName() {
        return this.name;
    }

    public String getTitleText1() {
        return this.name.getString() + " " + this.defeat1.getString();
    }

    public String getTitleText2() {
        return this.defeat2.getString();
    }

    public String getTitleText3() {
        return this.defeat3.getString();
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
        return this.name.getString();
    }
}
