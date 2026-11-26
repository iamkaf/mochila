package com.iamkaf.mochila.compat;

import com.iamkaf.amber.api.registry.v1.creativetabs.CreativeModeTabRegistry;
import com.iamkaf.amber.api.registry.v1.creativetabs.TabBuilder;
//? if >=1.19.3
import net.minecraft.world.item.CreativeModeTab;

/** Creative tab builders. Tabs sit in a top or bottom row from Minecraft 1.19.3. */
public final class CreativeTabs {
    private CreativeTabs() {
    }

    public static TabBuilder builder(String id) {
        //? if >=1.19.3 {
        return CreativeModeTabRegistry.builder(id).row(CreativeModeTab.Row.TOP);
        //?} else {
        /*return CreativeModeTabRegistry.builder(id);
        *///?}
    }
}
