package com.iamkaf.mochila.fabric.datagen;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.VanillaRegistries;
import com.iamkaf.mochila.registry.Items;
//? if >=1.21.4 {
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
//?}
//? if >=1.19 && <1.21.4
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;*/
//? if <1.19
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockStateDefinitionProvider;*/
//? if <1.21.4 {
/*import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
*///?}
//? if >=26.1
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//? if <26.1 && >=1.19.3
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;*/
//? if <1.19.3
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;*/
//? if >=26.1
import net.minecraft.client.resources.model.sprite.Material;
//? if >=1.21.4
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.BiConsumer;
//? if >=1.21.4
import java.util.Optional;

//? if >=1.19 {
public class ModModelProvider extends FabricModelProvider {
//?} else {
/*public class ModModelProvider extends FabricBlockStateDefinitionProvider {
*///?}
    //? if >=26.1 {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }
    //?} else if >=1.19.3 {
    /*public ModModelProvider(FabricDataOutput output) {
        super(output);
    }
    *///?} else {
    /*public ModModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }
    *///?}

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        //? if >=1.19 {
        forEachBackpackTexture((theBackpack, textureIndex) -> {
            // this whole part here is a bit of a mess, it's here because the way the textures are
            // named does not match the item ids
            //? if >=1.21.4 {
            ModelTemplate model = new ModelTemplate(
                    Optional.of(Identifier.withDefaultNamespace("item/generated")),
                    Optional.empty(),
                    TextureSlot.LAYER0
            );
            itemModelGenerators.itemModelOutput.accept(
                    theBackpack, ItemModelUtils.plainModel(model.create(
                            theBackpack,
                            //? if >=26.1
                            TextureMapping.layer0(new Material(Constants.resource("item/backpack" + textureIndex))),
                            //? if <26.1
                            /*TextureMapping.layer0(Constants.resource("item/backpack" + textureIndex)),*/
                            itemModelGenerators.modelOutput
                    ))
            );
            //?} else {
            /*ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(theBackpack),
                    TextureMapping.layer0(Constants.resource("item/backpack" + textureIndex)),
                    itemModelGenerators.output
            );
            *///?}
        });
        //?}

        itemModelGenerators.generateFlatItem(Items.ENDER_BACKPACK.get(), ModelTemplates.FLAT_ITEM);
    }

    /** Calls {@code action} with every colored and plain backpack, and the number of its texture. */
    static void forEachBackpackTexture(BiConsumer<Item, Integer> action) {
        List<String> materials = List.of("leather", "iron", "gold", "diamond", "netherite");
        List<String> colors = List.of(
                "",
                "white",
                "light_gray",
                "gray",
                "black",
                "brown",
                "red",
                "yellow",
                "orange",
                "lime",
                "green",
                "cyan",
                "light_blue",
                "blue",
                "purple",
                "magenta",
                "pink"
        );

        int i = 1;
        for (var color : colors) {
            for (var material : materials) {
                String itemName;
                if (!color.isEmpty()) {
                    itemName = String.join("_", color, material, "backpack");
                } else {
                    itemName = String.join("_", material, "backpack");
                }

                action.accept(item(itemName), i);
                i++;
            }
        }
    }

    private static Item item(String name) {
        return VanillaRegistries.item(Constants.resource(name));
    }
}
