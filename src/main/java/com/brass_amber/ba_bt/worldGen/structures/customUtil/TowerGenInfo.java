package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerProcessors.*;

public enum TowerGenInfo {
    EMPTY(
            new String[]{}, // Variant Names
            List.of() // All variants (Normal is first variant)
    ),
    LAND(
            new String[]{"normal", "overgrown", "sandy", "icy", "ruined"},
            List.of(
                    new VariantPieces(
                            List.of(new WeightedPiece("shell", 1, List.of(LAND_WALL, LAND_NORMAL_STAIRS, LAND_NORMAL_FLOOR), Vec3i.ZERO)),
                            List.of(new WeightedPiece("base", 1, List.of(LAND_WALL), Vec3i.ZERO)),
                            List.of(new WeightedPiece("main_hall", 1, List.of(LAND_NORMAL_FLOOR), Vec3i.ZERO)),
                            List.of(
                                    new WeightedPiece("barracks_abandoned", 0.05f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("barracks_open", 0.18f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("barracks", 0.18f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("kitchen", 0.34f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("library", 0.25f, List.of(LAND_CARPET_PLACER), Vec3i.ZERO)
                            ),
                            List.of(new WeightedPiece("cult_floor", 1, List.of(LAND_CARPET_PLACER), Vec3i.ZERO)),
                            List.of(new WeightedPiece("boss_floor", 1, List.of(), Vec3i.ZERO))
                    ),
                    new VariantPieces(
                            List.of(
                                    new WeightedPiece("lush", 0.5f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("farm", 0.3f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("integrated", 0.2f, List.of(), Vec3i.ZERO)
                            ),
                            List.of(new WeightedPiece("base", 1, List.of(LAND_WALL), new Vec3i(-3, 0, -3))),
                            List.of(new WeightedPiece("main_hall", 1, List.of(LAND_NORMAL_FLOOR), new Vec3i(-3, 0, 0))),
                            List.of(),
                            List.of(),
                            List.of(new WeightedPiece("giant_tree", 1, List.of(), new Vec3i(0, -6, 0)))
                    ),
                    new VariantPieces(
                            List.of(
                                    new WeightedPiece("ruined_shell", 0.4f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("windswept_corner", 0.3f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("heavy_sand", 0.2f, List.of(SAND_REMOVE_7), Vec3i.ZERO),
                                    new WeightedPiece("cactus_infested", 0.1f, List.of(), Vec3i.ZERO)
                            ),
                            List.of(new WeightedPiece("base", 1, List.of(SANDSTONE, SAND_REMOVE_7), new Vec3i(-2, 0, -2))),
                            List.of(new WeightedPiece("main_hall", 1, List.of(SAND_REMOVE_7), new Vec3i(-2, 0, -2))),
                            List.of(),
                            List.of(),
                            List.of(new WeightedPiece("boss_floor", 1, List.of(), new Vec3i(0, -3, 0)))
                    )
            )
    ),
    OCEAN(
            new String[]{"normal", "gilded", "island"},
            List.of(
                    new VariantPieces(
                            List.of(new WeightedPiece("shell", 1, List.of(OCEAN_NORMAL, NORMAL_STAIRS_OCEAN, OCEAN_NORMAL_FLOOR, WATERLOGGED), Vec3i.ZERO)),
                            List.of(new WeightedPiece("obelisk_platform", 1, List.of(OCEAN_NORMAL), Vec3i.ZERO)),
                            List.of(new WeightedPiece("main_hall", 1, List.of(), Vec3i.ZERO)),
                            List.of(
                                    new WeightedPiece("garden", 0.33f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("guardian_nest", 0.33f, List.of(), Vec3i.ZERO),
                                    new WeightedPiece("shark_pens", 0.34f, List.of(), Vec3i.ZERO)
                            ),
                            List.of(new WeightedPiece("open_shrine", 1, List.of(OCEAN_NORMAL_FLOOR), Vec3i.ZERO)),
                            List.of(new WeightedPiece("boss_floor", 1, List.of(), Vec3i.ZERO))
                    )
            )
    ),
    CORE(
            new String[]{"normal", "city", "colossal"},
            List.of(new VariantPieces(
                    List.of(new WeightedPiece("shell", 1, List.of(CORE_WALL, CORE_STAIRS, CORE_FLOOR), Vec3i.ZERO)),
                    List.of(new WeightedPiece("base", 1, List.of(LAND_WALL), Vec3i.ZERO)),
                    List.of(new WeightedPiece("blacksmith", 1, List.of(CORE_FLOOR, CORE_ROOF, CORE_ORE), Vec3i.ZERO)),
                    List.of(
                            new WeightedPiece("barracks_open", 0.18f, List.of(), Vec3i.ZERO),
                            new WeightedPiece("barracks", 0.17f, List.of(), Vec3i.ZERO),
                            new WeightedPiece("kitchen", 0.40f, List.of(), Vec3i.ZERO),
                            new WeightedPiece("library", 0.25f, List.of(LAND_CARPET_PLACER), Vec3i.ZERO)
                    ),
                    List.of(new WeightedPiece("cult_floor", 1, List.of(LAND_CARPET_PLACER), Vec3i.ZERO)),
                    List.of(new WeightedPiece("boss_floor", 1, List.of(CORE_FLOOR, CORE_ROOF), new Vec3i(-1, 0, -1)))
            ))
    ),
    NETHER(
            new String[]{"normal", "crimson", "blue", "anomaly"}, // Variant Names
            List.of()
    ),
    END(
            new String[]{"normal", "disturbance", "city"}, // Variant Names
            List.of()
    ),
    SKY(
            new String[]{"normal", "village", "hanging_gardens"}, // Variant Names
            List.of()
    );

    private final String[] variants;
    private final Map<String, VariantPieces> variantPieces;

    TowerGenInfo(
            String[] variants,
            List<VariantPieces>variantPiecesList
    ) {
        this.variants = variants;
        this.variantPieces = IntStream.range(0, variants.length).boxed().collect(
                Collectors.toMap(i -> variants[i], i -> i < variantPiecesList.size() ? variantPiecesList.get(i) : EMPTY_VARIANT)
        );
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

    public static TowerGenInfo getTypeForName(String name) {
        return switch (name) {
            case "land_tower" -> TowerGenInfo.LAND;
            case "ocean_tower" -> TowerGenInfo.OCEAN;
            case "core_tower" -> TowerGenInfo.CORE;
            case "nether_tower" -> TowerGenInfo.NETHER;
            case "end_tower" -> TowerGenInfo.END;
            case "sky_tower" -> TowerGenInfo.SKY;
            default -> EMPTY;
        };
    }

    public static int getFloorHeight(TowerGenInfo towerGenInfo) {
        return switch (towerGenInfo) {
            case OCEAN -> -12;
            case NETHER -> 10;
            case SKY -> 12;
            default -> 11;
        };
    }

    public String[] getVariants() {
        return variants;
    }

    public record WeightedPiece(String name, float chance, List<StructureProcessor> structureProcessors, Vec3i offset) {

    }

    public record VariantPieces(List<WeightedPiece> shellPieces, List<WeightedPiece> startPieces,
                                List<WeightedPiece> firstFloorPieces, List<WeightedPiece> middleFloorPieces,
                                List<WeightedPiece> finalFloorPieces, List<WeightedPiece> endPieces) {

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

    private final VariantPieces EMPTY_VARIANT = new VariantPieces(List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
}
