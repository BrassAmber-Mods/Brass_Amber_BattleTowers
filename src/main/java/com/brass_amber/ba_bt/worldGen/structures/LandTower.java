package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class LandTower extends TowerStructure {
    @ObjectHolder(registryName = "minecraft:configured_feature", value = "minecraft:freeze_top_layer")
    public static final PlacedFeature freezeTopLayer = null;


    public static final Codec<LandTower> CODEC = RecordCodecBuilder.<LandTower>mapCodec(instance ->
            instance.group(TowerStructure.settingsCodec(instance), TowerStructure.extraSettingsCodec()).apply(instance, LandTower::new)).codec();


    protected LandTower(StructureSettings structureSettings, BTStructureSettings extraSettings) {
        super(structureSettings, extraSettings);

        this.towerId = 0;
        this.towerName = "land_tower";
        this.towerTypeConversion = new String[]{"normal", "overgrown", "sandy", "icy", "ruined"};
    }

    @Override
    protected Pair<Boolean, BlockPos> isSpawnableChunk(GenerationContext generationContext) {
        WorldgenRandom worldgenRandom = generationContext.random();
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();

        Pair<BlockPos, Holder<Structure>> pair = chunkGen.findNearestMapStructure(
                SaveTowers.server.getLevel(Level.OVERWORLD), extraSettings.avoidStructures(),
                chunkPos.getMiddleBlockPosition(0),3, false
        );

        if (pair != null) {
            // BrassAmberBattleTowers.LOGGER.info("Has " + set + " Feature in range");
            return Pair.of(false, BlockPos.ZERO);
        }
        // Test/Check 3 by 3 square of chunks for possible spawns (x pattern)
        List<ChunkPos> testables = new ArrayList<>(
                List.of(
                        chunkPos,
                        new ChunkPos(chunkPos.x + 1, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)
                )
        );

        // BABTMain.LOGGER.info("Rquesting chunks to test: " + testables.toString());

        List<ChunkPos> usablePositions =  new ArrayList<>();
        ArrayList<Integer> usableHeights = new ArrayList<>();
        ArrayList<Boolean> hasLiquid = new ArrayList<>();
        ArrayList<Integer> towerTypes = new ArrayList<>();

        int newLandHeight;
        int lowestY;
        int highestY;
        int minX;
        int minZ;
        int newX;
        int newZ;
        boolean watered;

        for (ChunkPos pos : testables) {
            // BrassAmberBattleTowers.LOGGER.info("Land tower testing at " + pos);
            int middleHieght = chunkGen.getFirstOccupiedHeight(
                    pos.getMiddleBlockX(), pos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()
            );
            Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                    QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(pos.getMiddleBlockZ()), generationContext.randomState().sampler()
            );

            // re-check biome for extra chunks skipping to next chunk if not valid
            if (!isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(middleHieght), biome.get())) {
                continue;
            }

            lowestY = 215;
            highestY = 0;
            hasLiquid.clear();
            minX = pos.getMinBlockX();
            minZ = pos.getMinBlockZ();

            for (int x = 0; x < 6; x++) {
                for (int z = 0; z < 6; z++) {
                    newX = minX + (x*3);
                    newZ = minZ + (z*3);
                    newLandHeight = chunkGen.getFirstOccupiedHeight(newX, newZ, Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState());

                    lowestY = Math.min(newLandHeight, lowestY);
                    highestY = Math.max(newLandHeight, highestY);

                    // get column of blocks at blockpos.
                    NoiseColumn columnOfBlocks = chunkGen.getBaseColumn(newX, newZ, generationContext.heightAccessor(), generationContext.randomState());
                    // combine the column of blocks with land height, and you get the top block itself which you can test.
                    BlockState topBlock = columnOfBlocks.getBlock(newLandHeight);
                    // check whether the topBlock is a source block of water.
                    if (topBlock.getBlock() instanceof LiquidBlock) {
                        hasLiquid.add(Boolean.TRUE);
                    }
                }
            }

            if (highestY > 215) {
                BABTMain.LOGGER.info("Terrain to high for Land Tower");
                continue;
            }

            // 12 Blocks seem to work well with allowing a good number of small cliff spawns, while removing the mountainside spawns
            boolean isFlat = highestY - lowestY <= 12;

            // 256 blocks in one layer of a chunk, if more than 1/16 is water, avoid.
            watered = hasLiquid.size() >= 16;
            if (watered && this.towerType != 1) {
                return Pair.of(false, BlockPos.ZERO);
            }
            int usableHeight = lowestY + ((highestY - lowestY)/4);

            // BrassAmberBattleTowers.LOGGER.info("flat?: " + isFlat + " water?: " + watered + " usable height: " + usableHeight);

            if (isFlat) {
                // BrassAmberBattleTowers.LOGGER.info("Usable position at: " + pos + " " + usableHeight);
                usablePositions.add(pos);
                towerTypes.add(this.towerType);
                usableHeights.add(usableHeight);
            }

        }

        // Get a random usable position from the list, otherwise return false
        if (!usablePositions.isEmpty()) {
            int i = worldgenRandom.nextInt(usablePositions.size());
            this.towerType = towerTypes.get(i);
            // BrassAmberBattleTowers.LOGGER.info("Position chosen: " + usablePositions.get(i).getMiddleBlockPosition(usableHeights.get(i));
            return Pair.of(true, usablePositions.get(i).getMiddleBlockPosition(usableHeights.get(i)));
        }

        return Pair.of(false, BlockPos.ZERO);
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        super.afterPlace(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);

        // After Place is called for every chunk that the structure occupies.
        this.afterPlaceCount++;
        BoundingBox boundingbox = piecesContainer.calculateBoundingBox();
        int bbYStart = boundingbox.minY();

        BlockPos chunckCenter = chunkPos.getMiddleBlockPosition(bbYStart);

        // BrassAmberBattleTowers.LOGGER.info("Post Processing: In chunk: " + chunkPos + " " + chunckCenter);

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        blockpos$mutableblockpos.setY(bbYStart);
        // get start and end postions for x/z, using min/max to account for the MinBlock being -25 and the MaxBlock being -27
        int startX = chunckCenter.getX() - 15;
        int endX = chunckCenter.getX() + 15;
        // BrassAmberBattleTowers.LOGGER.info("X start: " + startX + " end: " + endX);

        int startZ = chunckCenter.getZ() - 15;
        int endZ = chunckCenter.getZ() + 15;
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
                if (acceptableBlocks.contains(worldGenLevel.getBlockState(blockpos$mutableblockpos))) {
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

        List<BlockState> acceptableDirtBlocks = List.of(
                Blocks.DIRT.defaultBlockState(), Blocks.DIRT_PATH.defaultBlockState(),
                Blocks.COARSE_DIRT.defaultBlockState(), Blocks.ROOTED_DIRT.defaultBlockState(),
                Blocks.GRAVEL.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()
        );
        List<BlockState> acceptableStoneBlocks = List.of(
                Blocks.STONE.defaultBlockState(),  Blocks.IRON_ORE.defaultBlockState(),
                Blocks.COAL_ORE.defaultBlockState(), Blocks.ANDESITE.defaultBlockState(),
                Blocks.GRANITE.defaultBlockState(), Blocks.DIORITE.defaultBlockState()
        );

        startPositions.clear();

        BlockState state;

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                blockpos$mutableblockpos.set(x, bbYStart + 3, z);
                state = worldGenLevel.getBlockState(blockpos$mutableblockpos);
                // BrassAmberBattleTowers.LOGGER.info("Block at: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (acceptableDirtBlocks.contains(state) || acceptableStoneBlocks.contains(state)) {
                    // BrassAmberBattleTowers.LOGGER.info("Block is acceptable: " + blockpos$mutableblockpos + " "+ worldGenLevel.getBlockState(blockpos$mutableblockpos));
                    startPositions.add(new BlockPos(x, bbYStart + 2, z));
                }
            }
        }


        for (BlockPos startPos: startPositions) {
            for (int y = startPos.getY(); y > worldGenLevel.getMinBuildHeight() ; y--) {
                blockpos$mutableblockpos.set(startPos.getX(), y, startPos.getZ());
                state = worldGenLevel.getBlockState(blockpos$mutableblockpos.above());

                // BrassAmberBattleTowers.LOGGER.info("Block to check: " + blockpos$mutableblockpos + " is: " + worldGenLevel.getBlockState(blockpos$mutableblockpos));
                if (worldGenLevel.isEmptyBlock(blockpos$mutableblockpos) || worldGenLevel.isWaterAt(blockpos$mutableblockpos)) {
                    if (acceptableDirtBlocks.contains(state) ){
                        worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.DIRT.defaultBlockState(), 2);
                    } else {
                        worldGenLevel.setBlock(blockpos$mutableblockpos, Blocks.STONE.defaultBlockState(), 2);
                    }
                }
            }
        }

        if (afterPlaceCount == 9) {
            BoundingBox topBB = piecesContainer.pieces().get(9).getBoundingBox();
            BlockPos center = topBB.getCenter().atY(topBB.minY()).above(3);
            ServerLevel level = worldGenLevel.getLevel();

            Entity monolith;
            monolith = new BTMonolith(BTEntityType.LAND_MONOLITH.get(), level, center.getX() + .5, center.getY(), center.getZ() + .5, Blocks.CLAY.defaultBlockState());
            worldGenLevel.addFreshEntity(monolith);
            BABTMain.LOGGER.info("Spawned Monolith at " + center);
            this.afterPlaceCount = 0;
        }
    }

    @Override
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Biome biome) {

        boolean coldEnoughToSnow = biome.warmEnoughToRain(blockpos);
        float temperature = biome.getBaseTemperature();
        BlockState topblock = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor(), context.randomState()).getBlock(blockpos.getY());

        if (temperature > 0.8
                && biome.getModifiedClimateSettings().downfall() >= .8
                && biome.hasPrecipitation()
        ) {
            // Overgrown
            this.towerType = 1;
        } else if (temperature > 1.8
                && !biome.hasPrecipitation()
                && topblock.getBlock() instanceof FallingBlock
        ) {
            // Desert
            this.towerType = 2;
        } else if (coldEnoughToSnow
                && biome.hasPrecipitation()
                && biome.getGenerationSettings().hasFeature(freezeTopLayer)
        ) {
            // Snowy
            this.towerType = 3;
        } else {
            // Default || Ruined
            towerType = context.random().nextInt(50) > 7 ? 0 : 4;
        }

        return true;
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.LAND_TOWER.get();
    }
}
