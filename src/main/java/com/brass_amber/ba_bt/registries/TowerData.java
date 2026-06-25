package com.brass_amber.ba_bt.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;

public class TowerData {
    public static final Codec<TowerData> CODEC = RecordCodecBuilder.create(towerGenInfoInstance ->
            towerGenInfoInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(towerData -> towerData.name),
                    EntityData.CODEC.fieldOf("entityData").forGetter(towerData -> towerData.entityData),
                    Codec.STRING.fieldOf("translationKey").forGetter(towerData -> towerData.translationKey),
                    Vec3i.CODEC.fieldOf("monolithOffset").forGetter(towerData -> towerData.monolithOffset),
                    Vec3i.CODEC.fieldOf("obeliskOffset").forGetter(towerData -> towerData.obeliskOffset)
            ).apply(towerGenInfoInstance, TowerData::new)
    );

    private final String name;
    private final EntityData entityData;
    private final String translationKey;
    private final Vec3i monolithOffset;
    private final Vec3i obeliskOffset;

    public TowerData(String name, EntityData entityData, String translationKey, Vec3i monolithOffset, Vec3i obeliskOffset) {
        this.name = name;
        this.entityData = entityData;
        this.translationKey = translationKey;
        this.monolithOffset = monolithOffset;
        this.obeliskOffset = obeliskOffset;
    }
}
