package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructures;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Predicate;

public class CoreTower extends TowerStructure {

    public static final Codec<CoreTower> CODEC = RecordCodecBuilder.<CoreTower>mapCodec(instance ->
            instance.group(
                    TowerStructure.settingsCodec(instance),
                    TowerStructure.extraSettingsCodec()
            ).apply(instance, CoreTower::new)).codec();


    public CoreTower(StructureSettings structureSettings, BTStructureSettings extraSettings) {
        super(structureSettings, extraSettings);

        this.towerId = 2;
        this.towerName = "core_tower";
        this.towerTypeConversion = new String[]{"normal", "colossal", "ancient"};
    }

    @Override
    protected Pair<Boolean, Integer> isSpawnableChunk(GenerationContext generationContext) {
        BABattleTowers.LOGGER.debug("Can Spawn Core");
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();


        int middleHieght = chunkGen.getFirstOccupiedHeight(
                chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()
        );

        Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(chunkPos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(chunkPos.getMiddleBlockZ()), generationContext.randomState().sampler()
        );

        if (!isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(middleHieght), biome)) {
            // BrassAmberBattleTowers.LOGGER.debug("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
            return Pair.of(false, 0);
        }


        return Pair.of(true, -60);
    }

    @Override
    protected boolean isValidBiome(GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {
        BABattleTowers.LOGGER.debug("Is Valid Core Tower Biome");
        HolderSet<Biome> holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BiomeTags.IS_OCEAN).orElseThrow();
        Predicate<Holder<Biome>> predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> oceanBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 128, predicate, context.random(), context.randomState().sampler());

        WorldgenRandom worldgenRandom = context.random();
        worldgenRandom.setSeed(context.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(blockpos);

        if (randomSource.nextFloat() < 25) {
            // Gilded or Island
            this.towerType = randomSource.nextFloat() > .8 ? 1 : 0;
        }

        return context.validBiome().test(biomeHolder) && oceanBiomeNearby == null;
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.CORE_TOWER.get();
    }
}
