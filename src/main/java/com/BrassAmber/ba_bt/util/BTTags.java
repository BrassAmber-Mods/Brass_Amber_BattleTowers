package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Predicate;

public class BTTags {

    public static class Biomes {
        public static final TagKey<Biome> TERRALITH_LAND = createBT("has_structure/terralith_land");
        public static final TagKey<Biome> TERRALITH_LAND_JUNGLE = createBT("has_structure/terralith_land_overgrown");
        public static final TagKey<Biome> TERRALITH_LAND_DESERT = createBT("has_structure/terralith_land_sandy");


        public static Predicate<Holder<Biome>> TERRA_LAND = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(BTTags.Biomes.TERRALITH_LAND).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> TERRA_LAND_OVERGROWN = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(Biomes.TERRALITH_LAND_JUNGLE).contains(adjustBiome(p_211672_));
        public static Predicate<Holder<Biome>> TERRA_LAND_SANDY = (p_211672_) ->  BuiltinRegistries.BIOME.getOrCreateTag(Biomes.TERRALITH_LAND_DESERT).contains(adjustBiome(p_211672_));

        protected static Holder<Biome> adjustBiome(Holder<Biome> p_204385_) {
            return p_204385_;
        }

        private static TagKey<Biome> createBT(String name) {
            return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BrassAmberBattleTowers.MOD_ID, name));
        }

        private static TagKey<Biome> createForge(String name) {
            return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("forge", name));
        }

        private static TagKey<Biome> createMinecraft(String name) {
            return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(name));
        }
    }
}
