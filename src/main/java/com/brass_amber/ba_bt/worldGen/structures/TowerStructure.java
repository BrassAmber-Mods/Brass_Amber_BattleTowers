package com.brass_amber.ba_bt.worldGen.structures;


import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.util.BTStatics;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerPieces;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.BABattleTowers.SAVE_TOWERS;
import static com.brass_amber.ba_bt.util.BTUtil.chunkDistanceTo;


public interface TowerStructure {
    String getTowerName();
    int getTowerType();
    int getTowerId();
    String[] getTowerTypeConversion();


    default boolean hasNearbyTower(ChunkPos towerPos) {
        int towerId = getTowerId();
        int minimumSeparation = 12;

        if (!SaveTowers.towers.get(towerId).isEmpty()) {
            for (Pair<ChunkPos, Rotation> towerPosRotation : SaveTowers.towers.get(towerId)) {
                var distance = chunkDistanceTo(towerPos, towerPosRotation.getFirst());
                if (distance < minimumSeparation && distance > 0) {
                    return true;
                }
                // BABTMain.LOGGER.debug("Tower distance from generation try:" + closestDistance);
            }
        }
        return false;
    }

    default void generatePieces(StructurePiecesBuilder piecesBuilder, Structure.GenerationContext generationContext, BlockPos blockPos) {
        List<TowerPieces.TowerPiece> list = Lists.newLinkedList();
        String variant;
        try {
            variant = getTowerTypeConversion()[getTowerType()];
        } catch (IndexOutOfBoundsException e) {
            variant = "normal";
        }
        TowerPieces.generateTower(generationContext.structureTemplateManager(), blockPos, list, generationContext.random(), getTowerName(), variant);
        list.forEach(piecesBuilder::addPiece);
        BABattleTowers.LOGGER.debug("Pieces : {}", list.stream().map(TowerPieces.TowerPiece::makeTemplateLocation).toList());
    }

    default void afterPlaceBT(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {

        // After Place is called for every chunk that the structure occupies.
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int bbYStart = boundingbox.minY();
        int minBuildHeight = worldGenLevel.getMinBuildHeight();
        BoundingBox boundingBox = piecesContainer.calculateBoundingBox();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        List<Block> towerBlocks = BTStatics.towerBlocks.get(getTowerId());
        BlockState baseBlock = BTStatics.towerBaseBlocks.get(getTowerId());
        ArrayList<BlockPos> startPositions = new ArrayList<>();

        for (int x = chunkBoundingBox.minX(); x <= chunkBoundingBox.maxX(); ++x) {
            for (int z = chunkBoundingBox.minZ(); z <= chunkBoundingBox.maxZ(); ++z) {
                blockpos$mutableblockpos.set(x, bbYStart, z);
                BlockState block = worldGenLevel.getBlockState(blockpos$mutableblockpos);
                if (!worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) && boundingbox.isInside(blockpos$mutableblockpos) && (towerBlocks.contains(block.getBlock()) || block.is(BlockTags.DIRT))) {
                    for (int i1 = bbYStart - 1; i1 > minBuildHeight; --i1) {
                        blockpos$mutableblockpos.setY(i1);
                        if (!worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) && !worldGenLevel.getBlockState(blockpos$mutableblockpos).getFluidState().isEmpty()) {
                            break;
                        }
                        if (block.is(Blocks.SAND) || block.is(Blocks.SANDSTONE)) {
                            worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.SAND.defaultBlockState(), 2);
                            break;
                        }
                        else {
                            worldGenLevel.setBlock(blockpos$mutableblockpos, baseBlock, 2);
                        }
                    }
                }
            }
        }
    }

    void checkVariant(Structure.GenerationContext context, BlockPos blockpos);

    Pair<Boolean, Integer> isSpawnableChunk(Structure.GenerationContext generationContext);

    // Used for tower saving and logging of tower positions
    // Rotation is saved for rotation of loaded datamarker block containers after generation
    default void saveTower(BlockPos spawnPos) {
        BABattleTowers.LOGGER.debug("{} Tower at {} {}", getTowerName(), spawnPos, new ChunkPos(spawnPos));
        SAVE_TOWERS.addTower(new ChunkPos(spawnPos), Rotation.NONE, getTowerId());
    }


}


