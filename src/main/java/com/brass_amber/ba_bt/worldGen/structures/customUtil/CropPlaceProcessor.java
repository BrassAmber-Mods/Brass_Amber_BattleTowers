package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

import static com.brass_amber.ba_bt.init.BTStructureProcessors.CROP_PLACE_PROCESSOR;

public class CropPlaceProcessor extends StructureProcessor {
    public static final Codec<CropPlaceProcessor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("crops").forGetter(cropPlaceProcessor -> cropPlaceProcessor.crops)
            ).apply(instance, CropPlaceProcessor::new)
    );

    private final List<BlockState> crops;

    public CropPlaceProcessor(List<BlockState> crops) {
        this.crops = crops;
    }

    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor pServerLevel, BlockPos pOffset, BlockPos pPos, List<StructureTemplate.StructureBlockInfo> pOriginalBlockInfos, List<StructureTemplate.StructureBlockInfo> pProcessedBlockInfos, StructurePlaceSettings pSettings) {
        List<StructureTemplate.StructureBlockInfo> newInfo = new java.util.ArrayList<>(List.copyOf(pProcessedBlockInfos));
        RandomSource randomsource = pSettings.getRandom(pPos);
        List<BlockPos> posList = pProcessedBlockInfos.stream().filter(structureBlockInfo -> structureBlockInfo.state().is(Blocks.FARMLAND)).map(structureBlockInfo -> structureBlockInfo.pos().above()).toList();
        newInfo.replaceAll(info -> posList.contains(info.pos()) ? new StructureTemplate.StructureBlockInfo(info.pos(), this.crops.get(randomsource.nextInt(this.crops.size())), info.nbt()) : info);

        return newInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return CROP_PLACE_PROCESSOR.get();
    }
}
