package com.iamkaf.mochila.compat;

import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
//? if >=26.1 {
import com.mojang.serialization.MapCodec;
import net.minecraft.network.codec.StreamCodec;
//?} else if >=1.21.2 {
/*import net.minecraft.world.item.crafting.CustomRecipe;
*///?} else if >=1.19.3 {
/*import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
*///?} else {
/*import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
*///?}

import java.util.function.Function;

/**
 * Serializers and datagen builders for recipes without data. Recipes load under their own id before 1.21, and 26.1
 * serializes a single instance.
 */
public final class CustomRecipes {
    private CustomRecipes() {
    }

    /** A serializer for {@code recipe}. Before 1.21, {@code byId} builds the recipe for the id it loads under. */
    //? if >=26.1 {
    public static <T extends PlainCraftingRecipe> RecipeSerializer<T> serializer(T recipe, Function<Identifier, T> byId) {
        return new RecipeSerializer<>(MapCodec.unit(recipe), StreamCodec.unit(recipe));
    }

    public static <T extends PlainCraftingRecipe> SpecialRecipeBuilder special(T recipe, RecipeSerializer<T> serializer) {
        return SpecialRecipeBuilder.special(() -> recipe);
    }
    //?} else if >=1.21.2 {
    /*public static <T extends PlainCraftingRecipe> RecipeSerializer<T> serializer(T recipe, Function<Identifier, T> byId) {
        return new CustomRecipe.Serializer<>(category -> recipe);
    }

    public static <T extends PlainCraftingRecipe> SpecialRecipeBuilder special(T recipe, RecipeSerializer<T> serializer) {
        return SpecialRecipeBuilder.special(category -> recipe);
    }
    *///?} else if >=1.21 {
    /*public static <T extends PlainCraftingRecipe> RecipeSerializer<T> serializer(T recipe, Function<Identifier, T> byId) {
        return new SimpleCraftingRecipeSerializer<>(category -> recipe);
    }

    public static <T extends PlainCraftingRecipe> SpecialRecipeBuilder special(T recipe, RecipeSerializer<T> serializer) {
        return SpecialRecipeBuilder.special(category -> recipe);
    }
    *///?} else if >=1.19.3 {
    /*public static <T extends PlainCraftingRecipe> RecipeSerializer<T> serializer(T recipe, Function<Identifier, T> byId) {
        return new SimpleCraftingRecipeSerializer<>((id, category) -> byId.apply(id));
    }

    // The builder wants the serializer type that serializer() creates on this line.
    public static <T extends PlainCraftingRecipe> SpecialRecipeBuilder special(T recipe, RecipeSerializer<T> serializer) {
        return SpecialRecipeBuilder.special((SimpleCraftingRecipeSerializer<?>) serializer);
    }
    *///?} else {
    /*public static <T extends PlainCraftingRecipe> RecipeSerializer<T> serializer(T recipe, Function<Identifier, T> byId) {
        return new SimpleRecipeSerializer<>(byId);
    }

    // The builder wants the serializer type that serializer() creates on this line.
    public static <T extends PlainCraftingRecipe> SpecialRecipeBuilder special(T recipe, RecipeSerializer<T> serializer) {
        return SpecialRecipeBuilder.special((SimpleRecipeSerializer<?>) serializer);
    }
    *///?}
}
