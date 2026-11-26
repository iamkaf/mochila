package com.iamkaf.mochila.registry;

import com.iamkaf.amber.api.registry.v1.DeferredRegister;
import com.iamkaf.amber.api.registry.v1.RegistrySupplier;
import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.VanillaRegistries;
import com.iamkaf.mochila.recipe.BackpackColoring;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Constants.MOD_ID, VanillaRegistries.recipeSerializerKey());

    public static final RegistrySupplier<RecipeSerializer<BackpackColoring>> BACKPACK_COLORING_SERIALIZER =
            RECIPE_SERIALIZERS.register(
                    "crafting_special_backpackcoloring",
                    () -> BackpackColoring.SERIALIZER
            );

    public static final RegistrySupplier<RecipeSerializer<BackpackUpgrading>> BACKPACK_UPGRADING_SERIALIZER =
            RECIPE_SERIALIZERS.register(
                    "crafting_special_backpackupgrading",
                    () -> BackpackUpgrading.SERIALIZER
            );

    public static void init() {
        RECIPE_SERIALIZERS.register();
    }
}
