package com.BrassAmber.ba_bt.worldGen.structures;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTStructures;

import com.BrassAmber.ba_bt.util.SaveTowers;
import com.BrassAmber.ba_bt.worldGen.BTTowerJigsawPlacement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.BrassAmber.ba_bt.BattleTowersConfig.*;
import static com.BrassAmber.ba_bt.BrassAmberBattleTowers.SAVETOWERS;
import static com.BrassAmber.ba_bt.util.BTUtil.chunkDistanceTo;


public class BattleTowerStructure extends Structure {

    public static final Codec<BattleTowerStructure> CODEC = RecordCodecBuilder.<BattleTowerStructure>mapCodec(instance ->
            instance.group(BattleTowerStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("avoid_structures").forGetter(structure -> structure.avoidStructures),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_terra").forGetter(structure -> structure.biomesTerra),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_bop").forGetter(structure -> structure.biomesBOP),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes_byg").forGetter(structure -> structure.biomesBYG)
            ).apply(instance, BattleTowerStructure::new)).codec();

    protected final Holder<StructureTemplatePool> startPool;
    protected final int size;
    protected final HolderSet<Structure> avoidStructures;

    protected final HolderSet<Biome> biomesTerra;
    protected final HolderSet<Biome> biomesBOP;
    protected final HolderSet<Biome> biomesBYG;

    protected String towerName;

    protected final List<Integer> minimumSeperations;
    protected final List<Integer> averageSeperations;
    protected int towerType;

    protected final Boolean buryTower = false;
    protected final Boolean randomBuryDepth = false;

    public BattleTowerStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool,
                                int size, HolderSet<Structure> avoidStructures,
                                HolderSet<Biome> biomesTerra, HolderSet<Biome> biomesBOP, HolderSet<Biome> biomesBYG) {
        super(config);
        this.startPool = startPool;
        this.size = size;
        this.avoidStructures = avoidStructures;

        this.towerName = "Base";

        this.biomesTerra = biomesTerra;
        this.biomesBOP = biomesBOP;
        this.biomesBYG = biomesBYG;

        this.minimumSeperations = List.of(landMinimumSeperation.get(), oceanMinimumSeperation.get());
        this.averageSeperations = List.of(landAverageSeperationModifier.get(), oceanAverageSeperationModifier.get());
        this.towerType = -1;
    }


    public BlockPos isSpawnableChunk(Structure.GenerationContext context) {
        return BlockPos.ZERO;
    }

    @Override
    public @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {

        int firstTowerSeparation = firstTowerDistance.get();
        ChunkPos chunkPos = context.chunkPos();

        // Ensure tower chunk is outside player requested initial spawn range
        boolean firstTowerDistanceCheck = chunkDistanceTo(ChunkPos.ZERO, chunkPos) < firstTowerSeparation;
        if (firstTowerDistanceCheck) {
            return Optional.empty();
        }

        ChunkGenerator chunkGen = context.chunkGenerator();
        int minimumSeparation = this.minimumSeperations.get(this.towerType);
        int seperationRange = this.averageSeperations.get(this.towerType);

        int nextSeperation =  minimumSeparation + context.random().nextInt(seperationRange * 2);
        int closestDistance = 2000;

        // Check for already generated towers
        if (!SaveTowers.towers.get(this.towerType).isEmpty()) {
            for (ChunkPos towerPos: SaveTowers.towers.get(this.towerType)) {
                int distance = chunkDistanceTo(chunkPos, towerPos);
                closestDistance = Math.min(closestDistance, distance);
                // BrassAmberBattleTowers.LOGGER.info("Tower distance from generation try:" + distance);
            }
        }

        if (closestDistance <= nextSeperation) {
            // BrassAmberBattleTowers.LOGGER.info("Land not outside tower separation " + nextSeperation);
            return Optional.empty();
        }

        Pair<BlockPos, Holder<Structure>> pair = chunkGen.findNearestMapStructure(Minecraft.getInstance().level.getServer().overworld(), this.avoidStructures, chunkPos.getMiddleBlockPosition(0),3, false);

        if (pair != null) {
            // BrassAmberBattleTowers.LOGGER.info("Has " + set + " Feature in range");
            return Optional.empty();
        }

        // Check that biome is acceptable
        BlockPos chunkCenter = chunkPos.getMiddleBlockPosition(0);
        int x = chunkCenter.getX();
        int z = chunkCenter.getZ();
        int y =  chunkGen.getFirstFreeHeight(chunkCenter.getX(), chunkCenter.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

        Holder<Biome> biome = chunkGen.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), context.randomState().sampler());

        boolean acceptableBiome = acceptableBiome(biome, context);

        if (!acceptableBiome) {
            return Optional.empty();
        }

        BlockPos spawnPos = isSpawnableChunk(context);

        if (spawnPos.getY() != 0) {

            this.saveTower(spawnPos, chunkPos);

            return BTTowerJigsawPlacement.addPieces(
                    context, // Used for JigsawPlacement to get all the proper behaviors done.
                    this.startPool, // The starting pool to use to create the structure layout from
                    this.size, // How deep a branch of pieces can go away from center piece. (5 means branches cannot be longer than 5 pieces from center piece)
                    spawnPos,  // Where to spawn the structure.
                    this.buryTower, // Should the tower base be buried underground
                    this.randomBuryDepth // Whether the bury depth should be random.
            );
        }
        return Optional.empty();
    }

    // Used for tower saving and logging of tower positions
    public void saveTower(BlockPos spawnPos, ChunkPos chunkPos) {
        BrassAmberBattleTowers.LOGGER.info(this.towerName + " Tower at " + spawnPos);
        SAVETOWERS.addTower(chunkPos, this.towerName.replace(" ", "_"));
    }

    public boolean acceptableBiome(Holder<Biome> biome, Structure.GenerationContext context) {
        boolean acceptable = false;

        // Test base minecraft acceptable biomes
        acceptable = context.validBiome().test(biome);

        // Test Terralith Mod acceptable biomes
        if (terralithBiomeSpawning.get()) {
            acceptable = biomesTerra.contains(biome);
        }

        // Test Biomes Of Plenty Mod acceptable biomes
        if (biomesOfPlentyBiomeSpawning.get()) {
            acceptable = biomesBOP.contains(biome);
        }

        if (biomesYoullGoBiomeSpawning.get()) {
            acceptable = biomesBYG.contains(biome);
        }

        return acceptable;
    }

    public @NotNull StructureType<?> type() {
        return BTStructures.LAND_BATTLE_TOWER.get(); // Helps the game know how to turn this structure back to json to save to chunks
    }
}
