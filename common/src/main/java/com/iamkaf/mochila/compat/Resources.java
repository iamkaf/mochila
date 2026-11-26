package com.iamkaf.mochila.compat;

import net.minecraft.resources.Identifier;

/** Resource names. Before 1.21 they were built with a constructor. */
public final class Resources {
    private Resources() {
    }

    public static Identifier of(String namespace, String path) {
        //? if >=1.21 {
        return Identifier.fromNamespaceAndPath(namespace, path);
        //?} else {
        /*return new Identifier(namespace, path);
        *///?}
    }
}
