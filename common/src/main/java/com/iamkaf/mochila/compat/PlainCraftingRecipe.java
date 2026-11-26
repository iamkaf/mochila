package com.iamkaf.mochila.compat;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
//? if >=1.21 {
import net.minecraft.world.item.crafting.CraftingInput;
//?} else {
/*import net.minecraft.world.inventory.CraftingContainer;

import java.util.ArrayList;
*///?}
//? if >=1.21 && <26.1 {
/*import net.minecraft.core.HolderLookup;
*///?}
//? if >=1.19.3 && <26.1 {
/*import net.minecraft.world.item.crafting.CraftingBookCategory;
*///?}
//? if >=1.19.3 && <1.21 {
/*import net.minecraft.core.RegistryAccess;
*///?}

import java.util.List;

/**
 * A special crafting recipe that works on its ingredients as a plain list. The recipe API changed in 1.19.3, 1.21
 * and 26.1. A recipe knows its id only before 1.21.
 */
public abstract class PlainCraftingRecipe extends CustomRecipe {
    //? if >=26.1 {
    protected PlainCraftingRecipe(Identifier id) {
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return matchesItems(input.items());
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input) {
        return assembleItems(input.items());
    }
    //?} else if >=1.21 {
    /*protected PlainCraftingRecipe(Identifier id) {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return matchesItems(input.items());
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return assembleItems(input.items());
    }
    *///?} else if >=1.19.3 {
    /*protected PlainCraftingRecipe(Identifier id) {
        super(id, CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return matchesItems(items(container));
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, RegistryAccess registries) {
        return assembleItems(items(container));
    }
    *///?} else {
    /*protected PlainCraftingRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return matchesItems(items(container));
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container) {
        return assembleItems(items(container));
    }
    *///?}

    //? if <1.21 {
    /*private static List<ItemStack> items(CraftingContainer container) {
        List<ItemStack> items = new ArrayList<>(container.getContainerSize());
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.add(container.getItem(i));
        }
        return items;
    }
    *///?}

    public abstract boolean matchesItems(List<ItemStack> items);

    public abstract @NotNull ItemStack assembleItems(List<ItemStack> items);
}
