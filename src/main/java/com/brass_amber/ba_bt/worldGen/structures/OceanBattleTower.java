package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.util.BTStatics;
import com.brass_amber.ba_bt.util.GolemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.QuartPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


// Comments from TelepathicGrunts

public class OceanBattleTower extends BattleTowerStructure {

    public OceanBattleTower(Structure.StructureSettings config,
                            Holder<StructureTemplatePool> startPool,
                            int size, HolderSet<Structure> avoidStructures,
                            HolderSet<Biome> biomesTerra, HolderSet<Biome> biomesBOP, HolderSet<Biome> biomesBYG) {
        super(config, startPool, size, avoidStructures, biomesTerra, biomesBOP, biomesBYG);
        this.towerName = "Ocean Tower";
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    public BlockPos isSpawnableChunk(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator chunkGen = context.chunkGenerator();

        int seaLevel = chunkGen.getSeaLevel();
        Predicate<Holder<Biome>> predicate = context.validBiome();

        // Test/Check 3 by 3 square of chunks for possible spawns
        List<ChunkPos> testable = new ArrayList<>(
                List.of(
                        new ChunkPos(chunkPos.x + 4, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x + 3, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z + 4),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z + 4),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x - 4, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x - 4, chunkPos.z - 2),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z - 4),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z - 4),
                        new ChunkPos(chunkPos.x + 3, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x + 4, chunkPos.z - 2),

                        new ChunkPos(chunkPos.x + 3, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z - 2),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z - 2),
                        new ChunkPos(chunkPos.x + 3, chunkPos.z - 1)
                )
        );

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel), QuartPos.fromBlock(pos.getMiddleBlockZ()), context.randomState().sampler());

            if (!predicate.test(biome)) {
                // BrassAmberBattleTowers.LOGGER.info("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
                return BlockPos.ZERO;
            }
        }
        return chunkPos.getMiddleBlockPosition(seaLevel - 12);
    }

    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int bbYStart = boundingbox.minY();
        List<Block> towerBlocks = BTStatics.towerBlocks.get(GolemType.getNumForType(GolemType.OCEAN));

        BlockPos chunckCenter = chunkPos.getMiddleBlockPosition(bbYStart);

        // BrassAmberBattleTowers.LOGGER.info("Post Processing: In chunk: " + chunkPos + " " + chunckCenter);

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        blockpos$mutableblockpos.setY(bbYStart);
        // get start and end postions for x/z, using min/max to account for the MinBlock being -25 and the MaxBlock being -27
        int startX = chunckCenter.getX() - 8;
        int endX = chunckCenter.getX() + 8;
        // BrassAmberBattleTowers.LOGGER.info("X start: " + startX + " end: " + endX);

        int startZ = chunckCenter.getZ() - 8;
        int endZ = chunckCenter.getZ() + 8;
        // BrassAmberBattleTowers.LOGGER.info("X start: " + startZ + " end: " + endZ);

        ArrayList<BlockPos> startPositions = new ArrayList<>();

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                blockpos$mutableblockpos.set(x, bbYStart, z);
                // BrassAmberBattleTowers.LOGGER.info("Block at: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (towerBlocks.contains(worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock())) {
                    // BrassAmberBattleTowers.LOGGER.info("Block is acceptable: " + blockpos$mutableblockpos + " "+ worldGenLevel.getBlockState(blockpos$mutableblockpos));
                    startPositions.add(new BlockPos(x, bbYStart - 1, z));
                }
            }
        }

        for (BlockPos startPos: startPositions) {
            for (int y = startPos.getY(); y > worldGenLevel.getMinBuildHeight() ; y--) {
                blockpos$mutableblockpos.set(startPos.getX(), y, startPos.getZ());
                // BrassAmberBattleTowers.LOGGER.info("Block to check: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) || worldGenLevel.isWaterAt(blockpos$mutableblockpos)
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof SeagrassBlock
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof TallSeagrassBlock) {
                    worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.PRISMARINE_BRICKS.defaultBlockState(), 2);
                } else {
                    // Add two blocks into this ground level as well.
                    worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.PRISMARINE_BRICKS.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos$mutableblockpos.below(), Blocks.PRISMARINE_BRICKS.defaultBlockState(), 2);
                    break;
                }
            }
        }
    }
}
