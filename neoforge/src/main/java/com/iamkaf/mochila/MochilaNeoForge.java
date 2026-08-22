package com.iamkaf.mochila;

import com.iamkaf.mochila.compat.curios.CuriosCompat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Constants.MOD_ID)
public class MochilaNeoForge {
    public MochilaNeoForge(IEventBus eventBus) {
        MochilaMod.init();
        if (ModList.get().isLoaded("curios")) {
            eventBus.addListener(MochilaNeoForge::registerCuriosCompat);
        }
    }

    private static void registerCuriosCompat(FMLCommonSetupEvent event) {
        event.enqueueWork(CuriosCompat::init);
    }
}
