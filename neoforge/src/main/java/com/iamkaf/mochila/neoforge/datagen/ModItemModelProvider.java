package com.iamkaf.mochila.neoforge.datagen;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.registry.Items;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Mochila.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

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

                singleTexture(
                        itemName,
                        ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated"),
                        "layer0",
                        modLoc("item/backpack" + i)
                );

                i++;
            }
        }

        basicItem(Items.ENDER_BACKPACK.get());
    }

    public void buttonItem(String id, String baseBlockId) {
        this.withExistingParent(id, mcLoc("block/button_inventory"))
                .texture("texture", modLoc("block/" + baseBlockId));
    }

    public void fenceItem(String id, String baseBlockId) {
        this.withExistingParent(id, mcLoc("block/fence_inventory"))
                .texture("texture", modLoc("block/" + baseBlockId));
    }

    public void wallItem(String id, String baseBlockId) {
        this.withExistingParent(id, mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/" + baseBlockId));
    }

    private ItemModelBuilder handheldItem(String id) {
        return this.withExistingParent(modLoc(id).getPath(), ResourceLocation.parse("item/handheld"))
                .texture("layer0", modLoc("item/" + id));
    }
}
