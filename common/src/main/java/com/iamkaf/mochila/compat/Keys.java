package com.iamkaf.mochila.compat;

//? if >=1.21.9 {
import com.iamkaf.mochila.Constants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
//?} else {
/*import net.minecraft.client.gui.screens.Screen;
*///?}

/** Key binding categories and modifier keys. Minecraft 1.21.9 made categories registered objects and moved {@code hasShiftDown} onto the client. */
public final class Keys {
    private Keys() {
    }

    //? if >=1.21.9 {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Constants.resource("mochila"));

    public static boolean hasShiftDown() {
        return Minecraft.getInstance().hasShiftDown();
    }
    //?} else {
    /*public static final String CATEGORY = "key.categories.mochila";

    public static boolean hasShiftDown() {
        return Screen.hasShiftDown();
    }
    *///?}
}
