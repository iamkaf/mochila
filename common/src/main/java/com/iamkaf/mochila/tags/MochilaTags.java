package com.iamkaf.mochila.tags;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.item.BackpackItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MochilaTags {
    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, Mochila.resource(name));
    }

    private static TagKey<Item> createItemTag(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    private static TagKey<Block> createBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, Mochila.resource(name));
    }

    private static TagKey<Block> createBlockTag(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static class Items {
        public static final TagKey<Item> BACKPACKS = createItemTag("backpacks");
        public static final TagKey<Item> LEATHER_BACKPACKS = createItemTag("leather_backpacks");
        public static final TagKey<Item> IRON_BACKPACKS = createItemTag("iron_backpacks");
        public static final TagKey<Item> GOLD_BACKPACKS = createItemTag("gold_backpacks");
        public static final TagKey<Item> DIAMOND_BACKPACKS = createItemTag("diamond_backpacks");
        public static final TagKey<Item> NETHERITE_BACKPACKS = createItemTag("netherite_backpacks");

        public static TagKey<Item> tagByTier(BackpackItem.Tier tier) {
            return switch (tier) {
                case LEATHER -> LEATHER_BACKPACKS;
                case IRON -> IRON_BACKPACKS;
                case GOLD -> GOLD_BACKPACKS;
                case DIAMOND -> DIAMOND_BACKPACKS;
                case NETHERITE -> NETHERITE_BACKPACKS;
            };
        }
    }
}
