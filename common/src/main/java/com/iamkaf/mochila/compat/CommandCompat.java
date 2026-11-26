package com.iamkaf.mochila.compat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
//? if >=1.21.11 {
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
//?} else {
/*import net.minecraft.commands.Commands;
*///?}
//? if >=1.19 {
import net.minecraft.commands.CommandBuildContext;
//?} else {
/*import net.minecraft.core.RegistryAccess;
*///?}

/**
 * Command building and feedback. Item arguments gained a build context in 1.19, {@code sendSuccess} took a supplier
 * in 1.20, permissions became objects in 1.21.11, and {@code ItemInput} dropped a parameter in 26.1.
 */
public final class CommandCompat {
    private CommandCompat() {
    }

    //? if >=1.19 {
    public static ItemArgument itemArgument(CommandBuildContext context) {
        return ItemArgument.item(context);
    }
    //?} else {
    /*public static ItemArgument itemArgument(RegistryAccess registries) {
        return ItemArgument.item();
    }
    *///?}

    public static void success(CommandSourceStack source, Component message) {
        //? if >=1.20 {
        source.sendSuccess(() -> message, false);
        //?} else {
        /*source.sendSuccess(message, false);
        *///?}
    }

    public static boolean isOwner(CommandSourceStack source) {
        //? if >=1.21.11 {
        return source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS));
        //?} else {
        /*return source.hasPermission(Commands.LEVEL_OWNERS);
        *///?}
    }

    public static ItemStack stackOf(ItemInput input) throws CommandSyntaxException {
        //? if >=26.1 {
        return input.createItemStack(1);
        //?} else {
        /*return input.createItemStack(1, false);
        *///?}
    }
}
