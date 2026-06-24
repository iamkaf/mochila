package com.iamkaf.mochila.recipe;

import com.google.common.collect.ImmutableSet;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.registry.RecipeSerializers;
import com.mojang.serialization.MapCodec;
//? if <26.1
/*import net.minecraft.core.HolderLookup;*/
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Upgrades a backpack copying its contents and color.
 */
public class BackpackUpgrading extends CustomRecipe {
    public static final BackpackUpgrading INSTANCE = new BackpackUpgrading();
    public static final MapCodec<BackpackUpgrading> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, BackpackUpgrading> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    //? if >=26.1
    public static final RecipeSerializer<BackpackUpgrading> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    //? if <26.1
    /*public static final RecipeSerializer<BackpackUpgrading> SERIALIZER = new CustomRecipe.Serializer<>(BackpackUpgrading::new);*/
    private final Set<Item> UPGRADE_MATERIALS =
            ImmutableSet.of(Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND);

    private BackpackUpgrading() {
        //? if <26.1
        /*super(CraftingBookCategory.MISC);*/
    }

    //? if <26.1
    /*public BackpackUpgrading(CraftingBookCategory category) { super(category); }*/

    public boolean matches(CraftingInput input, Level level) {
        ItemStack backpack = ItemStack.EMPTY;
        int backpackCount = 0;
        Map<Item, Integer> materialMap = new HashMap<>();

        for (int k = 0; k < input.size(); k++) {
            ItemStack itemStack = input.getItem(k);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (itemStack.getItem() instanceof BackpackItem) {
                backpackCount++;
                backpack = itemStack;
                continue;
            }
            materialMap.merge(itemStack.getItem(), 1, Integer::sum);
        }

        if (backpackCount != 1) {
            return false;
        }

        if (materialMap.keySet().size() != 1) {
            return false;
        }

        Item material = materialMap.keySet().stream().findFirst().get();

        if (!UPGRADE_MATERIALS.contains(material)) {
            return false;
        }

        if (materialMap.get(material) != 8) {
            return false;
        }

        var currentTier = BackpackUtils.determineTier(backpack);
        var nextTier = BackpackUtils.getNextTier(currentTier);

        if (nextTier == null) {
            return false;
        }

        return material.equals(getMaterialForTier(nextTier));
    }

    //? if >=26.1
    public @NotNull ItemStack assemble(CraftingInput input) {
    //? if <26.1
    /*public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {*/
        ItemStack backpack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack inputStack = input.getItem(i);
            if (!inputStack.isEmpty()) {
                Item item = inputStack.getItem();
                if (item instanceof BackpackItem) {
                    backpack = inputStack;
                }
            }
        }

        var currentTier = BackpackUtils.determineTier(backpack);
        var nextTier = BackpackUtils.getNextTier(currentTier);
        var color = BackpackUtils.determineDyeColor(backpack);

        assert nextTier != null;

        Item item = BackpackUtils.getBackpackByTierAndColor(nextTier, color);
        return backpack.transmuteCopy(item, 1);
    }

    public @NotNull ItemStack assembleForCommands(CraftingInput input, Level level) {
        //? if >=26.1
        return assemble(input);
        //? if <26.1
        /*return assemble(input, level.registryAccess());*/
    }

    public static Item getMaterialForTier(BackpackUtils.Tier tier) {
        return switch (tier) {
            case LEATHER -> Items.LEATHER;
            case IRON -> Items.IRON_INGOT;
            case GOLD -> Items.GOLD_INGOT;
            case DIAMOND -> Items.DIAMOND;
            case NETHERITE -> Items.NETHERITE_INGOT;
        };
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializers.BACKPACK_UPGRADING_SERIALIZER.get();
    }
}
