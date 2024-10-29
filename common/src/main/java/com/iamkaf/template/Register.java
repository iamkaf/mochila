package com.iamkaf.template;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ArmorMaterial;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Register {
    @ExpectPlatform
    public static <T> Supplier<DataComponentType<T>> dataComponentType(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Holder<ArmorMaterial> armorMaterial(String name, ArmorMaterial material) {
        throw new AssertionError();
    }
}
