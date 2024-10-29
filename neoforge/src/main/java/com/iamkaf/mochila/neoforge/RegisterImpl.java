package com.iamkaf.mochila.neoforge;

import com.iamkaf.mochila.Mochila;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RegisterImpl {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Mochila.MOD_ID);

    public static <T> Supplier<DataComponentType<T>> dataComponentType(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name,
                () -> builderOperator.apply(DataComponentType.builder()).build()
        );
    }

    public static Holder<ArmorMaterial> armorMaterial(String name, ArmorMaterial material) {
        var obj = Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL,
                Mochila.resource(name),
                material
        );
        return obj;
    }
}
