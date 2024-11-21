package com.iamkaf.mochila.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EnderBackpackItem extends Item {
    public EnderBackpackItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player,
            InteractionHand usedHand) {
        PlayerEnderChestContainer playerEnderChestContainer = player.getEnderChestInventory();
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        } else {
            playSound(player);
            player.openMenu(new SimpleMenuProvider((i, inventory, playerx) -> {
                return new ChestMenu(MenuType.GENERIC_9x3, i, inventory, playerEnderChestContainer, 3) {
                    @Override
                    public void clicked(int slotId, int button, ClickType clickType, Player player) {
                        if (slotId > -1 && getSlot(slotId).getItem().equals(stack)) {
                            return;
                        }
                        super.clicked(slotId, button, clickType, player);
                    }
                };
            }, Component.translatable("container.enderchest")));
            player.awardStat(Stats.OPEN_ENDERCHEST);
            return InteractionResultHolder.consume(stack);
        }
    }

    private void playSound(Player player) {
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
}
