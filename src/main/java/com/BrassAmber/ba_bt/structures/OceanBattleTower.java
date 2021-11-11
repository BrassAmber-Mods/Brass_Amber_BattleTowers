package com.BrassAmber.ba_bt.structures;

import com.BrassAmber.ba_bt.BTJigsawManager;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
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
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;


public class OceanBattleTower extends Structure<NoFeatureConfig> {
    public OceanBattleTower(Codec<NoFeatureConfig> codec) {
        super(codec);
    }
    
    private Biome biomeIn;

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return OceanBattleTower.Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos cornerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);

        // Grab height of land. Will stop at first non-air block.
        int landHeight = chunkGenerator.getFirstOccupiedHeight(cornerOfChunk.getX(), cornerOfChunk.getZ(), Heightmap.Type.OCEAN_FLOOR_WG);

        // Grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
        // In nether, it will be netherrack, lava, and air. End will only be endstone and air. It depends on what block
        // the chunk generator will place for that dimension.
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(cornerOfChunk.getX(), cornerOfChunk.getZ());

        // Combine the column of blocks with land height and you get the top block itself which you can test.
        BlockState topBlock = columnOfBlocks.getBlockState(cornerOfChunk.above(landHeight).above());

        
        // Now we test to make sure our structure is not spawning on water or other fluids.
        BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Ocean Battle Tower at " + topBlock.getFluidState().isSource() + " " + topBlock);
                // We also check that canSpawn returned true and whether it is low enough (50 and below) to spawn the tower.
        return (topBlock.getFluidState().isSource() && landHeight <= 40); //;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

            // Turns the chunk coordinates into actual coordinates we can use
            int x = chunkX * 16;
            int z = chunkZ * 16;

            /*
             * We pass this into addPieces to tell it where to generate the structure.
             * If addPieces's last parameter is true, blockpos's Y value is ignored and the
             * structure will spawn at terrain height instead. Set that parameter to false to
             * force the structure to spawn at blockpos's Y value instead. You got options here!
             */
            int landHeight = chunkGenerator.getFirstOccupiedHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
            BlockPos centerPos = new BlockPos(x, landHeight, z);

            /*
             * If you are doing Nether structures, you'll probably want to spawn your structure on top of ledges.
             * Best way to do that is to use getBaseColumn to grab a column of blocks at the structure's x/z position.
             * Then loop through it and look for land with air above it and set blockpos's Y value to it.
             * Make sure to set the final boolean in JigsawManager.addPieces to false so
             * that the structure spawns at blockpos's y value instead of placing the structure on the Bedrock roof!
             */

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            BTJigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            // The path to the starting Template Pool JSON file to read.
                            //
                            // Note, this is "ba_bt:ocean_tower/start_pool"
                            // the game will automatically look into the following path for the template pool:
                            // "resources/data/ba_bt/worldgen/template_pool/ocean_tower/start_pool.json"

                            .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "ocean_tower/start_pool")),

                            // How many pieces outward from center can a recursive jigsaw structure spawn.
                            // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                            // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                            // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                            5),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this.pieces, // The list that will be populated with the jigsaw pieces after this method.
                    this.random,
                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                    // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                    false);  // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
            // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.


            // **THE FOLLOWING TWO LINES ARE OPTIONAL**
            //
            // Right here, you can do interesting stuff with the pieces in this.pieces such as offset the
            // center piece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
            // only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
            // holds just the piece's size and positions. Blocks will be placed later in JigsawManager.
            //
            BlockPos topPos = centerPos.above(76);
            BTJigsawManager.IPieceFactory factory = AbstractVillagePiece::new;
            VillageConfig config2 = new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                    .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "ocean_tower/start_pool_2")), 1);
            Rotation rotation = this.pieces.get(this.pieces.size() - 1).getRotation();
            JigsawPattern jigsawpattern = config2.startPool().get();
            JigsawPiece jigsawpiece = jigsawpattern.getRandomTemplate(this.random);
            AbstractVillagePiece abstractvillagepiece = factory.create(templateManagerIn, jigsawpiece, topPos, jigsawpiece.getGroundLevelDelta(), rotation, jigsawpiece.getBoundingBox(templateManagerIn, topPos, rotation));
            this.pieces.add(abstractvillagepiece);
            // In this case, we do `piece.offset` to raise pieces up by 1 block so that the house is not right on
            // the surface of water or sunken into land a bit.
            //
            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.pieces.get(this.pieces.size() - 1).toString() + " | " + this.pieces.get(this.pieces.size() - 2).toString());
                    // Then we extend the bounding box down by 1 by doing `piece.getBoundingBox().minY` which will cause the
            // land formed around the structure to be lowered and not cover the doorstep. You can raise the bounding
            // box to force the structure to be buried as well. This bounding box stuff with land is only for structures
            // that you added to Structure.NOISE_AFFECTING_FEATURES field handles adding land around the base of structures.
            this.pieces.forEach(piece -> piece.move(0, 0, 0));
            this.pieces.forEach(piece -> piece.getBoundingBox().y0 -= 0);
            this.pieces.forEach(piece -> piece.getBoundingBox().y1 -= 0);
            // Sets the bounds of the structure once you are finished.
            this.calculateBoundingBox();

            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Ocean Battle Tower at " + this.pieces.get(0).getBoundingBox().x0 + " "
                    + this.pieces.get(0).getBoundingBox().y0 + " " + this.pieces.get(0).getBoundingBox().z0);
        }

    }
}
