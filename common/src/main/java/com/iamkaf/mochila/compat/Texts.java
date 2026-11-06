package com.iamkaf.mochila.compat;

import net.minecraft.network.chat.MutableComponent;
//? if >=1.19 {
import net.minecraft.network.chat.Component;
//?} else {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///?}

/** Component factories. 1.18.2 predates {@code Component.translatable} and {@code Component.literal}. */
public final class Texts {
    private Texts() {
    }

    //? if >=1.19 {
    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }

    public static MutableComponent empty() {
        return Component.empty();
    }

    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }
    //?} else {
    /*public static MutableComponent translatable(String key, Object... args) {
        return new TranslatableComponent(key, args);
    }

    public static MutableComponent empty() {
        return new TextComponent("");
    }

    public static MutableComponent literal(String text) {
        return new TextComponent(text);
    }
    *///?}
}
