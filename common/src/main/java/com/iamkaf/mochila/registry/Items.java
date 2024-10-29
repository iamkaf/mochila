package com.iamkaf.mochila.registry;

import com.iamkaf.mochila.Mochila;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Mochila.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("mochila_item",
            () -> new Item(new Item.Properties().arch$tab(CreativeModeTabs.MOCHILA))
    );

    public static void init() {
        ITEMS.register();
    }
}