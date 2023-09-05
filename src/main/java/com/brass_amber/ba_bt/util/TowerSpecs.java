package com.brass_amber.ba_bt.util;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TowerSpecs implements StringRepresentable {
    EMPTY(null,0,0, ""),
    LAND("land", 112, 2, "#9BDAE7"),
    OCEAN("ocean", 90, 2, "#EAE78A"),
    NETHER("core", 112, 2, "#88EB63"),
    CORE("nether", 112, 1, "#F79B3A"),
    END("end", 112, 1, "#BA49EF"),
    SKY("sky", 112, 1, "#FBC1EB");

    private final Component name;
    private final int height;
    private final int crumbleSpeed;
    private final String colorCode;
    private final Component defeat1 = Component.translatable("title.ba_bt.guardian_defeated_1");
    private final Component defeat2;
    private final Component defeat3;
    TowerSpecs(String baseName, int height, int crumbleSpeed, String colorCode) {
        this.name = Component.translatable("title.ba_bt." + baseName);
        this.height = height;
        this.crumbleSpeed = crumbleSpeed;
        this.colorCode = colorCode;
        defeat2 = Component.translatable("title.ba_bt." + baseName + "_defeated_2");
        defeat3 = Component.translatable("title.ba_bt." + baseName + "_defeated_3");
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
