package com.iamkaf.mochila.platform;

import com.iamkaf.mochila.compat.trinkets.TrinketsCompat;
import com.iamkaf.mochila.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Optional<ItemStack> findEquippedItem(ServerPlayer player, Predicate<ItemStack> predicate) {
        return isModLoaded("trinkets") ? TrinketsCompat.findEquippedItem(player, predicate) : Optional.empty();
    }

    @Override
    public boolean equipAccessoryForDebug(ServerPlayer player, String backend, ItemStack stack) {
        return backend.equals("trinkets")
                && isModLoaded("trinkets")
                && TrinketsCompat.equipForDebug(player, stack);
    }

}
