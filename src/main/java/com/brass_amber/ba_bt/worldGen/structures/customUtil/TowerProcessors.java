package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;
import java.util.Random;

import static com.brass_amber.ba_bt.util.BTTags.Blocks.BASE_PROTECTED_TAG;

public class TowerProcessors {

    static final StructureProcessor BASE_PROTECTED;

    static final StructureProcessor LAND_WALL;
    static final StructureProcessor LAND_NORMAL_FLOOR;
    static final StructureProcessor LAND_NORMAL_STAIRS;
    static final StructureProcessor LAND_CARPET_PLACER;
    static final StructureProcessor LAND_CROP_PLACER;

    static final StructureProcessor SAND_REMOVE_7;
    static final StructureProcessor SANDSTONE;

    static final StructureProcessor OCEAN_NORMAL;
    static final StructureProcessor OCEAN_NORMAL_FLOOR;
    static final StructureProcessor NORMAL_STAIRS_OCEAN;

    static final StructureProcessor BONE_REMOVE;
    static final StructureProcessor WATERLOGGED;

    static final StructureProcessor CORE_WALL;
    static final StructureProcessor CORE_FLOOR;
    static final StructureProcessor CORE_STAIRS;
    static final StructureProcessor CORE_ROOF;
    static final StructureProcessor CORE_ORE;


    static final StructureProcessor SKY_BASE_SNOW;
    static final StructureProcessor SKY_BASE_GOLD;

