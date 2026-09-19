package com.brass_amber.ba_bt.worldGen.structures.towerChanges;

import com.brass_amber.ba_bt.entity.block.ObeliskEntity;
import com.brass_amber.ba_bt.registries.LocalModification;
import com.brass_amber.ba_bt.registries.LocalModificationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class EmptyModification extends LocalModification {

    public static final MapCodec<EmptyModification> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                settingsCodec(instance)
            ).apply(instance, EmptyModification::new));

    public EmptyModification(Settings settings) {
        super(settings);
    }

    public void cleanupModificationArea(ObeliskEntity obeliskEntity) {
        this.generationState = GenerationState.GATHER_AREA_BLOCKS;
    }

    public void gatherAreaBlocks(ObeliskEntity obeliskEntity) {
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
