package com.iamkaf.mochila.registry;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.item.BackpackContainer;
import com.iamkaf.mochila.item.BackpackItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Mochila.MOD_ID, Registries.ITEM);

    public static final BackpackContainer.BackpackSize LEATHER_BACKPACK_SIZE = BackpackContainer.BackpackSize.TWO_ROWS;
    public static final BackpackContainer.BackpackSize IRON_BACKPACK_SIZE = BackpackContainer.BackpackSize.THREE_ROWS;
    public static final BackpackContainer.BackpackSize GOLD_BACKPACK_SIZE = BackpackContainer.BackpackSize.FOUR_ROWS;
    public static final BackpackContainer.BackpackSize DIAMOND_BACKPACK_SIZE = BackpackContainer.BackpackSize.FIVE_ROWS;
    public static final BackpackContainer.BackpackSize NETHERITE_BACKPACK_SIZE = BackpackContainer.BackpackSize.SIX_ROWS;

    public static RegistrySupplier<Item> LEATHER_BACKPACK = ITEMS.register("leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> IRON_BACKPACK = ITEMS.register("iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GOLD_BACKPACK = ITEMS.register("gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> DIAMOND_BACKPACK = ITEMS.register("diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> NETHERITE_BACKPACK = ITEMS.register("netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> WHITE_LEATHER_BACKPACK = ITEMS.register("white_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> WHITE_IRON_BACKPACK = ITEMS.register("white_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> WHITE_GOLD_BACKPACK = ITEMS.register("white_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> WHITE_DIAMOND_BACKPACK = ITEMS.register("white_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> WHITE_NETHERITE_BACKPACK = ITEMS.register("white_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> LIGHT_GRAY_LEATHER_BACKPACK = ITEMS.register("light_gray_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_GRAY_IRON_BACKPACK = ITEMS.register("light_gray_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_GRAY_GOLD_BACKPACK = ITEMS.register("light_gray_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_GRAY_DIAMOND_BACKPACK = ITEMS.register("light_gray_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_GRAY_NETHERITE_BACKPACK = ITEMS.register("light_gray_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> GRAY_LEATHER_BACKPACK = ITEMS.register("gray_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GRAY_IRON_BACKPACK = ITEMS.register("gray_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GRAY_GOLD_BACKPACK = ITEMS.register("gray_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GRAY_DIAMOND_BACKPACK = ITEMS.register("gray_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GRAY_NETHERITE_BACKPACK = ITEMS.register("gray_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> BLACK_LEATHER_BACKPACK = ITEMS.register("black_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLACK_IRON_BACKPACK = ITEMS.register("black_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLACK_GOLD_BACKPACK = ITEMS.register("black_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLACK_DIAMOND_BACKPACK = ITEMS.register("black_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLACK_NETHERITE_BACKPACK = ITEMS.register("black_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> BROWN_LEATHER_BACKPACK = ITEMS.register("brown_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BROWN_IRON_BACKPACK = ITEMS.register("brown_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BROWN_GOLD_BACKPACK = ITEMS.register("brown_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BROWN_DIAMOND_BACKPACK = ITEMS.register("brown_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BROWN_NETHERITE_BACKPACK = ITEMS.register("brown_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> RED_LEATHER_BACKPACK = ITEMS.register("red_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> RED_IRON_BACKPACK = ITEMS.register("red_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> RED_GOLD_BACKPACK = ITEMS.register("red_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> RED_DIAMOND_BACKPACK = ITEMS.register("red_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> RED_NETHERITE_BACKPACK = ITEMS.register("red_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> YELLOW_LEATHER_BACKPACK = ITEMS.register("yellow_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> YELLOW_IRON_BACKPACK = ITEMS.register("yellow_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> YELLOW_GOLD_BACKPACK = ITEMS.register("yellow_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> YELLOW_DIAMOND_BACKPACK = ITEMS.register("yellow_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> YELLOW_NETHERITE_BACKPACK = ITEMS.register("yellow_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> ORANGE_LEATHER_BACKPACK = ITEMS.register("orange_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> ORANGE_IRON_BACKPACK = ITEMS.register("orange_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> ORANGE_GOLD_BACKPACK = ITEMS.register("orange_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> ORANGE_DIAMOND_BACKPACK = ITEMS.register("orange_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> ORANGE_NETHERITE_BACKPACK = ITEMS.register("orange_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> LIME_LEATHER_BACKPACK = ITEMS.register("lime_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIME_IRON_BACKPACK = ITEMS.register("lime_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIME_GOLD_BACKPACK = ITEMS.register("lime_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIME_DIAMOND_BACKPACK = ITEMS.register("lime_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIME_NETHERITE_BACKPACK = ITEMS.register("lime_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> GREEN_LEATHER_BACKPACK = ITEMS.register("green_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GREEN_IRON_BACKPACK = ITEMS.register("green_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GREEN_GOLD_BACKPACK = ITEMS.register("green_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GREEN_DIAMOND_BACKPACK = ITEMS.register("green_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> GREEN_NETHERITE_BACKPACK = ITEMS.register("green_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> CYAN_LEATHER_BACKPACK = ITEMS.register("cyan_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> CYAN_IRON_BACKPACK = ITEMS.register("cyan_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> CYAN_GOLD_BACKPACK = ITEMS.register("cyan_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> CYAN_DIAMOND_BACKPACK = ITEMS.register("cyan_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> CYAN_NETHERITE_BACKPACK = ITEMS.register("cyan_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> LIGHT_BLUE_LEATHER_BACKPACK = ITEMS.register("light_blue_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_BLUE_IRON_BACKPACK = ITEMS.register("light_blue_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_BLUE_GOLD_BACKPACK = ITEMS.register("light_blue_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_BLUE_DIAMOND_BACKPACK = ITEMS.register("light_blue_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> LIGHT_BLUE_NETHERITE_BACKPACK = ITEMS.register("light_blue_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> BLUE_LEATHER_BACKPACK = ITEMS.register("blue_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLUE_IRON_BACKPACK = ITEMS.register("blue_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLUE_GOLD_BACKPACK = ITEMS.register("blue_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLUE_DIAMOND_BACKPACK = ITEMS.register("blue_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> BLUE_NETHERITE_BACKPACK = ITEMS.register("blue_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> PURPLE_LEATHER_BACKPACK = ITEMS.register("purple_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PURPLE_IRON_BACKPACK = ITEMS.register("purple_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PURPLE_GOLD_BACKPACK = ITEMS.register("purple_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PURPLE_DIAMOND_BACKPACK = ITEMS.register("purple_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PURPLE_NETHERITE_BACKPACK = ITEMS.register("purple_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> MAGENTA_LEATHER_BACKPACK = ITEMS.register("magenta_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> MAGENTA_IRON_BACKPACK = ITEMS.register("magenta_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> MAGENTA_GOLD_BACKPACK = ITEMS.register("magenta_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> MAGENTA_DIAMOND_BACKPACK = ITEMS.register("magenta_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> MAGENTA_NETHERITE_BACKPACK = ITEMS.register("magenta_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static RegistrySupplier<Item> PINK_LEATHER_BACKPACK = ITEMS.register("pink_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PINK_IRON_BACKPACK = ITEMS.register("pink_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PINK_GOLD_BACKPACK = ITEMS.register("pink_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PINK_DIAMOND_BACKPACK = ITEMS.register("pink_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static RegistrySupplier<Item> PINK_NETHERITE_BACKPACK = ITEMS.register("pink_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));



    public static void init() {
        ITEMS.register();
    }
}