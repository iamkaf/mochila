package com.iamkaf.mochila.fabric.datagen;

//? if <26.1
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;*/
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//? if <26.1
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;*/
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

//? if >=26.1
public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {
//? if <26.1
/*public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {*/
    //? if >=26.1
    public ModBlockLootTableProvider(FabricPackOutput dataOutput,
    //? if <26.1
    /*public ModBlockLootTableProvider(FabricDataOutput dataOutput,*/
            CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
    }
}
