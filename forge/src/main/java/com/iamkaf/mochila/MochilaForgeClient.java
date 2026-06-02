package com.iamkaf.mochila;

import com.iamkaf.konfig.forge.api.v1.KonfigForgeClientScreens;

final class MochilaForgeClient {
    private MochilaForgeClient() {
    }

    static void init() {
        KonfigForgeClientScreens.register(Constants.MOD_ID);
    }
}
