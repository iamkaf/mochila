package com.iamkaf.mochila.fabric;

import com.iamkaf.mochila.Mochila;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RegisterImpl {
    public static <T> Supplier<DataComponentType<T>> dataComponentType(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        var obj = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                Mochila.resource(name),
                builderOperator.apply(DataComponentType.builder()).build()
        );
        return () -> obj;
    }

    public static Holder<ArmorMaterial> armorMaterial(String name, ArmorMaterial material) {
        var obj = Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL,
                Mochila.resource(name),
                material
        );
        return obj;
    }
}
