package com.brass_amber.ba_bt.worldGen.structures;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructures;
import com.brass_amber.ba_bt.util.BTTags;
import com.brass_amber.ba_bt.util.BTUtil;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class CoreTower extends Structure implements TowerStructure{

    public static final Codec<CoreTower> CODEC = RecordCodecBuilder.<CoreTower>mapCodec(instance ->
            instance.group(
                    Structure.settingsCodec(instance)
            ).apply(instance, CoreTower::new)).codec();

    private int towerType = 0;

    @Override
    public String getTowerName() {
        return "core_tower";
    }

    @Override
    public int getTowerId() {
        return 2; // Tower number (Land = 0, Ocean = 1, etc. )
    }

    @Override
    public String[] getTowerTypeConversion() {
        return new String[]{"normal", "colossal", "ancient"};
    }

    @Override
    public int getTowerType() {
        return this.towerType;
    }

    public CoreTower(StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    public Optional<Structure.GenerationStub> findValidGenerationPoint(Structure.GenerationContext generationContext) {
        return this.findGenerationPoint(generationContext);
    }

    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        WorldgenRandom worldgenRandom = generationContext.random();
        worldgenRandom.setSeed(generationContext.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(chunkPos.getMiddleBlockPosition(0));


        if (this.hasNearbyTower(chunkPos)) {
            // BABTMain.LOGGER.debug("Land not outside tower separation " + nextSeperation);
            return Optional.empty();
        }

        // BABattleTowers.LOGGER.debug("Attempting Land Tower Spawn at " + chunkPos.x + " " + chunkPos.z);

        Pair<Boolean, Integer> canSpawn = isSpawnableChunk(generationContext);
        Rotation rotation = Rotation.getRandom(randomSource);

        if (canSpawn.getFirst()) {
            BlockPos spawnPos = chunkPos.getMiddleBlockPosition(canSpawn.getSecond());

            GenerationStub stub = new GenerationStub(spawnPos, (piecesBuilder) -> {
                this.generatePieces(piecesBuilder, generationContext, spawnPos, rotation);
            });
            saveTower(spawnPos, rotation);
            return Optional.of(stub);
        }

        return Optional.empty();
    }

    @Override
    public Pair<Boolean, Integer> isSpawnableChunk(GenerationContext generationContext) {
        // BABattleTowers.LOGGER.debug("Can Spawn Core");
        ChunkPos chunkPos = generationContext.chunkPos();
        ChunkGenerator chunkGen = generationContext.chunkGenerator();

        int middleHieght = chunkGen.getFirstOccupiedHeight(
                chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()
        );

        Holder<Biome> biome = generationContext.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(chunkPos.getMiddleBlockX()), QuartPos.fromBlock(middleHieght), QuartPos.fromBlock(chunkPos.getMiddleBlockZ()), generationContext.randomState().sampler()
        );

        if (isValidBiome(generationContext, chunkPos.getMiddleBlockPosition(middleHieght), biome)) {
            // BrassAmberBattleTowers.LOGGER.debug("Bad Biome for Ocean: " + biome.unwrapKey() + " " + pos);
            return Pair.of(true, middleHieght);
        }

        return Pair.of(false, 0);
    }

    @Override
    public boolean isValidBiome(GenerationContext context, BlockPos blockpos, Holder<Biome> biomeHolder) {
        // BABattleTowers.LOGGER.debug("Is Valid Core Tower Biome");
        HolderSet<Biome> holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BiomeTags.IS_OCEAN).orElseThrow();
        Predicate<Holder<Biome>> predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> oceanBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), context.chunkGenerator().getSeaLevel(), blockpos.getZ(), 77, predicate, context.random(), context.randomState().sampler());

        holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BTTags.Biomes.CORE_TOWER_MOUNTAIN_BIOMES).orElseThrow();
        predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> mountainBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), context.chunkGenerator().getSeaLevel() + 20, blockpos.getZ(), 400, predicate, context.random(), context.randomState().sampler());

        holderset = context.registryAccess().registryOrThrow(Registries.BIOME).getTag(BiomeTags.HAS_ANCIENT_CITY).orElseThrow();
        predicate = holderset::contains;
        Pair<BlockPos, Holder<Biome>> sculkBiomeNearby = context.chunkGenerator().getBiomeSource().findBiomeHorizontal(blockpos.getX(), -60, blockpos.getZ(), 64, predicate, context.random(), context.randomState().sampler());


        WorldgenRandom worldgenRandom = context.random();
        worldgenRandom.setSeed(context.seed());
        RandomSource randomSource = worldgenRandom.forkPositional().at(blockpos);

        if (randomSource.nextFloat() < 25) {
            // Gilded or Island
            this.towerType = randomSource.nextFloat() > .8 ? 1 : 0;
        }
        BABattleTowers.LOGGER.debug(
                "Is Valid Core Tower Biome? {} distance {}, {} distance {}, {} distance {}",
                oceanBiomeNearby != null ? oceanBiomeNearby.getSecond().unwrapKey().get().location().getPath() : "none", oceanBiomeNearby != null ? BTUtil.distanceTo2DInt(blockpos, oceanBiomeNearby.getFirst()) : 0,
                mountainBiomeNearby != null ? mountainBiomeNearby.getSecond().unwrapKey().get().location().getPath() : "none", mountainBiomeNearby != null ? BTUtil.distanceTo2DInt(blockpos, mountainBiomeNearby.getFirst()) : 0,
                sculkBiomeNearby != null ? sculkBiomeNearby.getSecond().unwrapKey().get().location().getPath() : "none", sculkBiomeNearby != null ? BTUtil.distanceTo2DInt(blockpos, sculkBiomeNearby.getFirst()) : 0);

        return context.validBiome().test(biomeHolder) && oceanBiomeNearby == null && mountainBiomeNearby != null && sculkBiomeNearby == null;
    }

    @Override
    public StructureType<?> type() {
        return BTStructures.CORE_TOWER.get();
    }
}
