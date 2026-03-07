package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class BTRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BTRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }



    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeOutput) {

        towerChestRecipeBuilder(recipeOutput, BTBlocks.LAND_CHEST.get(), BTItems.LAND_CHEST_SHARD.get());
        golemChestRecipeBuilder(recipeOutput, BTBlocks.LAND_GOLEM_CHEST.get(), BTItems.LAND_CHEST_SHARD.get(), BTItems.LAND_GOLEM_EYE.get());

        towerChestRecipeBuilder(recipeOutput, BTBlocks.OCEAN_CHEST.get(), BTItems.OCEAN_CHEST_SHARD.get());
        golemChestRecipeBuilder(recipeOutput, BTBlocks.OCEAN_GOLEM_CHEST.get(), BTItems.OCEAN_CHEST_SHARD.get(), BTItems.OCEAN_GOLEM_EYE.get());

        towerChestRecipeBuilder(recipeOutput, BTBlocks.CORE_CHEST.get(), BTItems.CORE_CHEST_SHARD.get());
        golemChestRecipeBuilder(recipeOutput, BTBlocks.CORE_GOLEM_CHEST.get(), BTItems.CORE_CHEST_SHARD.get(), BTItems.CORE_GOLEM_EYE.get());

        towerChestRecipeBuilder(recipeOutput, BTBlocks.NETHER_CHEST.get(), BTItems.NETHER_CHEST_SHARD.get());
        golemChestRecipeBuilder(recipeOutput, BTBlocks.NETHER_GOLEM_CHEST.get(), BTItems.NETHER_CHEST_SHARD.get(), BTItems.NETHER_GOLEM_EYE.get());

        towerChestRecipeBuilder(recipeOutput, BTBlocks.END_CHEST.get(), BTItems.END_CHEST_SHARD.get());
        golemChestRecipeBuilder(recipeOutput, BTBlocks.END_GOLEM_CHEST.get(), BTItems.END_CHEST_SHARD.get(), BTItems.END_GOLEM_EYE.get());

        towerChestRecipeBuilder(recipeOutput, BTBlocks.SKY_CHEST.get(), BTItems.SKY_CHEST_SHARD.get());
        golemChestRecipeBuilder(recipeOutput, BTBlocks.SKY_GOLEM_CHEST.get(), BTItems.SKY_CHEST_SHARD.get(), BTItems.SKY_GOLEM_EYE.get());

        oreSmelting(recipeOutput, List.of(BTBlocks.LAND_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.LAND_GOLEM_EYE.get(), 0.20f, 250, "bt_smelt");
        oreSmelting(recipeOutput, List.of(BTBlocks.OCEAN_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.OCEAN_GOLEM_EYE.get(), 0.20f, 250, "bt_smelt");
        oreSmelting(recipeOutput, List.of(BTBlocks.CORE_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.CORE_GOLEM_EYE.get(), 0.20f, 250, "bt_smelt");
        oreSmelting(recipeOutput, List.of(BTBlocks.NETHER_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.NETHER_GOLEM_EYE.get(), 0.20f, 250, "bt_smelt");
        oreSmelting(recipeOutput, List.of(BTBlocks.END_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.END_GOLEM_EYE.get(), 0.20f, 250, "bt_smelt");
        oreSmelting(recipeOutput, List.of(BTBlocks.SKY_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.SKY_GOLEM_EYE.get(), 0.20f, 250, "bt_smelt");

        oreSmelting(recipeOutput, List.of(BTBlocks.CORRITE_BLOCK.get()), RecipeCategory.MISC, BTItems.CORRITE_ROD.get(), 0.2f, 300, "bt_smelt");
        oreBlasting(recipeOutput, List.of(BTBlocks.CORRITE_BLOCK.get()), RecipeCategory.MISC, BTItems.CORRITE_ROD.get(), 0.2f, 300, "bt_smelt");
        
        shapedRecipeBuilder(
                RecipeCategory.DECORATIONS, BTBlocks.CORRITE_LADDER.get(), 3,
                List.of("A A", "AAA", "A A"),
                List.of(Pair.of('A', BTItems.CORRITE_ROD.get())),
                List.of(BTBlocks.CORRITE_BLOCK.get())
        ).save(recipeOutput);

        shapedRecipeBuilder(
                RecipeCategory.DECORATIONS, BTBlocks.CORRITE_CHISELED_BOOKSHELF.get(), 1,
                List.of("AAA", "BBB", "AAA"),
                List.of(Pair.of('A', BTBlocks.CORRITE_BLOCK.get()), Pair.of('B', BTBlocks.CORRITE_SLAB.get())),
                List.of(BTBlocks.CORRITE_BLOCK.get())
        ).save(recipeOutput);
        
        shapedRecipeBuilder(
                RecipeCategory.TOOLS, BTItems.CORREYE.get(), 1,
                List.of("ACR", "CER", "ACR"),
                List.of(
                        Pair.of('A', BTBlocks.ACTIVE_CORRITE_BLOCK.get()), Pair.of('C', BTBlocks.CORE_MATTER.get()), 
                        Pair.of('E', BTItems.CORRITE_ROD.get()), Pair.of('R', BTItems.CORE_GOLEM_EYE.get())
                ),
                List.of(BTBlocks.CORE_MATTER.get(), BTItems.CORE_GOLEM_EYE.get())
        ).save(recipeOutput);
        
        
        slabBuilder(BTBlocks.CORRITE_SLAB.get(), BTBlocks.CORRITE_BLOCK.get());
        stairBuilder(BTBlocks.CORRITE_STAIR.get(), BTBlocks.CORRITE_BLOCK.get());
        wallBuilder(BTBlocks.CORRITE_WALL.get(), BTBlocks.CORRITE_BLOCK.get());

        slabBuilder(BTBlocks.ACTIVE_CORRITE_SLAB.get(), BTBlocks.ACTIVE_CORRITE_BLOCK.get());
        stairBuilder(BTBlocks.ACTIVE_CORRITE_STAIR.get(), BTBlocks.ACTIVE_CORRITE_BLOCK.get());
        wallBuilder(BTBlocks.ACTIVE_CORRITE_WALL.get(), BTBlocks.ACTIVE_CORRITE_BLOCK.get());

        slabBuilder(BTBlocks.CORE_MATTER_SLAB.get(), BTBlocks.CORE_MATTER.get());
        stairBuilder(BTBlocks.CORE_MATTER_STAIR.get(), BTBlocks.CORE_MATTER.get());
        wallBuilder(BTBlocks.CORE_MATTER_WALL.get(), BTBlocks.CORE_MATTER.get());
    }
    
    protected static ShapedRecipeBuilder slabBuilder(ItemLike result, ItemLike block) {
        return shapedRecipeBuilder(
                RecipeCategory.BUILDING_BLOCKS, result, 6,
                "###",
                Pair.of('#', block)
        );
    }

    protected static ShapedRecipeBuilder stairBuilder(ItemLike result, ItemLike block) {
        return shapedRecipeBuilder(
                RecipeCategory.DECORATIONS, result, 8,
                List.of("A  ", "AA ", "AAA"),
                List.of(Pair.of('A', block)),
                List.of(block)
        );
    }

    protected static ShapedRecipeBuilder wallBuilder(ItemLike result, ItemLike block) {
        return shapedRecipeBuilder(
                RecipeCategory.DECORATIONS, result, 6,
                List.of("AAA", "AAA"),
                List.of(Pair.of('A', block)),
                List.of(block)
        );
    }

    protected static void towerChestRecipeBuilder(Consumer<FinishedRecipe> pRecipeOutput, ItemLike pchest, ItemLike pMaterial) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pchest)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', pMaterial)
                .unlockedBy("has_chest_shard", has(pMaterial))
                .save(pRecipeOutput);
    }

    protected static void golemChestRecipeBuilder(Consumer<FinishedRecipe> pRecipeOutput, ItemLike pchest, ItemLike pMaterial, ItemLike pCenterMaterial) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pchest)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', pMaterial)
                .define('B', pCenterMaterial)
                .unlockedBy("has_chest_shard", has(pMaterial))
                .unlockedBy("has_golem_eye", has(pCenterMaterial))
                .save(pRecipeOutput);
    }

    protected static ShapedRecipeBuilder shapedRecipeBuilder(RecipeCategory category, ItemLike result, int count, String pattern, Pair<Character, ItemLike> patternMaterial) {
        return ShapedRecipeBuilder.shaped(category, result, count)
                .pattern(pattern)
                .define(patternMaterial.getFirst(), patternMaterial.getSecond())
                .unlockedBy(getHasName(patternMaterial.getSecond()), has(patternMaterial.getSecond()));
    }

    protected static ShapedRecipeBuilder shapedRecipeBuilder(RecipeCategory category, ItemLike result, int count, List<String> patterns, List<Pair<Character, ItemLike>> patternMaterials, List<ItemLike> unlockedBy) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(category, result, count);
        for (int i = 0; i < patterns.size(); i++) {
            Pair<Character, ItemLike> patternMaterial = patternMaterials.get(i);
            builder = builder.pattern(patterns.get(i))
                    .define(patternMaterial.getFirst(), patternMaterial.getSecond());
        }
        for (ItemLike unlock: unlockedBy) {
            builder = builder.unlockedBy(getHasName(unlock), has(unlock));
        }
        return builder;
    }

    protected static String getHasName(ItemLike pItemLike) {
        return "has_" + getItemName(pItemLike);
    }

    protected static String getItemName(ItemLike pItemLike) {
        return ForgeRegistries.ITEMS.getKey(pItemLike.asItem()).getPath();
    }
    
    
}
