package com.iamkaf.mochila.debug;

import com.iamkaf.amber.api.event.v1.events.common.CommandEvents;
import com.iamkaf.amber.api.networking.v1.PacketContext;
import com.iamkaf.konfig.api.v1.ConfigValue;
import com.iamkaf.konfig.impl.v1.config.model.ConfigHandleImpl;
import com.iamkaf.mochila.MochilaConfig;
import com.iamkaf.mochila.compat.BackpackData;
import com.iamkaf.mochila.compat.CommandCompat;
import com.iamkaf.mochila.compat.Players;
import com.iamkaf.mochila.compat.Stacks;
import com.iamkaf.mochila.compat.Texts;
import com.iamkaf.mochila.compat.VanillaRegistries;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.backpack.BackpackContainer;
import com.iamkaf.mochila.item.backpack.BackpackMenu;
import com.iamkaf.mochila.item.backpack.BackpackUtils;
import com.iamkaf.mochila.item.backpack.QuickStash;
import com.iamkaf.mochila.network.ChangeBackpackModePacket;
import com.iamkaf.mochila.recipe.BackpackColoring;
import com.iamkaf.mochila.recipe.BackpackUpgrading;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class MochilaDebugCommands {
    private static final Component SAMPLE_NAME = Texts.literal("Preserved Pack");

    private MochilaDebugCommands() {
    }

    public static void init() {
        CommandEvents.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(command(CommandCompat.itemArgument(registryAccess))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> command(ItemArgument item) {
        return Commands.literal("mochila")
                .then(Commands.literal("debug")
                        .requires(CommandCompat::isOwner)
                        .then(Commands.literal("sample")
                                .then(Commands.argument("item", item)
                                        .executes(MochilaDebugCommands::sample)))
                        .then(Commands.literal("tooltip")
                                .executes(MochilaDebugCommands::tooltip))
                        .then(configCommand())
                        .then(Commands.literal("mode").executes(MochilaDebugCommands::mode))
                        .then(Commands.literal("quickstash")
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .then(Commands.argument("direction", StringArgumentType.word())
                                                .executes(MochilaDebugCommands::quickstash))))
                        .then(Commands.literal("assert-sample")
                                .then(Commands.argument("item", item)
                                        .executes(MochilaDebugCommands::assertSample)))
                        .then(Commands.literal("menu")
                                .then(Commands.literal("assert-open-backpack-locked")
                                        .executes(MochilaDebugCommands::assertOpenBackpackLocked))
                                .then(Commands.literal("assert-storage-refuses")
                                        .then(Commands.argument("item", item)
                                                .executes(MochilaDebugCommands::assertStorageRefuses)))
                                .then(Commands.literal("assert-closed")
                                        .executes(MochilaDebugCommands::assertMenuClosed)))
                        .then(Commands.literal("recipe")
                                .then(Commands.literal("color")
                                        .then(Commands.argument("dye", item)
                                                .executes(MochilaDebugCommands::color)))
                                .then(Commands.literal("upgrade")
                                        .then(Commands.argument("material", item)
                                                .executes(MochilaDebugCommands::upgrade)))
                                .then(Commands.literal("smith-netherite")
                                        .executes(MochilaDebugCommands::smithNetherite))
                                .then(Commands.literal("assert-invalid-upgrade")
                                        .then(Commands.argument("material", item)
                                                .executes(MochilaDebugCommands::assertInvalidUpgrade)))));
    }

    // Runs the handler of the packet the sneak-and-backpack-key press sends, as the command's player.
    private static int mode(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ChangeBackpackModePacket.handle(new ChangeBackpackModePacket(0), new PacketContext() {
            @Override
            public boolean isClientSide() {
                return false;
            }

            @Override
            public net.minecraft.world.entity.player.Player getPlayer() {
                return player;
            }

            @Override
            public void execute(Runnable task) {
                task.run();
            }
        });
        return Command.SINGLE_SUCCESS;
    }

    // Changes the quickstash switch, target list and feedback setting, which are the ones tests flip.
    private static LiteralArgumentBuilder<CommandSourceStack> configCommand() {
        LiteralArgumentBuilder<CommandSourceStack> config = Commands.literal("config")
                .then(Commands.literal("reset").executes(context -> {
                    MochilaConfig.QUICKSTASH_ENABLED.set(true);
                    MochilaConfig.QUICKSTASH_TARGETS.set(MochilaConfig.QUICKSTASH_TARGETS.defaultValue());
                    MochilaConfig.QUICKSTASH_FEEDBACK.set(true);
                    return configApplied(context);
                }))
                .then(Commands.literal("targets")
                        .then(Commands.argument("blocks", StringArgumentType.greedyString()).executes(context -> {
                            String raw = StringArgumentType.getString(context, "blocks").trim();
                            MochilaConfig.QUICKSTASH_TARGETS.set(raw.isEmpty() ? List.of() : List.of(raw.split(",")));
                            return configApplied(context);
                        })));
        flag(config, "enabled", MochilaConfig.QUICKSTASH_ENABLED);
        flag(config, "feedback", MochilaConfig.QUICKSTASH_FEEDBACK);
        return config;
    }

    private static void flag(LiteralArgumentBuilder<CommandSourceStack> parent, String name,
            ConfigValue<Boolean> value) {
        parent.then(Commands.literal(name)
                .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            value.set(BoolArgumentType.getBool(context, "value"));
                            return configApplied(context);
                        })));
    }

    // The config only syncs to a player when they join, and this process hosts both ends, so apply the saved snapshot
    // the way a joining client would.
    private static int configApplied(CommandContext<CommandSourceStack> context) {
        ConfigHandleImpl handle = (ConfigHandleImpl) MochilaConfig.HANDLE;
        handle.save();
        handle.applySyncSnapshot(handle.snapshotJson());
        CommandCompat.success(context.getSource(), Texts.literal("Config applied."));
        return Command.SINGLE_SUCCESS;
    }

    private static int sample(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = CommandCompat.stackOf(ItemArgument.getItem(context, "item"));
        if (!(stack.getItem() instanceof BackpackItem backpackItem)) {
            return fail("Item is not a Mochila backpack.");
        }

        Stacks.setName(stack, SAMPLE_NAME);
        BackpackData.setQuickstashMode(stack, 1);

        BackpackContainer backpack = new BackpackContainer(stack, backpackItem.size);
        backpack.setItem(0, new ItemStack(Items.COBBLESTONE, 16));
        backpack.setItem(1, new ItemStack(Items.OAK_LOG, 8));
        backpack.setChanged();

        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        CommandCompat.success(context.getSource(), Texts.literal("Created sample backpack."));
        return Command.SINGLE_SUCCESS;
    }

    // The held backpack has to list its own tooltip lines.
    private static int tooltip(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof BackpackItem)) {
            return fail("Main hand item is not a Mochila backpack.");
        }

        List<Component> lines = Stacks.tooltipLines(stack, player);
        String hint = Texts.translatable("item.mochila.backpack.hold_shift").getString();
        if (lines.stream().noneMatch(line -> line.getString().equals(hint))) {
            return fail("The tooltip does not list \"" + hint + "\".");
        }

        CommandCompat.success(context.getSource(), Texts.literal("Tooltip lists " + lines.size() + " lines."));
        return Command.SINGLE_SUCCESS;
    }

    private static int quickstash(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof BackpackItem backpackItem)) {
            return fail("Main hand item is not a Mochila backpack.");
        }

        Direction direction = Direction.byName(StringArgumentType.getString(context, "direction"));
        if (direction == null) {
            return fail("Invalid direction.");
        }

        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        Level level = Players.level(player);
        BlockState state = level.getBlockState(pos);
        QuickStash.Result result = QuickStash.quickStash(
                state,
                player,
                level,
                pos,
                InteractionHand.MAIN_HAND,
                direction,
                backpackItem.size,
                QuickStash.getMode(stack)
        );
        if (!result.moved()) {
            return fail("Quickstash moved no items: " + result.message().getString());
        }

        CommandCompat.success(context.getSource(), result.message());
        return Command.SINGLE_SUCCESS;
    }

    private static int assertSample(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack expected = CommandCompat.stackOf(ItemArgument.getItem(context, "item"));
        ItemStack actual = player.getMainHandItem();
        if (!actual.is(expected.getItem())) {
            return fail("Expected " + itemId(expected.getItem()) + " but found " + itemId(actual.getItem()) + ".");
        }
        if (!(actual.getItem() instanceof BackpackItem backpackItem)) {
            return fail("Main hand item is not a Mochila backpack.");
        }
        if (!Stacks.isNamed(actual, SAMPLE_NAME)) {
            return fail("Backpack custom name was not preserved.");
        }
        if (BackpackData.quickstashMode(actual) != 1) {
            return fail("Backpack quickstash mode was not preserved.");
        }

        NonNullList<ItemStack> contents = BackpackData.readContents(actual, BackpackContainer.sizeToInt(backpackItem.size));
        if (contents.size() != BackpackContainer.sizeToInt(backpackItem.size)) {
            return fail("Backpack container size was not preserved.");
        }
        if (!isStack(contents.get(0), Items.COBBLESTONE, 16)) {
            return fail("Backpack slot 0 was not preserved.");
        }
        if (!isStack(contents.get(1), Items.OAK_LOG, 8)) {
            return fail("Backpack slot 1 was not preserved.");
        }

        CommandCompat.success(context.getSource(), Texts.literal("Sample backpack assertions passed."));
        return Command.SINGLE_SUCCESS;
    }

    // Takes the open backpack the way a sorting mod would, through the slot's own checks.
    private static int assertOpenBackpackLocked(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        if (!(player.containerMenu instanceof BackpackMenu menu)
                || !(menu.getContainer() instanceof BackpackContainer backpack)) {
            return fail("No backpack menu is open.");
        }

        for (Slot slot : menu.slots) {
            ItemStack held = slot.getItem();
            if (!backpack.isBackpack(held)) {
                continue;
            }
            if (slot.mayPickup(player) || slot.allowModification(player)) {
                return fail("The open backpack's slot is not locked.");
            }
            if (!slot.safeTake(1, 1, player).isEmpty() || slot.getItem() != held) {
                return fail("The open backpack was taken from its slot.");
            }
            int inventorySlot = slot.getContainerSlot();
            CommandCompat.success(
                    context.getSource(),
                    Texts.literal("Open backpack is locked in inventory slot " + inventorySlot + ".")
            );
            return Command.SINGLE_SUCCESS;
        }
        return fail("The open backpack is not in a menu slot.");
    }

    // Hoppers and sorting mods check mayPlace before they insert into a slot.
    private static int assertStorageRefuses(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        if (!(player.containerMenu instanceof BackpackMenu menu)
                || !(menu.getContainer() instanceof BackpackContainer backpack)) {
            return fail("No backpack menu is open.");
        }

        ItemStack stack = CommandCompat.stackOf(ItemArgument.getItem(context, "item"));
        int storageSlots = 0;
        for (Slot slot : menu.slots) {
            if (slot.container != backpack) {
                continue;
            }
            storageSlots++;
            if (slot.mayPlace(stack)) {
                return fail("Backpack slot " + slot.getContainerSlot() + " accepts " + itemId(stack.getItem()) + ".");
            }
        }
        if (storageSlots == 0) {
            return fail("The backpack menu has no storage slots.");
        }

        int checked = storageSlots;
        CommandCompat.success(
                context.getSource(),
                Texts.literal("All " + checked + " backpack slots refuse " + itemId(stack.getItem()) + ".")
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int assertMenuClosed(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        if (player.containerMenu instanceof BackpackMenu) {
            return fail("A backpack menu is still open.");
        }
        CommandCompat.success(context.getSource(), Texts.literal("No backpack menu is open."));
        return Command.SINGLE_SUCCESS;
    }

    private static int color(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack dye = CommandCompat.stackOf(ItemArgument.getItem(context, "dye"));
        if (!(dye.getItem() instanceof DyeItem)) {
            return fail("Color recipe requires a dye item.");
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack backpack = player.getMainHandItem();
        List<ItemStack> items = List.of(backpack, dye);
        if (!BackpackColoring.INSTANCE.matchesItems(items)) {
            return fail("Coloring recipe did not match.");
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, BackpackColoring.INSTANCE.assembleItems(items));
        CommandCompat.success(context.getSource(), Texts.literal("Applied coloring recipe."));
        return Command.SINGLE_SUCCESS;
    }

    private static int upgrade(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack result = assembleUpgrade(player.getMainHandItem(), ItemArgument.getItem(context, "material"));
        if (result.isEmpty()) {
            return fail("Upgrade recipe did not match.");
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, result);
        CommandCompat.success(context.getSource(), Texts.literal("Applied upgrade recipe."));
        return Command.SINGLE_SUCCESS;
    }

    private static int assertInvalidUpgrade(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack result = assembleUpgrade(player.getMainHandItem(), ItemArgument.getItem(context, "material"));
        if (!result.isEmpty()) {
            return fail("Upgrade recipe unexpectedly matched.");
        }
        CommandCompat.success(context.getSource(), Texts.literal("Invalid upgrade assertion passed."));
        return Command.SINGLE_SUCCESS;
    }

    private static int smithNetherite(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack backpack = player.getMainHandItem();
        if (!(backpack.getItem() instanceof BackpackItem)) {
            return fail("Main hand item is not a Mochila backpack.");
        }

        BackpackUtils.Tier tier = BackpackUtils.determineTier(backpack);
        if (tier != BackpackUtils.Tier.DIAMOND) {
            return fail("Smithing test requires a diamond-tier backpack.");
        }

        Item item = BackpackUtils.getBackpackByTierAndColor(
                BackpackUtils.Tier.NETHERITE,
                BackpackUtils.determineDyeColor(backpack)
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, BackpackData.convert(backpack, item));
        CommandCompat.success(context.getSource(), Texts.literal("Applied netherite smithing transform."));
        return Command.SINGLE_SUCCESS;
    }

    private static ItemStack assembleUpgrade(ItemStack backpack, ItemInput materialInput)
            throws CommandSyntaxException {
        ItemStack material = CommandCompat.stackOf(materialInput);
        List<ItemStack> items = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            items.add(material.copy());
        }
        items.set(4, backpack);

        return BackpackUpgrading.INSTANCE.matchesItems(items)
                ? BackpackUpgrading.INSTANCE.assembleItems(items)
                : ItemStack.EMPTY;
    }

    private static boolean isStack(ItemStack stack, Item item, int count) {
        return stack.is(item) && stack.getCount() == count;
    }

    private static String itemId(Item item) {
        return VanillaRegistries.items().getKey(item).toString();
    }

    // Throwing marks the command as failed. A plain zero result still counts as success.
    private static int fail(String message) throws CommandSyntaxException {
        throw new SimpleCommandExceptionType(Texts.literal(message)).create();
    }
}
