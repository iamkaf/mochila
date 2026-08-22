package com.iamkaf.mochila.compat.trinkets;

//? if <26.1
import dev.emi.trinkets.api.TrinketsApi;
//? if >=26.1
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

public final class TrinketsCompat {
    private TrinketsCompat() {
    }

    public static Optional<ItemStack> findEquippedItem(ServerPlayer player, Predicate<ItemStack> predicate) {
        //? if <26.1 {
        return TrinketsApi.getTrinketComponent(player)
                .flatMap(component -> component.getEquipped(predicate).stream()
                        .map(equipped -> equipped.getB())
                        .findFirst());
        //?} else {
        /*return Optional.ofNullable(TrinketsApi.getAttachment(player))
                .flatMap(attachment -> attachment.findFirst(predicate))
                .map(slot -> slot.get());*/
        //?}
    }

    public static boolean equipForDebug(ServerPlayer player, ItemStack stack) {
        //? if <26.1 {
        return TrinketsApi.getTrinketComponent(player)
                .map(component -> component.getInventory().getOrDefault("chest", java.util.Map.of()).get("back"))
                .filter(java.util.Objects::nonNull)
                .map(inventory -> {
                    inventory.setItem(0, stack);
                    return true;
                })
                .orElse(false);
        //?} else {
        /*var attachment = TrinketsApi.getAttachment(player);
        return attachment != null && attachment.getSlotAccess("chest/back", 0).set(stack);*/
        //?}
    }

}
