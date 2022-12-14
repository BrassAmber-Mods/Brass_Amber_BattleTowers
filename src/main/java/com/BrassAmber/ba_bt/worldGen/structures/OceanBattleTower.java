package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.util.BTStatics;
import com.BrassAmber.ba_bt.util.GolemType;
import com.BrassAmber.ba_bt.worldGen.BTOceanJigsawPlacement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
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

import static com.BrassAmber.ba_bt.BrassAmberBattleTowers.SAVETOWERS;
import static com.BrassAmber.ba_bt.util.BTUtil.chunkDistanceTo;
import static com.BrassAmber.ba_bt.util.SaveTowers.towers;


// Comments from TelepathicGrunts

public class OceanBattleTower extends StructureFeature<JigsawConfiguration> {


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


    public static BlockPos isSpawnableChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context,
                                            ChunkPos chunkPos, ChunkGenerator chunkGen) {

        int seaLevel = chunkGen.getSeaLevel();
        Predicate<Holder<Biome>> predicate = context.validBiome();

        if (chunkGen.hasFeatureChunkInRange(BuiltinStructureSets.OCEAN_MONUMENTS, context.seed(), chunkPos.x, chunkPos.z, 6)) {
            // BrassAmberBattleTowers.LOGGER.info("Has " + BuiltinStructureSets.OCEAN_MONUMENTS + " Feature in range");
            return BlockPos.ZERO;
        }

        // Test/Check 3 by 3 square of chunks for possible spawns
        List<ChunkPos> testable = new ArrayList<>(
                List.of(
                        new ChunkPos(chunkPos.x + 4, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x + 3, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z + 4),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z + 4),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x - 4, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x - 4, chunkPos.z - 2),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z - 4),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z - 4),
                        new ChunkPos(chunkPos.x + 3, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x + 4, chunkPos.z - 2),

                        new ChunkPos(chunkPos.x + 3, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z + 3),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z + 2),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x - 3, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 2, chunkPos.z - 2),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z - 3),
                        new ChunkPos(chunkPos.x + 2, chunkPos.z - 2),
                        new ChunkPos(chunkPos.x + 3, chunkPos.z - 1)
                )
        );

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = chunkGen.getNoiseBiome(QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel), QuartPos.fromBlock(pos.getMiddleBlockZ()));

            if (!predicate.test(biome)) {
                // BrassAmberBattleTowers.LOGGER.info("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
                return BlockPos.ZERO;
            }
        }
        return chunkPos.getMiddleBlockPosition(seaLevel - 12);
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        // Check if the spot is valid for our structure. This is just as another method for cleanness.
        // Returning an empty optional tells the game to skip this spot as it will not generate the structure. -- TelepathicGrunt

        Predicate<Holder<Biome>> predicate = context.validBiome();

        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator chunkGen = context.chunkGenerator();

        BlockPos chunkCenter = chunkPos.getMiddleBlockPosition(0);
        int x = chunkCenter.getX();
        int z = chunkCenter.getZ();
        int y =  chunkGen.getFirstFreeHeight(chunkCenter.getX(), chunkCenter.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

        Holder<Biome> biome = chunkGen.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z));

        BlockPos spawnPos;
        if (!predicate.test(biome)) {
            return Optional.empty();
            // BrassAmberBattleTowers.LOGGER.info("Incorrect Biome: " + biome);

        }

        Optional<PieceGenerator<JigsawConfiguration>> piecesGenerator;
        int firstTowerDistance = BattleTowersConfig.firstTowerDistance.get();
        int minimumSeparation = BattleTowersConfig.oceanMinimumSeperation.get();
        int seperationRange = BattleTowersConfig.oceanAverageSeperationModifier.get();

        boolean firstTowerDistanceCheck = chunkDistanceTo(ChunkPos.ZERO, chunkPos) >= firstTowerDistance;
        if (!firstTowerDistanceCheck) {
            // BrassAmberBattleTowers.LOGGER.info("Ocean Distance Does Not exceed First Tower Distance in config");
            return Optional.empty();
        }
        // BrassAmberBattleTowers.LOGGER.info("current distance " + (int) Mth.absMax(chunkPos.x, chunkPos.z) + "  config distance " + BattleTowersConfig.firstTowerDistance.get());

        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(context.seed(), chunkPos.x, chunkPos.z);

        int nextSeperation =  minimumSeparation + worldgenRandom.nextInt(seperationRange * 2);
        int closestDistance = 2000;

        if (!towers.get(1).isEmpty()) {
            for (ChunkPos towerPos: towers.get(1)) {
                int distance = chunkDistanceTo(chunkPos, towerPos);
                closestDistance = Math.min(closestDistance, distance);
                // BrassAmberBattleTowers.LOGGER.info("Tower distance from generation try:" + distance);
            }
        }

        if (closestDistance <= nextSeperation) {
            // BrassAmberBattleTowers.LOGGER.info("Ocean at " + closestDistance + " not outside tower separation of " + nextSeperation);
            return Optional.empty();
        }

        /*BrassAmberBattleTowers.LOGGER.info("Biome correct: " + biome.unwrapKey()
                    + " Block: " + chunkCenter.atY(context.chunkGenerator().getFirstFreeHeight(
                            chunkCenter.getX(), chunkCenter.getZ(),
                    Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor()
                ))
            );**/

        spawnPos = isSpawnableChunk(context, chunkPos, chunkGen);

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
                SAVETOWERS.addTower(chunkPos, "Ocean_Towers");
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
