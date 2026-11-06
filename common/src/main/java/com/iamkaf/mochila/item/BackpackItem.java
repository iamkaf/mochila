package com.iamkaf.mochila.item;

import com.iamkaf.amber.api.functions.v1.ClientFunctions.SmartTooltip;
import com.iamkaf.mochila.MochilaConfig;
import com.iamkaf.mochila.compat.BackpackData;
import com.iamkaf.mochila.compat.CompatItem;
import com.iamkaf.mochila.compat.Sounds;
import com.iamkaf.mochila.compat.Texts;
import com.iamkaf.mochila.item.backpack.BackpackContainer;
import com.iamkaf.mochila.item.backpack.BackpackMenu;
import com.iamkaf.mochila.item.backpack.QuickStash;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BackpackItem extends CompatItem {
    public final BackpackContainer.BackpackSize size;

    public BackpackItem(BackpackContainer.BackpackSize size, Properties properties) {
        super(BackpackData.withDefaults(properties));
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
        Sounds.playArmorEquip(player);
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, menuPlayer) -> new BackpackMenu(
                        container.getMenuType(),
                        id,
                        inventory,
                        container,
                        container.rows()
                ),
                stack.getDisplayName()
        ));
    }

    @Override
    protected void onUse(Player player, ItemStack stack) {
        openForPlayer(player, stack);
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

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    protected void addTooltip(ItemStack stack, Consumer<Component> tooltipAdder) {
        QuickStash.Mode mode = QuickStash.getMode(stack);
        Component modeText = Texts.translatable("item.mochila.backpack.mode." + mode.toString().toLowerCase())
                .withStyle(mode == QuickStash.Mode.DUMP ? ChatFormatting.YELLOW : ChatFormatting.AQUA);

        SmartTooltip tooltip = new SmartTooltip()
                .add(Texts.translatable("item.mochila.backpack.hold_shift").withStyle(ChatFormatting.GRAY));
        if (MochilaConfig.tooltipShowSize()) {
            tooltip.shift(Texts.translatable(
                    "item.mochila.backpack.size",
                    String.format("%s slots", BackpackContainer.sizeToInt(size))
            ));
        }
        if (MochilaConfig.tooltipShowMode()) {
            tooltip.shift(Texts.translatable(
                            "item.mochila.backpack.mode",
                            modeText
                    )
                    .withStyle(ChatFormatting.BLUE));
            tooltip.shift(Texts.translatable("item.mochila.backpack." + mode.toString().toLowerCase())
                    .withStyle(ChatFormatting.DARK_AQUA));
            tooltip.shift(Texts.translatable("item.mochila.backpack.shift").withStyle(ChatFormatting.GRAY));
        }
        tooltip.into(tooltipAdder);
    }
}
