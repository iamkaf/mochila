package com.iamkaf.mochila;

import com.iamkaf.konfig.neoforge.api.v1.KonfigNeoForgeClientScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class MochilaNeoForgeClient {
    public MochilaNeoForgeClient(ModContainer container) {
        KonfigNeoForgeClientScreens.register(container, Constants.MOD_ID);
    }
}
