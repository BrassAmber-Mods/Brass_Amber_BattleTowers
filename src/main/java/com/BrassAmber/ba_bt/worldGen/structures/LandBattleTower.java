package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.worldGen.BTJigsawManager;
import org.apache.logging.log4j.Level;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.ArrayList;


// Comments from TelepathicGrunts

public class LandBattleTower extends Structure<NoFeatureConfig> {
    public LandBattleTower(Codec<NoFeatureConfig> codec) {
        super(codec);
    }
    
    private Biome biomeIn;

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return LandBattleTower.Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        biomeIn = biome;
        BlockPos cornerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);

        // Grab height of land. Will stop at first non-air block. --TelepathicGrunt
        int landHeight = chunkGenerator.getFirstOccupiedHeight(cornerOfChunk.getX(), cornerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

        // Grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
        // In nether, it will be netherrack, lava, and air. End will only be endstone and air. It depends on what block
        // the chunk generator will place for that dimension. --TelepathicGrunt
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(cornerOfChunk.getX(), cornerOfChunk.getZ());

        // Combine the column of blocks with land height and you get the top block itself which you can test. --TelepathicGrunt
        BlockState topBlock = columnOfBlocks.getBlockState(cornerOfChunk.above(landHeight));


        boolean distance = (BattleTowersConfig.firstTowerDistance.get() <= cornerOfChunk.getZ()) ||
                (BattleTowersConfig.firstTowerDistance.get() <= cornerOfChunk.getX());
        
        // Now we test to make sure our structure is not spawning on water or other fluids. --TelepathicGrunt

        // We also check that canSpawn returned true and whether it is low enough (150 and below) to spawn the tower. 
        return isFlatLand(chunkGenerator, new BlockPos(cornerOfChunk.getX(), 0, cornerOfChunk.getZ())) && landHeight <= 150 && distance; //;
    }
    
    public boolean isFlatLand(ChunkGenerator chunk, BlockPos pos) {
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

        int landHeight = chunk.getFirstOccupiedHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

        // create integers to hold how many landheights are the same and how many isFlat are true/false 
        int t = 0;
        int f = 0;

        // Check that a + sign of blocks at each position is all the same level. (flat) 
        for (BlockPos x: list) {
            // get land height for each block on the +  

            int newLandHeight = chunk.getFirstOccupiedHeight(x.getX(), x.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

            // get column of blocks at blockpos.
            IBlockReader columnOfBlocks = chunk.getBaseColumn(x.getX(), x.getZ());

            // combine the column of blocks with land height and you get the top block itself which you can test.
            BlockState topBlock = columnOfBlocks.getBlockState(x.above(landHeight));

            // check whether the topBlock is a source block of water.
            hasWater.add(topBlock.getFluidState().isSource());

            // check how many landheights are the same 
            if (landHeight == newLandHeight) {
                t += 1;
            } else {
                f += 1;
            }
        }

        isFlat = t > f;

        // set the base output to be true.
        boolean noWater = !hasWater.contains(true);

        BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Land Battle Tower at " + pos.getX() + " " + pos.getZ()
                + " " + t +" " + f + " "+ noWater);

        // if there are more flat areas than not flat areas and no water return true 
        return isFlat && noWater;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

            // Turns the chunk coordinates into actual coordinates we can use -- TelepathicGrunt
            int x = chunkX * 16;
            int z = chunkZ * 16;

            /*
             * We pass this into addPieces to tell it where to generate the structure.
             * If addPieces's last parameter is true, blockpos's Y value is ignored and the
             * structure will spawn at terrain height instead. Set that parameter to false to
             * force the structure to spawn at blockpos's Y value instead. You got options here!
             * -- TelepathicGrunt
             */
            BlockPos centerPos = new BlockPos(x, 0, z);

            /*
             * If you are doing Nether structures, you'll probably want to spawn your structure on top of ledges.
             * Best way to do that is to use getBaseColumn to grab a column of blocks at the structure's x/z position.
             * Then loop through it and look for land with air above it and set blockpos's Y value to it.
             * Make sure to set the final boolean in JigsawManager.addPieces to false so
             * that the structure spawns at blockpos's y value instead of placing the structure on the Bedrock roof!
             * -- TelepathicGrunt
             */

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            try {
                BTJigsawManager.addPieces(
                        dynamicRegistryManager,
                        new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                                // The path to the starting Template Pool JSON file to read.
                                //
                                // Note, this is "ba_bt:land_tower/start_pool"
                                // the game will automatically look into the following path for the template pool:
                                // "resources/data/ba_bt/worldgen/template_pool/land_tower/start_pool.json" -- TelepathicGrunt (modified by M)

                                .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "land_tower/start_pool")),

                                // How many pieces outward from center this recursive jigsaw structure can spawn.
                                // Technically (testing needed) I think I could have this value down at 9, since there are 7 structure floor connections-
                                // - and each spawner only extends that number to 8 connections at the top floor.
                                // However no testing of this has been done (yet)  and I would rather be on the safe side.
                                // Since the tower is not a completely recursive structure, this number could technically be as high as I wanted it to be (within integer limits ofc)
                                // As long as it is more than the total number of possible connections the tower will spawn correctly.
                                10),
                        AbstractVillagePiece::new,
                        chunkGenerator,
                        templateManagerIn,
                        centerPos, // Position of the structure. Y value is ignored if last parameter is set to true. --TelepathicGrunt
                        this.pieces, // The list that will be populated with the jigsaw pieces after this method. --TelepathicGrunt
                        this.random,
                        false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                        // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way. --TelepathicGrunt
                        true,// Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        // --TelepathicGrunt
                        null // null here == random rotation.
                );
            } catch (Exception e) {
                BrassAmberBattleTowers.LOGGER.warn(this.pieces.get(this.pieces.size() - 1).toString());
            }


            if (biomeIn.getBiomeCategory() == Biome.Category.SWAMP || biomeIn.getBiomeCategory() == Biome.Category.JUNGLE) {
                //add code here to start the spawning of the overgrown add-on
                BTJigsawManager.addPieces(
                        dynamicRegistryManager,
                        new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                                .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "land_tower/start_pool_overgrown")),
                                7),
                        AbstractVillagePiece::new,
                        chunkGenerator,
                        templateManagerIn,
                        centerPos,
                        this.pieces, // Thankfully passing the same list as above just adds the new pieces at the end of the list. 
                        this.random,
                        false,
                        true,
                        this.pieces.get(0).getRotation() // pass in the rotation of the base piece of the tower
                        // This makes the overgrown tower correctly overlap the base tower. 
                );
                BTJigsawManager.addPieces(
                        dynamicRegistryManager,
                        new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                                .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "land_tower/start_pool_overgrown_roof")),
                                1),
                        AbstractVillagePiece::new,
                        chunkGenerator,
                        templateManagerIn,
                        centerPos.above(88),
                        this.pieces,
                        this.random,
                        false,
                        true,
                        this.pieces.get(0).getRotation()
                );
            }
            else if (biomeIn.getBiomeCategory() == Biome.Category.DESERT) {

            }
            else if (biomeIn.getPrecipitation() == Biome.RainType.SNOW) {

            }

            // Right here, you can do interesting stuff with the pieces in this.pieces such as offset the
            // center piece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
            // only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
            // holds just the piece's size and positions. Blocks will be placed later in JigsawManager.
            // --TelepathicGrunt

            // In this case, we do `piece.offset` down by 4 to overcome problems with slight hills
            this.pieces.forEach(piece -> piece.move(0, -3, 0));
            this.pieces.forEach(piece -> piece.getBoundingBox().y0 -= 3);
            this.pieces.forEach(piece -> piece.getBoundingBox().y1 -= 3);
            // Sets the bounds of the structure once you are finished. --TelepathicGrunt
            this.calculateBoundingBox();

            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Land Battle Tower at " + this.pieces.get(0).getBoundingBox().x0 + " "
                    + this.pieces.get(0).getBoundingBox().y0 + " " + this.pieces.get(0).getBoundingBox().z0);
        }

    }
}
