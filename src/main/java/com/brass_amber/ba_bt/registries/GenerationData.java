package com.brass_amber.ba_bt.registries;

import com.brass_amber.ba_bt.datagen.datapack.ProcessorListBootstrap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerationData {
    public static final Codec<GenerationData> CODEC = RecordCodecBuilder.create(towerGenInfoInstance ->
            towerGenInfoInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(generationData -> generationData.name),
                    Vec3i.CODEC.fieldOf("monolith_offset").forGetter(generationData -> generationData.monolithOffset),
                    Vec3i.CODEC.fieldOf("obelisk_offset").forGetter(generationData -> generationData.obeliskOffset),
                    Codec.STRING.listOf().fieldOf("variants").forGetter(generationData -> generationData.variants),
                    VariantPieces.CODEC.listOf().fieldOf("variant_pieces").forGetter(generationData -> generationData.variantPieces.values().stream().toList())
            ).apply(towerGenInfoInstance, GenerationData::new)
    );

    private final VariantPieces EMPTY_VARIANT = new VariantPieces(List.of(), List.of(), List.of(), List.of(), List.of(), List.of());

    private final String name;
    private final Vec3i monolithOffset;
    private final Vec3i obeliskOffset;
    private final List<String> variants;
    private final Map<String, VariantPieces> variantPieces;


    public GenerationData(String name, Vec3i monolithOffset, Vec3i obeliskOffset, List<String> variants,
                          List<VariantPieces> variantPiecesList) {
        this.variants = variants;
        this.variantPieces = IntStream.range(0, variants.size()).boxed().collect(
                Collectors.toMap(variants::get, i -> i < variantPiecesList.size() ? variantPiecesList.get(i) : EMPTY_VARIANT)
        );
        this.name = name;
        this.monolithOffset = monolithOffset;
        this.obeliskOffset = obeliskOffset;
    }

    public Pair<WeightedPiece, String> getRandomVariantPieceFrom(PieceListType listType, String variant, RandomSource randomSource) {
        float chanceGate = 0;
        float chance = randomSource.nextFloat();
        for (WeightedPiece piece : this.variantPieces.get(variant).getListForType(listType)) {
            chanceGate += piece.chance();
            if (chance <= chanceGate) {
                return Pair.of(piece, variant);
            }
        }
        chanceGate = 0;
        for (WeightedPiece piece : this.variantPieces.get("normal").getListForType(listType)) {
            chanceGate += piece.chance();
            if (chance <= chanceGate) {
                return Pair.of(piece, "normal");
            }
        }

        return null;
    }

    public Map<String, VariantPieces> getVariantPieces() {
        return variantPieces;
    }

    public Pair<WeightedPiece, String> getSpecificVariantPiece(PieceListType listType, String variant, int pieceId) {
        List<WeightedPiece> pieces = this.variantPieces.get(variant).getListForType(listType);
        return Pair.of(pieces.get(Math.min(pieceId, pieces.size())), variant);
    }

    public List<String> getVariants() {
        return variants;
    }

    public record WeightedPiece(String name, float chance, Optional<Holder<StructureProcessorList>> structureProcessors, Vec3i offset) {
        public static final Codec<WeightedPiece> CODEC = RecordCodecBuilder.create(weightedPieceInstance -> 
                weightedPieceInstance.group(
                        Codec.STRING.fieldOf("name").forGetter(weightedPiece -> weightedPiece.name),
                        Codec.FLOAT.fieldOf("chance").forGetter(weightedPiece -> weightedPiece.chance),
                        StructureProcessorType.LIST_CODEC.optionalFieldOf("structure_processors").forGetter(weightedPiece -> weightedPiece.structureProcessors),
                        Vec3i.CODEC.fieldOf("offset").forGetter(weightedPiece -> weightedPiece.offset)
                ).apply(weightedPieceInstance, WeightedPiece::new)
        );
    }

    public record VariantPieces(List<WeightedPiece> shellPieces,
                                List<WeightedPiece> startPieces,
                                List<WeightedPiece> firstFloorPieces,
                                List<WeightedPiece> middleFloorPieces,
                                List<WeightedPiece> finalFloorPieces,
                                List<WeightedPiece> endPieces) {

        public static final Codec<VariantPieces> CODEC = RecordCodecBuilder.create(variantPiecesInstance ->
                variantPiecesInstance.group(
                        WeightedPiece.CODEC.listOf().fieldOf("shell_pieces").forGetter(variantPieces -> variantPieces.shellPieces),
                        WeightedPiece.CODEC.listOf().fieldOf("start_pieces").forGetter(variantPieces -> variantPieces.startPieces),
                        WeightedPiece.CODEC.listOf().fieldOf("first_floor_pieces").forGetter(variantPieces -> variantPieces.firstFloorPieces),
                        WeightedPiece.CODEC.listOf().fieldOf("middle_floor_pieces").forGetter(variantPieces -> variantPieces.middleFloorPieces),
                        WeightedPiece.CODEC.listOf().fieldOf("final_floor_pieces").forGetter(variantPieces -> variantPieces.finalFloorPieces),
                        WeightedPiece.CODEC.listOf().fieldOf("end_pieces").forGetter(variantPieces -> variantPieces.endPieces)
                ).apply(variantPiecesInstance, VariantPieces::new)
        );

        public List<WeightedPiece> getListForType(PieceListType type) {
            return switch (type) {
                case SHELL -> shellPieces();
                case START -> startPieces();
                case FIRST_FLOOR -> firstFloorPieces();
                case MIDDLE_FLOOR -> middleFloorPieces();
                case FINAL_FLOOR -> finalFloorPieces();
                case END -> endPieces();
            };
        }
    }

    public enum PieceListType {
        SHELL,
        START,
        FIRST_FLOOR,
        MIDDLE_FLOOR,
        FINAL_FLOOR,
        END
    }


}
