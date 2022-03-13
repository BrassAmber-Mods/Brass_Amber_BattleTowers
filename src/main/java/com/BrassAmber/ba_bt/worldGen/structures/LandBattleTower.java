package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.init.BTStructures;
import com.BrassAmber.ba_bt.worldGen.BTJigsawConfiguration;
import com.BrassAmber.ba_bt.worldGen.BTLandJigsawPlacement;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import org.apache.logging.log4j.Level;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// Comments from TelepathicGrunts

public class LandBattleTower extends StructureFeature<BTJigsawConfiguration> {
    public LandBattleTower() {
        super(BTJigsawConfiguration.CODEC, LandBattleTower::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    public LandBattleTower(Codec<BTJigsawConfiguration> codec, boolean tower) {
        super(codec, OvergrownLandTower::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    public static boolean isFeatureChunk(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {

        ChunkPos chunkPos = context.chunkPos();
        BlockPos centerOfChunk = context.chunkPos().getMiddleBlockPosition(0);

        boolean firstTowerDistanceCheck = BattleTowersConfig.firstTowerDistance.get() >= Mth.abs(centerOfChunk.getX())
                || BattleTowersConfig.firstTowerDistance.get() >= Mth.abs(centerOfChunk.getZ());

        context.chunkGenerator();
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

        boolean structureTooClose = false;

        for (ResourceKey<StructureSet> set : vanillaStructures) {
            if(context.chunkGenerator().hasFeatureChunkInRange(set, context.seed(), chunkPos.x, chunkPos.z, 10)) {
                structureTooClose = true;
            }
        }


        // We check using the isFlatLand() function below for water and spacing
        return isFlatLand(context.chunkGenerator(), centerOfChunk, context.heightAccessor()) && landHeight <= 210 && !structureTooClose && firstTowerDistanceCheck; //;
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
        int f = 0;

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
            hasWater.add(topBlock.getFluidState().isSource());
        }
        // set the base output to be true.
        boolean noWater = !hasWater.contains(true);

        // check if any of the blockpos have water.

        BrassAmberBattleTowers.LOGGER.info("Land Battle Tower at " + pos.getX() + " " + pos.getZ()
                + " " + t +" " + f + " "+ noWater);

        // if there are more flat areas than not flat areas and no water return true 
        return isFlat && noWater;
    }

    private static final Lazy<List<MobSpawnSettings.SpawnerData>> STRUCTURE_MONSTERS = Lazy.of(() -> ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 100, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.HUSK, 80, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 100, 1, 4)
    ));

    // Hooked up in StructureTutorialMain. You can move this elsewhere or change it up.
    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == BTStructures.LAND_BATTLE_TOWER.get()) {
            for (MobSpawnSettings.SpawnerData data : STRUCTURE_MONSTERS.get()) {
                event.removeEntitySpawn(MobCategory.MONSTER, data);
            }
        }
    }


    public static @NotNull Optional<PieceGenerator<BTJigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {
        // Check if the spot is valid for our structure. This is just as another method for cleanness.
        // Returning an empty optional tells the game to skip this spot as it will not generate the structure. -- TelepathicGrunt
        if (!LandBattleTower.isFeatureChunk(context)) {
            return Optional.empty();
        }
        /*
         * We pass this into addPieces to tell it where to generate the structure.
         * If addPieces's last parameter is true, blockpos's Y value is ignored and the
         * structure will spawn at terrain height instead. Set that parameter to false to
         * force the structure to spawn at blockpos's Y value instead. You got options here!
         * -- TelepathicGrunt
         */

        BTJigsawConfiguration config = new BTJigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                // The path to the starting Template Pool JSON file to read.
                //
                // Note, this is "ba_bt:land_tower/start_pool"
                // the game will automatically look into the following path for the template pool:
                // "resources/data/ba_bt/worldgen/template_pool/land_tower/start_pool.json" -- TelepathicGrunt (modified by M)

                .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "land_tower/start_pool")),

                // How many pieces outward from center this recursive jigsaw structure can spawn.
                // Technically (testing needed) I think I could have this value down at 9, since there are 7 structure floor connections-
                // - and each spawner only extends that number to 8 connections at the top floor.
                // However, no testing of this has been done (yet)  and I would rather be on the safe side.
                // Since the tower is not a completely recursive structure, this number could technically be as high as I wanted it to be (within integer limits ofc)
                // As long as it is more than the total number of possible connections the tower will spawn correctly.
                12);

        PieceGeneratorSupplier.Context<BTJigsawConfiguration> baseTower = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                config,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );

        // Get chunk center coordinates
        BlockPos centerPos = context.chunkPos().getMiddleBlockPosition(0);

        Optional<PieceGenerator<BTJigsawConfiguration>> piecesGenerator;
        // All a structure has to do is call this method to turn it into a jigsaw based structure!

        piecesGenerator =
                BTLandJigsawPlacement.addPieces(
                        baseTower, // Used for JigsawPlacement to get all the proper behaviors done.
                        PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                        centerPos, // Position of the structure. Y value is ignored if last parameter is set to true. --TelepathicGrunt
                        false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting. --TelepathicGrunt
                        true,// Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        // --TelepathicGrunt
                        false // null here == random rotation.
                );


        if(piecesGenerator.isPresent()) {
            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            BrassAmberBattleTowers.LOGGER.info("Land Tower at " + centerPos);
        }

        // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
        return piecesGenerator;



    }
}
