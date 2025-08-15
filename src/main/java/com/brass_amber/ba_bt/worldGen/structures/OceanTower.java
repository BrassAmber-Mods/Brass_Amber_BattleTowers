package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.BTTags;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.ArrayList;
import java.util.List;

public class OceanTower extends TowerStructure {

    public static final Codec<OceanTower> CODEC = RecordCodecBuilder.<OceanTower>mapCodec(instance ->
            instance.group(TowerStructure.settingsCodec(instance), TowerStructure.extraSettingsCodec()).apply(instance, OceanTower::new)).codec();


    public OceanTower(StructureSettings structureSettings, BTStructureSettings extraSettings) {
        super(structureSettings, extraSettings);

        this.towerId = 1;
        this.towerName = "ocean_tower";
        this.towerTypeConversion = new String[]{"normal", "gilded", "island"};

    }

    @Override
    protected Pair<Boolean, BlockPos> isSpawnableChunk(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();
        int seaLevel = chunkGen.getSeaLevel();
        HolderSet.Named<Structure> avoidStructures = SaveTowers.server.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(BTTags.Structures.OCEAN_TOWER_AVOID_STRUCTURES).orElseThrow();

        Pair<BlockPos, Holder<Structure>> pair = chunkGen.findNearestMapStructure(
                SaveTowers.server.getLevel(Level.OVERWORLD), avoidStructures,
                chunkPos.getMiddleBlockPosition(0), extraSettings.minDistanceFromAvoidStructures(), false
        );

        if (pair != null) {
            // BrassAmberBattleTowers.LOGGER.info("Has " + set + " Feature in range");
            return Pair.of(false, BlockPos.ZERO);
        }

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

        // BABTMain.LOGGER.info("Rquesting chunks to test: " + testables.toString());

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                    QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel), QuartPos.fromBlock(pos.getMiddleBlockZ()), generationContext.randomState().sampler()
            );

            if (!isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(seaLevel), biome)) {
                // BrassAmberBattleTowers.LOGGER.info("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
                return Pair.of(false, BlockPos.ZERO);
            }
        }
        return Pair.of(true, chunkPos.getMiddleBlockPosition(seaLevel));
    }

    @Override
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {

        if (context.random().nextFloat() < 25) {
            // Gilded or Island
            towerType = context.random().nextFloat() > .6 ? 2 : 1;
        }

        return context.validBiome().test(biomeHolder);
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.OCEAN_TOWER.get();
    }
}
