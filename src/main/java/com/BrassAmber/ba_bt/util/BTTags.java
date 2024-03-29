package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Predicate;

public class BTTags {

    public static class Biomes {

        public static final TagKey<Biome> BT_LAND = createBT("has_structure/land_tower_biomes");
        public static final TagKey<Biome> BT_LAND_JUNGLE =  createBT("has_structure/overgrown_land_tower_biomes");
        public static final TagKey<Biome> BT_LAND_DESERT =  createBT("has_structure/sandy_land_tower_biomes");

        public static final TagKey<Biome> TERRALITH_LAND = createBT("has_structure/modded_biomes/terralith_land");
        public static final TagKey<Biome> TERRALITH_LAND_JUNGLE = createBT("has_structure/modded_biomes/terralith_land_overgrown");
        public static final TagKey<Biome> TERRALITH_LAND_DESERT = createBT("has_structure/modded_biomes/terralith_land_sandy");

        public static final TagKey<Biome> BIOMESOP_LAND = createBT("has_structure/modded_biomes/bop_land");
        public static final TagKey<Biome> BIOMESOP_LAND_JUNGLE = createBT("has_structure/modded_biomes/bop_land_overgrown");
        public static final TagKey<Biome> BIOMESOP_LAND_DESERT = createBT("has_structure/modded_biomes/bop_land_sandy");

        public static final TagKey<Biome> BIOMESYG_LAND = createBT("has_structure/modded_biomes/byg_land");
        public static final TagKey<Biome> BIOMESYG_LAND_JUNGLE = createBT("has_structure/modded_biomes/byg_land_overgrown");
        public static final TagKey<Biome> BIOMESYG_LAND_DESERT = createBT("has_structure/modded_biomes/byg_land_sandy");


        public static Predicate<Holder<Biome>> LAND = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BT_LAND).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> LAND_OVERGROWN = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BT_LAND_JUNGLE).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> LAND_SANDY = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BT_LAND_DESERT).contains(adjustBiome(p_211672_));

        public static Predicate<Holder<Biome>> TERRA_LAND = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(TERRALITH_LAND).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> TERRA_LAND_OVERGROWN = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(TERRALITH_LAND_JUNGLE).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> TERRA_LAND_SANDY = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(TERRALITH_LAND_DESERT).contains(adjustBiome(p_211672_));

        public static Predicate<Holder<Biome>> BOP_LAND = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BIOMESOP_LAND).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> BOP_LAND_OVERGROWN = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BIOMESOP_LAND_JUNGLE).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> BOP_LAND_SANDY = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BIOMESOP_LAND_DESERT).contains(adjustBiome(p_211672_));

        public static Predicate<Holder<Biome>> BYG_LAND = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BIOMESYG_LAND).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> BYG_LAND_OVERGROWN = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BIOMESYG_LAND_JUNGLE).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> BYG_LAND_SANDY = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BIOMESYG_LAND_DESERT).contains(adjustBiome(p_211672_));

        protected static Holder<Biome> adjustBiome(Holder<Biome> p_204385_) {
            return p_204385_;
        }

        private static TagKey<Biome> createBT(String name) {
            return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BrassAmberBattleTowers.MOD_ID, name));
        }
    }
}
