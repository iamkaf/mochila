package com.iamkaf.mochila.neoforge;

import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.registry.Keybinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Mochila.MOD_ID, dist = Dist.CLIENT)
public final class MochilaNeoForgeClient {
    public MochilaNeoForgeClient() {
        // Run our common setup.
        Keybinds.init();
    }
}
