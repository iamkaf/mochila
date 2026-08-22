package com.iamkaf.mochila.compat.trinkets;

//? if >=26.1 {
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketsApi;
//?}
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

public final class TrinketsCompat {
    private TrinketsCompat() {
    }

    public static Optional<ItemStack> findEquippedItem(ServerPlayer player, Predicate<ItemStack> predicate) {
        //? if >=26.1 {
        TrinketAttachment attachment = TrinketsApi.getAttachment(player);
        return attachment == null
                ? Optional.empty()
                : attachment.findFirst(predicate).map(slot -> slot.get());
        //?} else {
        /*return Optional.empty();*/
        //?}
    }

    public static boolean equipForDebug(ServerPlayer player, ItemStack stack) {
        //? if >=26.1 {
        TrinketAttachment attachment = TrinketsApi.getAttachment(player);
        return attachment != null && attachment.getSlotAccess("chest/back", 0).set(stack);
        //?} else {
        /*return false;*/
        //?}
    }

}
