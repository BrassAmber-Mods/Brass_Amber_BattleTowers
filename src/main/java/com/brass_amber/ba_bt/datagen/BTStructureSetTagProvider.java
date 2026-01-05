package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.util.BTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.concurrent.CompletableFuture;

public class BTStructureSetTagProvider extends TagsProvider<StructureSet> {
   /** @deprecated Forge: Use the {@linkplain #BTStructureSetTagProvider(PackOutput, CompletableFuture, String, net.minecraftforge.common.data.ExistingFileHelper) mod id variant} */
   @Deprecated
   public BTStructureSetTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
      super(packOutput, Registries.STRUCTURE_SET, provider);
   }
   public BTStructureSetTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
      super(packOutput, Registries.STRUCTURE_SET, provider, modId, existingFileHelper);
   }

   protected void addTags(HolderLookup.Provider provider) {
      this.tag(BTTags.StructureSets.LAND_TOWER_AVOID_STRUCTURES)
              .add(BuiltinStructureSets.PILLAGER_OUTPOSTS)
              .add(BuiltinStructureSets.WOODLAND_MANSIONS)
              .add(BuiltinStructureSets.JUNGLE_TEMPLES)
              .add(BuiltinStructureSets.DESERT_PYRAMIDS)
              .add(BuiltinStructureSets.IGLOOS)
              .add(BuiltinStructureSets.SWAMP_HUTS)
              .add(BuiltinStructureSets.VILLAGES)
              .add(BuiltinStructureSets.RUINED_PORTALS)
              .add(BuiltinStructureSets.TRAIL_RUINS);

      this.tag(BTTags.StructureSets.OCEAN_TOWER_AVOID_STRUCTURES)
              .add(BuiltinStructureSets.OCEAN_MONUMENTS)
              .add(BuiltinStructureSets.OCEAN_RUINS)
              .add(BuiltinStructureSets.SHIPWRECKS)
              .add(BuiltinStructureSets.STRONGHOLDS);

      this.tag(BTTags.StructureSets.CORE_TOWER_AVOID_STRUCTURES)
              .add(BuiltinStructureSets.STRONGHOLDS)
              .add(BuiltinStructureSets.ANCIENT_CITIES)
              .add(BuiltinStructureSets.MINESHAFTS);
   }
}
