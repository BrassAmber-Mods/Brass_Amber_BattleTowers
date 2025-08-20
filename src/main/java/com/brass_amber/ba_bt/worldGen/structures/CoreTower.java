package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.SaveTowers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CoreTower extends TowerStructure {

    public static final Codec<CoreTower> CODEC = RecordCodecBuilder.<CoreTower>mapCodec(instance ->
            instance.group(
                    TowerStructure.settingsCodec(instance),
                    TowerStructure.extraSettingsCodec()
            ).apply(instance, CoreTower::new)).codec();


    private HolderSet<Structure> cityStructures;

    public CoreTower(StructureSettings structureSettings, BTStructureSettings extraSettings) {
        super(structureSettings, extraSettings);

        this.towerId = 2;
        this.towerName = "core_tower";
        this.towerTypeConversion = new String[]{"normal", "colossal", "ancient"};
    }

    @Override
    protected Pair<Boolean, BlockPos> isSpawnableChunk(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();
        int seaLevel = chunkGen.getSeaLevel();

        Pair<BlockPos, Holder<Structure>> pair = chunkGen.findNearestMapStructure(
                SaveTowers.server.overworld(), this.extraSettings.avoidStructures(),
                chunkPos.getMiddleBlockPosition(0), this.extraSettings.minDistanceFromAvoidStructures(), false
        );
        if (pair != null) {
            // BrassAmberBattleTowers.LOGGER.debug("Has " + set + " Feature in range");
            return Pair.of(false, BlockPos.ZERO);
        }

        // Test/Check 4 by 4 square of chunks for mountains next to the sea
        List<ChunkPos> testable = new ArrayList<>(
                List.of(
                        chunkPos,
                        new ChunkPos(chunkPos.x + 1, chunkPos.z + 1),
                        new ChunkPos(chunkPos.x + 1, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z - 1),
                        new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)
                )
        );

        // BABTMain.LOGGER.debug("Requesting chunks to test: " + testables.toString());

        for (ChunkPos pos : testable) {
            Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                    QuartPos.fromBlock(pos.getMiddleBlockX()), QuartPos.fromBlock(seaLevel-20), QuartPos.fromBlock(pos.getMiddleBlockZ()), generationContext.randomState().sampler()
            );

            if (!isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(seaLevel), biome)) {
                // BrassAmberBattleTowers.LOGGER.debug("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
                return Pair.of(false, BlockPos.ZERO);
            }
        }

        pair = chunkGen.findNearestMapStructure(
                SaveTowers.server.overworld(), HolderSet.direct(generationContext.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolder(BuiltinStructures.ANCIENT_CITY).get()),
                chunkPos.getMiddleBlockPosition(0), 10, false
        );
        if (pair != null) {
            this.towerType = 2;
        }

        return Pair.of(true, chunkPos.getMiddleBlockPosition(0));
    }

    @Override
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {
        HolderSet<Biome> holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BiomeTags.IS_OCEAN).orElseThrow();
        Predicate<Holder<Biome>> predicate = holderset::contains;

        Pair<BlockPos, Holder<Biome>> oceanBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 128, predicate, context.random(), context.randomState().sampler());

        if (context.random().nextFloat() < 25) {
            // Gilded or Island
            this.towerType = context.random().nextFloat() > .8 ? 1 : 0;
        }

        return context.validBiome().test(biomeHolder) && oceanBiomeNearby == null;
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.CORE_TOWER.get();
    }
}
