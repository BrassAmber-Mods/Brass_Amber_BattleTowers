package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
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
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Biome biome) {

        boolean coldEnoughToSnow = biome.coldEnoughToSnow(blockpos);
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
