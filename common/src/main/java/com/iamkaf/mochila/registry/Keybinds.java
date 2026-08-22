package com.iamkaf.mochila.registry;

import com.iamkaf.amber.api.event.v1.events.common.client.ClientTickEvents;
import com.iamkaf.amber.api.registry.v1.KeybindHelper;
import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.network.ChangeBackpackModePacket;
import com.iamkaf.mochila.network.MochilaNetworking;
import com.iamkaf.mochila.network.OpenBackpackPacket;
import com.iamkaf.mochila.network.OpenEnderBackpackPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyMapping.Category MOCHILA_CATEGORY = KeyMapping.Category.register(Constants.resource("mochila"));
    public static final KeyMapping OPEN_BACKPACK =
            new KeyMapping("key.mochila.open_backpack", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, MOCHILA_CATEGORY);
    public static final KeyMapping OPEN_ENDER_BACKPACK =
            new KeyMapping(
                    "key.mochila.open_ender_backpack",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_V,
                    MOCHILA_CATEGORY
            );


    public static void init() {
        KeybindHelper.register(OPEN_BACKPACK);
        KeybindHelper.register(OPEN_ENDER_BACKPACK);

        ClientTickEvents.END_CLIENT_TICK.register(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            while (OPEN_BACKPACK.consumeClick()) {
                var player = minecraft.player;
                if (player == null) {
                    return;
                }

                if (minecraft.hasShiftDown()) {
                    MochilaNetworking.CHANNEL.sendToServer(new ChangeBackpackModePacket(0));
                } else {
                    MochilaNetworking.CHANNEL.sendToServer(new OpenBackpackPacket(0));
                }
            }

            while (OPEN_ENDER_BACKPACK.consumeClick()) {
                var player = minecraft.player;
                if (player == null) {
                    return;
                }

                MochilaNetworking.CHANNEL.sendToServer(new OpenEnderBackpackPacket());
            }
        });
    }
}
