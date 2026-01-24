package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.brass_amber.ba_bt.init.BTStructureProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.ArrayList;
import java.util.List;

public class NearbyBlockMatchProcessor extends StructureProcessor {
    public static final Codec<NearbyBlockMatchProcessor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RuleTest.CODEC.fieldOf("block_match")
                            .forGetter(nearbyBlockMatchTest -> nearbyBlockMatchTest.blockMatch),
                    ExtraCodecs.nonEmptyList(BlockMatchRule.CODEC.listOf()).fieldOf("block_check_rules")
                            .forGetter(nearbyBlockMatchProcessor -> nearbyBlockMatchProcessor.blockMatchRules),
                    BlockState.CODEC.fieldOf("output_state")
                            .forGetter(nearbyBlockMatchProcessor -> nearbyBlockMatchProcessor.outputState),
                    Codec.floatRange(0f ,1f).optionalFieldOf("placement_chance", 1f)
                            .forGetter(nearbyBlockMatchProcessor -> nearbyBlockMatchProcessor.placementChance)
            ).apply(instance, NearbyBlockMatchProcessor::new)
    );

    private final RuleTest blockMatch;
    private final List<BlockMatchRule> blockMatchRules;
    private final BlockState outputState;
    private final float placementChance;

    public NearbyBlockMatchProcessor(RuleTest blockMatch, List<BlockMatchRule> blockMatchRules, BlockState outputState, float placementChance) {
        this.blockMatch = blockMatch;
        this.blockMatchRules = blockMatchRules;
        this.outputState = outputState;
        this.placementChance = placementChance;
    }


    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor pServerLevel, BlockPos pOffset, BlockPos pPos, List<StructureTemplate.StructureBlockInfo> pOriginalBlockInfos, List<StructureTemplate.StructureBlockInfo> pProcessedBlockInfos, StructurePlaceSettings pSettings) {
        List<StructureTemplate.StructureBlockInfo> newInfo = new java.util.ArrayList<>(List.copyOf(pProcessedBlockInfos));
        RandomSource randomsource = pSettings.getRandom(pPos);

        for (int i = 0; i < pProcessedBlockInfos.size(); i++) {
            StructureTemplate.StructureBlockInfo info = pProcessedBlockInfos.get(i);
            if (!this.blockMatch.test(info.state(), randomsource)) {
                continue;
            }
            int ruleCheck = 0;

            for (BlockMatchRule rule : this.blockMatchRules) {
                List<BlockPos> posList = new ArrayList<>();
                rule.directions.forEach(direction -> posList.add(info.pos().relative(direction, rule.distance)));
                int matches = pProcessedBlockInfos.stream().filter(blockInfo -> posList.contains(blockInfo.pos()) && rule.checkStates.contains(blockInfo.state())).toList().size();
                ruleCheck += (matches >= rule.minMatch && matches <= rule.maxMatch) ? 1 : 0;
            }

            if (ruleCheck == this.blockMatchRules.size() && randomsource.nextFloat() <= this.placementChance) {
                newInfo.set(i, new StructureTemplate.StructureBlockInfo(info.pos(), this.outputState, info.nbt()));
            }
        }

        return newInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BTStructureProcessors.NEARBY_BLOCK_MATCH_PROCESSOR.get();
    }


    public record BlockMatchRule(List<BlockState> checkStates, List<Direction> directions, int minMatch, int maxMatch, int distance) {
        public static final Codec<NearbyBlockMatchProcessor.BlockMatchRule> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("check_states")
                                .forGetter(blockMatchRule -> blockMatchRule.checkStates),
                        ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("direction_check")
                                .forGetter(blockMatchRule -> blockMatchRule.directions),
                        Codec.intRange(1, 6).optionalFieldOf("min_match", 1)
                                .forGetter(blockMatchRule -> blockMatchRule.minMatch),
                        Codec.intRange(1, 6).optionalFieldOf("max_match", 6)
                                .forGetter(blockMatchRule -> blockMatchRule.maxMatch),
                        Codec.INT.optionalFieldOf("distance", 1)
                                .forGetter(blockMatchRule -> blockMatchRule.distance)
                ).apply(instance, BlockMatchRule::new)
        );
    }
}
