package com.brass_amber.ba_bt.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import static com.brass_amber.ba_bt.BABattleTowers.locate;

public class BTTags {

    public static class Blocks {

        public static TagKey<Block> BASE_PROTECTED_TAG = createBT("tower_base_cannot_replace");
        public static TagKey<Block> BT_CHESTS = createBT("bt_chests");
        public static TagKey<Block> BT_SPAWNERS = createBT("bt_spawners");
        public static TagKey<Block> BT_CORRITE_BLOCKS = createBT("corrite_blocks");
        public static TagKey<Block> IS_MAGMA_BLOCK = createBT("is_magma_block");

        private static TagKey<Block> createBT(String name) {
            return TagKey.create(Registries.BLOCK, locate(name));
        }
    }

    public static class Structures {

        public static final TagKey<Structure> BATTLE_TOWERS = createBT("battle_towers");

        private static TagKey<Structure> createBT(String name) {
            return TagKey.create(Registries.STRUCTURE, locate(name));
        }
    }

    public static class StructureSets {

        public static final TagKey<StructureSet> LAND_TOWER_AVOID_STRUCTURES = createBT("land_tower_avoid_structures");
        public static final TagKey<StructureSet> OCEAN_TOWER_AVOID_STRUCTURES = createBT("ocean_tower_avoid_structures");
        public static final TagKey<StructureSet> CORE_TOWER_AVOID_STRUCTURES = createBT("core_tower_avoid_structures");

        private static TagKey<StructureSet> createBT(String name) {
            return TagKey.create(Registries.STRUCTURE_SET, locate(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> LAND_TOWER_BIOMES = createTag("has_structure/land_tower_biomes");
        public static final TagKey<Biome> LAND_TOWER_OVERGROWN_BIOMES = createTag("land_tower_overgrown_biomes");
        public static final TagKey<Biome> LAND_TOWER_SANDY_BIOMES = createTag("land_tower_sandy_biomes");
        public static final TagKey<Biome> LAND_TOWER_SNOWY_BIOMES = createTag("land_tower_snowy_biomes");
        public static final TagKey<Biome> OCEAN_TOWER_BIOMES = createTag("has_structure/ocean_tower_biomes");
        public static final TagKey<Biome> CORE_TOWER_BIOMES = createTag("has_structure/core_tower_biomes");
        public static final TagKey<Biome> CORE_TOWER_MOUNTAIN_BIOMES = createTag("core_tower_mountain_biomes");

        private static TagKey<Biome> createTag(String name) {
            return create(locate(name));
        }
        public static TagKey<Biome> create(final ResourceLocation name) {
            return TagKey.create(Registries.BIOME, name);
        }
    }
}
