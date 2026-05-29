package com.iamkaf.mochila.item.backpack;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BackpackMenu extends ChestMenu {
    public BackpackMenu(MenuType<?> type, int containerId, Inventory playerInventory, Container container,
            int rows) {
        super(type, containerId, playerInventory, container, rows);
    }

    @Override
    public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
        if (slotId > -1) {
            Slot slot = this.slots.get(slotId);

            if (clickType.equals(ContainerInput.SWAP) && slotContainsBlacklistedSwapItem(player, button)) {
                return;
            }

            if (slot.hasItem() && slotContainsBlacklistedItem(slotId)) {
                return;
            }
        }

        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasItem() && slotContainsBlacklistedItem(index)) {
            return ItemStack.EMPTY;
        }
        return super.quickMoveStack(player, index);
    }

    private boolean slotContainsBlacklistedItem(int slotId) {
        Item theItem = getSlot(slotId).getItem().getItem();
        return BackpackUtils.isBlacklistedItem(theItem);
    }

    private boolean slotContainsBlacklistedSwapItem(Player player, int button) {
        ItemStack stack = getSwapSourceStack(player, button);
        return !stack.isEmpty() && BackpackUtils.isBlacklistedItem(stack.getItem());
    }

    private ItemStack getSwapSourceStack(Player player, int button) {
        if (button == 40) {
            return player.getOffhandItem();
        }
        if (button >= 0 && button < 9) {
            return player.getInventory().getItem(button);
        }
        return ItemStack.EMPTY;
    }
}
