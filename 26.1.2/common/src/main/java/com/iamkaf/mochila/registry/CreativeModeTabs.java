package com.iamkaf.mochila.registry;

import com.iamkaf.amber.api.registry.v1.creativetabs.CreativeModeTabRegistry;
import com.iamkaf.amber.api.registry.v1.DeferredRegister;
import com.iamkaf.amber.api.registry.v1.RegistrySupplier;
import com.iamkaf.mochila.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeModeTabs {
    private static final List<Supplier<Item>> ITEMS = new ArrayList<>();

    public static final RegistrySupplier<CreativeModeTab> MOCHILA = CreativeModeTabRegistry.register(
            CreativeModeTabRegistry.builder(Constants.MOD_ID)
                    .row(CreativeModeTab.Row.TOP)
                    .column(0)
                    .icon(() -> new ItemStack(Items.LIGHT_BLUE_IRON_BACKPACK.get()))
                    .title(Component.translatable("creativetab." + Constants.MOD_ID + "." + Constants.MOD_ID))
    );

    public static void addItem(Supplier<Item> item) {
        ITEMS.add(item);
    }

    public static void init() {
        ITEMS.forEach(item -> CreativeModeTabRegistry.getTabBuilder(MOCHILA.getId()).addItem(item::get));
    }
}
