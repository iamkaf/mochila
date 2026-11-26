package com.iamkaf.mochila.compat;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
//? if >=1.20.5
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
//? if >=1.20.5
import java.util.Objects;

/**
 * Item stack comparisons, names and tooltips. {@code isSame} became {@code isSameItem} in 1.20, and custom names and
 * tooltip context became data components in 1.20.5.
 */
public final class Stacks {
    private Stacks() {
    }

    public static boolean isSameItem(ItemStack first, ItemStack second) {
        //? if >=1.20 {
        return ItemStack.isSameItem(first, second);
        //?} else {
        /*return ItemStack.isSame(first, second);
        *///?}
    }

    //? if >=1.20.5 {
    public static void setName(ItemStack stack, Component name) {
        stack.set(DataComponents.CUSTOM_NAME, name);
    }

    public static boolean isNamed(ItemStack stack, Component name) {
        return Objects.equals(stack.get(DataComponents.CUSTOM_NAME), name);
    }

    public static List<Component> tooltipLines(ItemStack stack, Player player) {
        return stack.getTooltipLines(Item.TooltipContext.of(player.level()), player, TooltipFlag.Default.NORMAL);
    }
    //?} else {
    /*public static void setName(ItemStack stack, Component name) {
        stack.setHoverName(name);
    }

    public static boolean isNamed(ItemStack stack, Component name) {
        return stack.hasCustomHoverName() && name.getString().equals(stack.getHoverName().getString());
    }

    public static List<Component> tooltipLines(ItemStack stack, Player player) {
        return stack.getTooltipLines(player, TooltipFlag.Default.NORMAL);
    }
    *///?}
}
