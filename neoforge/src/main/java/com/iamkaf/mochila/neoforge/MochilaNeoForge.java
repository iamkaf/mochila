package com.iamkaf.mochila.neoforge;

import net.neoforged.fml.common.Mod;

import com.iamkaf.mochila.Mochila;

@Mod(Mochila.MOD_ID)
public final class MochilaNeoForge {
    public MochilaNeoForge() {
        // Run our common setup.
        Mochila.init();
    }
}
