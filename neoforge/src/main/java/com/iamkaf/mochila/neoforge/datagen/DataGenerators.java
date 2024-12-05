package com.iamkaf.mochila.neoforge.datagen;

import com.iamkaf.mochila.Mochila;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Mochila.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void serverDatagen(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
        BlockTagsProvider blockTagsProvider =
                new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true,
                new ModItemTagProvider(packOutput,
                        lookupProvider,
                        blockTagsProvider.contentsGetter(),
                        existingFileHelper
                )
        );
        generator.addProvider(true, new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new ModLanguageProvider(packOutput));
    }

//    @SubscribeEvent
//    public static void clientDatagen(GatherDataEvent.Client event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput packOutput = generator.getPackOutput();
//        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
//
//        generator.addProvider(true, new ModItemModelProvider(packOutput, existingFileHelper));
//        generator.addProvider(true, new ModLanguageProvider(packOutput));
//    }
}