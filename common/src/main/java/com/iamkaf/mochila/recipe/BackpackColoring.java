package com.iamkaf.mochila.recipe;

import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.registry.RecipeSerializers;
import com.mojang.serialization.MapCodec;
//? if <26.1
/*import net.minecraft.core.HolderLookup;*/
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Colors a backpack copying its contents and tier.
 */
public class BackpackColoring extends CustomRecipe {
    public static final BackpackColoring INSTANCE = new BackpackColoring();
    public static final MapCodec<BackpackColoring> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, BackpackColoring> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    //? if >=26.1
    public static final RecipeSerializer<BackpackColoring> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    //? if <26.1
    /*public static final RecipeSerializer<BackpackColoring> SERIALIZER = new CustomRecipe.Serializer<>(BackpackColoring::new);*/

    private BackpackColoring() {
        //? if <26.1
        /*super(CraftingBookCategory.MISC);*/
    }

    //? if <26.1
    /*public BackpackColoring(CraftingBookCategory category) { super(category); }*/

    public boolean matches(CraftingInput input, Level level) {
        int backpackCount = 0;
        int dyeCount = 0;

        for (int k = 0; k < input.size(); k++) {
            ItemStack itemStack = input.getItem(k);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof BackpackItem) {
                    backpackCount++;
                } else {
                    if (!(itemStack.getItem() instanceof DyeItem)) {
                        return false;
                    }

                    dyeCount++;
                }

                if (dyeCount > 1 || backpackCount > 1) {
                    return false;
                }
            }
        }

        return backpackCount == 1 && dyeCount == 1;
    }

    // TODO: add support for coloring higher tiers backpacks
    //? if >=26.1
    public @NotNull ItemStack assemble(CraftingInput input) {
    //? if <26.1
    /*public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {*/
        ItemStack backpack = ItemStack.EMPTY;
        DyeColor dyeColor = DyeColor.WHITE;

        for (int i = 0; i < input.size(); i++) {
            ItemStack inputStack = input.getItem(i);
            if (!inputStack.isEmpty()) {
                Item item = inputStack.getItem();
                if (item instanceof BackpackItem) {
                    backpack = inputStack;
                } else if (item instanceof DyeItem) {
                    //? if >=26.1
                    dyeColor = inputStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
                    //? if <26.1
                    /*dyeColor = ((DyeItem) item).getDyeColor();*/
                }
            }
        }

        Item item = BackpackUtils.getBackpackByColor(backpack, dyeColor);
        return backpack.transmuteCopy(item, 1);
    }

    public @NotNull ItemStack assembleForCommands(CraftingInput input, Level level) {
        //? if >=26.1
        return assemble(input);
        //? if <26.1
        /*return assemble(input, level.registryAccess());*/
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializers.BACKPACK_COLORING_SERIALIZER.get();
    }
}
