package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BTStructureTagProvider extends StructureTagsProvider {

    public BTStructureTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BTTags.Structures.LAND_TOWER_AVOID_STRUCTURES)
                .add(BuiltinStructures.PILLAGER_OUTPOST)
                .add(BuiltinStructures.WOODLAND_MANSION)
                .add(BuiltinStructures.JUNGLE_TEMPLE)
                .add(BuiltinStructures.DESERT_PYRAMID)
                .add(BuiltinStructures.IGLOO)
                .add(BuiltinStructures.SWAMP_HUT)
                .add(BuiltinStructures.VILLAGE_PLAINS)
                .add(BuiltinStructures.VILLAGE_DESERT)
                .add(BuiltinStructures.VILLAGE_SAVANNA)
                .add(BuiltinStructures.VILLAGE_SNOWY)
                .add(BuiltinStructures.VILLAGE_TAIGA)
                .add(BuiltinStructures.RUINED_PORTAL_STANDARD)
                .add(BuiltinStructures.RUINED_PORTAL_DESERT)
                .add(BuiltinStructures.RUINED_PORTAL_JUNGLE)
                .add(BuiltinStructures.RUINED_PORTAL_SWAMP)
                .add(BuiltinStructures.RUINED_PORTAL_MOUNTAIN)
                .add(BuiltinStructures.TRAIL_RUINS);

        this.tag(BTTags.Structures.OCEAN_TOWER_AVOID_STRUCTURES)
                .add(BuiltinStructures.OCEAN_MONUMENT)
                .add(BuiltinStructures.OCEAN_RUIN_COLD)
                .add(BuiltinStructures.OCEAN_RUIN_WARM)
                .add(BuiltinStructures.SHIPWRECK)
                .add(BuiltinStructures.STRONGHOLD);

        this.tag(BTTags.Structures.CORE_TOWER_AVOID_STRUCTURES)
                .add(BuiltinStructures.STRONGHOLD)
                .add(BuiltinStructures.ANCIENT_CITY);
    }
}
