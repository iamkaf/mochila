package com.iamkaf.mochila.item.backpack;

import com.iamkaf.mochila.compat.BackpackData;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Based on the NeoForge docs: https://docs.neoforged.net/docs/blockentities/container#containers-on-itemstacks

public class BackpackContainer extends SimpleContainer {
    private final ItemStack stack;
    private final BackpackSize size;

    public BackpackContainer(ItemStack stack, BackpackSize size) {
        super(BackpackData.readContents(stack, sizeToInt(size)).toArray(new ItemStack[0]));
        this.stack = stack;
        this.size = size;
    }

    public static int sizeToInt(BackpackSize size) {
        return switch (size) {
            case TWO_ROWS -> 18;
            case THREE_ROWS -> 27;
            case FOUR_ROWS -> 36;
            case FIVE_ROWS -> 45;
            case SIX_ROWS -> 54;
        };
    }

    public boolean isBackpack(ItemStack candidate) {
        return candidate == stack;
    }

    // The menu writes to this exact stack, so it stays usable only while the player still holds it.
    @Override
    public boolean stillValid(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (isBackpack(inventory.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    public MenuType<ChestMenu> getMenuType() {
        return sizeToChestMenu(size);
    }

    private static MenuType<ChestMenu> sizeToChestMenu(BackpackSize size) {
        return switch (size) {
            case TWO_ROWS -> MenuType.GENERIC_9x2;
            case THREE_ROWS -> MenuType.GENERIC_9x3;
            case FOUR_ROWS -> MenuType.GENERIC_9x4;
            case FIVE_ROWS -> MenuType.GENERIC_9x5;
            case SIX_ROWS -> MenuType.GENERIC_9x6;
        };
    }

    public int rows() {
        return switch (size) {
            case TWO_ROWS -> 2;
            case THREE_ROWS -> 3;
            case FOUR_ROWS -> 4;
            case FIVE_ROWS -> 5;
            case SIX_ROWS -> 6;
        };
    }

    @Override
    public void setChanged() {
        super.setChanged();
        List<ItemStack> contents = new ArrayList<>(getContainerSize());
        for (int i = 0; i < getContainerSize(); i++) {
            contents.add(getItem(i));
        }
        BackpackData.writeContents(this.stack, contents);
    }

    public enum BackpackSize {
        TWO_ROWS,
        THREE_ROWS,
        FOUR_ROWS,
        FIVE_ROWS,
        SIX_ROWS
    }
}
