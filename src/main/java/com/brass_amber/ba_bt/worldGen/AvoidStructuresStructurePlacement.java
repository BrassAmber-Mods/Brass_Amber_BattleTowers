package com.brass_amber.ba_bt.worldGen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.BattleTowersConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.Optional;

public class AvoidStructuresStructurePlacement extends RandomSpreadStructurePlacement {

    public static final Codec<AvoidStructuresStructurePlacement> CODEC = RecordCodecBuilder.<AvoidStructuresStructurePlacement>mapCodec((instance) -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(AvoidStructuresStructurePlacement::locateOffset),
            StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(AvoidStructuresStructurePlacement::frequencyReductionMethod),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(AvoidStructuresStructurePlacement::frequency),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(AvoidStructuresStructurePlacement::salt),
            StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(AvoidStructuresStructurePlacement::exclusionZone),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spacing").forGetter(AvoidStructuresStructurePlacement::spacing),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("separation").forGetter(AvoidStructuresStructurePlacement::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(AvoidStructuresStructurePlacement::spreadType),
            RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET).fieldOf("avoid_structure_sets").forGetter(AvoidStructuresStructurePlacement::avoidStructures),
            Codec.intRange(3, Integer.MAX_VALUE).fieldOf("min_distance_from_avoid_structures").forGetter(AvoidStructuresStructurePlacement::minDistanceFromAvoidStructures)
    ).apply(instance, instance.stable(AvoidStructuresStructurePlacement::new))).codec();

    private final HolderSet<StructureSet> avoidStructures;
    private final int minDistanceFromAvoidStructures;
    
    public AvoidStructuresStructurePlacement(Vec3i locationOffset,
                                             StructurePlacement.FrequencyReductionMethod frequencyReductionMethod,
                                             float frequency,
                                             int salt,
                                             Optional<ExclusionZone> exclusionZone,
                                             int spacing,
                                             int separation,
                                             RandomSpreadType spreadType,
                                             HolderSet<StructureSet> avoidStructures,
                                             int minDistanceFromAvoidStructures
                                             ) {
        super(locationOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
        this.avoidStructures = avoidStructures;
        this.minDistanceFromAvoidStructures = minDistanceFromAvoidStructures;

        // Helpful validation to ensure that spacing value is always greater than separation value C: TelepathicGrunt
        if (spacing <= separation) {
            throw new RuntimeException("""
                        Spacing cannot be less or equal to separation.
                        Please correct this error as there's no way to spawn this structure properly
                            Spacing: %s
                            Separation: %s.
                    """.formatted(spacing, separation));
        }
    }

    public HolderSet<StructureSet> avoidStructures() {
        return avoidStructures;
    }

    public int minDistanceFromAvoidStructures() {
        return minDistanceFromAvoidStructures;
    }

    /**
     * Reference taken from TelepathicGrunt DistanceBasedStructurePlacement class, uses config value instead of placement variable
     */

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int z) {

        // Simple fast distance check without needing to do a square root. The threshold is circular around world origin.
        if (((long) x * x) + ((long) z * z) < (((long) BattleTowersConfig.firstTowerDistance) * BattleTowersConfig.firstTowerDistance)) {
            // BABattleTowers.LOGGER.debug("Structure {} within first tower distance", chunkGeneratorStructureState.possibleStructureSets().get(0).unwrapKey().get().location().getPath());
            return false;
        }

        boolean hasStructure = false;
        for (Holder<StructureSet> set : avoidStructures) {
            hasStructure = chunkGeneratorStructureState.hasStructureChunkInRange(set, x, z, minDistanceFromAvoidStructures);
            // BABattleTowers.LOGGER.debug("Structure from set {} in range", set.unwrapKey().get().location().getPath());
        }

        ChunkPos chunkpos = this.getPotentialStructureChunk(chunkGeneratorStructureState.getLevelSeed(), x, z);
        return chunkpos.x == x && chunkpos.z == z && !hasStructure;
    }
}
