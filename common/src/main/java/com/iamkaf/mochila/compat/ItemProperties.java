package com.iamkaf.mochila.compat;

import net.minecraft.world.item.Item;
//? if >=1.21.2 {
import com.iamkaf.mochila.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?} else if <1.19.3 {
/*import net.minecraft.world.item.CreativeModeTab;
*///?}

/** Properties for the mod's single-stack items. Items carry their registry id from 1.21.2. */
public final class ItemProperties {
    private ItemProperties() {
    }

    public static Item.Properties single(String id) {
        //? if >=1.21.2 {
        return new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Constants.resource(id)));
        //?} else if >=1.19.3 {
        /*return new Item.Properties().stacksTo(1);
        *///?} else {
        /*// Minecraft 1.18.2 files recipe advancements under an item's creative tab, and lists only items that have
        // one in Creative search.
        return new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS);
        *///?}
    }
}
