package com.iamkaf.mochila.item;

import com.iamkaf.mochila.compat.CompatItem;
import com.iamkaf.mochila.compat.Sounds;
import com.iamkaf.mochila.compat.Texts;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

public class EnderBackpackItem extends CompatItem {
    public EnderBackpackItem(Properties properties) {
        super(properties);
    }

    public static void onEnderBackpackKeybindPressed(ServerPlayer player) {
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() instanceof EnderBackpackItem enderBackpack) {
                enderBackpack.openForPlayer(player, stack);
                return;
            }
        }
    }

    @Override
    protected void onUse(Player player, ItemStack stack) {
        openForPlayer((ServerPlayer) player, stack);
    }

    public void openForPlayer(ServerPlayer player, ItemStack stack) {
        PlayerEnderChestContainer playerEnderChestContainer = player.getEnderChestInventory();
        Sounds.playEnderChestOpen(player);
        player.openMenu(new SimpleMenuProvider((i, inventory, playerx) -> new ChestMenu(
                MenuType.GENERIC_9x3,
                i,
                inventory,
                playerEnderChestContainer,
                3
        ) {
            @Override
            public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
                if (slotId > -1 && getSlot(slotId).getItem().equals(stack)) {
                    return;
                }
                super.clicked(slotId, button, clickType, player);
            }
        }, Texts.translatable("container.enderchest")));
        player.awardStat(Stats.OPEN_ENDERCHEST);
    }
}