    static {

        Random random = new Random();

        BASE_PROTECTED = new ProtectedBlockProcessor(BASE_PROTECTED_TAG);

        LAND_WALL = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.33F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.22F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.CRACKED_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.08F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.CHISELED_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.COBBLESTONE_STAIRS, 0.22F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PRISMARINE_SLAB, 0.65F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PRISMARINE_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.COBBLESTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PRISMARINE_BRICK_SLAB, 0.65F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PRISMARINE_BRICK_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.COBBLESTONE_SLAB.defaultBlockState()
                )

        ));

        LAND_NORMAL_STAIRS = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_SLAB, 0.4F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.COBBLESTONE_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_SLAB, 0.6F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_SLAB, 0.6F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.STONE_BRICK_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPUR_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICK_SLAB, 0.4F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.COBBLESTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICK_SLAB, 0.6F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICK_SLAB, 0.6F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.END_STONE_BRICK_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.3F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.COBBLESTONE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.3F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.5F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.5F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.7F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.CRACKED_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPUR_BLOCK),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.POLISHED_GRANITE.defaultBlockState()
                )
        ));

        LAND_NORMAL_FLOOR = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICKS, 0.33F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MOSSY_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICKS, 0.22F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.CRACKED_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICKS, 0.08F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.CHISELED_STONE_BRICKS.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICKS, 0.2F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.SMOOTH_STONE.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.END_STONE_BRICKS),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.STONE_BRICKS.defaultBlockState()
                )
        ));


        LAND_CARPET_PLACER = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.RED_WOOL),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.RED_CARPET.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPLE_WOOL),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PURPLE_CARPET.defaultBlockState()
                )

        ));

        LAND_CROP_PLACER = new CropPlaceProcessor(
                List.of(
                        Blocks.CARROTS.defaultBlockState(),
                        Blocks.WHEAT.defaultBlockState(),
                        Blocks.POTATOES.defaultBlockState(),
                        Blocks.BEETROOTS.defaultBlockState()
                )
        );

        SANDSTONE = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.STONE_BRICKS),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.SANDSTONE.defaultBlockState()
                )
        ));

        SAND_REMOVE_7 = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.SAND, 0.07F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.AIR.defaultBlockState()
                )
        ));

        OCEAN_NORMAL = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.DARK_PRISMARINE, 0.29F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE.defaultBlockState()
                )

        ));

        NORMAL_STAIRS_OCEAN = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_SLAB, 0.2F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_SLAB, 0.3F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE_BRICK_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPUR_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DARK_PRISMARINE_SLAB.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICK_SLAB, 0.2F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICK_SLAB, 0.3F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.END_STONE_BRICK_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DARK_PRISMARINE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICKS, 0.2F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DARK_PRISMARINE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.END_STONE_BRICKS, 0.1F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.END_STONE_BRICKS),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE_BRICKS.defaultBlockState()
                )
        ));

        OCEAN_NORMAL_FLOOR = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.2F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.05F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.MAGMA_BLOCK.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.05F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.SOUL_SAND.defaultBlockState()
                ),

                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.PURPUR_BLOCK, 0.15F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.WARPED_STEM.defaultBlockState().setValue(RotatedPillarBlock.AXIS, random.nextFloat() > .5 ? Direction.Axis.Z : Direction.Axis.X )
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPUR_BLOCK),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.PRISMARINE_BRICKS.defaultBlockState()
                )
        ));

        BONE_REMOVE = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.BONE_BLOCK),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.WATER.defaultBlockState()
                )
        ));

        WATERLOGGED = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.IRON_BARS),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.IRON_BARS.defaultBlockState().setValue(CrossCollisionBlock.WATERLOGGED, true)
                )
        ));

        CORE_WALL = new RuleProcessor(ImmutableList.of(

        ));

        CORE_STAIRS = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPUR_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        BTBlocks.CORRITE_SLAB.get().defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.END_STONE_BRICK_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        BTBlocks.CORRITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PURPUR_BLOCK),
                        AlwaysTrueTest.INSTANCE,
                        BTBlocks.CORRITE_BLOCK.get().defaultBlockState()
                )
        ));

        CORE_FLOOR = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.END_STONE_BRICKS),
                        AlwaysTrueTest.INSTANCE,
                        BTBlocks.CORRITE_BLOCK.get().defaultBlockState()
                )
        ));

        CORE_ROOF = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PRISMARINE_BRICK_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        BTBlocks.CORRITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.PRISMARINE_SLAB),
                        AlwaysTrueTest.INSTANCE,
                        BTBlocks.CORRITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM)
                )

        ));


        CORE_ORE = new RuleProcessor(ImmutableList.of(
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.CALCITE, 0.04F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.CALCITE, 0.17F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.CALCITE, 0.21F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.CALCITE, 0.3F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.CALCITE, 0.6F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE_IRON_ORE.defaultBlockState()
                ),
                new ProcessorRule(
                        new RandomBlockMatchTest(Blocks.CALCITE, 0.8F),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE_COAL_ORE.defaultBlockState()
                ),
                new ProcessorRule(
                        new BlockMatchTest(Blocks.CALCITE),
                        AlwaysTrueTest.INSTANCE,
                        Blocks.DEEPSLATE.defaultBlockState()
                )
        ));

        SKY_BASE_SNOW = new NearbyBlockMatchProcessor(
                new BlockMatchTest(BTBlocks.CLOUD.get()),
                List.of(
                        new NearbyBlockMatchProcessor.BlockMatchRule(
                                List.of(BTBlocks.CLOUD.get().defaultBlockState()),
                                List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN),
                                3,
                                6,
                                1
                        )
                ),
                Blocks.SNOW_BLOCK.defaultBlockState(),
                1f
        );

        SKY_BASE_GOLD = new NearbyBlockMatchProcessor(
                new BlockMatchTest(BTBlocks.CLOUD.get()),
                List.of(
                        new NearbyBlockMatchProcessor.BlockMatchRule(
                                List.of(BTBlocks.CLOUD.get().defaultBlockState(), Blocks.SNOW_BLOCK.defaultBlockState()),
                                List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN),
                                6,
                                6,
                                1
                        ),
                        new NearbyBlockMatchProcessor.BlockMatchRule(
                                List.of(Blocks.AIR.defaultBlockState()),
                                List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN),
                                2,
                                6,
                                2
                        )
                ),
                Blocks.GOLD_BLOCK.defaultBlockState(),
                .5f
        );
    }
}
