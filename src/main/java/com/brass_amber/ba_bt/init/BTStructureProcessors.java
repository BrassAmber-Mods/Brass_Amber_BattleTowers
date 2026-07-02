package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.worldGen.structures.customUtil.CropPlaceProcessor;
import com.brass_amber.ba_bt.worldGen.structures.customUtil.NearbyBlockMatchProcessor;
import com.brass_amber.ba_bt.worldGen.structures.customUtil.SpawnerMarkerProcessor;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BTStructureProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR_TYPE_REGISTER = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, BABattleTowers.MOD_ID);

    public static final RegistryObject<StructureProcessorType<NearbyBlockMatchProcessor>> NEARBY_BLOCK_MATCH_PROCESSOR = STRUCTURE_PROCESSOR_TYPE_REGISTER.register("nearby_block_match_processor", () -> explicitStructureTypeTyping(NearbyBlockMatchProcessor.CODEC));
    public static final RegistryObject<StructureProcessorType<CropPlaceProcessor>> CROP_PLACE_PROCESSOR = STRUCTURE_PROCESSOR_TYPE_REGISTER.register("crop_place_processor", () -> explicitStructureTypeTyping(CropPlaceProcessor.CODEC));
    public static final RegistryObject<StructureProcessorType<SpawnerMarkerProcessor>> SPAWNER_MARKER_PROCESSOR = STRUCTURE_PROCESSOR_TYPE_REGISTER.register("spawner_marker_processor", () -> explicitStructureTypeTyping(SpawnerMarkerProcessor.CODEC));

    private static <T extends StructureProcessor> StructureProcessorType<T> explicitStructureTypeTyping(Codec<T> structureProcessorTypeCodec) {
        return () -> structureProcessorTypeCodec;
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_PROCESSOR_TYPE_REGISTER.register(eventBus);
    }

}
