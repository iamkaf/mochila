package com.iamkaf.mochila.neoforge.datagen;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.registry.Items;
import com.iamkaf.mochila.tags.MochilaTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture,
            CompletableFuture<TagLookup<Block>> completableFuture2,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, Mochila.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var backpacks = tag(MochilaTags.Items.BACKPACKS);
        var leather_backpacks = tag(MochilaTags.Items.LEATHER_BACKPACKS);
        var iron_backpacks = tag(MochilaTags.Items.IRON_BACKPACKS);
        var gold_backpacks = tag(MochilaTags.Items.GOLD_BACKPACKS);
        var diamond_backpacks = tag(MochilaTags.Items.DIAMOND_BACKPACKS);
        var netherite_backpacks = tag(MochilaTags.Items.NETHERITE_BACKPACKS);
        for (var item : Items.ITEMS) {
            Mochila.LOGGER.info("Mochila: {}", item);
            backpacks.add(item.get());

            if (item.getId().getPath().contains("leather")) {
                leather_backpacks.add(item.get());
            }
            if (item.getId().getPath().contains("iron")) {
                iron_backpacks.add(item.get());
            }
            if (item.getId().getPath().contains("gold")) {
                gold_backpacks.add(item.get());
            }
            if (item.getId().getPath().contains("diamond")) {
                diamond_backpacks.add(item.get());
            }
            if (item.getId().getPath().contains("netherite")) {
                netherite_backpacks.add(item.get());
            }
        }
    }
}
