package com.iamkaf.mochila.registry;

import com.iamkaf.amber.api.registry.v1.DeferredRegister;
import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.EnderBackpackItem;
import com.iamkaf.mochila.item.backpack.BackpackContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    private static Supplier<Item> e(String id, BackpackContainer.BackpackSize size) {
        return ITEMS.register(id, () -> new  BackpackItem(size, new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Constants.resource(id)))));
    }

    public static final BackpackContainer.BackpackSize LEATHER_BACKPACK_SIZE = BackpackContainer.BackpackSize.TWO_ROWS;
    public static final BackpackContainer.BackpackSize IRON_BACKPACK_SIZE = BackpackContainer.BackpackSize.THREE_ROWS;
    public static final BackpackContainer.BackpackSize GOLD_BACKPACK_SIZE = BackpackContainer.BackpackSize.FOUR_ROWS;
    public static final BackpackContainer.BackpackSize DIAMOND_BACKPACK_SIZE = BackpackContainer.BackpackSize.FIVE_ROWS;
    public static final BackpackContainer.BackpackSize NETHERITE_BACKPACK_SIZE = BackpackContainer.BackpackSize.SIX_ROWS;

    public static Supplier<Item> LEATHER_BACKPACK = e("leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA).setId(ResourceKey.create(Registries.ITEM, Mochila.resource("leather_backpack")))));
    public static Supplier<Item> IRON_BACKPACK = e("iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA).setId(ResourceKey.create(Registries.ITEM, Mochila.resource("iron_backpack")))));
    public static Supplier<Item> GOLD_BACKPACK = e("gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> DIAMOND_BACKPACK = e("diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> NETHERITE_BACKPACK = e("netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> WHITE_LEATHER_BACKPACK = e("white_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("white_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> WHITE_IRON_BACKPACK = e("white_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("white_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> WHITE_GOLD_BACKPACK = e("white_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("white_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> WHITE_DIAMOND_BACKPACK = e("white_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("white_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> WHITE_NETHERITE_BACKPACK = e("white_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("white_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> LIGHT_GRAY_LEATHER_BACKPACK = e("light_gray_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("light_gray_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_GRAY_IRON_BACKPACK = e("light_gray_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("light_gray_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_GRAY_GOLD_BACKPACK = e("light_gray_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("light_gray_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_GRAY_DIAMOND_BACKPACK = e("light_gray_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("light_gray_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_GRAY_NETHERITE_BACKPACK = e("light_gray_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("light_gray_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> GRAY_LEATHER_BACKPACK = e("gray_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("gray_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GRAY_IRON_BACKPACK = e("gray_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("gray_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GRAY_GOLD_BACKPACK = e("gray_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("gray_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GRAY_DIAMOND_BACKPACK = e("gray_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("gray_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GRAY_NETHERITE_BACKPACK = e("gray_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("gray_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> BLACK_LEATHER_BACKPACK = e("black_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("black_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLACK_IRON_BACKPACK = e("black_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("black_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLACK_GOLD_BACKPACK = e("black_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("black_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLACK_DIAMOND_BACKPACK = e("black_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("black_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLACK_NETHERITE_BACKPACK = e("black_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("black_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> BROWN_LEATHER_BACKPACK = e("brown_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("brown_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BROWN_IRON_BACKPACK = e("brown_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("brown_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BROWN_GOLD_BACKPACK = e("brown_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("brown_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BROWN_DIAMOND_BACKPACK = e("brown_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("brown_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BROWN_NETHERITE_BACKPACK = e("brown_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("brown_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> RED_LEATHER_BACKPACK = e("red_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("red_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> RED_IRON_BACKPACK = e("red_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("red_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> RED_GOLD_BACKPACK = e("red_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("red_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> RED_DIAMOND_BACKPACK = e("red_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("red_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> RED_NETHERITE_BACKPACK = e("red_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("red_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> YELLOW_LEATHER_BACKPACK = e("yellow_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("yellow_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> YELLOW_IRON_BACKPACK = e("yellow_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("yellow_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> YELLOW_GOLD_BACKPACK = e("yellow_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("yellow_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> YELLOW_DIAMOND_BACKPACK = e("yellow_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("yellow_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> YELLOW_NETHERITE_BACKPACK = e("yellow_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("yellow_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> ORANGE_LEATHER_BACKPACK = e("orange_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("orange_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> ORANGE_IRON_BACKPACK = e("orange_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("orange_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> ORANGE_GOLD_BACKPACK = e("orange_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("orange_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> ORANGE_DIAMOND_BACKPACK = e("orange_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("orange_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> ORANGE_NETHERITE_BACKPACK = e("orange_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("orange_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> LIME_LEATHER_BACKPACK = e("lime_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("lime_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIME_IRON_BACKPACK = e("lime_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("lime_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIME_GOLD_BACKPACK = e("lime_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("lime_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIME_DIAMOND_BACKPACK = e("lime_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("lime_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIME_NETHERITE_BACKPACK = e("lime_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("lime_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> GREEN_LEATHER_BACKPACK = e("green_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("green_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GREEN_IRON_BACKPACK = e("green_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("green_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GREEN_GOLD_BACKPACK = e("green_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("green_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GREEN_DIAMOND_BACKPACK = e("green_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("green_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> GREEN_NETHERITE_BACKPACK = e("green_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("green_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> CYAN_LEATHER_BACKPACK = e("cyan_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("cyan_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> CYAN_IRON_BACKPACK = e("cyan_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("cyan_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> CYAN_GOLD_BACKPACK = e("cyan_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("cyan_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> CYAN_DIAMOND_BACKPACK = e("cyan_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("cyan_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> CYAN_NETHERITE_BACKPACK = e("cyan_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("cyan_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> LIGHT_BLUE_LEATHER_BACKPACK = e("light_blue_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("light_blue_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_BLUE_IRON_BACKPACK = e("light_blue_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("light_blue_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_BLUE_GOLD_BACKPACK = e("light_blue_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("light_blue_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_BLUE_DIAMOND_BACKPACK = e("light_blue_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("light_blue_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> LIGHT_BLUE_NETHERITE_BACKPACK = e("light_blue_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("light_blue_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> BLUE_LEATHER_BACKPACK = e("blue_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("blue_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLUE_IRON_BACKPACK = e("blue_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("blue_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLUE_GOLD_BACKPACK = e("blue_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("blue_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLUE_DIAMOND_BACKPACK = e("blue_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("blue_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> BLUE_NETHERITE_BACKPACK = e("blue_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("blue_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> PURPLE_LEATHER_BACKPACK = e("purple_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("purple_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PURPLE_IRON_BACKPACK = e("purple_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("purple_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PURPLE_GOLD_BACKPACK = e("purple_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("purple_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PURPLE_DIAMOND_BACKPACK = e("purple_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("purple_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PURPLE_NETHERITE_BACKPACK = e("purple_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("purple_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> MAGENTA_LEATHER_BACKPACK = e("magenta_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("magenta_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> MAGENTA_IRON_BACKPACK = e("magenta_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("magenta_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> MAGENTA_GOLD_BACKPACK = e("magenta_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("magenta_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> MAGENTA_DIAMOND_BACKPACK = e("magenta_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("magenta_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> MAGENTA_NETHERITE_BACKPACK = e("magenta_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("magenta_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> PINK_LEATHER_BACKPACK = e("pink_leather_backpack", LEATHER_BACKPACK_SIZE);// ITEMS.register("pink_leather_backpack", () -> new BackpackItem(LEATHER_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PINK_IRON_BACKPACK = e("pink_iron_backpack", IRON_BACKPACK_SIZE);// ITEMS.register("pink_iron_backpack", () -> new BackpackItem(IRON_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PINK_GOLD_BACKPACK = e("pink_gold_backpack", GOLD_BACKPACK_SIZE);// ITEMS.register("pink_gold_backpack", () -> new BackpackItem(GOLD_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PINK_DIAMOND_BACKPACK = e("pink_diamond_backpack", DIAMOND_BACKPACK_SIZE);// ITEMS.register("pink_diamond_backpack", () -> new BackpackItem(DIAMOND_BACKPACK_SIZE, new Item.Properties().stacksTo(1).arch$tab(CreativeModeTabs.MOCHILA)));
    public static Supplier<Item> PINK_NETHERITE_BACKPACK = e("pink_netherite_backpack", NETHERITE_BACKPACK_SIZE);// ITEMS.register("pink_netherite_backpack", () -> new BackpackItem(NETHERITE_BACKPACK_SIZE, new Item.Properties().stacksTo(1).fireResistant().arch$tab(CreativeModeTabs.MOCHILA)));

    public static Supplier<Item> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new EnderBackpackItem(new Item.Properties().stacksTo(1).fireResistant().setId(ResourceKey.create(Registries.ITEM, Constants.resource("ender_backpack")))));

    public static void init() {
        ITEMS.register();
    }
}