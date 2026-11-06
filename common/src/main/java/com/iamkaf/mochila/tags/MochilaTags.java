package com.iamkaf.mochila.tags;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.VanillaRegistries;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MochilaTags {
    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(VanillaRegistries.itemKey(), Constants.resource(name));
    }

    public static class Items {
        public static final TagKey<Item> BACKPACKS = createItemTag("backpacks");
        public static final TagKey<Item> LEATHER_BACKPACKS = createItemTag("leather_backpacks");
        public static final TagKey<Item> IRON_BACKPACKS = createItemTag("iron_backpacks");
        public static final TagKey<Item> GOLD_BACKPACKS = createItemTag("gold_backpacks");
        public static final TagKey<Item> DIAMOND_BACKPACKS = createItemTag("diamond_backpacks");
        public static final TagKey<Item> NETHERITE_BACKPACKS = createItemTag("netherite_backpacks");

        public static TagKey<Item> tagByTier(BackpackUtils.Tier tier) {
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
