package com.brass_amber.ba_bt.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;

public class TowerData {
    public static final Codec<TowerData> CODEC = RecordCodecBuilder.create(towerGenInfoInstance ->
            towerGenInfoInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(towerData -> towerData.name),
                    EntityData.CODEC.fieldOf("entity_data").forGetter(towerData -> towerData.entityData),
                    GenerationData.CODEC.fieldOf("generation_data").forGetter(towerData -> towerData.generationData),
                    Vec3i.CODEC.fieldOf("monolith_offset").forGetter(towerData -> towerData.monolithOffset),
                    Vec3i.CODEC.fieldOf("obelisk_offset").forGetter(towerData -> towerData.obeliskOffset)
            ).apply(towerGenInfoInstance, TowerData::new)
    );

    private final String name;
    private final EntityData entityData;
    private final GenerationData generationData;
    private final Vec3i monolithOffset;
    private final Vec3i obeliskOffset;

    public TowerData(String name, EntityData entityData, GenerationData generationData, Vec3i monolithOffset, Vec3i obeliskOffset) {
        this.name = name;
        this.entityData = entityData;
        this.generationData = generationData;
        this.monolithOffset = monolithOffset;
        this.obeliskOffset = obeliskOffset;
    }

    public String getName() {
        return name;
    }

    public EntityData getEntityData() {
        return entityData;
    }
}
