package com.iamkaf.mochila.compat;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//? if >=1.20.5 {
import com.iamkaf.amber.api.registry.v1.DeferredRegister;
import com.iamkaf.amber.api.registry.v1.RegistrySupplier;
import com.iamkaf.mochila.Constants;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
*///?}

import java.util.List;

/**
 * What a backpack stack remembers: its contents and its quickstash mode.
 * Minecraft 1.20.5 and later keep them in data components. Earlier lines keep them in a {@code mochila} tag on the stack.
 */
public final class BackpackData {
    private BackpackData() {
    }

    //? if >=1.20.5 {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Constants.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    private static final RegistrySupplier<DataComponentType<Integer>> QUICKSTASH_MODE =
            DATA_COMPONENTS.register(
                    "quickstash_mode",
                    () -> DataComponentType.<Integer>builder()
                            // Older releases saved the mode as {value: N}. Both forms load, and it saves as a number.
                            .persistent(Codec.withAlternative(Codec.INT, Codec.INT.fieldOf("value").codec()))
                            .build()
            );

    public static void init() {
        DATA_COMPONENTS.register();
    }

    /** The item properties with the data an empty backpack starts with. */
    public static Item.Properties withDefaults(Item.Properties properties) {
        return properties.component(QUICKSTASH_MODE.get(), 0);
    }

    public static NonNullList<ItemStack> readContents(ItemStack stack, int size) {
        NonNullList<ItemStack> contents = NonNullList.withSize(size, ItemStack.EMPTY);
        stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(contents);
        return contents;
    }

    public static void writeContents(ItemStack stack, List<ItemStack> contents) {
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(contents));
    }

    public static int quickstashMode(ItemStack stack) {
        return stack.getOrDefault(QUICKSTASH_MODE.get(), 0);
    }

    public static void setQuickstashMode(ItemStack stack, int mode) {
        stack.set(QUICKSTASH_MODE.get(), mode);
    }

    /** A copy of the backpack as another item, keeping everything it stores. */
    public static ItemStack convert(ItemStack backpack, Item item) {
        return backpack.transmuteCopy(item, 1);
    }
    //?} else {
    /*private static final String TAG = "mochila";
    private static final String ITEMS = "Items";
    private static final String QUICKSTASH_MODE = "QuickstashMode";

    public static void init() {
    }

    public static Item.Properties withDefaults(Item.Properties properties) {
        return properties;
    }

    public static NonNullList<ItemStack> readContents(ItemStack stack, int size) {
        NonNullList<ItemStack> contents = NonNullList.withSize(size, ItemStack.EMPTY);
        CompoundTag tag = stack.getTagElement(TAG);
        if (tag != null) {
            ContainerHelper.loadAllItems(tag, contents);
        }
        return contents;
    }

    public static void writeContents(ItemStack stack, List<ItemStack> contents) {
        CompoundTag tag = stack.getOrCreateTagElement(TAG);
        tag.remove(ITEMS);
        ContainerHelper.saveAllItems(tag, NonNullList.of(ItemStack.EMPTY, contents.toArray(new ItemStack[0])), false);
    }

    public static int quickstashMode(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(TAG);
        return tag == null ? 0 : tag.getInt(QUICKSTASH_MODE);
    }

    public static void setQuickstashMode(ItemStack stack, int mode) {
        stack.getOrCreateTagElement(TAG).putInt(QUICKSTASH_MODE, mode);
    }

    public static ItemStack convert(ItemStack backpack, Item item) {
        ItemStack converted = new ItemStack(item);
        if (backpack.hasTag()) {
            converted.setTag(backpack.getTag().copy());
        }
        return converted;
    }
    *///?}
}
