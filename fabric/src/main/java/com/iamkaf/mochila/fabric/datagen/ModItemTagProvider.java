package com.iamkaf.mochila.fabric.datagen;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.tags.MochilaTags;
//? if <26.1
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;*/
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//? if <26.1
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;*/
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

//? if >=26.1
public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
//? if <26.1
/*public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {*/
    //? if >=26.1
    public ModItemTagProvider(FabricPackOutput output,
    //? if <26.1
    /*public ModItemTagProvider(FabricDataOutput output,*/
            CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var backpacks = tagAppender(MochilaTags.Items.BACKPACKS);
        var leather_backpacks = tagAppender(MochilaTags.Items.LEATHER_BACKPACKS);
        var iron_backpacks = tagAppender(MochilaTags.Items.IRON_BACKPACKS);
        var gold_backpacks = tagAppender(MochilaTags.Items.GOLD_BACKPACKS);
        var diamond_backpacks = tagAppender(MochilaTags.Items.DIAMOND_BACKPACKS);
        var netherite_backpacks = tagAppender(MochilaTags.Items.NETHERITE_BACKPACKS);

        for (var item : BuiltInRegistries.ITEM) {
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            if (!id.getNamespace().equals("mochila")) {
                continue;
            }
            Constants.LOG.info("Mochila: {}", item);
            backpacks.add(item);

            if (id.getPath().contains("leather")) {
                leather_backpacks.add(item);
            }
            if (id.getPath().contains("iron")) {
                iron_backpacks.add(item);
            }
            if (id.getPath().contains("gold")) {
                gold_backpacks.add(item);
            }
            if (id.getPath().contains("diamond")) {
                diamond_backpacks.add(item);
            }
            if (id.getPath().contains("netherite")) {
                netherite_backpacks.add(item);
            }
        }
    }

    private ItemTagAppender tagAppender(net.minecraft.tags.TagKey<Item> tag) {
        return new ItemTagAppender(builder(tag));
    }

    private record ItemTagAppender(
            //? if >=26.2
            TagAppender<Item> delegate
            //? if <26.2
            /*TagAppender<ResourceKey<Item>, Item> delegate*/
    ) {
        ItemTagAppender add(Item item) {
            delegate.add(itemKey(item));
            return this;
        }

        private static ResourceKey<Item> itemKey(Item item) {
            return BuiltInRegistries.ITEM.getResourceKey(item)
                    .orElseThrow(() -> new IllegalStateException("Unregistered item " + item));
        }
    }
}
