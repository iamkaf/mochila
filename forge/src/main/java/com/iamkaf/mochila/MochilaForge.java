package com.iamkaf.mochila;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
public class MochilaForge {

    public MochilaForge() {
        MochilaMod.init();
        if (FMLLoader.getDist().isClient()) {
            MochilaForgeClient.init();
        }
    }
}
