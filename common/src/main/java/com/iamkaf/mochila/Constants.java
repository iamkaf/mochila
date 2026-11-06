package com.iamkaf.mochila;

import com.iamkaf.mochila.compat.Resources;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    /**
     * Identifier of the mod. Update these fields when reusing the Mochila.
     */
    public static final String MOD_ID = "mochila";
    public static final String MOD_NAME = "Mochila";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static Identifier resource(String path) {
        return resource(MOD_ID, path);
    }

    public static Identifier resource(String namespace, String path) {
        return Resources.of(namespace, path);
    }
}
