package com.iamkaf.mochila.platform;

import com.iamkaf.mochila.compat.curios.CuriosCompat;
import com.iamkaf.mochila.compat.trinkets.TrinketsCompat;
import com.iamkaf.mochila.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Optional<ItemStack> findEquippedItem(ServerPlayer player, Predicate<ItemStack> predicate) {
        Optional<ItemStack> curios = isModLoaded("curios")
                ? CuriosCompat.findEquippedItem(player, predicate)
                : Optional.empty();
        if (curios.isPresent()) {
            return curios;
        }
        return isModLoaded("trinkets_updated")
                ? TrinketsCompat.findEquippedItem(player, predicate)
                : Optional.empty();
    }

    @Override
    public boolean equipAccessoryForDebug(ServerPlayer player, String backend, ItemStack stack) {
        return switch (backend) {
            case "curios" -> isModLoaded("curios") && CuriosCompat.equipForDebug(player, stack);
            case "trinkets" -> isModLoaded("trinkets_updated") && TrinketsCompat.equipForDebug(player, stack);
            default -> false;
        };
    }
}
