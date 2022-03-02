package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.init.BTStructures;
import com.BrassAmber.ba_bt.worldGen.BTJigsawConfiguration;
import com.BrassAmber.ba_bt.worldGen.BTLandJigsawPlacement;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import org.apache.logging.log4j.Level;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// Comments from TelepathicGrunts

public class LandBattleTower extends StructureFeature<BTJigsawConfiguration> {
    public LandBattleTower(Codec<BTJigsawConfiguration> codec) {
        super(codec, LandBattleTower::createPiecesGenerator, PostPlacementProcessor.NONE);
    }
    
    private static Biome biome;


    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    public static boolean isFeatureChunk(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {


        BlockPos centerOfChunk = context.chunkPos().getMiddleBlockPosition(0);

        biome = context.chunkGenerator().getNoiseBiome(centerOfChunk.getX(), centerOfChunk.getY(), centerOfChunk.getZ());

        context.chunkGenerator();
        // Grab height of land. Will stop at first non-air block. --TelepathicGrunt
        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());


        // Grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
        // In nether, it will be netherrack, lava, and air. End will only be endstone and air. It depends on what block
        // the chunk generator will place for that dimension. --TelepathicGrunt
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());

        // Combine the column of blocks with land height and you get the top block itself which you can test. --TelepathicGrunt
        BlockState topBlock = columnOfBlocks.getBlock(landHeight);

        
        // Now we test to make sure our structure is not spawning on water or other fluids. --TelepathicGrunt

