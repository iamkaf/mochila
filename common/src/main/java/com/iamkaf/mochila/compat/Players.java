package com.iamkaf.mochila.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Player state. {@code Entity.level} became a method in 1.20. */
public final class Players {
    private Players() {
    }

    public static Level level(Player player) {
        //? if >=1.20 {
        return player.level();
        //?} else {
        /*return player.level;
        *///?}
    }
}
