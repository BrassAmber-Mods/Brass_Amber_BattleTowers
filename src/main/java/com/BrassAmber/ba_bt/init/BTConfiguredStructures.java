package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BTConfiguredStructures {

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_LAND_BATTLE_TOWER = BTStructures.LAND_BATTLE_TOWER.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));;
    /* public static StructureFeature<?, ?> CONFIGURED_SKY_BATTLE_TOWER = BTStructures
            .SKY_BATTLE_TOWER.get().configured(IFeatureConfig.NONE); */

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BrassAmberBattleTowers.MOD_ID, "bottom_floor_1"), CONFIGURED_LAND_BATTLE_TOWER);

        // Dummy JigsawConfiguration values for now. We will modify the pool at runtime since we cannot get json pool files here at mod init.
        // You can create and register your pools in code, pass in the code create pool here, and delete both newConfig and newContext in RunDownHouseStructure's createPiecesGenerator.
        // Note: JigsawConfiguration only takes 0 - 7 size so that's another reason why we are going to bypass that "codec" by changing size at runtime to get higher sizes.

        /* Ok so, this part may be hard to grasp but basically, just add your structure to this to
         * prevent any sort of crash or issue with other mod's custom ChunkGenerators. If they use
         * FlatGenerationSettings.STRUCTURE_FEATURES in it and you don't add your structure to it, the game
         * could crash later when you attempt to add the StructureSeparationSettings to the dimension.
         *
         * (It would also crash with superflat worldtype if you omit the below line
         * and attempt to add the structure's StructureSeparationSettings to the world)
         *
         * Note: If you want your structure to spawn in superflat, remove the FlatChunkGenerator check
         * in StructureTutorialMain.addDimensionalSpacing and then create a superflat world, exit it,
         * and re-enter it and your structures will be spawning. I could not figure out why it needs
         * the restart but honestly, superflat is really buggy and shouldn't be your main focus in my opinion.
         *
         * Requires AccessTransformer ( see resources/META-INF/accesstransformer.cfg )
         */
        FlatGenerationSettings.STRUCTURE_FEATURES.put(BTStructures.LAND_BATTLE_TOWER.get(), CONFIGURED_LAND_BATTLE_TOWER);
        // FlatGenerationSettings.STRUCTURE_FEATURES.put(BTStructures.SKY_BATTLE_TOWER.get(), CONFIGURED_SKY_BATTLE_TOWER);
    }
}
