package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.BTStatics;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.worldGen.BTOceanJigsawPlacement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

import static com.BrassAmber.ba_bt.util.BTUtil.chunkDistanceTo;
import static com.BrassAmber.ba_bt.util.BTUtil.median;


// Comments from TelepathicGrunts

public class OceanBattleTower extends StructureFeature<JigsawConfiguration> {

    private static ChunkPos beforeLastPosition;
    private static ChunkPos lastPosition;

    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
            Codec.intRange(0, 40).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
    ).apply(codec, JigsawConfiguration::new));

    public OceanBattleTower() {
        super(CODEC, OceanBattleTower::createPiecesGenerator, OceanBattleTower::afterPlace);
        lastPosition = ChunkPos.ZERO;
        beforeLastPosition = ChunkPos.ZERO;
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    public static BlockPos isSpawnableChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context,
                                            ChunkPos chunkPos, ChunkGenerator chunkGen, WorldgenRandom worldgenRandom) {

        int seaLevel = chunkGen.getSeaLevel();
        int oceanFloorHeight = chunkGen.getFirstOccupiedHeight(chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(), Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor());
        Predicate<Holder<Biome>> predicate = context.validBiome();

        if (seaLevel - 8 < oceanFloorHeight) {
            BrassAmberBattleTowers.LOGGER.info("Ocean Floor too high: " + oceanFloorHeight);
            return BlockPos.ZERO;
        }

        List<ResourceKey<StructureSet>> vanillaStructures = new ArrayList<>();
        vanillaStructures.add(BuiltinStructureSets.OCEAN_RUINS);
        vanillaStructures.add(BuiltinStructureSets.OCEAN_MONUMENTS);
        vanillaStructures.add(BuiltinStructureSets.RUINED_PORTALS);
        vanillaStructures.add(BuiltinStructureSets.SHIPWRECKS);

        for (ResourceKey<StructureSet> set : vanillaStructures) {
            // BrassAmberBattleTowers.LOGGER.info(context.chunkGenerator().hasFeatureChunkInRange(set, context.seed(), chunkPos.x, chunkPos.z, 3));

            if (chunkGen.hasFeatureChunkInRange(set, context.seed(), chunkPos.x, chunkPos.z, 6)) {
                // BrassAmberBattleTowers.LOGGER.info("Has " + set + " Feature in range");
                return BlockPos.ZERO;
            }
        }

        // Test/Check 3 by 3 square of chunks for possible spawns
        List<ChunkPos> testable = new ArrayList<>(
                List.of(
                        chunkPos,
                        new ChunkPos(chunkPos.x, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z),
                        new ChunkPos(chunkPos.x, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z)
                )
        );

        List<ChunkPos> usablePositions =  new ArrayList<>();
        int bottomFloorRange = seaLevel - 44;
        int topFloorRange = seaLevel - 8;
        int newOceanFloorHeight;
        int lowestY = seaLevel;
        int highestY = bottomFloorRange;
        int minX;
        int minZ;
        int newX;
        int newZ;
        int averageHeight = 0;
        ArrayList<Integer> averages = new ArrayList<>();

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = chunkGen.getNoiseBiome(QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(0), QuartPos.fromBlock(pos.getMiddleBlockX()));
            minX = pos.getMinBlockX();
            minZ = pos.getMinBlockX();

            averages.clear();
            for (int x = 0; x < 6; x++) {
                for (int z = 0; z < 6; z++) {
                    newX = minX + (x * 3);
                    newZ = minZ + (z * 3);
                    newOceanFloorHeight = chunkGen.getFirstOccupiedHeight(newX, newZ, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor());
                    lowestY = Math.min(newOceanFloorHeight, lowestY);
                    highestY = Math.max(newOceanFloorHeight, highestY);
                    averages.add((highestY + lowestY) / 2);
                    if (highestY > seaLevel) {
                        return BlockPos.ZERO;
                    }
                }

            }

            averageHeight = median(averages);
            BrassAmberBattleTowers.LOGGER.info("Ocean floor average height for position = " + averageHeight);
            if (averageHeight >= bottomFloorRange && averageHeight <= topFloorRange && predicate.test(biome)) {
                usablePositions.add(pos);
                BrassAmberBattleTowers.LOGGER.info("Ocean floor height for usable position = " + lowestY + " " + highestY);
            }
        }
        if (usablePositions.size() > 0) {
            int index = worldgenRandom.nextInt(usablePositions.size());
            BrassAmberBattleTowers.LOGGER.info("Position chosen: " + usablePositions.get(index).getMiddleBlockPosition(seaLevel - 12));
            return usablePositions.get(index).getMiddleBlockPosition(seaLevel - 12);
        }
        return BlockPos.ZERO;
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        // Check if the spot is valid for our structure. This is just as another method for cleanness.
        // Returning an empty optional tells the game to skip this spot as it will not generate the structure. -- TelepathicGrunt

        Predicate<Holder<Biome>> predicate = context.validBiome();
        Optional<PieceGenerator<JigsawConfiguration>> piecesGenerator;

        int firstTowerDistance = BattleTowersConfig.firstTowerDistance.get();
        int minimumSeparation = BattleTowersConfig.oceanMinimumSeperation.get();
        int seperationRange = BattleTowersConfig.oceanAverageSeperationModifier.get();

        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator chunkGen = context.chunkGenerator();
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(context.seed(), chunkPos.x, chunkPos.z);

        boolean firstTowerDistanceCheck = chunkDistanceTo(ChunkPos.ZERO, chunkPos) >= firstTowerDistance;
        if (!firstTowerDistanceCheck) {
            return Optional.empty();
        }

        int nextSeperation =  minimumSeparation + worldgenRandom.nextInt(seperationRange * 2);
        int beforeLastDistance = chunkDistanceTo(beforeLastPosition, chunkPos);
        int lastDistance = chunkDistanceTo(lastPosition, chunkPos);
        int closestDistance = Math.min(beforeLastDistance, lastDistance);

        if (closestDistance <= nextSeperation) {
            // BrassAmberBattleTowers.LOGGER.info("Land not outside tower separation " + nextSeperation);
            return Optional.empty();
        }

        BlockPos chunkCenter = chunkPos.getMiddleBlockPosition(0);
        int x = chunkCenter.getX();
        int z = chunkCenter.getZ();
        int y =  chunkGen.getFirstFreeHeight(chunkCenter.getX(), chunkCenter.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

        Holder<Biome> biome = chunkGen.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z));

        BlockPos spawnPos;
        if (predicate.test(biome)) {
            spawnPos = isSpawnableChunk(context, chunkPos, chunkGen, worldgenRandom);
            /*BrassAmberBattleTowers.LOGGER.info("Biome correct: " + biome.unwrapKey()
                    + " Block: " + chunkCenter.atY(context.chunkGenerator().getFirstFreeHeight(
                            chunkCenter.getX(), chunkCenter.getZ(),
                    Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor()
                ))
            );**/
        } else {
            return Optional.empty();
        }

        // BrassAmberBattleTowers.LOGGER.info("Ocean last position: " + lastPosition);

        if (spawnPos.getY() != 0) {
            // Moved Biome check in JigsawPlacement outside
            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            piecesGenerator =
                    BTOceanJigsawPlacement.addPieces(
                            context, // Used for JigsawPlacement to get all the proper behaviors done.
                            PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                            spawnPos
                    );

            if (piecesGenerator.isPresent()) {
                BrassAmberBattleTowers.LOGGER.info("Ocean Tower at " + spawnPos);
                lastPosition = context.chunkPos();
            }

            return piecesGenerator;
        }
        return Optional.empty();
    }

    public static void afterPlace(WorldGenLevel worldGenLevel, StructureFeatureManager featureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
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
