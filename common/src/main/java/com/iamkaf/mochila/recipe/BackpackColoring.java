package com.iamkaf.mochila.recipe;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.BackpackData;
import com.iamkaf.mochila.compat.CustomRecipes;
import com.iamkaf.mochila.compat.Dyes;
import com.iamkaf.mochila.compat.PlainCraftingRecipe;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.registry.RecipeSerializers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Colors a backpack copying its contents and tier.
 */
public class BackpackColoring extends PlainCraftingRecipe {
    public static final BackpackColoring INSTANCE = new BackpackColoring(Constants.resource("backpack_coloring"));
    public static final RecipeSerializer<BackpackColoring> SERIALIZER =
            CustomRecipes.serializer(INSTANCE, BackpackColoring::new);

    public BackpackColoring(Identifier id) {
        super(id);
    }

    @Override
    public boolean matchesItems(List<ItemStack> items) {
        int backpackCount = 0;
        int dyeCount = 0;

        for (ItemStack itemStack : items) {
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof BackpackItem) {
                    backpackCount++;
                } else {
                    if (!(itemStack.getItem() instanceof DyeItem)) {
                        return false;
                    }

                    dyeCount++;
                }

                if (dyeCount > 1 || backpackCount > 1) {
                    return false;
                }
            }
        }

        return backpackCount == 1 && dyeCount == 1;
    }

    // TODO: add support for coloring higher tiers backpacks
    @Override
    public @NotNull ItemStack assembleItems(List<ItemStack> items) {
        ItemStack backpack = ItemStack.EMPTY;
        DyeColor dyeColor = DyeColor.WHITE;

        for (ItemStack inputStack : items) {
            if (!inputStack.isEmpty()) {
                Item item = inputStack.getItem();
                if (item instanceof BackpackItem) {
                    backpack = inputStack;
                } else if (item instanceof DyeItem) {
                    dyeColor = Dyes.colorOf(inputStack);
                }
            }
        }

        Item item = BackpackUtils.getBackpackByColor(backpack, dyeColor);
        return BackpackData.convert(backpack, item);
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializers.BACKPACK_COLORING_SERIALIZER.get();
    }
}
