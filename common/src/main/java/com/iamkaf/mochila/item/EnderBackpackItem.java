package com.iamkaf.mochila.item;

import com.iamkaf.mochila.compat.CompatItem;
import com.iamkaf.mochila.compat.Sounds;
import com.iamkaf.mochila.compat.Texts;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
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
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, menuPlayer) -> new EnderBackpackMenu(id, inventory, playerEnderChestContainer, stack),
                Texts.translatable("container.enderchest")
        ));
        player.awardStat(Stats.OPEN_ENDERCHEST);
    }

    /** An ender chest menu that keeps the open backpack from being moved. */
    private static class EnderBackpackMenu extends ChestMenu {
        private final ItemStack backpack;

        EnderBackpackMenu(int id, Inventory inventory, PlayerEnderChestContainer container, ItemStack backpack) {
            super(MenuType.GENERIC_9x3, id, inventory, container, 3);
            this.backpack = backpack;
        }

        @Override
        public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
            if (slotId > -1 && getSlot(slotId).getItem().equals(backpack)) {
                return;
            }
            super.clicked(slotId, button, clickType, player);
        }
    }
}
