package com.iamkaf.mochila.registry;

import com.iamkaf.amber.api.registry.v1.DeferredRegister;
import com.iamkaf.amber.api.registry.v1.RegistrySupplier;
import com.iamkaf.mochila.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Constants.MOD_ID, Registries.CREATIVE_MODE_TAB);

//    public static final RegistrySupplier<CreativeModeTab> MOCHILA = TABS.register(
//            Constants.MOD_ID, () -> CreativeTabRegistry.create(
//                    Component.translatable("creativetab." + Constants.MOD_ID + "." + Constants.MOD_ID),
//                    () -> new ItemStack(Items.LIGHT_BLUE_IRON_BACKPACK.get())
//            )
//    );

    public static final RegistrySupplier<CreativeModeTab> MOCHILA = TABS.register(
            Constants.MOD_ID,
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(Items.LIGHT_BLUE_IRON_BACKPACK.get()))
                    .title(Component.translatable("creativetab." + Constants.MOD_ID + "." + Constants.MOD_ID))
                    .build()
    );

    public static void init() {
        TABS.register();
    }
}