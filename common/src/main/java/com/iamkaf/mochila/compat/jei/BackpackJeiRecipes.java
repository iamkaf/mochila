package com.iamkaf.mochila.compat.jei;

import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class BackpackJeiRecipes {
    private BackpackJeiRecipes() {
    }

    public static List<ColoringDisplay> coloringDisplays() {
        List<ColoringDisplay> displays = new ArrayList<>();
        for (BackpackUtils.Tier tier : BackpackUtils.Tier.values()) {
            for (DyeColor color : DyeColor.values()) {
                displays.add(new ColoringDisplay(tier, color));
            }
        }
        return displays;
    }

    public static List<UpgradeDisplay> upgradeDisplays() {
        List<UpgradeDisplay> displays = new ArrayList<>();
        for (BackpackUtils.Tier tier : BackpackUtils.Tier.values()) {
            BackpackUtils.Tier nextTier = BackpackUtils.getNextTier(tier);
            if (nextTier == null || !isCraftingUpgradeMaterial(BackpackUpgrading.getMaterialForTier(nextTier))) {
                continue;
            }

            displays.add(new UpgradeDisplay(tier, null));
            for (DyeColor color : DyeColor.values()) {
                displays.add(new UpgradeDisplay(tier, color));
            }
        }
        return displays;
    }

    public static List<ItemStack> backpacksForTier(BackpackUtils.Tier tier) {
        List<ItemStack> backpacks = new ArrayList<>();
        backpacks.add(new ItemStack(BackpackUtils.getBackpackByTierAndColor(tier, null)));
        for (DyeColor color : DyeColor.values()) {
            backpacks.add(new ItemStack(BackpackUtils.getBackpackByTierAndColor(tier, color)));
        }
        return backpacks;
    }

    public static Item dyeItem(DyeColor color) {
        return switch (color) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }

    private static boolean isCraftingUpgradeMaterial(Item material) {
        return material == Items.IRON_INGOT || material == Items.GOLD_INGOT || material == Items.DIAMOND;
    }

    public record ColoringDisplay(BackpackUtils.Tier tier, DyeColor color) {
        public List<ItemStack> backpackInputs() {
            return backpacksForTier(tier);
        }

        public ItemStack dyeInput() {
            return new ItemStack(dyeItem(color));
        }

        public ItemStack output() {
            return new ItemStack(BackpackUtils.getBackpackByTierAndColor(tier, color));
        }
    }

    public record UpgradeDisplay(BackpackUtils.Tier tier, @Nullable DyeColor color) {
        public ItemStack backpackInput() {
            return new ItemStack(BackpackUtils.getBackpackByTierAndColor(tier, color));
        }

        public ItemStack materialInput() {
            BackpackUtils.Tier nextTier = BackpackUtils.getNextTier(tier);
            if (nextTier == null) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(BackpackUpgrading.getMaterialForTier(nextTier));
        }

        public ItemStack output() {
            BackpackUtils.Tier nextTier = BackpackUtils.getNextTier(tier);
            if (nextTier == null) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(BackpackUtils.getBackpackByTierAndColor(nextTier, color));
        }
    }

}
