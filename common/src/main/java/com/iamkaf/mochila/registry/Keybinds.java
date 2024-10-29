package com.iamkaf.mochila.registry;

import com.iamkaf.mochila.Mochila;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.lwjgl.glfw.GLFW;

import static com.iamkaf.mochila.item.BackpackItem.BACKPACK_EQUIP_SOUND;

public class Keybinds {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Mochila.MOD_ID, Registries.ITEM);

    public static final KeyMapping OPEN_BACKPACK = new KeyMapping(
            "key.mochila.open_backpack",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.mochila"
    );


    public static void init() {
        KeyMappingRegistry.register(OPEN_BACKPACK);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (OPEN_BACKPACK.consumeClick()) {
                var player = minecraft.player;
                if (player == null) {
                    return;
                }
                player.playSound(BACKPACK_EQUIP_SOUND.value());
                NetworkManager.sendToServer(new Mochila.OpenBackpackPayload(0));
            }
        });
    }
}