package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.BTStatics;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.util.SaveTowers;
import com.BrassAmber.ba_bt.worldGen.BTOceanJigsawPlacement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import static com.BrassAmber.ba_bt.util.BTStatics.oceanTowerBiomes;
import static com.BrassAmber.ba_bt.util.BTUtil.chunkDistanceTo;


// Comments from TelepathicGrunts

public class OceanBattleTower extends StructureFeature<JigsawConfiguration> {

    public static SaveTowers TOWERS;

    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
            Codec.intRange(0, 40).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
    ).apply(codec, JigsawConfiguration::new));

    public OceanBattleTower() {
        super(CODEC, OceanBattleTower::createPiecesGenerator, OceanBattleTower::afterPlace);
        TOWERS = new SaveTowers("Ocean_Tower");
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
        int newOceanFloorHeight;
        int highestY = 0;
        BlockPos highestBlock = BlockPos.ZERO;
        int minX;
        int minZ;
        int[] sideVals = new int[]{0,15};
        int[] vals = new int[]{3,4,7,8,11,12,15};

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = chunkGen.getNoiseBiome(QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(0), QuartPos.fromBlock(pos.getMiddleBlockX()));
            minX = pos.getMinBlockX();
            minZ = pos.getMinBlockX();
            highestY = 0;

            // Check z vals at x 0 and 15
            for (int x: sideVals) {
                for (int z: vals) {
                    newOceanFloorHeight = chunkGen.getFirstOccupiedHeight(minX + x, minZ + z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor());
                    highestY = Math.max(newOceanFloorHeight, highestY);
                    highestBlock = new BlockPos(minX + x, highestY, minZ + z);
                }
            }

            // Check x vals at z 0 and 15
            for (int z: sideVals) {
                for (int x: vals) {
                    newOceanFloorHeight = chunkGen.getFirstOccupiedHeight(minX + x, minZ + z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor());
                    highestY = Math.max(newOceanFloorHeight, highestY);
                    highestBlock = new BlockPos(minX + x, highestY, minZ + z);
                }
            }

            if (highestY < seaLevel - 8 && context.validBiome().test(biome)) {
                usablePositions.add(pos);
                BrassAmberBattleTowers.LOGGER.info("Highest Ocean floor block height for usable position = " + highestY);
            } else {
                boolean accepatableArea = true;
                for (ResourceKey<Biome> biomeKey: oceanTowerBiomes) {
                    if(biome.is(biomeKey)) {

                        List<ChunkPos> testable2 = new ArrayList<>(
                                List.of(
                                        pos,
                                        new ChunkPos(pos.x, pos.z + 1),
                                        new ChunkPos(pos.x + 1, pos.z),
                                        new ChunkPos(pos.x, pos.z - 1),
                                        new ChunkPos(pos.x - 1, pos.z)
                                )
                        );
                        for (ChunkPos pos2: testable2) {
                            Holder<Biome> biome2 = chunkGen.getNoiseBiome(QuartPos.fromBlock(pos2.getMiddleBlockX()), QuartPos.fromBlock(0), QuartPos.fromBlock(pos2.getMiddleBlockX()));
                            if (!predicate.test(biome2)) {
                                accepatableArea = false;
                                break;
                            }
                        }
                    }
                }
                if (accepatableArea) {
                    usablePositions.add(pos);
                    BrassAmberBattleTowers.LOGGER.info("Correct Biome for : " + " " + biome.unwrapKey());
                }
                else {
                    if (context.validBiome().test(biome)) {
                        BrassAmberBattleTowers.LOGGER.info("Ocean Floor too high: " + highestBlock);
                    }
                    return BlockPos.ZERO;
                }
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

        TOWERS.setSeed(context.seed());

        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator chunkGen = context.chunkGenerator();

        boolean firstTowerDistanceCheck = chunkDistanceTo(ChunkPos.ZERO, chunkPos) >= firstTowerDistance;
        if (!firstTowerDistanceCheck) {
            BrassAmberBattleTowers.LOGGER.info("Ocean Distance Does Not exceed First Tower Distance in config");
            return Optional.empty();
        }
        // BrassAmberBattleTowers.LOGGER.info("current distance " + (int) Mth.absMax(chunkPos.x, chunkPos.z) + "  config distance " + BattleTowersConfig.firstTowerDistance.get());

        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(context.seed(), chunkPos.x, chunkPos.z);


        int nextSeperation =  minimumSeparation + worldgenRandom.nextInt(seperationRange * 2);
        int closestDistance = 2000;

        if (!TOWERS.towers.isEmpty()) {
            for (ChunkPos towerPos: TOWERS.towers) {
                int distance = chunkDistanceTo(chunkPos, towerPos);
                closestDistance = Math.min(closestDistance, distance);
            }
        }

        if (closestDistance <= nextSeperation) {
            // BrassAmberBattleTowers.LOGGER.info("Ocean at " + closestDistance + " not outside tower separation of " + nextSeperation);
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
            // BrassAmberBattleTowers.LOGGER.info("Incorrect Biome: " + biome);
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
                TOWERS.addTower( chunkPos);
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
