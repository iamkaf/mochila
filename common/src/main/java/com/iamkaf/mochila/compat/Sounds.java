package com.iamkaf.mochila.compat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
//? if >=1.20.5 {
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
//?}

/**
 * Vanilla sounds as plain events, and sounds sent to one player. The armor sounds became registry holders in
 * 1.20.5, the button click in 1.19.3, and the sound packet gained a seed in 1.20.5.
 */
public final class Sounds {
    private Sounds() {
    }

    //? if >=1.20.5 {
    public static SoundEvent armorEquipLeather() {
        return SoundEvents.ARMOR_EQUIP_LEATHER.value();
    }

    public static SoundEvent armorEquipNetherite() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE.value();
    }
    //?} else {
    /*public static SoundEvent armorEquipLeather() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    public static SoundEvent armorEquipNetherite() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }
    *///?}

    //? if >=1.19.3 {
    public static SoundEvent uiButtonClick() {
        return SoundEvents.UI_BUTTON_CLICK.value();
    }
    //?} else {
    /*public static SoundEvent uiButtonClick() {
        return SoundEvents.UI_BUTTON_CLICK;
    }
    *///?}

    //? if >=1.20.5 {
    public static void playArmorEquip(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    SoundSource.PLAYERS,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0.5f,
                    1f,
                    player.level().getRandom().nextLong()
            ));
        }
    }

    public static void playEnderChestOpen(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    Holder.direct(SoundEvents.ENDER_CHEST_OPEN),
                    SoundSource.PLAYERS,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0.5f,
                    1f,
                    player.level().getRandom().nextLong()
            ));
        }
    }
    //?} else {
    /*public static void playArmorEquip(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.playNotifySound(SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
        }
    }

    public static void playEnderChestOpen(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.playNotifySound(SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 0.5f, 1f);
        }
    }
    *///?}
}
