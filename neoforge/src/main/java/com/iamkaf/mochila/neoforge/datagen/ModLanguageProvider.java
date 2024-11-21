package com.iamkaf.mochila.neoforge.datagen;

import com.iamkaf.mochila.Mochila;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModLanguageProvider extends LanguageProvider {

    private static final List<String> MATERIALS = List.of("leather", "iron", "gold", "diamond", "netherite");
    private static final List<String> COLORS = List.of(
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

    public ModLanguageProvider(PackOutput output) {
        super(output, Mochila.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("creativetab.mochila.mochila", "Mochila");
        add("key.categories.mochila", "Mochila");
        add("key.mochila.open_backpack", "Open Backpack");
        add("tag.item.mochila.backpacks", "Backpacks");
        add("tag.item.mochila.leather_backpacks", "Leather Backpacks");
        add("tag.item.mochila.iron_backpacks", "Iron Backpacks");
        add("tag.item.mochila.gold_backpacks", "Gold Backpacks");
        add("tag.item.mochila.diamond_backpacks", "Diamond Backpacks");
        add("tag.item.mochila.netherite_backpacks", "Netherite Backpacks");
        addBackpackTranslations();
        add("item.mochila.ender_backpack", "Ender Backpack");
    }

    private void addBackpackTranslations() {
        for (String color : COLORS) {
            for (String material : MATERIALS) {
                String itemName = formatItemName(color, material);
                String key = formatTranslationKey(color, material);
                add(String.format("item.%s.%s", Mochila.MOD_ID, key), itemName);
            }
        }
    }

    private String formatItemName(String color, String material) {
        String formattedMaterial = StringUtils.capitalize(material) + " Backpack";

        if (color.isEmpty()) {
            return formattedMaterial;
        }

        String formattedColor =
                Arrays.stream(color.split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));

        return formattedColor + " " + formattedMaterial;
    }

    private String formatTranslationKey(String color, String material) {
        return color.isEmpty() ? material + "_backpack" : color + "_" + material + "_backpack";
    }
}
