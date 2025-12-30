package com.brass_amber.ba_bt.worldGen;

import com.brass_amber.ba_bt.BattleTowersConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

import static com.brass_amber.ba_bt.init.BTStructurePlacements.TOWER_STRUCTURE_PLACEMENT;
import static com.brass_amber.ba_bt.util.BTUtil.chunkDistanceTo;

public class TowerStructurePlacement extends RandomSpreadStructurePlacement {

    public static final Codec<TowerStructurePlacement> CODEC = RecordCodecBuilder.<TowerStructurePlacement>mapCodec((instance) -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(TowerStructurePlacement::locateOffset),
            StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(TowerStructurePlacement::frequencyReductionMethod),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(TowerStructurePlacement::frequency),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(TowerStructurePlacement::salt),
            StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(TowerStructurePlacement::exclusionZone),
            Codec.intRange(9, Integer.MAX_VALUE).fieldOf("spacing").forGetter(TowerStructurePlacement::spacing),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("separation").forGetter(TowerStructurePlacement::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(TowerStructurePlacement::spreadType),
            RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET).fieldOf("avoid_structure_sets").forGetter(TowerStructurePlacement::avoidStructures),
            Codec.intRange(3, Integer.MAX_VALUE).fieldOf("min_distance_from_avoid_structures").forGetter(TowerStructurePlacement::minDistanceFromAvoidStructures)
    ).apply(instance, instance.stable(TowerStructurePlacement::new))).codec();

    private final HolderSet<StructureSet> avoidStructures;
    private final int minDistanceFromAvoidStructures;
    
    public TowerStructurePlacement(Vec3i locationOffset,
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
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int chunkX, int chunkZ) {

        // Simple fast distance check without needing to do a square root. The threshold is circular around world origin.
        if (((long) chunkX * chunkX) + ((long) chunkZ * chunkZ) < (((long) BattleTowersConfig.firstTowerDistance) * BattleTowersConfig.firstTowerDistance)) {
            // BABattleTowers.LOGGER.debug("Structure {} within first tower distance", chunkGeneratorStructureState.possibleStructureSets().get(0).unwrapKey().get().location().getPath());
            return false;
        }

        boolean hasStructure = false;

        for (Holder<StructureSet> set : avoidStructures) {
            hasStructure = chunkGeneratorStructureState.hasStructureChunkInRange(set, chunkX, chunkZ, minDistanceFromAvoidStructures);
            if (hasStructure) {
                // BABattleTowers.LOGGER.debug("Structure from set {} in range", set.unwrapKey().get().location().getPath());
                break;
            }
        }

        ChunkPos chunkpos = this.getPotentialStructureChunkUseLowest(chunkGeneratorStructureState.getLevelSeed(), chunkX, chunkZ);
        return chunkpos.x == chunkX && chunkpos.z == chunkZ && !hasStructure;
    }

    public ChunkPos getPotentialStructureChunkUseLowest(long seed, int chunkX, int chunkZ) {
        int i = Math.floorDiv(chunkX, this.spacing());
        int j = Math.floorDiv(chunkZ, this.spacing());
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureWithSalt(seed, i, j, this.salt());
        int k = this.spacing() - this.separation();
        // Allow placements between first tower distance and minimum seperation
        if (this.separation() > chunkDistanceTo(ChunkPos.ZERO , new ChunkPos(chunkX, chunkZ)) && this.separation() > BattleTowersConfig.firstTowerDistance) {
            k = this.spacing() - BattleTowersConfig.firstTowerDistance;
        }
        int l = this.spreadType().evaluate(worldgenrandom, k);
        int i1 = this.spreadType().evaluate(worldgenrandom, k);
        return new ChunkPos(i * this.spacing() + l, j * this.spacing() + i1);
    }

    public StructurePlacementType<?> type() {
        return TOWER_STRUCTURE_PLACEMENT.get();
    }
}
