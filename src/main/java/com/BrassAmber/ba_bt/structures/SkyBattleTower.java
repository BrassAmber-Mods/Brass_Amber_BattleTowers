package com.BrassAmber.ba_bt.structures;

import com.BrassAmber.ba_bt.BTJigsawManager;
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
import org.apache.logging.log4j.Level;

import java.util.Random;


public class SkyBattleTower extends Structure<NoFeatureConfig> {
    public SkyBattleTower(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return SkyBattleTower.Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);


        // Grab height of land. Will stop at first non-air block.
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        int landHeight2 = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX()-4, centerOfChunk.getZ()-4, Heightmap.Type.WORLD_SURFACE_WG);
        int landHeight3 = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX()-4, centerOfChunk.getZ()+4, Heightmap.Type.WORLD_SURFACE_WG);
        int landHeight4 = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX()+4, centerOfChunk.getZ()+4, Heightmap.Type.WORLD_SURFACE_WG);
        int landHeight5 = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX()+4, centerOfChunk.getZ()-4, Heightmap.Type.WORLD_SURFACE_WG);

        Random rand = new Random();
        int x = rand.nextInt(3);

        BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Sky Battle Tower at " + centerOfChunk.getX() + " "
                + centerOfChunk.getZ() + " " + (landHeight <= 110) + " " + (landHeight2 <= 110) + " "
                + (landHeight3 <= 110) + " " + (landHeight4 <= 110) + " " + (landHeight5 <= 110)  + " " + x);



        // Now we test to make sure our structure is not spawning on Land Towers
        return (landHeight <= 110 && landHeight2 <= 110 && landHeight3 <= 110 && landHeight4 <= 110 && landHeight5 <= 110) && x == 1;
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
            BlockPos centerPos = new BlockPos(x, 143, z);

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            BTJigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            // The path to the starting Template Pool JSON file to read.
                            //
                            // Note, this is "ba_bt:land_tower/start_pool"
                            // the game will automatically look into the following path for the template pool:
                            // "resources/data/ba_bt/worldgen/template_pool/land_tower/start_pool.json"

                            .get(new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "sky_tower/start_pool")),

                            // How many pieces outward from center can a recursive jigsaw structure spawn.
                            // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                            // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                            // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                            10),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this.pieces, // The list that will be populated with the jigsaw pieces after this method.
                    this.random,
                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                    // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                    false, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                    // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                    null // null here == random rotation.
            );

            this.calculateBoundingBox();

            BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, "Sky Battle Tower at " + this.pieces.get(0).getBoundingBox().x0 + " "
                    + this.pieces.get(0).getBoundingBox().y0 + " " + this.pieces.get(0).getBoundingBox().z0);
        }

    }
}
