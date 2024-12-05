package com.iamkaf.mochila.compat.emi;

import com.google.common.collect.Lists;
import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.BackpackUtility;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import com.iamkaf.mochila.tags.MochilaTags;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Objects;

public class BackpackUpgradingEmiRecipe extends EmiCraftingRecipe implements EmiRecipe {
    public BackpackUpgradingEmiRecipe(BackpackItem.Tier tier, DyeColor color) {
        super(
                arrangeIngredients(tier, color),
                EmiStack.of(BackpackUtility.getBackpackByTierAndColor(nextTier(tier), color)),
                Mochila.resource("/backpack_upgrading/" + nextTier(tier).toString()
                        .toLowerCase() + "/" + color.toString().toLowerCase()),
                false
        );
    }

    private static List<EmiIngredient> arrangeIngredients(BackpackItem.Tier tier, DyeColor color) {
        List<EmiIngredient> ingredients = Lists.newArrayList();

        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                ingredients.add(EmiIngredient.of(MochilaTags.Items.tagByTier(tier)));
            } else {
                ingredients.add(EmiIngredient.of(Ingredient.of(BackpackUpgrading.getMaterialForTier(Objects.requireNonNull(
                        BackpackUtility.getNextTier(tier))))));
            }
        }

        return ingredients;
    }

    private static BackpackItem.Tier nextTier(BackpackItem.Tier tier) {
        return Objects.requireNonNull(BackpackUtility.getNextTier(tier));
    }
}
