package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;
import static com.brass_amber.ba_bt.BrassAmberBattleTowers.SAVETOWERS;


public class LandBattleTower extends BattleTowerStructure {

    public static final Codec<LandBattleTower> CODEC = RecordCodecBuilder.<LandBattleTower>mapCodec(instance ->
            instance.group(LandBattleTower.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("avoid_structures").forGetter(structure -> structure.avoidStructures),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_terra").forGetter(structure -> structure.biomesTerra),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_bop").forGetter(structure -> structure.biomesBOP),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_byg").forGetter(structure -> structure.biomesBYG),

                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_sandy").forGetter(structure -> structure.biomesSandy),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_sandy_terra").forGetter(structure -> structure.biomesSandyTerra),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_sandy_bop").forGetter(structure -> structure.biomesSandyBOP),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_sandy_byg").forGetter(structure -> structure.biomesSandyBYG),

                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_jungle").forGetter(structure -> structure.biomesJungle),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_jungle_terra").forGetter(structure -> structure.biomesJungleTerra),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_jungle_bop").forGetter(structure -> structure.biomesJungleBOP),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_jungle_byg").forGetter(structure -> structure.biomesJungleBYG)
            ).apply(instance, LandBattleTower::new)).codec();

    private final HolderSet<Biome> biomesSandy;
    private final HolderSet<Biome> biomesSandyTerra;
    private final HolderSet<Biome> biomesSandyBOP;
    private final HolderSet<Biome> biomesSandyBYG;

    private final HolderSet<Biome> biomesJungle;
    private final HolderSet<Biome> biomesJungleTerra;
    private final HolderSet<Biome> biomesJungleBOP;
    private final HolderSet<Biome> biomesJungleBYG;

    private boolean watered;

    public LandBattleTower(Structure.StructureSettings config,
                           Holder<StructureTemplatePool> startPool,
                           int size, HolderSet<Structure> avoidStructures,
                           HolderSet<Biome> biomesTerra, HolderSet<Biome> biomesBOP, HolderSet<Biome> biomesBYG,
                           HolderSet<Biome> biomesSandy, HolderSet<Biome> biomesSandyTerra, HolderSet<Biome> biomesSandyBOP, HolderSet<Biome> biomesSandyBYG,
                           HolderSet<Biome> biomesJungle, HolderSet<Biome> biomesJungleTerra, HolderSet<Biome> biomesJungleBOP, HolderSet<Biome> biomesJungleBYG) {
        super(config, startPool, size, avoidStructures, biomesTerra, biomesBOP, biomesBYG);

        this.towerType = 1;
        this.watered = false;
        this.towerName = "Land Tower";

        this.biomesSandy = biomesSandy;
        this.biomesSandyTerra = biomesSandyTerra;
        this.biomesSandyBOP = biomesSandyBOP;
        this.biomesSandyBYG = biomesSandyBYG;

        this.biomesJungle = biomesJungle;
        this.biomesJungleTerra = biomesJungleTerra;
        this.biomesJungleBOP = biomesJungleBOP;
        this.biomesJungleBYG = biomesJungleBYG;
    }

    public BlockPos isSpawnableChunk(Structure.GenerationContext context) {
        WorldgenRandom worldgenRandom = context.random();
        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator chunkGen = context.chunkGenerator();

        // Test/Check 3 by 3 square of chunks for possible spawns
        List<ChunkPos> testables = new ArrayList<>(
                List.of(
                        chunkPos,
                        new ChunkPos(chunkPos.x, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z),
                        new ChunkPos(chunkPos.x, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z)
                )
        );

        List<ChunkPos> usablePositions =  new ArrayList<>();
        ArrayList<Integer> usableHeights = new ArrayList<>();
        ArrayList<Boolean> hasWater = new ArrayList<>();
        ArrayList<Integer> towerTypes = new ArrayList<>();
        int newLandHeight;
        int lowestY;
        int highestY;
        int minX;
        int minZ;
        int newX;
        int newZ;

        for (ChunkPos pos : testables) {
            // BrassAmberBattleTowers.LOGGER.info("Land tower testing at " + pos);
            int middleHieght = chunkGen.getFirstOccupiedHeight(pos.getMiddleBlockX(), pos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(pos.getMiddleBlockZ()), context.randomState().sampler());
            lowestY = 215;
            highestY = 0;
            hasWater.clear();
            minX = pos.getMinBlockX();
            minZ = pos.getMinBlockZ();

            for (int x = 0; x < 6; x++) {
                for (int z = 0; z < 6; z++) {
                    newX = minX + (x*3);
                    newZ = minZ + (z*3);
                    newLandHeight = chunkGen.getFirstOccupiedHeight(newX, newZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

                    lowestY = Math.min(newLandHeight, lowestY);
                    highestY = Math.max(newLandHeight, highestY);

                    // get column of blocks at blockpos.
                    NoiseColumn columnOfBlocks = chunkGen.getBaseColumn(newX, newZ, context.heightAccessor(), context.randomState());
                    // combine the column of blocks with land height, and you get the top block itself which you can test.
                    BlockState topBlock = columnOfBlocks.getBlock(newLandHeight);
                    // check whether the topBlock is a source block of water.
                    if (topBlock == Blocks.WATER.defaultBlockState()) {
                        hasWater.add(Boolean.TRUE);
                    }
                }
            }

            if (highestY > 215) {
                BrassAmberBattleTowers.LOGGER.info("Terrain to high for Land Tower");
                return  BlockPos.ZERO;
            }

            boolean acceptableBiome = acceptableBiome(biome, context);

            // 12 Blocks seem to work well with allowing a good number of small cliff spawns, while removing the mountainside spawns
            boolean isFlat = highestY - lowestY <= 12;

            // 256 blocks in one layer of a chunk, if more than 1/16 is water, avoid.
            this.watered = hasWater.size() >= 16;
            if (this.watered && this.towerType != 1) {
                return BlockPos.ZERO;
            }
            int usableHeight = lowestY + ((highestY - lowestY)/4);



            // BrassAmberBattleTowers.LOGGER.info("flat?: " + isFlat + " water?: " + watered + " usable height: " + usableHeight + " acceptable biome? " + acceptableBiome);

            if (isFlat && acceptableBiome) {
                // BrassAmberBattleTowers.LOGGER.info("Usable position at: " + pos + " " + usableHeight);
                usablePositions.add(pos);
                towerTypes.add(this.towerType);
                usableHeights.add(usableHeight);
            }

        }

        if (usablePositions.size() > 0) {
            int index = worldgenRandom.nextInt(usablePositions.size());
            int landHeight = usableHeights.get(index);
            this.towerType = towerTypes.get(index);
            // BrassAmberBattleTowers.LOGGER.info("Position chosen: " + usablePositions.get(index).getMiddleBlockPosition(landHeight));
            return usablePositions.get(index).getMiddleBlockPosition(landHeight);
        }

        return BlockPos.ZERO;
    }

    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int bbYStart = boundingbox.minY();
        boundingbox.getCenter();

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
        List<BlockState> acceptableBlocks = List.of(
                Blocks.STONE_BRICKS.defaultBlockState(), Blocks.CRACKED_STONE_BRICKS.defaultBlockState(),
                Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), Blocks.CHISELED_STONE_BRICKS.defaultBlockState()
        );

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                blockpos$mutableblockpos.set(x, bbYStart, z);
                // BrassAmberBattleTowers.LOGGER.info("Block at: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if ( acceptableBlocks.contains(worldGenLevel.getBlockState(blockpos$mutableblockpos))) {
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
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof TallGrassBlock
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof FlowerBlock
                        || worldGenLevel.getBlockState(blockpos$mutableblockpos).getBlock() instanceof DeadBushBlock) {
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

    public boolean acceptableBiome(Holder<Biome> biome, Structure.GenerationContext context) {

        // Test base minecraft acceptable biomes
        boolean acceptable= super.acceptableBiome(biome, context);

        if (!acceptable) {
            acceptable = biomesJungle.contains(biome);
            this.towerType = 2;
        }
        if (!acceptable) {
            acceptable = biomesSandy.contains(biome);
            this.towerType = 3;
        }

        // Test Terralith Mod acceptable biomes
        if (terralithBiomeSpawning.get()) {
            if (!acceptable) {
                acceptable = biomesJungleTerra.contains(biome);
                this.towerType = 2;
            }
            if (!acceptable) {
                acceptable = biomesSandyTerra.contains(biome);
                this.towerType = 3;
            }
        }

        // Test Biomes Of Plenty Mod acceptable biomes
        if (biomesOfPlentyBiomeSpawning.get()) {
            if (!acceptable) {
                acceptable = biomesJungleBOP.contains(biome);
                this.towerType = 2;
            }
            if (!acceptable) {
                acceptable = biomesSandyBOP.contains(biome);
                this.towerType = 3;
            }
        }

        if (biomesYoullGoBiomeSpawning.get()) {
            if (!acceptable) {
                acceptable = biomesJungleBYG.contains(biome);
                this.towerType = 2;
            }
            if (!acceptable) {
                acceptable = biomesSandyBYG.contains(biome);
                this.towerType = 3;
            }
        }

        return acceptable;
    }
}
