package com.iamkaf.mochila.compat;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//?}

/** Registries and their keys. Minecraft 1.19.3 moved them out of {@code Registry} into {@code BuiltInRegistries} and {@code Registries}. */
public final class VanillaRegistries {
    private VanillaRegistries() {
    }

    //? if >=1.19.3 {
    public static ResourceKey<Registry<Item>> itemKey() {
        return Registries.ITEM;
    }

    public static ResourceKey<Registry<Block>> blockKey() {
        return Registries.BLOCK;
    }

    public static ResourceKey<Registry<RecipeSerializer<?>>> recipeSerializerKey() {
        return Registries.RECIPE_SERIALIZER;
    }

    public static Registry<Item> items() {
        return BuiltInRegistries.ITEM;
    }

    public static Registry<Block> blocks() {
        return BuiltInRegistries.BLOCK;
    }
    //?} else {
    /*public static ResourceKey<Registry<Item>> itemKey() {
        return Registry.ITEM_REGISTRY;
    }

    public static ResourceKey<Registry<Block>> blockKey() {
        return Registry.BLOCK_REGISTRY;
    }

    public static ResourceKey<Registry<RecipeSerializer<?>>> recipeSerializerKey() {
        return Registry.RECIPE_SERIALIZER_REGISTRY;
    }

    public static Registry<Item> items() {
        return Registry.ITEM;
    }

    public static Registry<Block> blocks() {
        return Registry.BLOCK;
    }
    *///?}

    public static Item item(Identifier id) {
        //? if >=1.21.2 {
        return items().getValue(id);
        //?} else {
        /*return items().get(id);
        *///?}
    }
}
