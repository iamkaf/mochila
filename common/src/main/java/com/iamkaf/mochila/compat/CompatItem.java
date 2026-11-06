package com.iamkaf.mochila.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
//? if >=1.21.5 {
import net.minecraft.world.item.component.TooltipDisplay;
//?} else if >=1.20.5 {
/*import java.util.List;
*///?} else {
/*import org.jetbrains.annotations.Nullable;

import java.util.List;
*///?}
//? if >=1.21.2 {
import net.minecraft.world.InteractionResult;
//?} else {
/*import net.minecraft.world.InteractionResultHolder;
*///?}

import java.util.function.Consumer;

/**
 * An item with stable hooks for being used and for its tooltip. {@code use} returned a result holder before 1.21.2,
 * and {@code appendHoverText} changed in 1.20.5 and again in 1.21.5.
 */
public abstract class CompatItem extends Item {
    protected CompatItem(Properties properties) {
        super(properties);
    }

    /** Runs on the server when a player uses the item. */
    protected abstract void onUse(Player player, ItemStack stack);

    /** Adds the item's own tooltip lines. */
    protected void addTooltip(ItemStack stack, Consumer<Component> lines) {
    }

    //? if >=1.21.2 {
    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        onUse(player, player.getItemInHand(hand));
        return InteractionResult.CONSUME;
    }
    //?} else {
    /*@Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }
        onUse(player, stack);
        return InteractionResultHolder.consume(stack);
    }
    *///?}

    //? if >=1.21.5 {
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        addTooltip(stack, tooltipAdder);
    }
    //?} else if >=1.20.5 {
    /*@Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        addTooltip(stack, tooltip::add);
    }
    *///?} else {
    /*@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        addTooltip(stack, tooltip::add);
    }
    *///?}
}
