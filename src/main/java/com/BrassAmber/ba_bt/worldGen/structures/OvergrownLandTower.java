package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.worldGen.BTJigsawConfiguration;
import com.BrassAmber.ba_bt.worldGen.BTLandJigsawPlacement;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.material.Fluids;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class OvergrownLandTower extends StructureFeature<BTJigsawConfiguration> {
    public OvergrownLandTower() {
        super(BTJigsawConfiguration.CODEC, OvergrownLandTower::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    private static boolean watered;
    private static BlockPos SpawnPos;
    private static ChunkPos lastSpawnPosition = ChunkPos.ZERO;

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }



    public static boolean isSpawnableChunk(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {

        ChunkPos chunkPos = context.chunkPos();

        boolean firstTowerDistanceCheck = (int) Mth.absMax(chunkPos.x, chunkPos.z) >= BattleTowersConfig.firstTowerDistance.get();
        // BrassAmberBattleTowers.LOGGER.info("current distance " + Mth.absMax(chunkPos.x, chunkPos.z) + "  config f distance " + BattleTowersConfig.firstTowerDistance.get());

        if (!firstTowerDistanceCheck) {
            return false;
        }

        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);

        int seperationRange = BattleTowersConfig.landAverageSeperationModifier.get() + BattleTowersConfig.landAverageSeperationModifier.get();

        int nextSeperation = BattleTowersConfig.landMinimumSeperation.get() + worldgenrandom.nextInt(seperationRange);

        BrassAmberBattleTowers.LOGGER.info("Overgrown distance from last " + lastSpawnPosition.getChessboardDistance(chunkPos) + "  config distance allowed " + nextSeperation);

        if (lastSpawnPosition.getChessboardDistance(chunkPos) < nextSeperation) {
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
                return false;
            }
        }

        if (landHeight > 200) {
            BrassAmberBattleTowers.LOGGER.info("Overgrown LandHeight: " + landHeight + " at: " + centerOfChunk);
            return false;
        }

        // First Test the center chunk given by the context
        boolean chunkOkay = isFlatLand(context.chunkGenerator(), centerOfChunk, context.heightAccessor());
        if (chunkOkay) {
            SpawnPos = new BlockPos(centerOfChunk.getX(), landHeight, centerOfChunk.getZ());
            return true;
        }
        // if false, check surrounding chunks for possible spawns

        List<BlockPos> testables = new ArrayList<>(List.of(
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

        List<Boolean> possiblePositions =  new ArrayList<>();
        List<BlockPos> usablePositions =  new ArrayList<>();

        for (BlockPos pos : testables) {
            possiblePositions.add(isFlatLand(context.chunkGenerator(), pos, context.heightAccessor()));
        }

        int i = 0;
        for (boolean toTest : possiblePositions) {
            if (toTest) {
                usablePositions.add(testables.get(i));
            }
            i++;
        }

        if (usablePositions.size() > 0) {
            SpawnPos = usablePositions.get(worldgenrandom.nextInt(usablePositions.size()));
            return true;
        }

        return false;
    }

    public static boolean isFlatLand(ChunkGenerator chunk, BlockPos pos, LevelHeightAccessor heightAccessor) {
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

        // create integers to hold how many landheights are the same and how many isFlat are true/false
        int t = 0;
        int f = -4;

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
        int water = 0;

        for (boolean d : hasWater) {
            if(d) {
                water++;
            }
        }
        boolean noWater = water < 3;

        // check if any of the blockpos have water.

        BrassAmberBattleTowers.LOGGER.info("Overgrown Land Battle Tower at " + pos.getX() + " " + pos.getZ()
                + " " + t +" " + f + " "+ noWater);

        // if there are more flat areas than not flat areas and no water return true
        if (!hasWater.contains(true)) {
            watered = true;
        }
        return isFlat;
    }


    public static @NotNull Optional<PieceGenerator<BTJigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {
        // Check if the spot is valid for our structure. This is just as another method for cleanness.
        // Returning an empty optional tells the game to skip this spot as it will not generate the structure. -- TelepathicGrunt
        if (isSpawnableChunk(context)) {
            // Moved Biome check in JigsawPlacement outside
            Predicate<Holder<Biome>> predicate = context.validBiome();
            int i;
            int j;
            int k;
            try {
                i = SpawnPos.getX();
                j = SpawnPos.getX();
                k = SpawnPos.getX() + context.chunkGenerator().getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            } catch (Exception ignored) {
                return Optional.empty();
            }

            if (!predicate.test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j)))) {
                return Optional.empty();
            }
            Optional<PieceGenerator<BTJigsawConfiguration>> piecesGenerator;
            // All a structure has to do is call this method to turn it into a jigsaw based structure!

            piecesGenerator =
                    BTLandJigsawPlacement.addPieces(
                            context, // Used for JigsawPlacement to get all the proper behaviors done.
                            PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                            SpawnPos, // Position of the structure. Y value is ignored if last parameter is set to true. --TelepathicGrunt
                            true, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                            // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                            // --TelepathicGrunt
                            watered
                    );


            if(piecesGenerator.isPresent()) {
                // I use to debug and quickly find out if the structure is spawning or not and where it is.
                // This is returning the coordinates of the center starting piece.
                BrassAmberBattleTowers.LOGGER.info("Overgrown Land Tower at " + SpawnPos);
                lastSpawnPosition = context.chunkPos();
            }

            // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
            return piecesGenerator;
        } else {
            return Optional.empty();
        }
    }
}
