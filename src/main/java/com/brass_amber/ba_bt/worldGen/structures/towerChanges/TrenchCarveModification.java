package com.brass_amber.ba_bt.worldGen.structures.towerChanges;

import com.brass_amber.ba_bt.entity.block.ObeliskEntity;
import com.brass_amber.ba_bt.registries.LocalModification;
import com.brass_amber.ba_bt.registries.LocalModificationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class TrenchCarveModification extends LocalModification {

    public static final MapCodec<TrenchCarveModification> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                settingsCodec(instance)
            ).apply(instance, TrenchCarveModification::new));


    private final int baseWallDistance;
    private final int depth;
    private final int wallStep;
    private final int floorHeight;


    public TrenchCarveModification(Settings settings, int baseWallDistance, int depth, int wallStep, int floorHeight) {
        super(settings);
    }

    public void cleanupModificationArea(ObeliskEntity obeliskEntity) {
        this.generationState = GenerationState.GATHER_AREA_BLOCKS;
    }

    public void gatherAreaBlocks(ObeliskEntity obeliskEntity) {

        while( this.currentCarveLayer > this.bottom) {
            int blockCheckYRange = this.currentCarveY + this.floorHeight;
        }

        this.generationState = GenerationState.REMOVE_AREA_BLOCKS;
    }

    public void removeAreaBlocks(ObeliskEntity obeliskEntity) {
        this.generationState = GenerationState.ADD_AREA_FEATURES;
    }

    public void addAreaFeatures(ObeliskEntity obeliskEntity) {
        this.generationState = GenerationState.FINISHED;
    }
    @Override
    public Codec<? extends LocalModification> codec() {
        return null;
    }

    @Override
    public LocalModificationType<?> type() {
        return null;
    }
}
