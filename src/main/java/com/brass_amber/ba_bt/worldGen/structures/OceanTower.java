package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructures;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OceanTower extends Structure implements TowerStructure {

    public static final Codec<OceanTower> CODEC = RecordCodecBuilder.<OceanTower>mapCodec(instance ->
            instance.group(Structure.settingsCodec(instance)).apply(instance, OceanTower::new)).codec();

    private int towerType = 0;

    @Override
    public String getTowerName() {
        return "ocean_tower";
    }

    @Override
    public int getTowerId() {
        return 1; // Tower number (Land = 0, Ocean = 1, etc. )
    }

    @Override
    public String[] getTowerTypeConversion() {
        return new String[]{"normal", "gilded", "island"};
    }

    @Override
    public int getTowerType() {
        return this.towerType;
    }

    public OceanTower(StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    public Optional<Structure.GenerationStub> findValidGenerationPoint(Structure.GenerationContext generationContext) {
        return this.findGenerationPoint(generationContext);
    }

    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();
        WorldgenRandom worldgenRandom = generationContext.random();
        worldgenRandom.setSeed(generationContext.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(chunkPos.getMiddleBlockPosition(0));

        // BABattleTowers.LOGGER.debug("Attempting Land Tower Spawn at " + chunkPos.x + " " + chunkPos.z);

        Pair<Boolean, Integer> canSpawn = isSpawnableChunk(generationContext);

        if (canSpawn.getFirst()) {
            BlockPos spawnPos = chunkPos.getMiddleBlockPosition(canSpawn.getSecond());

            GenerationStub stub = new GenerationStub(spawnPos, (piecesBuilder) -> {
                this.generatePieces(piecesBuilder, generationContext, spawnPos);
            });
            return Optional.of(stub);
        }

        return Optional.empty();
    }

    @Override
    public Pair<Boolean, Integer> isSpawnableChunk(GenerationContext generationContext) {
        // BABattleTowers.LOGGER.debug("Can Spawn Ocean");
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();
        int seaLevel = chunkGen.getSeaLevel();

        // Test/Check 4 by 4 square of chunks for nearby land
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

        // BABTMain.LOGGER.debug("Rquesting chunks to test: " + testables.toString());

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                    QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel), QuartPos.fromBlock(pos.getMiddleBlockZ()), generationContext.randomState().sampler()
            );

            if (!biome.is(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)) {
                return Pair.of(false, 0);
            }
        }

        Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(chunkPos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel), QuartPos.fromBlock(chunkPos.getMiddleBlockZ()), generationContext.randomState().sampler()
        );

        if (isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(seaLevel), biome)) {
            // BrassAmberBattleTowers.LOGGER.debug("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
            return Pair.of(true, seaLevel);
        }

        return Pair.of(false, 0);
    }

    public boolean isValidBiome(Structure.GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {
        // BABattleTowers.LOGGER.debug("Is Valid Ocean Tower Biome");
        WorldgenRandom worldgenRandom = context.random();
        worldgenRandom.setSeed(context.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(blockpos);

        if (randomSource.nextFloat() < 25) {
            // Gilded or Island
            this.towerType = randomSource.nextFloat() > .6 ? 2 : 1;
        }

        return context.validBiome().test(biomeHolder);
    }

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        super.afterPlace(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
        afterPlaceBT(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.OCEAN_TOWER.get();
    }
}
