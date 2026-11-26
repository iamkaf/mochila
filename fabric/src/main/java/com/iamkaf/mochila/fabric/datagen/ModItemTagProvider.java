package com.iamkaf.mochila.fabric.datagen;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.VanillaRegistries;
import com.iamkaf.mochila.tags.MochilaTags;
//? if >=26.1 {
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
//?} else if >=1.19.3 {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
*///?} else {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
*///?}
//? if >=1.19.3
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

//? if >=1.19.3
import java.util.concurrent.CompletableFuture;

//? if >=26.1 {
public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }
//?} else if >=1.19.3 {
/*public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }
*///?} else {
/*public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }
*///?}

    //? if >=1.19.3 {
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addBackpackTags();
    }
    //?} else {
    /*@Override
    protected void generateTags() {
        addBackpackTags();
    }
    *///?}

    private void addBackpackTags() {
        for (var item : VanillaRegistries.items()) {
            Identifier id = VanillaRegistries.items().getKey(item);
            if (!id.getNamespace().equals("mochila")) {
                continue;
            }
            Constants.LOG.info("Mochila: {}", item);
            add(MochilaTags.Items.BACKPACKS, item);

            if (id.getPath().contains("leather")) {
                add(MochilaTags.Items.LEATHER_BACKPACKS, item);
            }
            if (id.getPath().contains("iron")) {
                add(MochilaTags.Items.IRON_BACKPACKS, item);
            }
            if (id.getPath().contains("gold")) {
                add(MochilaTags.Items.GOLD_BACKPACKS, item);
            }
            if (id.getPath().contains("diamond")) {
                add(MochilaTags.Items.DIAMOND_BACKPACKS, item);
            }
            if (id.getPath().contains("netherite")) {
                add(MochilaTags.Items.NETHERITE_BACKPACKS, item);
            }
        }
    }

    private void add(TagKey<Item> tag, Item item) {
        //? if >=1.21.6 {
        builder(tag).add(VanillaRegistries.items().getResourceKey(item)
                .orElseThrow(() -> new IllegalStateException("Unregistered item " + item)));
        //?} else {
        /*getOrCreateTagBuilder(tag).add(item);
        *///?}
    }
}
