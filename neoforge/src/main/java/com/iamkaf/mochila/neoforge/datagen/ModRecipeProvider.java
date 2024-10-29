package com.iamkaf.mochila.neoforge.datagen;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.recipe.BackpackColoring;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients,
            RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(
                recipeOutput,
                RecipeSerializer.SMELTING_RECIPE,
                SmeltingRecipe::new,
                pIngredients,
                pCategory,
                pResult,
                pExperience,
                pCookingTIme,
                pGroup,
                "_from_smelting"
        );
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients,
            RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(
                recipeOutput,
                RecipeSerializer.BLASTING_RECIPE,
                BlastingRecipe::new,
                pIngredients,
                pCategory,
                pResult,
                pExperience,
                pCookingTime,
                pGroup,
                "_from_blasting"
        );
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput,
            RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
            List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience,
            int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(
                    Ingredient.of(itemlike),
                    pCategory,
                    pResult,
                    pExperience,
                    pCookingTime,
                    pCookingSerializer,
                    factory
            ).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(
                    recipeOutput,
                    Mochila.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike)
            );
        }
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, com.iamkaf.mochila.registry.Items.LEATHER_BACKPACK.get())
                .pattern("ABA")
                .pattern("ACD")
                .pattern("AEB")
                .define('A', Items.LEATHER)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.CHEST)
                .define('D', Items.STRING)
                .define('E', ItemTags.WOOL)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput);

        SpecialRecipeBuilder.special(BackpackColoring::new).save(recipeOutput, Mochila.resource("backpack_coloring"));
        SpecialRecipeBuilder.special(BackpackUpgrading::new).save(recipeOutput, Mochila.resource("backpack_upgrading"));
    }

    private void simpleFullArmorSetRecipe(String id, Item material, Item helmet, Item chestplate,
            Item leggings, Item boots, RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .pattern("AAA")
                .pattern("A A")
                .define('A', material)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
                .pattern("A A")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', material)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings)
                .pattern("AAA")
                .pattern("A A")
                .pattern("A A")
                .define('A', material)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
                .pattern("A A")
                .pattern("A A")
                .define('A', material)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
    }

    private void simpleFullToolSetRecipe(String id, Item material, Item shovel, Item pickaxe, Item axe,
            Item hoe, Item sword, RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', material)
                .define('B', Items.STICK)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
                .pattern("AAA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', material)
                .define('B', Items.STICK)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
                .pattern("AA ")
                .pattern("AB ")
                .pattern(" B ")
                .define('A', material)
                .define('B', Items.STICK)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
                .pattern("AA ")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', material)
                .define('B', Items.STICK)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', material)
                .define('B', Items.STICK)
                .unlockedBy("has_" + id, has(material))
                .save(recipeOutput);
    }
}
