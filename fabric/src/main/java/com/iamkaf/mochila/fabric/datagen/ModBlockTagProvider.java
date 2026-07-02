package com.iamkaf.mochila.fabric.datagen;

//? if <26.1
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;*/
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//? if <26.1
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;*/
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

//? if >=26.1
public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
//? if <26.1
/*public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {*/
    //? if >=26.1
    public ModBlockTagProvider(FabricPackOutput output,
    //? if <26.1
    /*public ModBlockTagProvider(FabricDataOutput output,*/
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
