package com.brass_amber.ba_bt.datagen.datapack;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.registries.GenerationData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.Optional;

import static com.brass_amber.ba_bt.datagen.datapack.ProcessorListBootstrap.*;
import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerProcessors.*;
import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerProcessors.OCEAN_NORMAL_FLOOR;

public class GenerationDataBootstrap {

    public static final ResourceKey<GenerationData> LAND_GENERATION_DATA = registerGenerationDataKey("land");
    public static final ResourceKey<GenerationData> OCEAN_GENERATION_DATA = registerGenerationDataKey("ocean");
    public static final ResourceKey<GenerationData> CORE_GENERATION_DATA = registerGenerationDataKey("core");
    public static final ResourceKey<GenerationData> NETHER_GENERATION_DATA = registerGenerationDataKey("nether");
    public static final ResourceKey<GenerationData> END_GENERATION_DATA = registerGenerationDataKey("end");
    public static final ResourceKey<GenerationData> SKY_GENERATION_DATA = registerGenerationDataKey("sky");

    public static void generationBootstrap(BootstapContext<GenerationData> generationDataContext) {
        HolderGetter<StructureProcessorList> structureProcessorListHolderGetter = generationDataContext.lookup(Registries.PROCESSOR_LIST);

        generationDataContext.register(
                LAND_GENERATION_DATA,
                new GenerationData(
                        "land", new Vec3i(0, 91, 0), new Vec3i(0, 1, 0),
                        List.of("normal", "overgrown", "sandy", "icy", "ruined", "classic"),
                        List.of(
                                new GenerationData.VariantPieces(
                                        List.of(new GenerationData.WeightedPiece("shell", 1,  Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_SHELL_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("base", 1, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_WALL_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("main_hall", 1, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_FLOOR_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(
                                                new GenerationData.WeightedPiece("barracks_abandoned", 0.05f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("barracks_open", 0.18f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("barracks", 0.18f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("kitchen", 0.34f, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_CROP_PLACEMENT)), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("library", 0.25f, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_CARPET_FIXER)), Vec3i.ZERO)
                                        ),
                                        List.of(new GenerationData.WeightedPiece("cult_floor", 1, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_CARPET_FIXER)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("boss_floor", 1, Optional.empty(), Vec3i.ZERO))
                                ),
                                new GenerationData.VariantPieces(
                                        List.of(
                                                new GenerationData.WeightedPiece("lush", 0.5f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("farm", 0.3f, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_CROP_PLACEMENT)), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("integrated", 0.2f, Optional.empty(), Vec3i.ZERO)
                                        ),
                                        List.of(new GenerationData.WeightedPiece("base", 1, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_WALL_DEGRADATION)), new Vec3i(-3, 0, -3))),
                                        List.of(new GenerationData.WeightedPiece("main_hall", 1, Optional.of(structureProcessorListHolderGetter.getOrThrow(LAND_FLOOR_DEGRADATION)), new Vec3i(-3, 0, 0))),
                                        List.of(),
                                        List.of(),
                                        List.of(new GenerationData.WeightedPiece("giant_tree", 1, Optional.empty(), new Vec3i(0, -6, 0)))
                                ),
                                new GenerationData.VariantPieces(
                                        List.of(
                                                new GenerationData.WeightedPiece("ruined_shell", 0.4f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("windswept_corner", 0.3f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("heavy_sand", 0.2f, Optional.of(structureProcessorListHolderGetter.getOrThrow(SAND_DEGRADATION)), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("cactus_infested", 0.1f, Optional.empty(), Vec3i.ZERO)
                                        ),
                                        List.of(new GenerationData.WeightedPiece("base", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(SANDSTONE_REPLACER)), new Vec3i(-2, 0, -2))),
                                        List.of(new GenerationData.WeightedPiece("main_hall", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(SAND_DEGRADATION)), new Vec3i(-2, 0, -2))),
                                        List.of(),
                                        List.of(),
                                        List.of(new GenerationData.WeightedPiece("boss_floor", 1f, Optional.empty(), new Vec3i(0, -3, 0)))
                                )
                        )
                )
        );

        generationDataContext.register(
                OCEAN_GENERATION_DATA,
                new GenerationData(
                        "ocean", new Vec3i(0, -85, 0), new Vec3i(0, 3, 0), 
                        List.of("normal", "gilded", "island"),
                        List.of(
                                new GenerationData.VariantPieces(
                                        List.of(new GenerationData.WeightedPiece("shell", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(OCEAN_SHELL_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("obelisk_platform", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(OCEAN_WALL_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("main_hall", 1f, Optional.empty(), Vec3i.ZERO)),
                                        List.of(
                                                new GenerationData.WeightedPiece("garden", 0.33f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("guardian_nest", 0.33f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("shark_pens", 0.34f, Optional.empty(), Vec3i.ZERO)
                                        ),
                                        List.of(new GenerationData.WeightedPiece("open_shrine", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(OCEAN_FLOOR_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("boss_floor", 1f, Optional.empty(), Vec3i.ZERO))
                                )
                        )
                )
        );

        generationDataContext.register(
                CORE_GENERATION_DATA,
                new GenerationData(
                        "core", new Vec3i(0, 91, 0), new Vec3i(0, 1, 0),
                        List.of("normal", "city", "colossal"),
                        List.of(
                                new GenerationData.VariantPieces(
                                        List.of(new GenerationData.WeightedPiece("shell", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(OCEAN_SHELL_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("base", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(OCEAN_WALL_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("core_hall", 1f, Optional.empty(), Vec3i.ZERO)),
                                        List.of(
                                                new GenerationData.WeightedPiece("blacksmith", 0.10f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("orb_hall", 0.25f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("worship", 0.40f, Optional.empty(), Vec3i.ZERO),
                                                new GenerationData.WeightedPiece("chunky_veins", 0.25f, Optional.empty(), Vec3i.ZERO)
                                        ),
                                        List.of(new GenerationData.WeightedPiece("core_library", 1f, Optional.of(structureProcessorListHolderGetter.getOrThrow(OCEAN_FLOOR_DEGRADATION)), Vec3i.ZERO)),
                                        List.of(new GenerationData.WeightedPiece("boss_floor", 1f, Optional.empty(), new Vec3i(-1, 0, -1)))
                                )
                        )
                )
        );

        generationDataContext.register(
                NETHER_GENERATION_DATA,
                new GenerationData(
                        "nether", new Vec3i(0, 0, 0), new Vec3i(0, 0, 0),
                        List.of("normal", "crimson", "blue", "anomaly"),
                        List.of()
                )
        );

        generationDataContext.register(
                END_GENERATION_DATA,
                new GenerationData(
                        "end", new Vec3i(0, 0, 0), new Vec3i(0, 0, 0),
                        List.of("normal", "disturbance", "city"),
                        List.of()
                )
        );

        generationDataContext.register(
                SKY_GENERATION_DATA,
                new GenerationData(
                        "sky", new Vec3i(0, 0, 0), new Vec3i(0, 0, 0),
                        List.of("normal", "village", "hanging_gardens"),
                        List.of()
                )
        );

    }

    private static ResourceKey<GenerationData> registerGenerationDataKey(String name) {
        return ResourceKey.create(BTRegistries.Keys.GENERATION_DATA, BABattleTowers.locate(name));
    }
}
