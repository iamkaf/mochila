package com.iamkaf.mochila.compat;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//? if >=26.1 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.world.item.DyeItem;
*///?}

/** Dyes and their colors. Minecraft 26.1 moved the color into a data component and removed {@code DyeItem.byColor}. */
public final class Dyes {
    private Dyes() {
    }

    //? if >=26.1 {
    public static DyeColor colorOf(ItemStack dye) {
        return dye.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
    }

    public static Item itemOf(DyeColor color) {
        return VanillaRegistries.items().getValue(Identifier.withDefaultNamespace(color.getSerializedName() + "_dye"));
    }
    //?} else {
    /*public static DyeColor colorOf(ItemStack dye) {
        return ((DyeItem) dye.getItem()).getDyeColor();
    }

    public static Item itemOf(DyeColor color) {
        return DyeItem.byColor(color);
    }
    *///?}
}
