package com.iamkaf.mochila.compat.curios;

import com.iamkaf.mochila.registry.Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Optional;
import java.util.function.Predicate;

public final class CuriosCompat {
    private static final ICurioItem BACKPACK_CURIO = new ICurioItem() {
    };

    private CuriosCompat() {
    }

    public static void init() {
        Items.ITEMS.forEach(item -> CuriosApi.registerCurio(item.get(), BACKPACK_CURIO));
    }

    public static boolean equipForDebug(ServerPlayer player, ItemStack stack) {
        return CuriosApi.getCuriosInventory(player).map(inventory -> {
            inventory.setEquippedCurio("back", 0, stack);
            return inventory.findFirstCurio(equipped -> ItemStack.isSameItemSameComponents(equipped, stack)).isPresent();
        }).orElse(false);
    }

    public static Optional<ItemStack> findEquippedItem(ServerPlayer player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inventory -> inventory.findFirstCurio(predicate))
                .map(result -> result.stack());
    }
}
