package com.iamkaf.mochila.fabric.datagen;

//? if <1.19 {
/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iamkaf.mochila.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

// Minecraft 1.18.2 keeps the item model output private, so the backpack models cannot go through its
// model generator. This writes the same files directly.
public class ModLegacyItemModelProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public ModLegacyItemModelProvider(FabricDataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        try {
            ModModelProvider.forEachBackpackTexture((backpack, textureIndex) ->
                    ModelTemplates.FLAT_ITEM.create(
                            ModelLocationUtils.getModelLocation(backpack),
                            TextureMapping.layer0(Constants.resource("item/backpack" + textureIndex)),
                            (location, model) -> {
                                Path path = generator.getOutputFolder().resolve(
                                        "assets/" + location.getNamespace() + "/models/" + location.getPath() + ".json");
                                try {
                                    DataProvider.save(GSON, cache, model.get(), path);
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            }
                    ));
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    @Override
    public String getName() {
        return "Mochila Backpack Models";
    }
}
*///?}
