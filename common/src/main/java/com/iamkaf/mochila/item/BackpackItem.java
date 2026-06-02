package com.iamkaf.mochila.item;

import com.iamkaf.amber.api.functions.v1.ClientFunctions.SmartTooltip;
import com.iamkaf.mochila.MochilaConfig;
import com.iamkaf.mochila.item.backpack.BackpackContainer;
import com.iamkaf.mochila.item.backpack.BackpackMenu;
import com.iamkaf.mochila.item.backpack.QuickStash;
import com.iamkaf.mochila.registry.DataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BackpackItem extends Item {
    public static final Holder<SoundEvent> BACKPACK_EQUIP_SOUND = SoundEvents.ARMOR_EQUIP_LEATHER;
    public final BackpackContainer.BackpackSize size;

    public BackpackItem(BackpackContainer.BackpackSize size, Properties properties) {
        super(properties.component(DataComponents.QUICKSTASH_MODE.get(), 0));
        this.size = size;
    }

    public static void onBackpackKeybindPressed(ServerPlayer player) {
        // TODO: test if this works for backpacks in the armor slots and backpacks in a
        //  trinket/curio/accessory slot.
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() instanceof BackpackItem backpack) {
                backpack.openForPlayer(player, stack);
                return;
            }
        }
    }

    public void openForPlayer(Player player, ItemStack stack) {
        var container = new BackpackContainer(stack, size);
        playEquipSound(player);
        player.openMenu(new SimpleMenuProvider(
                (i, inventory, playerx) -> new BackpackMenu(
                        container.getMenuType(),
                i,
                inventory,
                container,
                container.rows()
        ), stack.getDisplayName()
        ));
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        openForPlayer(player, stack);
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Direction direction = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (player != null && player.isShiftKeyDown()) {
            QuickStash.Result result = QuickStash.quickStash(
                    state,
                    player,
                    level,
                    pos,
                    hand,
                    direction,
                    size,
                    QuickStash.getMode(player.getItemInHand(hand))
            );
            if (result.handled()) {
                return InteractionResult.SUCCESS;
            }

            // check for signs
            if (state.getBlock() instanceof WallSignBlock) {
                Direction facing = state.getValue(WallSignBlock.FACING);
                BlockPos blockPosBehindSign = pos.relative(facing.getOpposite());
                BlockState blockState = level.getBlockState(blockPosBehindSign);
                QuickStash.Result signResult = QuickStash.quickStash(
                        blockState,
                        player,
                        level,
                        blockPosBehindSign,
                        hand,
                        facing,
                        size,
                        QuickStash.getMode(player.getItemInHand(hand))
                );
                if (signResult.handled()) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }

    // deprecated, fix it
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        QuickStash.Mode mode = stack.getOrDefault(DataComponents.QUICKSTASH_MODE.get(), 0) == 0
                ? QuickStash.Mode.DUMP
                : QuickStash.Mode.STORE;
        Component modeText = Component.translatable("item.mochila.backpack.mode." + mode.toString().toLowerCase())
                .withStyle(mode == QuickStash.Mode.DUMP ? ChatFormatting.YELLOW : ChatFormatting.AQUA);

        SmartTooltip tooltip = new SmartTooltip()
                .add(Component.translatable("item.mochila.backpack.hold_shift").withStyle(ChatFormatting.GRAY));
        if (MochilaConfig.tooltipShowSize()) {
            tooltip.shift(Component.translatable(
                    "item.mochila.backpack.size",
                    String.format("%s slots", BackpackContainer.sizeToInt(size))
            ));
        }
        if (MochilaConfig.tooltipShowMode()) {
            tooltip.shift(Component.translatable(
                            "item.mochila.backpack.mode",
                            modeText
                    )
                    .withStyle(ChatFormatting.BLUE));
            tooltip.shift(Component.translatable("item.mochila.backpack." + mode.toString().toLowerCase())
                    .withStyle(ChatFormatting.DARK_AQUA));
            tooltip.shift(Component.translatable("item.mochila.backpack.shift").withStyle(ChatFormatting.GRAY));
        }
        tooltip.into(tooltipAdder);
    }

    private static void playEquipSound(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    BACKPACK_EQUIP_SOUND,
                    SoundSource.PLAYERS,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0.5f,
                    1f,
                    player.level().getRandom().nextLong()
            ));
        }
    }
}
