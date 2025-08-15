package com.brass_amber.ba_bt.util;

import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

import static java.lang.Math.round;

public enum BTRarity implements StringRepresentable {
    JUNK("JUNK", 0),
    COMMON("COMMON", 1),
    UNCOMMON("UNCOMMON", 2),
    RARE("RARE", 3),
    EPIC("EPIC", 4);


    private final int rarity;
    private final String name;
    private static final BTRarity[] VALUES = values();

    BTRarity(String name, int rarity) {
        this.name = name;
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }

    public BTRarity getBefore() {
        return switch (this) {
            case UNCOMMON -> COMMON;
            case RARE -> UNCOMMON;
            case EPIC -> RARE;
            default -> JUNK;
        };
    }

    public BTRarity getAfter() {
        return switch (this) {
            case JUNK -> COMMON;
            case COMMON -> UNCOMMON;
            case UNCOMMON -> RARE;
            default -> EPIC;
        };
    }

    public boolean inRarityRange(int rarity) {
        return Mth.abs(this.rarity - rarity) <= 1;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
