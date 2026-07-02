package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructureProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

public class SpawnerMarkerProcessor extends StructureProcessor {

    public static final Codec<SpawnerMarkerProcessor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RuleTest.CODEC.fieldOf("marker_block")
                            .forGetter(spawnerMarkerProcessor -> spawnerMarkerProcessor.markerBlock),
                    BlockState.CODEC.fieldOf("spawner_block")
                            .forGetter(spawnerMarkerProcessor -> spawnerMarkerProcessor.spawnerState),
                    Codec.intRange(0, 15).fieldOf("spawner_amount")
                            .forGetter(spawnerMarkerProcessor -> spawnerMarkerProcessor.spawnerAmount)
            ).apply(instance, SpawnerMarkerProcessor::new)
    );


    private final RuleTest markerBlock;
    private final BlockState spawnerState;
    private final int spawnerAmount;
    private int spawnerCount;

    public SpawnerMarkerProcessor(RuleTest markerBlock, BlockState spawnerState, int maxSpawners) {
        this.markerBlock = markerBlock;
        this.spawnerState = spawnerState;
        this.spawnerAmount = maxSpawners;
        this.spawnerCount = 0;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor pServerLevel, BlockPos pOffset, BlockPos pPos, List<StructureTemplate.StructureBlockInfo> pOriginalBlockInfos, List<StructureTemplate.StructureBlockInfo> pProcessedBlockInfos, StructurePlaceSettings pSettings) {
        List<StructureTemplate.StructureBlockInfo> newInfo = new java.util.ArrayList<>(List.copyOf(pProcessedBlockInfos));
        RandomSource randomsource = pSettings.getRandom(pPos);

        for (int i = 0; i < pProcessedBlockInfos.size(); i++) {
            StructureTemplate.StructureBlockInfo info = pProcessedBlockInfos.get(i);
            if (this.markerBlock.test(info.state(), randomsource) && this.spawnerCount < this.spawnerAmount) {
                newInfo.set(i, new StructureTemplate.StructureBlockInfo(info.pos(), this.spawnerState, info.nbt()));
                this.spawnerCount++;
            }
        }

        if (this.spawnerCount == 0) {
            BABattleTowers.LOGGER.debug("SpawnerMarkerProcessor for piece at {} unable to find blocks to process", pSettings.getBoundingBox());
        }

        return newInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BTStructureProcessors.SPAWNER_MARKER_PROCESSOR.get();
    }
}
