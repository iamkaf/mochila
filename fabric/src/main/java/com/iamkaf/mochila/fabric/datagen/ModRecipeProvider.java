package com.iamkaf.mochila.fabric.datagen;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.CustomRecipes;
import com.iamkaf.mochila.compat.VanillaRegistries;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.recipe.BackpackColoring;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
//? if >=26.1 {
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//?} else if >=1.19.3 {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
*///?} else {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
*///?}
//? if >=1.19 {
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
//?} else {
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
*///?}
//? if >=26.3 {
/*import net.minecraft.advancements.Advancement;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;
*///?}
//? if >=1.20.5
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
//? if >=1.21.2
import org.jetbrains.annotations.NotNull;

//? if >=1.20.5
import java.util.concurrent.CompletableFuture;
//? if <1.20.2
/*import java.util.function.Consumer;*/

//? if >=1.21.2 {
public class ModRecipeProvider extends RecipeProvider {
    //? if >=26.3 {
    /*private ModRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements);
    }
    *///?} else {
    protected ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }
    //?}

    @Override
    public void buildRecipes() {
        writeRecipes(output);
    }
//?}
//? if >=1.20.5 && <1.21.2 {
/*public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        writeRecipes(output);
    }
*///?}
//? if >=1.20.2 && <1.20.5 {
/*public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        writeRecipes(output);
    }
*///?}
//? if >=1.19.3 && <1.20.2 {
/*public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> output) {
        writeRecipes(output);
    }
*///?}
//? if <1.19.3 {
/*public class ModRecipeProvider extends FabricRecipesProvider {
    public ModRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateRecipes(Consumer<FinishedRecipe> output) {
        writeRecipes(output);
    }
*///?}

    //? if >=1.20.2
    private void writeRecipes(RecipeOutput output) {
    //? if <1.20.2
    /*private void writeRecipes(Consumer<FinishedRecipe> output) {*/
        shapedTool(com.iamkaf.mochila.registry.Items.LEATHER_BACKPACK.get())
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

        shapedTool(com.iamkaf.mochila.registry.Items.ENDER_BACKPACK.get())
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

        CustomRecipes.special(BackpackColoring.INSTANCE, BackpackColoring.SERIALIZER)
                .save(output, Constants.resource("backpack_coloring").toString());
        CustomRecipes.special(BackpackUpgrading.INSTANCE, BackpackUpgrading.SERIALIZER)
                .save(output, Constants.resource("backpack_upgrading").toString());

        netheriteUpgrade(output, BackpackUtils.getBackpackByTierAndColor(BackpackUtils.Tier.DIAMOND, null),
                BackpackUtils.getBackpackByTierAndColor(BackpackUtils.Tier.NETHERITE, null));
        for (DyeColor color : DyeColor.values()) {
            netheriteUpgrade(output, BackpackUtils.getBackpackByTierAndColor(BackpackUtils.Tier.DIAMOND, color),
                    BackpackUtils.getBackpackByTierAndColor(BackpackUtils.Tier.NETHERITE, color));
        }
    }

    private ShapedRecipeBuilder shapedTool(ItemLike result) {
        //? if >=1.21.2
        return shaped(RecipeCategory.TOOLS, result);
        //? if >=1.19.3 && <1.21.2
        /*return ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result);*/
        //? if <1.19.3
        /*return ShapedRecipeBuilder.shaped(result);*/
    }

    //? if >=1.20.2
    private void netheriteUpgrade(RecipeOutput output, Item from, Item to) {
    //? if <1.20.2
    /*private void netheriteUpgrade(Consumer<FinishedRecipe> output, Item from, Item to) {*/
        //? if >=1.20 {
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(from),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                to
        ).unlocks("backpackety", has(Items.NETHERITE_INGOT)).save(output, itemId(to));
        //?} else if >=1.19.3 {
        /*// Smithing templates arrive with Minecraft 1.20. Before that, smithing takes a base and an addition.
        LegacyUpgradeRecipeBuilder.smithing(
                Ingredient.of(from),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                to
        ).unlocks("backpackety", has(Items.NETHERITE_INGOT)).save(output, itemId(to));
        *///?} else {
        /*// Smithing templates arrive with Minecraft 1.20. Before that, smithing takes a base and an addition.
        UpgradeRecipeBuilder.smithing(
                Ingredient.of(from),
                Ingredient.of(Items.NETHERITE_INGOT),
                to
        ).unlocks("backpackety", has(Items.NETHERITE_INGOT)).save(output, itemId(to));
        *///?}
    }

    private static String itemId(Item item) {
        return VanillaRegistries.items().getKey(item).toString();
    }

    //? if >=1.21.2 {
    public static class Runner extends FabricRecipeProvider {
        //? if >=26.1 {
        public Runner(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }
        //?} else {
        /*public Runner(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }
        *///?}

        @Override
        //? if >=26.3 {
        /*protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider registries,
                BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
            return new ModRecipeProvider(recipes, advancements);
        }
        *///?} else {
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider registries,
                @NotNull RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }
        //?}

        @Override
        public @NotNull String getName() {
            return "Mochila Recipes";
        }
    }
    //?}
}
