package com.iamkaf.mochila.item.backpack;

import com.iamkaf.mochila.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

public final class BackpackAccess {
    private BackpackAccess() {
    }

    public static Optional<ItemStack> find(ServerPlayer player, Predicate<ItemStack> predicate) {
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (predicate.test(stack)) {
                return Optional.of(stack);
            }
        }
        return Services.PLATFORM.findEquippedItem(player, predicate);
    }
}
