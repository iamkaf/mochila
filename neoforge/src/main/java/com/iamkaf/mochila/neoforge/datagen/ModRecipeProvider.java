package com.iamkaf.mochila.neoforge.datagen;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.recipe.BackpackColoring;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        ShapedRecipeBuilder.shaped(
                        registries.lookupOrThrow(BuiltInRegistries.ITEM.key()),
                        RecipeCategory.TOOLS,
                        com.iamkaf.mochila.registry.Items.LEATHER_BACKPACK.get()
                )
                .pattern("ABA")
                .pattern("ACD")
                .pattern("AEB")
                .define('A', Items.LEATHER)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.CHEST)
                .define('D', Items.STRING)
                .define('E', ItemTags.WOOL)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(output);

        ShapedRecipeBuilder.shaped(
                        registries.lookupOrThrow(BuiltInRegistries.ITEM.key()),
                        RecipeCategory.TOOLS,
                        com.iamkaf.mochila.registry.Items.ENDER_BACKPACK.get()
                )
                .pattern("ABA")
                .pattern("ACD")
                .pattern("AEB")
                .define('A', Items.LEATHER)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.ENDER_CHEST)
                .define('D', Items.STRING)
                .define('E', ItemTags.WOOL)
                .unlockedBy("has_eye_of_ender", has(Items.ENDER_EYE))
                .save(output);

        SpecialRecipeBuilder.special(BackpackColoring::new)
                .save(output, Mochila.resource("backpack_coloring").toString());
        SpecialRecipeBuilder.special(BackpackUpgrading::new)
                .save(output, Mochila.resource("backpack_upgrading").toString());
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider registries,
                @NotNull RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public @NotNull String getName() {
            return "Mochila Recipes";
        }
    }
}
