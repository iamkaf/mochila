package com.iamkaf.mochila.platform;

import com.iamkaf.mochila.platform.services.IPlatformHelper;
//? if >=26.1 {
import net.minecraftforge.fml.loading.LoadingModList;
//?} else {
/*import net.minecraftforge.fml.ModList;
*///?}
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import java.nio.file.Path;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        //? if >=26.1
        return LoadingModList.getModFileById(modId) != null;
        //? if <26.1
        /*return ModList.get().isLoaded(modId);*/
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
