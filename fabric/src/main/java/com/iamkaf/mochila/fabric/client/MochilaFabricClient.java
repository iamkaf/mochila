package com.iamkaf.mochila.fabric.client;

import com.iamkaf.mochila.registry.Keybinds;
import net.fabricmc.api.ClientModInitializer;

public final class MochilaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Keybinds.init();
    }
}
