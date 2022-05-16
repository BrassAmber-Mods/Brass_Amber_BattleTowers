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
import net.minecraft.world.level.block.SandBlock;
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
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;


// Comments from TelepathicGrunts

public class LandBattleTower extends StructureFeature<JigsawConfiguration> {

    private static final int firstTowerDistance = BattleTowersConfig.firstTowerDistance.get();
    private static final int minimumSeparation = BattleTowersConfig.landMinimumSeperation.get();
    private static final int seperationRange = BattleTowersConfig.landAverageSeperationModifier.get();
    private static ChunkPos lastSpawnPosition = ChunkPos.ZERO;
    private static BlockPos SpawnPos;
    private static boolean watered;
    private static boolean sandy;

    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
            Codec.intRange(0, 40).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
    ).apply(codec, JigsawConfiguration::new));

    public LandBattleTower() {
        super(CODEC, LandBattleTower::createPiecesGenerator, LandBattleTower::afterPlace);
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    public static boolean isSpawnableChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context, int biomeType) {

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
            if (isFlatLand(context.chunkGenerator(), pos, context.heightAccessor(), biomeType)) {
                usablePositions.add(pos);
            }
        }

        if (usablePositions.size() > 0) {
            SpawnPos = usablePositions.get(worldgenrandom.nextInt(usablePositions.size()));
            return true;
        }

        return false;
    }
    
    public static boolean isFlatLand(ChunkGenerator chunk, BlockPos pos, LevelHeightAccessor heightAccessor, int biomeType) {
        //Create block positions to check 
        BlockPos north = new BlockPos(pos.getX(), 0, pos.getZ()+8);
        BlockPos northEast = new BlockPos(pos.getX()+4, 0, pos.getZ()+4);
        BlockPos east = new BlockPos(pos.getX()+8, 0, pos.getZ());
        BlockPos southEast = new BlockPos(pos.getX()+4, 0, pos.getZ()-4);
        BlockPos south = new BlockPos(pos.getX(),0 , pos.getZ()-8);
        BlockPos southWest = new BlockPos(pos.getX()-4, 0, pos.getZ()-4);
        BlockPos west = new BlockPos(pos.getX()-8, 0, pos.getZ());
        BlockPos northWest = new BlockPos(pos.getX()-4, 0, pos.getZ()+4);
        // create arraylist to allow easy iteration over BlockPos 
        ArrayList<BlockPos> list = new ArrayList<>(9);
        list.add(pos);
        list.add(north);
        list.add(northEast);
        list.add(east);
        list.add(southEast);
        list.add(south);
        list.add(southWest);
        list.add(west);
        list.add(northWest);


        // Create arraylists to hold the output of the iteration checks below 
        boolean isFlat;
        ArrayList<Boolean> hasWater = new ArrayList<>(9);

        int landHeight = chunk.getFirstOccupiedHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);

        // create integers to hold how many land-heights are the same
        int t = 0;
        int f;
        switch (biomeType) {
            default -> f = 0;
            case 1 -> f = -4;
            case 2 -> f = 2;
        }

        // Check that a + sign of blocks at each position is all the same level. (flat) 
        for (BlockPos x: list) {
            // get land height for each block on the +  
            int newLandHeight = chunk.getFirstOccupiedHeight(x.getX(), x.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);

            // check that the new landheight is the same as the center of the chunk
            if (landHeight == newLandHeight) {
                t += 1;
            } else {
                f += 1;
            }
        }
        // check if most BlockPos are the same height and add false to the list if not
        isFlat = t > f;

        // check that there is no water at any of the Blockpos 
        for (BlockPos x: list) {
            // get landheight
            int newLandHeight = chunk.getFirstOccupiedHeight(x.getX(), x.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);

            // get column of blocks at blockpos.
            NoiseColumn columnOfBlocks = chunk.getBaseColumn(x.getX(), x.getZ(), heightAccessor);

            // combine the column of blocks with land height and you get the top block itself which you can test.
            BlockState topBlock = columnOfBlocks.getBlock(newLandHeight);

            // check whether the topBlock is a source block of water.
            hasWater.add(topBlock.getFluidState().is(Fluids.WATER) || topBlock.getFluidState().is(Fluids.FLOWING_WATER));
        }
        // set the base output to be true.
        watered = hasWater.contains(true);

        // check if any of the blockpos have water.

        BrassAmberBattleTowers.LOGGER.info("Possible Land Tower at " + pos.getX() + " " + pos.getZ()
                + " " + isFlat + " "+ !watered);

        if (biomeType == 1) {
            return isFlat;
        }

        // if there are more flat areas than not flat areas and no water return true
        return isFlat && !watered;
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

        int towerType = 0;

        for (List<ResourceKey<Biome>> biomeList: BTUtil.landTowerBiomes) {
            for (ResourceKey<Biome> biomeKey: biomeList) {
                if(biome.is(biomeKey)) {
                    towerType = BTUtil.landTowerBiomes.indexOf(biomeList);
                    BrassAmberBattleTowers.LOGGER.info("Correct Biome for : " + BTUtil.landTowerNames.get(towerType) + " " + biome);
                }
            }
        }

        if (isSpawnableChunk(context, towerType)) {
            // Moved Biome check in JigsawPlacement outside
            int i;
            int j;
            int k;
            i = SpawnPos.getX();
            j = SpawnPos.getZ();
            k = SpawnPos.getY() + context.chunkGenerator().getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

            if (!predicate.test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j)))) {
                BrassAmberBattleTowers.LOGGER.info(BTUtil.landTowerNames.get(towerType) + "incorrect biome");
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
                                watered,
                                sandy
                        );



                // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces
            }
        } else {
            piecesGenerator = Optional.empty();
        }

        if (piecesGenerator.isPresent()) {
            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            BrassAmberBattleTowers.LOGGER.info(BTUtil.landTowerNames.get(towerType) +  " Tower at " + SpawnPos);
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
                if (worldGenLevel.getBlockState(blockpos$mutableblockpos) == Blocks.STONE_BRICKS.defaultBlockState()) {
                    BrassAmberBattleTowers.LOGGER.info("Block is acceptable: " + blockpos$mutableblockpos + " "+ worldGenLevel.getBlockState(blockpos$mutableblockpos));
                    startPositions.add(new BlockPos(x, bbYStart - 1, z));
                }
            }
        }

        for (BlockPos startPos: startPositions) {
            for (int y = startPos.getY(); y > worldGenLevel.getMinBuildHeight() ; y--) {
                blockpos$mutableblockpos.set(startPos.getX(), y, startPos.getZ());
                // BrassAmberBattleTowers.LOGGER.info("Block to check: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) || worldGenLevel.isWaterAt(blockpos$mutableblockpos)) {
                    worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                } else {
                    // Add two blocks into this ground level as well.
                    worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos$mutableblockpos.below(), Blocks.STONE_BRICKS.defaultBlockState(), 2);
                    break;
                }
            }
        }

    }
}