        // We also check that canSpawn returned true and whether it is low enough (150 and below) to spawn the tower. 
        return isFlatLand(context.chunkGenerator(), centerOfChunk, context.heightAccessor()) && landHeight <= 210 ; //;
    }
    
    public static boolean isFlatLand(ChunkGenerator chunk, BlockPos pos, LevelHeightAccessor heightAccessor) {
        //Create block positions to check 
        BlockPos north = new BlockPos(pos.getX(), 0, pos.getZ()+7);
        BlockPos east = new BlockPos(pos.getX()+7, 0, pos.getZ());
        BlockPos south = new BlockPos(pos.getX(),0 , pos.getZ()-7);
        BlockPos west = new BlockPos(pos.getX()-7, 0, pos.getZ());
        // create arraylist to allow easy iteration over BlockPos 
        ArrayList<BlockPos> list = new ArrayList<>(5);
        list.add(pos);
        list.add(north);
        list.add(east);
        list.add(south);
        list.add(west);


        // Create arraylists to hold the output of the iteration checks below 
        ArrayList<Boolean> isFlat = new ArrayList<>(5);
        ArrayList<Boolean> hasWater = new ArrayList<>(5);
        ArrayList<Boolean> sameHeight = new ArrayList<>(4);

        // create integers to hold how many landheights are the same and how many isFlat are true/false 
        int t = 0;
        int f = 0;

        // Check that a + sign of blocks at each position is all the same level. (flat) 
        for (BlockPos x: list) {
            // get land height for each block on the +  
            int landHeight = chunk.getFirstOccupiedHeight(x.getX(), x.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
            int landheight1 = chunk.getFirstOccupiedHeight(x.getX()-1, x.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
            int landheight2 = chunk.getFirstOccupiedHeight(x.getX(), x.getZ()-1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
            int landheight3 = chunk.getFirstOccupiedHeight(x.getX(), x.getZ()+1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
            int landheight4 = chunk.getFirstOccupiedHeight(x.getX()+1, x.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);

            // set t and f to 0
            t = 0;
            f = 0;

            // add landheights to arraylist
            sameHeight.add(landHeight == landheight1);
            sameHeight.add(landHeight == landheight2);
            sameHeight.add(landHeight == landheight3);
            sameHeight.add(landHeight == landheight4);

            // check how many landheights are the same 
            for (Boolean tf: sameHeight) {
                if (tf) {
                    t += 1;
                } else {
                    f += 1;
                }
            }

            // check if most BlockPos are the same height and add false to the list if not 
            isFlat.add(t >= 3);
        }

        // set t and f to 0
        t = 0;
        f = 0;

        // iterate over isFlat array and count how many are true and how many are false 
        for (Boolean tf: isFlat) {
            if (tf) {
                t += 1;
            } else {
                f += 1;
            }
        }

        // check that there is no water at any of the Blockpos 
        for (BlockPos x: list) {
            // get landheight
            int landHeight = chunk.getFirstOccupiedHeight(x.getX(), x.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);

            // get column of blocks at blockpos.
            NoiseColumn columnOfBlocks = chunk.getBaseColumn(x.getX(), x.getZ(), heightAccessor);

            // combine the column of blocks with land height and you get the top block itself which you can test.
            BlockState topBlock = columnOfBlocks.getBlock(landHeight);

            // check whether the topBlock is a source block of water.
            hasWater.add(topBlock.getFluidState().isSource());
        }
        // set the base output to be true.
        boolean noWater = true;

        // check if any of the blockpos have water.
        if (hasWater.contains(true)) {
            noWater = false;
        }

        BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Land Battle Tower at " + pos.getX() + " " + pos.getZ()
                + " " + t +" " + f + " "+ noWater);

        // if there are more flat areas than not flat areas and no water return true 
        return t > 3 && noWater;
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


    public static List<BTJigsawConfiguration> createConfigs(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {
        List<BTJigsawConfiguration> configs = new ArrayList<>();

        configs.add(new BTJigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
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
                12)
        );

        configs.add(new BTJigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "land_tower/start_pool_overgrown")),
                10)
        );

        configs.add(new BTJigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "land_tower/start_pool_overgrown_roof")),
                1)
        );

        return configs;
    }

    public static List<PieceGeneratorSupplier.Context<BTJigsawConfiguration>> createContexts(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context, List<BTJigsawConfiguration> configs) {

        List<PieceGeneratorSupplier.Context<BTJigsawConfiguration>> contexts = new ArrayList<>();

        // Create a new context with the new config that has our json pool. We will pass this into JigsawPlacement.addPieces --TelepathicGrunt
        contexts.add(
                new PieceGeneratorSupplier.Context<>(
                    context.chunkGenerator(),
                    context.biomeSource(),
                    context.seed(),
                    context.chunkPos(),
                    configs.get(0),
                    context.heightAccessor(),
                    context.validBiome(),
                    context.structureManager(),
                    context.registryAccess()
                )
        );
        contexts.add(
                new PieceGeneratorSupplier.Context<>(
                        context.chunkGenerator(),
                        context.biomeSource(),
                        context.seed(),
                        context.chunkPos(),
                        configs.get(1),
                        context.heightAccessor(),
                        context.validBiome(),
                        context.structureManager(),
                        context.registryAccess()
                )
        );
        contexts.add(
                new PieceGeneratorSupplier.Context<>(
                        context.chunkGenerator(),
                        context.biomeSource(),
                        context.seed(),
                        context.chunkPos(),
                        configs.get(2),
                        context.heightAccessor(),
                        context.validBiome(),
                        context.structureManager(),
                        context.registryAccess()
                )
        );

        return  contexts;
    }

    public static Optional<PieceGenerator<BTJigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context) {
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

        List<BTJigsawConfiguration> towerConfigs = createConfigs(context);
        List<PieceGeneratorSupplier.Context<BTJigsawConfiguration>> towerContexts = createContexts(context, towerConfigs);

        // Get chunk center coordinates
        BlockPos centerPos = context.chunkPos().getMiddleBlockPosition(0);

        Optional<PieceGenerator<BTJigsawConfiguration>> piecesGenerator;
        // All a structure has to do is call this method to turn it into a jigsaw based structure!

        if (biome.isHumid() && !biome.getBiomeCategory().equals(Biome.BiomeCategory.UNDERGROUND)) {
            //add code here to start the spawning of the overgrown add-on
             piecesGenerator =
                    BTLandJigsawPlacement.addAllPieces(
                            towerContexts, // Used for JigsawPlacement to get all the proper behaviors done.
                            PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                            centerPos,
                            false,
                            true,
                            null // pass in the rotation of the base tower
                    // This makes the overgrown tower correctly overlap the base tower.
            );

        } else {
            piecesGenerator =
                    BTLandJigsawPlacement.addPieces(
                            towerContexts.get(0), // Used for JigsawPlacement to get all the proper behaviors done.
                            PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                            centerPos, // Position of the structure. Y value is ignored if last parameter is set to true. --TelepathicGrunt
                            false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting. --TelepathicGrunt
                            true,// Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                            // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                            // --TelepathicGrunt
                            null // null here == random rotation.
                    );
        }



        if(piecesGenerator.isPresent()) {
            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Land Tower at " + centerPos);
        }

        // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
        return piecesGenerator;



    }
}
