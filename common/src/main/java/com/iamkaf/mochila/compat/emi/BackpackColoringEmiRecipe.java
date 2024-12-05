package com.iamkaf.mochila.compat.emi;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.BackpackUtility;
import com.iamkaf.mochila.tags.MochilaTags;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BackpackColoringEmiRecipe extends EmiCraftingRecipe implements EmiRecipe {
    public BackpackColoringEmiRecipe(BackpackItem.Tier tier, DyeColor color) {
        super(List.of(
                        EmiIngredient.of(Ingredient.of(backpacks(tier))),
                        EmiIngredient.of(Ingredient.of(DyeItem.byColor(color)))
                ),
                EmiStack.of(BackpackUtility.getBackpackByTierAndColor(tier, color)),
                Mochila.resource("/backpack_coloring/" + tier.toString()
                        .toLowerCase() + "/" + color.toString().toLowerCase()),
                true
        );
    }

    private static HolderSet.@NotNull Named<Item> backpacks(BackpackItem.Tier tier) {
        return BuiltInRegistries.ITEM.getOrThrow(MochilaTags.Items.tagByTier(tier));
    }
}
