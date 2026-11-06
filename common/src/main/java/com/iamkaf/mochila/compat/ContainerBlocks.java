package com.iamkaf.mochila.compat;

import java.util.List;

/** Vanilla blocks that hold items. Copper chests arrive in Minecraft 1.21.9. */
public final class ContainerBlocks {
    private ContainerBlocks() {
    }

    public static List<String> copperChests() {
        //? if >=1.21.9 {
        return List.of(
                "minecraft:copper_chest",
                "minecraft:exposed_copper_chest",
                "minecraft:weathered_copper_chest",
                "minecraft:oxidized_copper_chest",
                "minecraft:waxed_copper_chest",
                "minecraft:waxed_exposed_copper_chest",
                "minecraft:waxed_weathered_copper_chest",
                "minecraft:waxed_oxidized_copper_chest"
        );
        //?} else {
        /*return List.of();
        *///?}
    }
}
