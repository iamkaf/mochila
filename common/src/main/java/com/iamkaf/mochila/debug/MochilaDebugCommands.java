package com.iamkaf.mochila.debug;

import com.iamkaf.amber.api.event.v1.events.common.CommandEvents;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.backpack.BackpackContainer;
import com.iamkaf.mochila.item.backpack.QuickStash;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.recipe.BackpackColoring;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import com.iamkaf.mochila.registry.DataComponents;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MochilaDebugCommands {
    private static final Component SAMPLE_NAME = Component.literal("Preserved Pack");

    private MochilaDebugCommands() {
    }

    public static void init() {
        CommandEvents.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(command(registryAccess)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> command(CommandBuildContext registryAccess) {
        return Commands.literal("mochila")
                .then(Commands.literal("debug")
                        .requires(source -> source.permissions()
                                .hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                        .then(Commands.literal("sample")
                                .then(Commands.argument("item", ItemArgument.item(registryAccess))
                                        .executes(MochilaDebugCommands::sample)))
                        .then(Commands.literal("quickstash")
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .then(Commands.argument("direction", StringArgumentType.word())
                                                .executes(MochilaDebugCommands::quickstash))))
                        .then(Commands.literal("assert-sample")
                                .then(Commands.argument("item", ItemArgument.item(registryAccess))
                                        .executes(MochilaDebugCommands::assertSample)))
                        .then(Commands.literal("recipe")
                                .then(Commands.literal("color")
                                        .then(Commands.argument("dye", ItemArgument.item(registryAccess))
                                                .executes(MochilaDebugCommands::color)))
                                .then(Commands.literal("upgrade")
                                        .then(Commands.argument("material", ItemArgument.item(registryAccess))
                                                .executes(MochilaDebugCommands::upgrade)))
                                .then(Commands.literal("smith-netherite")
                                        .executes(MochilaDebugCommands::smithNetherite))
                                .then(Commands.literal("assert-invalid-upgrade")
                                        .then(Commands.argument("material", ItemArgument.item(registryAccess))
                                                .executes(MochilaDebugCommands::assertInvalidUpgrade)))));
    }

    private static int sample(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = ItemArgument.getItem(context, "item").createItemStack(1);
        if (!(stack.getItem() instanceof BackpackItem backpackItem)) {
            return fail(context, "Item is not a Mochila backpack.");
        }

        stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, SAMPLE_NAME);
        stack.set(DataComponents.QUICKSTASH_MODE.get(), 1);

        BackpackContainer backpack = new BackpackContainer(stack, backpackItem.size);
        backpack.setItem(0, new ItemStack(Items.COBBLESTONE, 16));
        backpack.setItem(1, new ItemStack(Items.OAK_LOG, 8));
        backpack.setChanged();

        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        context.getSource().sendSuccess(() -> Component.literal("Created sample backpack."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int quickstash(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof BackpackItem backpackItem)) {
            return fail(context, "Main hand item is not a Mochila backpack.");
        }

        Direction direction = Direction.byName(StringArgumentType.getString(context, "direction"));
        if (direction == null) {
            return fail(context, "Invalid direction.");
        }

        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        BlockState state = player.level().getBlockState(pos);
        QuickStash.Result result = QuickStash.quickStash(
                state,
                player,
                player.level(),
                pos,
                InteractionHand.MAIN_HAND,
                direction,
                backpackItem.size,
                QuickStash.getMode(stack)
        );
        if (!result.moved()) {
            return fail(context, "Quickstash moved no items: " + result.message().getString());
        }

        context.getSource().sendSuccess(() -> result.message(), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int assertSample(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack expected = ItemArgument.getItem(context, "item").createItemStack(1);
        ItemStack actual = player.getMainHandItem();
        if (!actual.is(expected.getItem())) {
            return fail(context, "Expected " + itemId(expected.getItem()) + " but found " + itemId(actual.getItem()) + ".");
        }
        if (!(actual.getItem() instanceof BackpackItem backpackItem)) {
            return fail(context, "Main hand item is not a Mochila backpack.");
        }
        if (!Objects.equals(actual.get(net.minecraft.core.component.DataComponents.CUSTOM_NAME), SAMPLE_NAME)) {
            return fail(context, "Backpack custom name was not preserved.");
        }
        if (actual.getOrDefault(DataComponents.QUICKSTASH_MODE.get(), 0) != 1) {
            return fail(context, "Backpack quickstash mode was not preserved.");
        }

        NonNullList<ItemStack> contents = NonNullList.withSize(BackpackContainer.sizeToInt(backpackItem.size), ItemStack.EMPTY);
        actual.getOrDefault(net.minecraft.core.component.DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                .copyInto(contents);
        if (contents.size() != BackpackContainer.sizeToInt(backpackItem.size)) {
            return fail(context, "Backpack container size was not preserved.");
        }
        if (!isStack(contents.get(0), Items.COBBLESTONE, 16)) {
            return fail(context, "Backpack slot 0 was not preserved.");
        }
        if (!isStack(contents.get(1), Items.OAK_LOG, 8)) {
            return fail(context, "Backpack slot 1 was not preserved.");
        }

        context.getSource().sendSuccess(() -> Component.literal("Sample backpack assertions passed."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int color(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack dye = ItemArgument.getItem(context, "dye").createItemStack(1);
        if (!(dye.getItem() instanceof DyeItem)) {
            return fail(context, "Color recipe requires a dye item.");
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack backpack = player.getMainHandItem();
        CraftingInput input = CraftingInput.of(2, 1, List.of(backpack, dye));
        if (!BackpackColoring.INSTANCE.matches(input, player.level())) {
            return fail(context, "Coloring recipe did not match.");
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, BackpackColoring.INSTANCE.assemble(input));
        context.getSource().sendSuccess(() -> Component.literal("Applied coloring recipe."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int upgrade(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack result = assembleUpgrade(player.getMainHandItem(), ItemArgument.getItem(context, "material"));
        if (result.isEmpty()) {
            return fail(context, "Upgrade recipe did not match.");
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, result);
        context.getSource().sendSuccess(() -> Component.literal("Applied upgrade recipe."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int assertInvalidUpgrade(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack result = assembleUpgrade(player.getMainHandItem(), ItemArgument.getItem(context, "material"));
        if (!result.isEmpty()) {
            return fail(context, "Upgrade recipe unexpectedly matched.");
        }
        context.getSource().sendSuccess(() -> Component.literal("Invalid upgrade assertion passed."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int smithNetherite(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack backpack = player.getMainHandItem();
        if (!(backpack.getItem() instanceof BackpackItem)) {
            return fail(context, "Main hand item is not a Mochila backpack.");
        }

        BackpackUtils.Tier tier = BackpackUtils.determineTier(backpack);
        if (tier != BackpackUtils.Tier.DIAMOND) {
            return fail(context, "Smithing test requires a diamond-tier backpack.");
        }

        Item item = BackpackUtils.getBackpackByTierAndColor(
                BackpackUtils.Tier.NETHERITE,
                BackpackUtils.determineDyeColor(backpack)
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, backpack.transmuteCopy(item, 1));
        context.getSource().sendSuccess(() -> Component.literal("Applied netherite smithing transform."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static ItemStack assembleUpgrade(ItemStack backpack, ItemInput materialInput) throws CommandSyntaxException {
        ItemStack material = materialInput.createItemStack(1);
        List<ItemStack> items = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            items.add(material.copy());
        }
        items.set(4, backpack);

        CraftingInput input = CraftingInput.of(3, 3, items);
        return BackpackUpgrading.INSTANCE.matches(input, null) ? BackpackUpgrading.INSTANCE.assemble(input) : ItemStack.EMPTY;
    }

    private static boolean isStack(ItemStack stack, Item item, int count) {
        return stack.is(item) && stack.getCount() == count;
    }

    private static String itemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    private static int fail(CommandContext<CommandSourceStack> context, String message) {
        context.getSource().sendFailure(Component.literal(message));
        return 0;
    }
}
