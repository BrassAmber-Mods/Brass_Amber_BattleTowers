package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OceanTower extends TowerStructure {
    @ObjectHolder(registryName = "minecraft:configured_feature", value = "minecraft:freeze_top_layer")
    public static final PlacedFeature freezeTopLayer = null;


    public static final Codec<OceanTower> CODEC = RecordCodecBuilder.<OceanTower>mapCodec(instance ->
            instance.group(TowerStructure.settingsCodec(instance), TowerStructure.extraSettingsCodec()).apply(instance, OceanTower::new)).codec();


    protected OceanTower(StructureSettings structureSettings, BTStructureSettings extraSettings) {
        super(structureSettings, extraSettings);

        this.towerId = 0;
        this.towerName = "ocean_tower";
        this.towerTypeConversion = new String[]{"normal", "gilded", "island"};
    }

    @Override
    protected Pair<Boolean, BlockPos> isSpawnableChunk(GenerationContext generationContext) {
        WorldgenRandom worldgenRandom = generationContext.random();
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();
        int seaLevel = chunkGen.getSeaLevel();
        Predicate<Holder<Biome>> predicate = generationContext.validBiome();

        Pair<BlockPos, Holder<Structure>> pair = chunkGen.findNearestMapStructure(
                SaveTowers.server.getLevel(Level.OVERWORLD), extraSettings.avoidStructures(),
                chunkPos.getMiddleBlockPosition(0),3, false
        );

        if (pair != null) {
            // BrassAmberBattleTowers.LOGGER.info("Has " + set + " Feature in range");
            return Pair.of(false, BlockPos.ZERO);
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

        // BABTMain.LOGGER.info("Rquesting chunks to test: " + testables.toString());

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                    QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel), QuartPos.fromBlock(pos.getMiddleBlockZ()), generationContext.randomState().sampler()
            );

            if (!predicate.test(biome)) {
                // BrassAmberBattleTowers.LOGGER.info("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
                return Pair.of(false, BlockPos.ZERO);
            }
        }
        return Pair.of(true, chunkPos.getMiddleBlockPosition(seaLevel + 18));
    }

    @Override
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Biome biome) {

        if (context.random().nextInt(50) < 15) {
            // Overgrown
            towerType = context.random().nextInt(50) > 30 ? 2 : 1;
        }

        return true;
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.OCEAN_TOWER.get();
    }
}
