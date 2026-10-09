package com.iamkaf.mochila.item.backpack;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
//? if <26.1
/*import net.minecraft.world.inventory.ClickType;*/
//? if >=26.1
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BackpackMenu extends ChestMenu {
    public BackpackMenu(MenuType<?> type, int containerId, Inventory playerInventory, BackpackContainer container,
            int rows) {
        super(type, containerId, playerInventory, container, rows);
    }

    // ChestMenu sets its container before it adds slots, so getContainer() is ready here.
    @Override
    protected Slot addSlot(Slot slot) {
        if (getContainer() instanceof BackpackContainer backpack) {
            if (slot.container == backpack) {
                // Uses the vanilla container-item rule, so direct inserts can't nest backpacks or shulker boxes.
                slot = new ShulkerBoxSlot(backpack, slot.getContainerSlot(), slot.x, slot.y);
            } else if (backpack.isBackpack(slot.getItem())) {
                slot = new OpenBackpackSlot(slot);
            }
        }
        return super.addSlot(slot);
    }

    @Override
    //? if >=26.1
    public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
    //? if <26.1
    /*public void clicked(int slotId, int button, ClickType clickType, Player player) {*/
        if (slotId > -1) {
            Slot slot = this.slots.get(slotId);

            //? if >=26.1
            if (clickType.equals(ContainerInput.SWAP) && slotContainsBlacklistedSwapItem(player, button)) {
            //? if <26.1
            /*if (clickType.equals(ClickType.SWAP) && slotContainsBlacklistedSwapItem(player, button)) {*/
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

    /**
     * Holds the open backpack. Sorting mods check {@link #mayPickup} before they move a stack, so this keeps
     * them from copying or moving the backpack while its menu writes to it.
     */
    private static class OpenBackpackSlot extends Slot {
        OpenBackpackSlot(Slot slot) {
            super(slot.container, slot.getContainerSlot(), slot.x, slot.y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
