package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.datagen.recipes.BTRecipeCategory;
import com.brass_amber.ba_bt.datagen.recipes.BTShapedRecipeBuilder;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
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

        oreSmelting(recipeOutput, List.of(BTBlocks.LAND_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.LAND_GOLEM_EYE.get(), 0.20f, 250, "bt_Chest");
        oreSmelting(recipeOutput, List.of(BTBlocks.OCEAN_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.OCEAN_GOLEM_EYE.get(), 0.20f, 250, "bt_Chest");
        oreSmelting(recipeOutput, List.of(BTBlocks.CORE_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.CORE_GOLEM_EYE.get(), 0.20f, 250, "bt_Chest");
        oreSmelting(recipeOutput, List.of(BTBlocks.NETHER_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.NETHER_GOLEM_EYE.get(), 0.20f, 250, "bt_Chest");
        oreSmelting(recipeOutput, List.of(BTBlocks.END_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.END_GOLEM_EYE.get(), 0.20f, 250, "bt_Chest");
        oreSmelting(recipeOutput, List.of(BTBlocks.SKY_GOLEM_CHEST.get()), RecipeCategory.MISC, BTItems.SKY_GOLEM_EYE.get(), 0.20f, 250, "bt_Chest");

    }
    
    protected static void towerChestRecipeBuilder (Consumer<FinishedRecipe> pRecipeOutput, ItemLike pchest, ItemLike pMaterial) {
        BTShapedRecipeBuilder.shaped(BTRecipeCategory.BATTLE_TOWERS, pchest)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', pMaterial)
                .unlockedBy("has_chest_shard", has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void golemChestRecipeBuilder (Consumer<FinishedRecipe> pRecipeOutput, ItemLike pchest, ItemLike pMaterial, ItemLike pCenterMaterial) {
        BTShapedRecipeBuilder.shaped(BTRecipeCategory.BATTLE_TOWERS, pchest)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', pMaterial)
                .define('B', pCenterMaterial)
                .unlockedBy("has_chest_shard", has(pMaterial))
                .unlockedBy("has_golem_eye", has(pCenterMaterial))
                .save(pRecipeOutput);
    }
    
    
}
