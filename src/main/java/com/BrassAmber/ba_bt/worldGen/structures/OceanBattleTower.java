package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.BTUtil;
import com.BrassAmber.ba_bt.worldGen.BTLandJigsawPlacement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;


// Comments from TelepathicGrunts

public class OceanBattleTower extends StructureFeature<JigsawConfiguration> {

    private static final int firstTowerDistance = BattleTowersConfig.firstTowerDistance.get();
    private static final int minimumSeparation = BattleTowersConfig.landMinimumSeperation.get();
    private static final int seperationRange = BattleTowersConfig.landAverageSeperationModifier.get();
    private static ChunkPos lastSpawnPosition = ChunkPos.ZERO;
    private static BlockPos SpawnPos;

    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
            Codec.intRange(0, 40).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
    ).apply(codec, JigsawConfiguration::new));

    public OceanBattleTower() {
        super(CODEC, OceanBattleTower::createPiecesGenerator, OceanBattleTower::afterPlace);
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    public static boolean isSpawnableChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        ChunkPos chunkPos = context.chunkPos();

        boolean firstTowerDistanceCheck = (int) Mth.absMax(chunkPos.x, chunkPos.z) >= firstTowerDistance;
        // BrassAmberBattleTowers.LOGGER.info("current distance " + (int) Mth.absMax(chunkPos.x, chunkPos.z) + "  config f distance " + BattleTowersConfig.firstTowerDistance.get());

        if (!firstTowerDistanceCheck) {
            return false;
        }

        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), chunkPos.x, chunkPos.z);

        int nextSeperation =  minimumSeparation + worldgenrandom.nextInt(seperationRange);

        int spawnDistance = Math.min(Mth.abs(chunkPos.x-lastSpawnPosition.x), Mth.abs(chunkPos.z-lastSpawnPosition.z));

        // BrassAmberBattleTowers.LOGGER.info("distance from last " + spawnDistance + "  config distance allowed " + nextSeperation);

        if (spawnDistance < nextSeperation) {
            return false;
        }

        BlockPos centerOfChunk = context.chunkPos().getMiddleBlockPosition(0);

        // Grab height of land. Will stop at first non-air block. --TelepathicGrunt
        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

        List<ResourceKey<StructureSet>> vanillaStructures = new ArrayList<>();
        vanillaStructures.add(BuiltinStructureSets.VILLAGES);
        vanillaStructures.add(BuiltinStructureSets.DESERT_PYRAMIDS);
        vanillaStructures.add(BuiltinStructureSets.IGLOOS);
        vanillaStructures.add(BuiltinStructureSets.JUNGLE_TEMPLES);
        vanillaStructures.add(BuiltinStructureSets.SWAMP_HUTS);
        vanillaStructures.add(BuiltinStructureSets.PILLAGER_OUTPOSTS);
        vanillaStructures.add(BuiltinStructureSets.WOODLAND_MANSIONS);
        vanillaStructures.add(BuiltinStructureSets.RUINED_PORTALS);
        vanillaStructures.add(BuiltinStructureSets.SHIPWRECKS);


        for (ResourceKey<StructureSet> set : vanillaStructures) {
            // BrassAmberBattleTowers.LOGGER.info(context.chunkGenerator().hasFeatureChunkInRange(set, context.seed(), chunkPos.x, chunkPos.z, 3));

            if (context.chunkGenerator().hasFeatureChunkInRange(set, context.seed(), chunkPos.x, chunkPos.z, 3)) {
                // BrassAmberBattleTowers.LOGGER.info("Has " + set + " Feature in range");
                return false;
            }
        }

        if (landHeight > 215) {
            // BrassAmberBattleTowers.LOGGER.info("LandHeight: " + landHeight + " at: " + centerOfChunk);
            return false;
        }
        // Test/Check surrounding chunks for possible spawns

        List<BlockPos> testables = new ArrayList<>(List.of(
                centerOfChunk,
                new BlockPos(centerOfChunk.getX(), centerOfChunk.getY(), centerOfChunk.getZ() + 32),
                new BlockPos(centerOfChunk.getX() + 32, centerOfChunk.getY(), centerOfChunk.getZ() + 32),
                new BlockPos(centerOfChunk.getX() + 32, centerOfChunk.getY(), centerOfChunk.getZ()),
                new BlockPos(centerOfChunk.getX() + 32, centerOfChunk.getY(), centerOfChunk.getZ() - 32),
                new BlockPos(centerOfChunk.getX(), centerOfChunk.getY(), centerOfChunk.getZ() - 32),
                new BlockPos(centerOfChunk.getX() - 32, centerOfChunk.getY(), centerOfChunk.getZ() - 32),
                new BlockPos(centerOfChunk.getX() - 32, centerOfChunk.getY(), centerOfChunk.getZ()),
                new BlockPos(centerOfChunk.getX() - 32, centerOfChunk.getY(), centerOfChunk.getZ() + 32)
        ));
        // North, Northeast, East, SouthEast, South, SouthWest, West, NorthWest
        // X = Empty, T = Checked
        // T X T X T
        // X X X X X
        // T X X X T
        // X X X X X
        // T X T X T

        List<BlockPos> usablePositions =  new ArrayList<>();

        for (BlockPos pos : testables) {
            if (isFlatLand(context.chunkGenerator(), pos, context.heightAccessor())) {
                usablePositions.add(pos);
            }
        }

        if (usablePositions.size() > 0) {
            SpawnPos = usablePositions.get(worldgenrandom.nextInt(usablePositions.size()));
            return true;
        }

        return false;
    }
    
    public static boolean isFlatLand(ChunkGenerator chunk, BlockPos pos, LevelHeightAccessor heightAccessor) {
        return true;
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        // Check if the spot is valid for our structure. This is just as another method for cleanness.
        // Returning an empty optional tells the game to skip this spot as it will not generate the structure. -- TelepathicGrunt

        Predicate<Holder<Biome>> predicate = context.validBiome();
        Optional<PieceGenerator<JigsawConfiguration>> piecesGenerator;

        BlockPos chunkCenter= context.chunkPos().getMiddleBlockPosition(0);
        int x = chunkCenter.getX();
        int z = chunkCenter.getZ();
        int y =  context.chunkGenerator().getFirstFreeHeight(chunkCenter.getX(), chunkCenter.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

        Holder<Biome> biome = context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z));

        if (!predicate.test(biome)) {
            piecesGenerator = Optional.empty();
            return piecesGenerator;
        }

        if (isSpawnableChunk(context)) {
            // Moved Biome check in JigsawPlacement outside
            int i;
            int j;
            int k;
            i = SpawnPos.getX();
            j = SpawnPos.getZ();
            k = SpawnPos.getY() + context.chunkGenerator().getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

            if (!predicate.test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j)))) {
                BrassAmberBattleTowers.LOGGER.info("Ocean tower incorrect biome");
                piecesGenerator = Optional.empty();
            } else {

                // All a structure has to do is call this method to turn it into a jigsaw based structure!
                piecesGenerator =
                        BTLandJigsawPlacement.addPieces(
                                context, // Used for JigsawPlacement to get all the proper behaviors done.
                                PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                                SpawnPos, // Position of the structure. Y value is ignored if last parameter is set to true. --TelepathicGrunt
                                true, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                                // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                                // --TelepathicGrunt
                                false,
                                false
                        );



                // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces
            }
        } else {
            piecesGenerator = Optional.empty();
        }

        if (piecesGenerator.isPresent()) {
            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            lastSpawnPosition = context.chunkPos();
        }

        return piecesGenerator;
    }

    public static void afterPlace(WorldGenLevel worldGenLevel, StructureFeatureManager featureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int bbYStart = boundingbox.minY();

        BlockPos chunckCenter = chunkPos.getMiddleBlockPosition(bbYStart);

        BrassAmberBattleTowers.LOGGER.info("Post Processing: In chunk: " + chunkPos + " " + chunckCenter);

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
                if (worldGenLevel.getBlockState(blockpos$mutableblockpos) == Blocks.PRISMARINE_BRICKS.defaultBlockState()) {
                    BrassAmberBattleTowers.LOGGER.info("Block is acceptable: " + blockpos$mutableblockpos + " "+ worldGenLevel.getBlockState(blockpos$mutableblockpos));
                    startPositions.add(new BlockPos(x, bbYStart - 1, z));
                }
            }
        }

        for (BlockPos startPos: startPositions) {
            for (int y = startPos.getY(); y > worldGenLevel.getMinBuildHeight() ; y--) {
                blockpos$mutableblockpos.set(startPos.getX(), y, startPos.getZ());
                // BrassAmberBattleTowers.LOGGER.info("Block to check: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) || worldGenLevel.isWaterAt(blockpos$mutableblockpos)
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof TallGrassBlock
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
