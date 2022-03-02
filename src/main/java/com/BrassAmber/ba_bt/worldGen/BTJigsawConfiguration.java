package com.BrassAmber.ba_bt.worldGen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

import java.util.function.Supplier;

public class BTJigsawConfiguration implements FeatureConfiguration {
    public static final Codec<BTJigsawConfiguration> CODEC = RecordCodecBuilder.create((p_67764_) -> {
        return p_67764_.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(BTJigsawConfiguration::startPool), Codec.intRange(0, 25).fieldOf("size").forGetter(BTJigsawConfiguration::maxDepth)).apply(p_67764_, BTJigsawConfiguration::new);
    });
    private final Supplier<StructureTemplatePool> startPool;
    private final int maxDepth;

    public BTJigsawConfiguration(Supplier<StructureTemplatePool> p_67761_, int p_67762_) {
        this.startPool = p_67761_;
        this.maxDepth = p_67762_;
    }

    public int maxDepth() {
        return this.maxDepth;
    }

    public Supplier<StructureTemplatePool> startPool() {
        return this.startPool;
    }
}
